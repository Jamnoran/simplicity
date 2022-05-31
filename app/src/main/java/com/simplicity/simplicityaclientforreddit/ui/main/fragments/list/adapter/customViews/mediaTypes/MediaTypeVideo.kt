package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.mediaTypes

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import com.simplicity.simplicityaclientforreddit.databinding.RedditPostBinding
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.*

class MediaTypeVideo(data: RedditPost.Data, binding: RedditPostBinding): BaseMediaType(data, binding) {
    override fun show() {
//        binding.redditMedia.redditCustomPlayer.customPlayer.visibility = View.VISIBLE
//        binding.redditMedia.redditCustomPlayer.customPlayerControllers.visibility = View.VISIBLE
//        val player = CustomPlayer(binding.redditMedia.redditCustomPlayer, data, binding.root.context)
//        player.init()
//        binding.redditMedia.redditCustomPlayer.customPlayer.layoutParams = player.getVideoParams()

        binding.redditMedia.redditVideoPlayer.let{ view ->
            val postMediaData = GetMediaDataUseCase().execute(data)
            val mediaBaseValues = GetMediaBaseValuesUseCase(data, postMediaData).execute()

            binding.redditMedia.redditVideoPlayer.redditVideoPlayerLayout.visibility = View.VISIBLE
            val htmlString = data.preview!!.images.first().source.url
            val url = if (Build.VERSION.SDK_INT >= 24) {
                Html.fromHtml(htmlString , Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                Html.fromHtml(htmlString).toString()
            }
//            this.requestManager
//                ?.load(url)
//                ?.into(view.thumbnail)
            loadImage(MediaData(url, GetGalleryImageUrlUseCase().getRatio(mediaBaseValues.mediaHeight, mediaBaseValues.mediaWidth), null, MediaBaseValues(mediaBaseValues.mediaHeight, mediaBaseValues.mediaWidth)), view.thumbnail)
            view.thumbnail.scaleType = ImageView.ScaleType.FIT_XY
//            view.thumbnail.layoutParams.height = mediaBaseValues.mediaHeight
        }
    }

}