package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.mediaTypes

import com.simplicity.simplicityaclientforreddit.databinding.RedditPostBinding
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetGalleryImageUrlUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.MediaBaseValues
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.MediaData

class MediaTypeImage(data: RedditPost.Data, binding: RedditPostBinding): BaseMediaType(data, binding) {
    override fun show() {
        data.urlOverriddenByDest?.let {
            if(it.contains(".gif")){
                loadGif(it, binding.root.context, binding.redditMedia.redditImageLayout.redditImage)
            }else{
                data.preview?.images?.first()?.source?.let{source ->
                    loadImage(MediaData(it, GetGalleryImageUrlUseCase().getRatio(source.height, source.width), MediaBaseValues(source.height, source.width)), binding.redditMedia.redditImageLayout.redditImage)
                }
            }
        }
    }
}