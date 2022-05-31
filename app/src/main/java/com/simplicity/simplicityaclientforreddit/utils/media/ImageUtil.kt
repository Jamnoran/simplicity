package com.simplicity.simplicityaclientforreddit.utils.media

import android.widget.ImageView
import com.squareup.picasso.Picasso

class ImageUtil {
    companion object {
        @JvmStatic
        fun load(drawableResource: Int, volumeControl: ImageView) {
            val picasso = Picasso.get()
            picasso
                .load(drawableResource)
                .into(volumeControl)
        }

        fun getConvertedImageUrl(s: String): String {
            return s.replace("&amp;", "&")
        }
    }
}