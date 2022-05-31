package com.simplicity.simplicityaclientforreddit.ui.main.usecases.cachedPosts

import com.google.gson.Gson
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost

class RemoveCachedPostUseCase(var subreddit: RedditPost) {
    fun execute(){
        val array = GetCachedPostUseCase().execute()
        val mutableList = array.toMutableList()
        mutableList.remove(subreddit)
        val newValueToStore = Gson().toJson(mutableList)
        SettingsSP().saveSetting(SettingsSP.KEY_CACHED_POSTS, newValueToStore)
    }
}