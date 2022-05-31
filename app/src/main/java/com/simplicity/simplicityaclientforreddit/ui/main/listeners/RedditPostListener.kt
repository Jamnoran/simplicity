package com.simplicity.simplicityaclientforreddit.ui.main.listeners

import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost

interface RedditPostListener {
        fun voteUp(post: RedditPost)
        fun voteDown(post: RedditPost)
        fun linkClicked(post: RedditPost)
        fun commentsClicked(post: RedditPost)
        fun redditLinkClicked(post: RedditPost)
        fun subRedditClicked(post: RedditPost)
        fun authorClicked(post: RedditPost)
        fun hideSubClicked(post: RedditPost)
        fun directLinkClicked(link: String)
        fun directAuthorClicked(author: String)
        fun directRedditClicked(reddit: String)
}
