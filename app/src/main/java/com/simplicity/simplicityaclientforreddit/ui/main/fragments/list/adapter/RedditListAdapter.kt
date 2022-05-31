package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.simplicity.simplicityaclientforreddit.databinding.RedditPostBinding
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util.PostViewHolder
import com.simplicity.simplicityaclientforreddit.ui.main.listeners.RedditPostListener
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.cachedPosts.RemoveCachedPostUseCase


class RedditListAdapter(val listener: RedditPostListener) : ListAdapter<RedditPost, PostViewHolder>(PostDiffCallback) {

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = RedditPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(listener, binding)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: PostViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.binding.redditTitle.text?.let{
            if(holder.showned){
                Log.i("RedditListAdapter", ">>: $it")
                holder.post?.let{ post->
                    RemoveCachedPostUseCase(post).execute()
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: PostViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.showned = true
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