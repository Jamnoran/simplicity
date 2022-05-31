package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews

import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.TextView
import com.simplicity.simplicityaclientforreddit.databinding.RedditPostBinding
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.mediaTypes.MediaTypeGallery
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.mediaTypes.MediaTypeImage
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.mediaTypes.MediaTypeLink
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.mediaTypes.MediaTypeVideo
import com.simplicity.simplicityaclientforreddit.ui.main.listeners.RedditPostListener
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.enums.PostType
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.text.GetFormattedTextUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetPostTypeUseCase


class RedditMedia(val post: RedditPost, val listener: RedditPostListener) {
    lateinit var binding : RedditPostBinding

    fun init(postBinding: RedditPostBinding) {
        binding = postBinding
        setUpViews()
        setUpDebug(post)
        post.data.let { data->
            // Show if data exists
            when(GetPostTypeUseCase().execute(data)){
                PostType.LINK -> {
                    MediaTypeLink(data, binding).show()
                }
                PostType.GALLERY -> {
                    MediaTypeGallery(data, binding).show()
                }
                PostType.IMAGE -> {
                    MediaTypeImage(data,binding).show()
                }
                PostType.IS_VIDEO -> {
                    MediaTypeVideo(data,binding).show()
                }
                PostType.RICH_VIDEO -> {
                    MediaTypeVideo(data,binding).show()
                }
                PostType.TOURNAMENT -> {
                    binding.root.visibility = View.GONE
                }
                PostType.IMGUR_LINK -> {
                    data.urlOverriddenByDest?.let{
                        MediaTypeLink(data, binding).show()
                    }
                }
                PostType.NONE -> {
                }
            }

            data.selftext?.let{
                binding.redditMedia.redditDescriptionLayout.redditTextContent.visibility = View.VISIBLE

                binding.redditMedia.redditDescriptionLayout.redditTextContent.movementMethod = LinkMovementMethod.getInstance()
                binding.redditMedia.redditDescriptionLayout.redditTextContent.setText(
                    GetFormattedTextUseCase(listener).execute(it), TextView.BufferType.SPANNABLE)
            }
        }
    }

    private fun setUpDebug(post: RedditPost) {
        binding.bottomLayout.debug.setOnClickListener { Log.i("DEBUG", "$post") }
        binding.bottomLayout.debug.visibility = View.GONE
    }

    private fun setUpViews() {
        binding.root.visibility = View.VISIBLE
        // Images
        binding.redditMedia.galleryLayout.root.visibility = View.GONE
        binding.redditMedia.galleryLayout.redditGalleryCount.visibility = View.GONE
        binding.redditMedia.redditImageLayout.redditImage.visibility = View.GONE
        // Web
        binding.redditMedia.redditWebLayout.redditWebview.visibility = View.GONE
        binding.redditMedia.redditWebLayout.redditWebview.loadUrl("about:blank")
        // Link
        binding.redditMedia.redditLinkLayout.redditLinkPreview.visibility = View.GONE
        binding.redditMedia.redditLinkLayout.redditLinkSrc.visibility = View.GONE
        binding.redditMedia.redditLinkLayout.redditLinkDescription.visibility = View.GONE
        // Description
        binding.redditMedia.redditDescriptionLayout.redditTextContent.visibility = View.GONE
        // Video
        binding.redditMedia.redditCustomPlayer.customPlayer.visibility = View.GONE
        binding.redditMedia.redditCustomPlayer.customPlayerControllers.visibility = View.GONE

        binding.redditMedia.redditVideoPlayer.redditVideoPlayerLayout.visibility = View.GONE
    }
}