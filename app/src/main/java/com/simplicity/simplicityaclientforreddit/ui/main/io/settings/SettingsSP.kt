package com.simplicity.simplicityaclientforreddit.ui.main.io.settings

import android.content.SharedPreferences
import com.simplicity.simplicityaclientforreddit.ui.main.Global


class SettingsSP() {
    fun saveSetting(key: String, value: Boolean){
        val editor = getPreferences().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun saveSetting(key: String, value: String){
        val editor = getPreferences().edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun saveSetting(key: String, value: Int){
        val editor = getPreferences().edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun loadSetting(key:String, defaultValue: Boolean): Boolean {
        val preferences = getPreferences()
        return preferences.getBoolean(key, defaultValue)
    }

    fun loadSetting(key:String, defaultValue: String): String {
        val preferences = getPreferences()
        return preferences.getString(key, defaultValue)?: defaultValue
    }

    fun loadSetting(key:String, defaultValue: Int): Int {
        val preferences = getPreferences()
        return preferences.getInt(key, defaultValue)
    }

    private fun getPreferences(): SharedPreferences {
        return Global.applicationContext
            .getSharedPreferences("RedditPreferences", 0) // 0 - for private mode
    }

    companion object {
        const val KEY_DEVICE_HEIGHT = "DEVICE_HEIGHT"
        const val KEY_DEVICE_WIDTH = "DEVICE_WIDTH"
        const val KEY_MUTE_VIDEOS = "MUTE_VIDEOS"
        const val KEY_PREVIOUS_VISITED_SUBREDDITS = "KEY_PREVIOUS_VISITED_SUBREDDITS"
        const val KEY_CACHED_POSTS = "KEY_CACHED_POSTS"
        const val KEY_CODE = "CODE"
        const val KEY_STATE = "STATE"
        const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"
        const val KEY_REFRESH_TOKEN = "REFRESH_TOKEN"
    }
}