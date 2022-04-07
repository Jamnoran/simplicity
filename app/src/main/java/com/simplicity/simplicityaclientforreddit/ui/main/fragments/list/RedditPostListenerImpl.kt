package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list

import com.simplicity.simplicityaclientforreddit.ui.main.listeners.RedditPostListener
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost

class RedditPostListenerImpl(private val viewModel: ListViewModel): RedditPostListener {
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
}