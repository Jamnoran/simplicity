package com.simplicity.simplicityaclientforreddit.ui.main.usecases.subreddits

import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP

class AddSubRedditVisited(var subreddit:String) {
    fun execute(){
        var array = GetSubRedditVisited().execute()
        array = array.plus(subreddit)
        val newValueToStore = array.joinToString(",")
        SettingsSP().saveSetting(SettingsSP.KEY_PREVIOUS_VISITED_SUBREDDITS, newValueToStore)
    }
}