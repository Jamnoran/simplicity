package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.mediaTypes

import android.view.View
import com.simplicity.simplicityaclientforreddit.databinding.RedditPostBinding
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost
import com.simplicity.simplicityaclientforreddit.utils.media.CustomPlayer

class MediaTypeVideo(data: RedditPost.Data, binding: RedditPostBinding): BaseMediaType(data, binding) {
    override fun show() {
        binding.redditMedia.redditCustomPlayer.customPlayer.visibility = View.VISIBLE
        binding.redditMedia.redditCustomPlayer.customPlayerControllers.visibility = View.VISIBLE
        val player = CustomPlayer(binding.redditMedia.redditCustomPlayer, data, binding.root.context)
        player.init()
        binding.redditMedia.redditCustomPlayer.customPlayer.layoutParams = player.getVideoParams()
    }

}