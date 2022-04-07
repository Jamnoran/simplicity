package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.databinding.RedditPostBinding
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.RedditMedia
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.CustomPostBottomSectionView
import com.simplicity.simplicityaclientforreddit.ui.main.listeners.RedditPostListener

class RedditListAdapter(val listener: RedditPostListener, private val layoutWidth: Int) :
    ListAdapter<RedditPost, RedditListAdapter.PostViewHolder>(PostDiffCallback) {

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = RedditPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(listener, layoutWidth, binding)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PostViewHolder(val listener: RedditPostListener,
                         val width: Int, var binding: RedditPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val TAG = "PostViewHolder"
        private var currentPost: RedditPost? = null

        init {

        }

        fun pauseViewHolder(){
            Log.i(TAG, "Got pause on this item : ${currentPost?.data?.title}")
        }

        /* Bind post data. */
        fun bind(post: RedditPost) {
            currentPost = post

            RedditMedia(width, post).init(binding)
            CustomPostBottomSectionView(binding, post, listener).init()

            setUpListeners(post)
//            debug(post, itemView)
            "r/${post.data.subreddit}".also { binding.redditSub.text = it }
            " posted by u/${post.data.author}".also { binding.redditPosted.text = it }

            if(post.data.postHint != "link"){
                "${post.data.title} [${post.data.postHint}]".also { binding.redditTitle.text = it }
                binding.redditTitle.visibility = View.VISIBLE
            }else{
                binding.redditTitle.visibility = View.GONE
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

        private fun debug(post: RedditPost, itemView: View) {
            itemView.findViewById<TextView>(R.id.reddit_title).text = post.data.title
            itemView.findViewById<View>(R.id.reddit_media).visibility = View.GONE
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<RedditPost>() {
    override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
        return oldItem.data.id == newItem.data.id
    }
}