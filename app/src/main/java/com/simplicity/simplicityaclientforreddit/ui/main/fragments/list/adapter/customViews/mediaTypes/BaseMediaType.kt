package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.mediaTypes

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.databinding.RedditPostBinding
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetGalleryImageUrlUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.MediaBaseValues
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.MediaData
import com.squareup.picasso.Picasso

open class BaseMediaType(var data: RedditPost.Data, var binding: RedditPostBinding){
    init { }
    open fun show(){}


    internal fun loadImageOrGif(urlForPicture: MediaData?, context: Context) {
        urlForPicture?.let {
            if(it.mediaUrl.contains(".gif")){
                loadGif(it.mediaUrl, context, binding.redditMedia.redditImageLayout.redditImage)
            }else{
                loadImage(it, binding.redditMedia.redditImageLayout.redditImage)
            }
        }
    }
    internal fun preLoadImageOrGif(mediaData: MediaData?, context: Context) {
        mediaData?.let {
            if(it.mediaUrl.contains(".gif")){
                loadGif(it.mediaUrl, context, binding.redditMedia.secondaryRedditImageLayout.redditImage)
            }else{
                loadImage(mediaData, binding.redditMedia.secondaryRedditImageLayout.redditImage)
            }
        }
    }

    private fun showImage(data: RedditPost.Data, context: Context) {
        data.urlOverriddenByDest?.let {
            if(it.contains(".gif")){
                loadGif(it, context, binding.redditMedia.redditImageLayout.redditImage)
            }else{
                data.preview?.images?.first()?.source?.let{source ->
                    loadImage(MediaData(it, GetGalleryImageUrlUseCase().getRatio(source.height, source.width), MediaBaseValues(source.height, source.width)), binding.redditMedia.redditImageLayout.redditImage)
                }
            }
        }
    }

    internal fun loadGif(it: String, context: Context, view: ImageView) {
        view.visibility = View.VISIBLE
        Glide
            .with(context)
            .load(it)
            .error(R.drawable.error_download) // show error drawable if the image is not a gif
            .into(view)
    }

    internal fun loadImage(it: MediaData, view: ImageView) {
//        view.layoutParams.height = GetMediaHeightUseCase(post.data, it).execute().mediaHeight
        view.requestLayout();
        view.visibility = View.VISIBLE
        val picasso = Picasso.get()
        val width = SettingsSP().loadSetting(SettingsSP.KEY_DEVICE_WIDTH, 0)
        if (width > 0) {
            picasso
                .load(it.mediaUrl)
                .resize(width, 0)
                .into(view)
        } else {
            picasso
                .load(it.mediaUrl)
                .resize(800, 0)
                .into(view)
        }
    }

}