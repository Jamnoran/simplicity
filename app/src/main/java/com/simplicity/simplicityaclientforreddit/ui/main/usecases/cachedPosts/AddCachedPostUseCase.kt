package com.simplicity.simplicityaclientforreddit.ui.main.usecases.cachedPosts

import com.google.gson.Gson
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost

class AddCachedPostUseCase(var subreddit:RedditPost) {
    fun execute(){
        var array = GetCachedPostUseCase().execute()
        array = array.plus(subreddit)
        val newValueToStore = Gson().toJson(array)
        SettingsSP().saveSetting(SettingsSP.KEY_CACHED_POSTS, newValueToStore)
    }
}