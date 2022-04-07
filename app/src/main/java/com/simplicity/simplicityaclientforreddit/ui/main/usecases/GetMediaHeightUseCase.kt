package com.simplicity.simplicityaclientforreddit.ui.main.usecases

import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.enums.PostType

class GetMediaHeightUseCase(val data: RedditPost.Data, val mediaData: MediaData) {
    fun execute(): MediaBaseValues {
        when(GetPostTypeUseCase().execute(data)){
            PostType.LINK -> {
                return MediaBaseValues(0, 0)
            }
            PostType.GALLERY -> {
                return getMediaBaseValues()
            }
            PostType.IMAGE -> {
                return getMediaBaseValues()
            }
            PostType.IS_VIDEO -> {
                return getMediaBaseValues()
            }
            PostType.RICH_VIDEO -> {
                return getMediaBaseValues()
            }
            PostType.TOURNAMENT -> {
                return MediaBaseValues(0, 0)
            }
            PostType.IMGUR_LINK -> {
                return MediaBaseValues(0, 0)
            }
            PostType.NONE -> {

            }
        }
        return MediaBaseValues(0, 0)
    }

    private fun getMediaBaseValues(): MediaBaseValues {
        val widthOfScreen = SettingsSP().loadSetting(SettingsSP.KEY_DEVICE_WIDTH, -1)
        return if (mediaData.baseValues.mediaHeight < mediaData.baseValues.mediaWidth) {
            val ratio: Float = widthOfScreen.toFloat() / mediaData.baseValues.mediaWidth.toFloat()
            val newHeight = ratio * mediaData.baseValues.mediaHeight
//            Log.i(TAG, "Calculations for landscape video : ScreenWidth $widthOfScreen with ratio for video at $ratio from $videoHeight / $videoWidth with new height at $newHeight")
            MediaBaseValues(widthOfScreen, newHeight.toInt())
//            ConstraintLayout.LayoutParams(widthOfScreen, newHeight.toInt())
        }else{
            val ratio: Float = mediaData.baseValues.mediaHeight.toFloat() / mediaData.baseValues.mediaWidth.toFloat()
            val newHeight = ratio * widthOfScreen
//            val params = ConstraintLayout.LayoutParams(0, newHeight.toInt())
//            Log.i(TAG, "Calculations for portrait video : ScreenWidth $widthOfScreen with ratio for video at $ratio from $videoHeight / $videoWidth with new height at $newHeight")
            MediaBaseValues(0, newHeight.toInt())
        }
    }

}