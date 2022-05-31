package com.simplicity.simplicityaclientforreddit.ui.main.usecases.subreddits

import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP

class RemoveSubRedditVisitedUseCase(var subreddit:String) {
    fun execute(){
        val array = GetSubRedditVisitedUseCase().execute()
        val mutableList = array.toMutableList()
        mutableList.remove(subreddit)
        SettingsSP().saveSetting(SettingsSP.KEY_PREVIOUS_VISITED_SUBREDDITS, mutableList.toList().joinToString(","))
    }
}