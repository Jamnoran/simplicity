package com.simplicity.simplicityaclientforreddit.ui.main.usecases

import android.util.Log
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.enums.PostType


class GetPostTypeUseCase {
    fun execute(data: RedditPost.Data): PostType {
        if (data.is_video) {
//            Log.i("GetPostTypeUseCase", "PostType.IS_VIDEO")
            return PostType.IS_VIDEO
        }
        if(data.postHint == "link" && containsImgur(data)){
//            Log.i("GetPostTypeUseCase", "PostType.IMGUR_LINK")
            return PostType.IMGUR_LINK
        }

        if (data.is_gallery) {
//            Log.i("GetPostTypeUseCase", "PostType.GALLERY")
            return PostType.GALLERY
        }
        if(data.tournament_data != null){
//            Log.i("GetPostTypeUseCase", "PostType.TOURNAMENT")
            return PostType.TOURNAMENT
        }

        // Specific postHint types
        return when (data.postHint) {
            "link" -> {
//                Log.i("GetPostTypeUseCase", "PostType.LINK")
                PostType.LINK
            }
            "rich:video" -> {
//                Log.i("GetPostTypeUseCase", "PostType.RICH_VIDEO")
                PostType.RICH_VIDEO
            }
            "image" -> {
//                Log.i("GetPostTypeUseCase", "PostType.IMAGE")
                PostType.IMAGE
            }
            else -> {
//                Log.i("GetPostTypeUseCase", "PostType.NONE")
                PostType.NONE
            }
        }
    }

    private fun containsImgur(data: RedditPost.Data): Boolean {
        if(data.url != null && data.url!!.contains("imgur.com")){
            return true
        }
        return false
    }
}