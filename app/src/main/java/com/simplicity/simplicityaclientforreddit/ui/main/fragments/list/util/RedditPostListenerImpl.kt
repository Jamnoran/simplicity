package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util

import com.simplicity.simplicityaclientforreddit.base.BasePostsListViewModel
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.ListViewModel
import com.simplicity.simplicityaclientforreddit.ui.main.listeners.RedditPostListener
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost

class RedditPostListenerImpl(private val viewModel: BasePostsListViewModel): RedditPostListener {
    override fun voteUp(post: RedditPost) {
        viewModel.upVote(post)
    }

    override fun voteDown(post: RedditPost) {
        viewModel.downVote(post)
    }

    override fun linkClicked(post: RedditPost) {
        viewModel.linkClicked(post)
    }

    override fun commentsClicked(post: RedditPost) {
        viewModel.commentsClicked(post)
    }

    override fun redditLinkClicked(post: RedditPost) {
        viewModel.redditLinkClicked(post)
    }

    override fun subRedditClicked(post: RedditPost) {
        viewModel.subRedditClicked(post.data.subreddit)
    }

    override fun authorClicked(post: RedditPost) {
        post.data.author?.let{
            viewModel.authorClicked(it)
        }
    }

    override fun hideSubClicked(post: RedditPost) {
        post.data.subreddit.let{ subReddit ->
            viewModel.hideSub(subReddit)
        }
    }

    override fun directLinkClicked(link: String) {
        viewModel.directLinkClicked(link)
    }

    override fun directAuthorClicked(author: String) {
        viewModel.authorClicked(author)
    }

    override fun directRedditClicked(reddit: String) {
        viewModel.subRedditClicked(reddit)
    }
}