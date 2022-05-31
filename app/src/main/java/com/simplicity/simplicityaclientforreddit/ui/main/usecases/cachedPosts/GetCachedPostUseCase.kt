package com.simplicity.simplicityaclientforreddit.ui.main.usecases.cachedPosts

import com.google.gson.Gson
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost

class GetCachedPostUseCase {
    fun execute(): List<RedditPost> {
        val storedValue = SettingsSP().loadSetting(SettingsSP.KEY_CACHED_POSTS, "")
        return if(storedValue.isNotEmpty()) {
            return Gson().fromJson(storedValue, Array<RedditPost>::class.java).toList()
        }else{
            emptyList()
        }
    }
}