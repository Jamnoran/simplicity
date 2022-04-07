package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simplicity.simplicityaclientforreddit.databinding.MainFragmentBinding
import com.simplicity.simplicityaclientforreddit.redirects.SingleFragmentActivity
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.custom.BaseFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.RedditListAdapter
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.menu.values.Keys
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.IsFirstTimeApplicationStartedUseCase


class ListFragment(var subReddit: String?) : BaseFragment() {
    private val TAG = "ListFragment"
    lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: ListViewModel
    private lateinit var redditListAdapter: RedditListAdapter
    var lastItemScrolledOutOfView: RedditPost? = null
    var viewHolderOfItemToPause: RedditListAdapter.PostViewHolder? = null

    companion object {
        fun newInstance(subReddit: String? = null) = ListFragment(subReddit)
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
        subReddit?.let{
            viewModel.setSubReddit(it)
        }
        viewModel.initSession(IsFirstTimeApplicationStartedUseCase().isFirstTime())
        viewModel.fetchPosts()
        viewModel.posts().observe(requireActivity()) { observeRedditPosts(it) }
        viewModel.count().observe(requireActivity()) { observeCount(it) }
        viewModel.requireSettingsUpdate().observe(requireActivity()) { observeSettingUpdate() }
        viewModel.scrollToNextItem().observe(requireActivity()) { observeScrollToNextItem() }
        viewModel.hideSub().observe(requireActivity()) { observeHideSub() }
        viewModel.openCommentsFragment().observe(requireActivity()) { observeOpenCommentsFragment(it) }
        viewModel.subRedditClicked().observe(requireActivity()){ observeSubredditClicked(it)}
        viewModel.authorClicked().observe(requireActivity()){ observeAuthorClicked(it)}
        viewModel.redditUrlClicked().observe(requireActivity()){ observeRedditLinkClicked(it)}
        viewModel.webViewActivityClicked().observe(requireActivity()){ observeWebViewActivityClicked(it)}
        viewModel.browserUrlClicked().observe(requireActivity()){ sendToBrowser(it)}
    }

    private fun initAdapter() {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        redditListAdapter = RedditListAdapter(
            RedditPostListenerImpl(viewModel),
            width
        )

        binding.recyclerView.let{ recyclerView ->
            recyclerView.adapter = redditListAdapter
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
//                        Toast.makeText(context, "Loading more posts", Toast.LENGTH_LONG).show()
                        viewModel.fetchPosts()
                    }
                    val layoutManger = binding.recyclerView.layoutManager as LinearLayoutManager
                    val topPosition = layoutManger.findFirstVisibleItemPosition()
                    if(topPosition > 0){
                        val itemScrolledOut = redditListAdapter.currentList[topPosition - 1]
                        if(lastItemScrolledOutOfView == null || lastItemScrolledOutOfView != itemScrolledOut){
                            lastItemScrolledOutOfView = itemScrolledOut
                            Log.i(TAG, "Item scrolled out of view -> ${itemScrolledOut.data.title}")

                            layoutManger.findViewByPosition(topPosition)?.let{
                                val postViewHolder = recyclerView.getChildViewHolder(it) as RedditListAdapter.PostViewHolder
                                viewHolderOfItemToPause = postViewHolder
                            }?: Log.i(TAG, "Couldnt find a view with this position")

                            viewHolderOfItemToPause?.pauseViewHolder()
                        }
                    }
                }
            })
        }
        binding.scrollToNext.setOnClickListener { viewModel.scrollToNext(getNextPosition()) }
//        binding.scrollToNext.visibility = View.INVISIBLE
    }

    private fun observeSettingUpdate() {
        viewModel.setSettingsValues(SettingsSP().loadSetting(Keys.NSFW, true), SettingsSP().loadSetting(Keys.SFW, true))
    }

    private fun observeScrollToNextItem() {
        scrollToNextPosition()
    }

    private fun observeHideSub() {
        Toast.makeText(requireContext(), "Sub is now hidden", Toast.LENGTH_LONG).show()
    }

    private fun observeOpenCommentsFragment(post: RedditPost) {
//        (activity as MainActivity).startFragment(CommentsFragment.newInstance(post.data.subreddit, post.data.id))
        val intent = Intent(activity, SingleFragmentActivity::class.java).apply {
            putExtra(Keys.KEY_FRAGMENT, Keys.VALUE_COMMENT)
            putExtra(Keys.KEY_SUB_REDDIT, post.data.subreddit)
            putExtra(Keys.KEY_ID, post.data.id)
        }
        startActivity(intent)
    }

    private fun observeCount(it: Int) {
        Toast.makeText(context, "Posts in db: $it", Toast.LENGTH_SHORT).show()
    }

    private fun scrollToNextPosition() {
        val layoutManger = binding.recyclerView.layoutManager as LinearLayoutManager
        layoutManger.scrollToPositionWithOffset(getNextPosition(), 0)
    }

    private fun getNextPosition(): Int{
        val layoutManger = binding.recyclerView.layoutManager as LinearLayoutManager
        return layoutManger.findFirstVisibleItemPosition() + 1
    }

    private fun observeSubredditClicked(it: String) {
        Log.i("ListFragment", "Opening sub for post: $it")
        sendToBrowser("https://www.reddit.com/r/$it")
    }

    private fun observeAuthorClicked(it: String) {
        Log.i("ListFragment", "Going to user: $it")
//        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.reddit.com/user/:${it.data.author}/"))
//        startActivity(browserIntent)
        sendToBrowser("https://www.reddit.com/user/$it/")
    }

    private fun observeRedditLinkClicked(it: String) {
            val convertedUrl = "https://www.reddit.com$it"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(convertedUrl))
            startActivity(browserIntent)
    }

    private fun sendToBrowser(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        Log.i("ListFragment", "Sending this url to browser: $url")
        startActivity(i)
    }

    private fun observeWebViewActivityClicked(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun observeRedditPosts(posts: ArrayList<RedditPost>?) {
        redditListAdapter.submitList(posts?.toMutableList())
    }

}