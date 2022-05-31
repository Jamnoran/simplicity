package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.databinding.RedditPostBinding
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.CustomPostBottomSectionView
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.RedditMedia
import com.simplicity.simplicityaclientforreddit.ui.main.listeners.RedditPostListener
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.text.GetFormattedTextUseCase

class PostViewHolder(val listener: RedditPostListener, var binding: RedditPostBinding) : RecyclerView.ViewHolder(binding.root) {
    var showned: Boolean = false
    private val TAG = "PostViewHolder"
    var post: RedditPost? = null

    /* Bind post data. */
    fun bind(post: RedditPost) {
        this.post = post

        binding.root.tag = this
        RedditMedia(post, listener).init(binding)
        CustomPostBottomSectionView(binding, post, listener).init()

        setUpListeners(post)
        binding.redditSub.text = binding.root.context.getString(R.string.sub_reddit, post.data.subreddit)
        binding.redditPosted.text = binding.root.context.getString(R.string.post_posted_by, post.data.author)

        if(post.data.postHint == "link"){
            binding.redditTitle.visibility = View.GONE
        }else{
            binding.redditTitle.text = binding.root.context.getString(R.string.reddit_title, GetFormattedTextUseCase(listener).execute(post.data.title?:""), post.data.postHint)
            binding.redditTitle.visibility = View.VISIBLE
        }
    }

    private fun setUpListeners(currentPost: RedditPost) {
        currentPost.let{ post ->
            binding.redditTitle.setOnClickListener { listener.redditLinkClicked(post) }
            binding.redditMedia.redditLinkLayout.redditLinkDescription.setOnClickListener { listener.redditLinkClicked(post) }
            binding.redditMedia.redditLinkLayout.redditLinkPreview.setOnClickListener { listener.linkClicked(post) }
            binding.redditMedia.redditLinkLayout.redditLinkSrc.setOnClickListener { listener.linkClicked(post) }

            binding.redditSub.setOnClickListener { listener.subRedditClicked(post) }
            binding.redditPosted.setOnClickListener { listener.authorClicked(post) }

            binding.bottomLayout.hideSub.setOnClickListener { listener.hideSubClicked(post) }
        }
    }

    fun getThumbnail(): ImageView{
        return binding.redditMedia.redditVideoPlayer.thumbnail
    }

    fun getProgressBar(): ProgressBar{
        return binding.redditMedia.redditVideoPlayer.progressBar
    }

    fun getVolumeControl(): ImageView{
        return binding.redditMedia.redditVideoPlayer.volumeControl
    }

    fun getFrameLayout(): FrameLayout{
        return binding.redditMedia.redditVideoPlayer.mediaContainer
    }

}