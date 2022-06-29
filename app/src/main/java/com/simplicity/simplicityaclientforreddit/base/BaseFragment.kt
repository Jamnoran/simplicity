package com.simplicity.simplicityaclientforreddit.base

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.simplicity.simplicityaclientforreddit.MainActivity
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.menu.values.Keys
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.user.UserDetailActivity
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost

open class BaseFragment : Fragment() {

    private lateinit var _viewModel: BaseViewModel

    fun init(viewModel: BaseViewModel) {
        _viewModel = viewModel
        _viewModel.networkError().observe(this) { observeNetworkError() }
        _viewModel.unAuthorizedError().observe(this) { observeUnAuthorizedError() }
    }

    private fun observeNetworkError() {
        Toast.makeText(requireContext(), getString(R.string.error_network), Toast.LENGTH_LONG).show()
    }

    private fun observeUnAuthorizedError() {
        Toast.makeText(requireContext(), getString(R.string.error_un_authorized), Toast.LENGTH_LONG).show()
    }

    open fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    fun observeHideSub() {
        Toast.makeText(requireContext(), "Sub is now hidden", Toast.LENGTH_LONG).show()
    }

    fun observeOpenCommentsFragment(post: RedditPost) {
        val intent = Intent(activity, SingleFragmentActivity::class.java).apply {
            putExtra(SingleFragmentActivity.KEY_FRAGMENT, SingleFragmentActivity.VALUE_COMMENT)
            putExtra(Keys.KEY_SUB_REDDIT, post.data.subreddit)
            putExtra(Keys.KEY_AUTHOR, post.data.author)
            putExtra(Keys.KEY_ID, post.data.id)
        }
        (activity as MainActivity).startActivityWithAnimation(intent)
    }

    fun observeSubredditClicked(it: String) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(MainActivity.KEY_SUBREDDIT, it)
        (activity as BaseActivity).startActivityWithAnimation(intent)
    }

    fun observeAuthorClicked(it: String) {
        val intent = Intent(context, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.KEY_USER_NAME, it)
        (activity as BaseActivity).startActivityWithAnimation(intent)
    }

    fun observeRedditLinkClicked(it: String) {
        val convertedUrl = "https://www.reddit.com$it"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(convertedUrl))
        startActivity(browserIntent)
    }

    fun observeSendToBrowser(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        Log.i("ListFragment", "Sending this url to browser: $url")
        startActivity(i)
    }

    fun observeWebViewActivityClicked(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}
