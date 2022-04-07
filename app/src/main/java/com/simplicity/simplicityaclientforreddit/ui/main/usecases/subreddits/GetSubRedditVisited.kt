package com.simplicity.simplicityaclientforreddit.ui.main.usecases.subreddits

import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP

class GetSubRedditVisited {
    fun execute(): List<String> {
        val storedValue = SettingsSP().loadSetting(SettingsSP.KEY_PREVIOUS_VISITED_SUBREDDITS, "")
        return if(storedValue.isNotEmpty()){
            storedValue.split(",").map { it.trim() }
        }else{
            emptyList()
        }
    }
}