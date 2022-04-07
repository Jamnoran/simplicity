package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.mediaTypes

import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import com.simplicity.simplicityaclientforreddit.databinding.RedditPostBinding
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost
import com.squareup.picasso.Picasso

class MediaTypeLink(data: RedditPost.Data, binding: RedditPostBinding): BaseMediaType(data, binding) {
    override fun show() {
        binding.redditMedia.redditLinkLayout.redditLinkDescription.visibility = View.VISIBLE
        binding.redditMedia.redditLinkLayout.redditLinkSrc.visibility = View.VISIBLE

        data.title?.let{
            binding.redditMedia.redditLinkLayout.redditLinkDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(it)
            }
        }
        data.domain?.let{binding.redditMedia.redditLinkLayout.redditLinkSrc.text = it}

        data.thumbnail?.let {
            binding.redditMedia.redditLinkLayout.redditLinkPreview.visibility = View.VISIBLE
            val picasso = Picasso.get()
            picasso.load(it)
                .into(binding.redditMedia.redditLinkLayout.redditLinkPreview)

            Log.i("RedditMedia", "Found image with urlOverriddenByDest $it")
        }
    }
}