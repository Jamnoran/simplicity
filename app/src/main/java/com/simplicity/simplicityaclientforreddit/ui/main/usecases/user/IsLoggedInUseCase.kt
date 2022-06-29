package com.simplicity.simplicityaclientforreddit.ui.main.usecases.user

import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP

class IsLoggedInUseCase {
    fun execute(): Boolean {
        val accessToken = SettingsSP().loadSetting(SettingsSP.KEY_ACCESS_TOKEN, null)
        return !accessToken.isNullOrBlank()
    }
}
