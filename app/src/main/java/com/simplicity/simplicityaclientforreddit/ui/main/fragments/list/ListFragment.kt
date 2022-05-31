package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.simplicity.simplicityaclientforreddit.MainActivity
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.base.SingleFragmentActivity
import com.simplicity.simplicityaclientforreddit.databinding.MainFragmentBinding
import com.simplicity.simplicityaclientforreddit.base.BaseFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.RedditListAdapter
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util.RedditPostListenerImpl
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util.VerticalSpacingItemDecorator
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.menu.values.Keys
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.user.UserDetailActivity
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.IsFirstTimeApplicationStartedUseCase


class ListFragment : BaseFragment() {
    private val TAG = "ListFragment"
    lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: ListViewModel
    private lateinit var redditListAdapter: RedditListAdapter

    companion object {
        fun newInstance(subReddit: String? = null): ListFragment {
            val fragment = ListFragment()
            val args = Bundle()
            args.putString(MainActivity.KEY_SUBREDDIT, subReddit)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)

        initAdapter()
        arguments?.getString(MainActivity.KEY_SUBREDDIT)?.let{
            viewModel.setSubReddit(it)
            Toast.makeText(requireContext(), "Fetching sub reddit $it", Toast.LENGTH_LONG).show()
        }
        viewModel.initSession(IsFirstTimeApplicationStartedUseCase().isFirstTime())
        viewModel.fetchPosts()

        viewModel.posts().observe(requireActivity()) { observeRedditPosts(it) }
        viewModel.requireSettingsUpdate().observe(requireActivity()) { observeSettingUpdate() }
        viewModel.showError().observe(requireActivity()) { observeShowError(it) }
        viewModel.scrollToNextItem().observe(requireActivity()) { observeScrollToNextItem() }
        viewModel.hideSub().observe(requireActivity()) { observeHideSub() }
        viewModel.openCommentsFragment().observe(requireActivity()) { observeOpenCommentsFragment(it) }
        viewModel.subRedditClicked().observe(requireActivity()){ observeSubredditClicked(it)}
        viewModel.authorClicked().observe(requireActivity()){ observeAuthorClicked(it)}
        viewModel.redditUrlClicked().observe(requireActivity()){ observeRedditLinkClicked(it)}
        viewModel.webViewActivityClicked().observe(requireActivity()){ observeWebViewActivityClicked(it)}
        viewModel.browserUrlClicked().observe(requireActivity()){ observeSendToBrowser(it)}
    }

    override fun onPause() {
        super.onPause()
        binding.recyclerView.pauseVideo()
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerView.resumeVideo()
    }

    private fun initAdapter() {
        binding.loadingBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE
        redditListAdapter = RedditListAdapter(RedditPostListenerImpl(viewModel))
        binding.recyclerView.let{ recyclerView ->
            recyclerView.setViewModel(viewModel)
            recyclerView.setRequestManager(initGlide())
            val layoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = layoutManager
            val itemDecorator =
                VerticalSpacingItemDecorator(
                    10
                )
            recyclerView.addItemDecoration(itemDecorator)
            recyclerView.adapter = redditListAdapter
        }
        binding.scrollToNext.setOnClickListener { viewModel.scrollToNext(getNextPosition()) }
    }

    private fun observeSettingUpdate() {
        viewModel.setSettingsValues(SettingsSP().loadSetting(Keys.NSFW, true), SettingsSP().loadSetting(Keys.SFW, true))
    }

    private fun observeShowError(errorMessageId: Int) {
        if (redditListAdapter.itemCount > 0) {
            Toast.makeText(requireContext(), getString(errorMessageId), Toast.LENGTH_LONG).show()
        } else {
            binding.errorMessage.visibility = View.VISIBLE
            binding.errorMessage.text = getString(errorMessageId)
            binding.loadingBar.visibility = View.GONE
        }
    }

    private fun observeScrollToNextItem() {
        val layoutManger = binding.recyclerView.layoutManager as LinearLayoutManager
        layoutManger.scrollToPositionWithOffset(getNextPosition(), 0)
        binding.recyclerView.smoothScrollBy(0, 1)
    }

    private fun getNextPosition(): Int{
        val layoutManger = binding.recyclerView.layoutManager as LinearLayoutManager
        return layoutManger.findFirstVisibleItemPosition() + 1
    }

    private fun observeRedditPosts(posts: ArrayList<RedditPost>?) {
        redditListAdapter.submitList(posts?.toMutableList())
        binding.recyclerView.set_posts(posts)
        if(redditListAdapter.itemCount > 0){
            binding.loadingBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.recyclerView.smoothScrollBy(0, 1)
        }
    }

    private fun initGlide(): RequestManager? {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.white_background)
            .error(R.drawable.white_background)
        return Glide.with(this)
            .setDefaultRequestOptions(options)
    }

}