package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.mediaTypes

import android.content.Context
import android.util.Log
import android.view.View
import com.simplicity.simplicityaclientforreddit.databinding.RedditPostBinding
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GalleryItem
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetGalleryImageUrlUseCase

class MediaTypeGallery(data: RedditPost.Data, binding: RedditPostBinding): BaseMediaType(data, binding) {
    override fun show() {
        binding.redditMedia.galleryLayout.root.visibility = View.VISIBLE
        binding.redditMedia.galleryLayout.redditGalleryCount.visibility = View.VISIBLE
        var galleryCountText = ""
        data.mediaMetadata?.let{ mediaMetaData ->
             galleryCountText = "1/${mediaMetaData.size}"

        }
        binding.redditMedia.galleryLayout.redditGalleryCount.text = galleryCountText

        var position = 0
        var galleryData = GetGalleryImageUrlUseCase().execute(data, position)
        loadGallery(galleryData, binding.root.context)
        binding.redditMedia.galleryLayout.navigateLeft.setOnClickListener {
            if(position > 0) position--
            galleryData = GetGalleryImageUrlUseCase().execute(data, position)
            Log.i("RedditMedia", "User clicked Left, showing picture on position $position with this image ${galleryData.current}")
            galleryData.current?.let {
                loadGallery(galleryData, binding.root.context)
                galleryCountText = "${(position + 1)}/${data.mediaMetadata?.size}"
                binding.redditMedia.galleryLayout.redditGalleryCount.text = galleryCountText
            }
        }
        binding.redditMedia.galleryLayout.navigateRight.setOnClickListener {
            if((position + 1) < data.mediaMetadata?.size?: 0) position++
            galleryData = GetGalleryImageUrlUseCase().execute(data, position)
            Log.i("RedditMedia", "User clicked Right, showing picture on position $position with this image ${galleryData.current}")
            galleryData.current?.let {
                loadGallery(galleryData, binding.root.context)
                galleryCountText = "${(position + 1)}/${data.mediaMetadata?.size}"
                binding.redditMedia.galleryLayout.redditGalleryCount.text = galleryCountText
            }
        }
    }

    private fun loadGallery(galleryData: GalleryItem, context: Context) {
        loadImageOrGif(galleryData.current, context)
        galleryData.next?.let{
            if (it.mediaUrl.isNotEmpty()) {
                preLoadImageOrGif(it, context)
            }
        }
    }
}