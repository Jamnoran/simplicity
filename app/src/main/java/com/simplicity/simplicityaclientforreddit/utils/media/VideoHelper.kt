package com.simplicity.simplicityaclientforreddit.utils.media

class VideoHelper {
    companion object {
        fun getAudioUrl(url: String): String {
            val firstPart = url.substring(0, url.indexOf("/DASH_") + 5)
            val firstRemoved = url.substring(firstPart.length, url.length)
            val positionOfDot = firstRemoved.indexOf(".")
            val rest = firstRemoved.substring(positionOfDot, firstRemoved.length)
            return "${firstPart}_audio${rest}"
        }
    }
}