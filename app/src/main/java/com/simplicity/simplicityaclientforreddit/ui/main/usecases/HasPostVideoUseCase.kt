package com.simplicity.simplicityaclientforreddit.ui.main.usecases

import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.enums.PostType


class HasPostVideoUseCase {
    fun execute(data: RedditPost.Data): Boolean {
        when(GetPostTypeUseCase().execute(data)){
            PostType.LINK -> {
                return false
            }
            PostType.GALLERY -> {
                return false
            }
            PostType.IMAGE -> {
                return false
            }
            PostType.IS_VIDEO -> {
                return true
            }
            PostType.RICH_VIDEO -> {
                return true
            }
            PostType.TOURNAMENT -> {
                return false
            }
            PostType.IMGUR_LINK -> {
                return false
            }
            PostType.NONE -> {
                return false
            }
        }
    }
}