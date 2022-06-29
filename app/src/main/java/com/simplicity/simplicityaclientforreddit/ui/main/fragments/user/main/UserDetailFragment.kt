package com.simplicity.simplicityaclientforreddit.ui.main.fragments.user.main

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
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.base.BaseActivity
import com.simplicity.simplicityaclientforreddit.base.BaseFragment
import com.simplicity.simplicityaclientforreddit.databinding.UserDetailFragmentBinding
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.RedditListAdapter
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util.RedditPostListenerImpl
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util.VerticalSpacingItemDecorator
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.user.User
import com.simplicity.simplicityaclientforreddit.utils.media.ImageUtil.Companion.getConvertedImageUrl
import com.squareup.picasso.Picasso
import java.text.NumberFormat

class UserDetailFragment() : BaseFragment() {

    lateinit var binding: UserDetailFragmentBinding
    private lateinit var viewModel: UserViewModel
    private var _requestManager: RequestManager? = null
    private lateinit var redditListAdapter: RedditListAdapter
    private var profileVisibility = true
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserDetailFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        savedInstanceState?.getString(USER_KEY)?.let {
            username = it
        }
        this.arguments?.getString(USER_KEY)?.let {
            username = it
        }

        username?.let {
            viewModel.setUserName(it)
            viewModel.fetchUser()
            viewModel.fetchPosts()
        }
        viewModel.user().observe(requireActivity()) { observeUser(it) }
        viewModel.posts().observe(requireActivity()) { observeRedditPosts(it) }
        viewModel.hideSub().observe(requireActivity()) { observeHideSub() }
        viewModel.openCommentsFragment().observe(requireActivity()) { observeOpenCommentsFragment(it) }
        viewModel.subRedditClicked().observe(requireActivity()) { observeSubredditClicked(it) }
        viewModel.authorClicked().observe(requireActivity()) { observeAuthorClicked(it) }
        viewModel.redditUrlClicked().observe(requireActivity()) { observeRedditLinkClicked(it) }
        viewModel.webViewActivityClicked().observe(requireActivity()) { observeWebViewActivityClicked(it) }
        viewModel.browserUrlClicked().observe(requireActivity()) { observeSendToBrowser(it) }
        viewModel.scrollToNextItem().observe(requireActivity()) { observeScrollToNextItem() }
        viewModel.networkError().observe(requireActivity()) { observeNetworkError() }

        initAdapter()
        setUpListeners()
        toggleProfileVisibility()
        (activity as BaseActivity).logFirebaseEvent("fragment_user_detail", TAG, "logged_in_false")
    }

    private fun observeNetworkError() {
        binding.loadingBar.visibility = View.GONE
        Toast.makeText(requireContext(), getText(R.string.not_logged_in), Toast.LENGTH_LONG).show()
        requireActivity().finish()
    }

    private fun setUpListeners() {
        binding.userDetailSection.openRedditButton.setOnClickListener {
            username?.let {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.reddit.com/user/$it/"))
                startActivity(browserIntent)
            }
            //        sendToBrowser("https://www.reddit.com/user/$username/")
        }
        binding.userDetailSection.root.setOnClickListener {
            toggleProfileVisibility()
        }
    }

    private fun toggleProfileVisibility() {
        profileVisibility = !profileVisibility
        binding.userDetailSection.let {
            if (profileVisibility) {
                it.profileBanner.visibility = View.VISIBLE
                it.userDetail.visibility = View.VISIBLE
                it.karma.visibility = View.VISIBLE
                it.profileImage.visibility = View.VISIBLE
            } else {
                it.profileBanner.visibility = View.GONE
                it.userDetail.visibility = View.GONE
                it.karma.visibility = View.GONE
                it.profileImage.visibility = View.GONE
            }
        }
    }

    private fun observeUser(user: User) {
        Log.i(TAG, "We got user: $user")
        val picasso = Picasso.get()
        user.subreddit?.iconImg?.let {
            if (it.isNotEmpty()) {
                picasso
                    .load(getConvertedImageUrl(it))
                    .into(binding.userDetailSection.profileImage)
            }
        }
        user.subreddit?.bannerImg?.let {
            if (it.isNotEmpty()) {
                picasso
                    .load(getConvertedImageUrl(it))
                    .into(binding.userDetailSection.profileBanner)
            }
        }
        user.name?.let {
            binding.userDetailSection.username.text = it
        }
        user.totalKarma?.let {
            binding.userDetailSection.karma.text = NumberFormat.getInstance().format(it)
        }
        user.subreddit?.publicDescription?.let {
            binding.userDetailSection.userDetail.text = it
        }
    }

    private fun observeRedditPosts(posts: ArrayList<RedditPost>?) {
        redditListAdapter.submitList(posts?.toMutableList())
        binding.recyclerView.set_posts(posts)
        if (redditListAdapter.itemCount > 0) {
            binding.loadingBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.recyclerView.smoothScrollBy(0, 1)
        }
    }

    private fun observeScrollToNextItem() {
        val layoutManger = binding.recyclerView.layoutManager as LinearLayoutManager
        layoutManger.scrollToPositionWithOffset(getNextPosition(), 0)
        binding.recyclerView.smoothScrollBy(0, 1)
    }

    private fun initAdapter() {
        _requestManager = initGlide()
        binding.loadingBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE
        redditListAdapter = RedditListAdapter(RedditPostListenerImpl(viewModel))
        binding.recyclerView.let { recyclerView ->
            recyclerView.setViewModel(viewModel)
            recyclerView.setRequestManager(_requestManager)
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

    private fun initGlide(): RequestManager? {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.white_background)
            .error(R.drawable.white_background)
        return Glide.with(this)
            .setDefaultRequestOptions(options)
    }

    private fun getNextPosition(): Int {
        val layoutManger = binding.recyclerView.layoutManager as LinearLayoutManager
        return layoutManger.findFirstVisibleItemPosition() + 1
    }

    companion object {
        fun newInstance(username: String?): UserDetailFragment {
            val frag = UserDetailFragment()
            val bundle = Bundle()
            bundle.putString(USER_KEY, username)
            frag.arguments = bundle
            return frag
        }
        private const val TAG = "UserDetailFragment"
        private const val USER_KEY = "user.key"
    }
}
