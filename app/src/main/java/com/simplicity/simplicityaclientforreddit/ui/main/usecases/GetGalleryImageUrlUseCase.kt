package com.simplicity.simplicityaclientforreddit.ui.main.usecases

import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost


class GetGalleryImageUrlUseCase {
    fun execute(data: RedditPost.Data, positionToGetImage: Int): GalleryItem {
        val galleryItem = GalleryItem(null, null, null)
        for((i, item) in data.galleryData.items.withIndex()){
            val previousPosition = positionToGetImage - 1
            val nextPosition = positionToGetImage + 1

            // Get previous image
            if(i > 0 && i == previousPosition){
                galleryItem.previous = getMediaData(data, item.media_id)
            }
            // Get current image
            if(i == positionToGetImage){
                galleryItem.current = getMediaData(data, item.media_id)
            }
            // Get next image
            if(nextPosition < data.galleryData.items.size && i == nextPosition){
                galleryItem.next = getMediaData(data, item.media_id)
            }
        }
        return galleryItem
    }

    private fun getMediaData(data: RedditPost.Data, mediaId: String): MediaData? {
        val mediaData = data.mediaMetadata[mediaId]
        mediaData?.let{
            if(it.s.gif != null){ return MediaData(getConvertedImageUrl(it.s.gif), getRatio(it.s.x, it.s.y), MediaBaseValues(it.s.x, it.s.y)) }
            if(it.s.u != null){ return MediaData(getConvertedImageUrl(it.s.u), getRatio(it.s.x, it.s.y), MediaBaseValues(it.s.x, it.s.y)) }

            if(it.p.isNotEmpty()){ return MediaData(getConvertedImageUrl(it.p.first().u), getRatio(it.p.first().x, it.p.first().y), MediaBaseValues(it.p.first().x, it.p.first().y)) }

        }
        return null
    }

    fun getRatio(y: Int, x: Int): Float? {
        return y.toFloat() / x.toFloat()
    }

    private fun getConvertedImageUrl(s: String): String {
        return s.replace("&amp;", "&")
    }

}
class GalleryItem(var previous: MediaData?, var current: MediaData?, var next: MediaData?)
class MediaData(var mediaUrl: String, var imageRatio: Float?, var baseValues: MediaBaseValues)
class MediaBaseValues(var mediaWidth: Int, var mediaHeight: Int)