package com.simplicity.simplicityaclientforreddit.ui.main.usecases

import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import okhttp3.MediaType
import okhttp3.RequestBody

class GetRefreshTokenBodyUseCase {
    fun getBody(): RequestBody {
        return RequestBody.create(
            MediaType.parse("application/x-www-form-urlencoded"),
            "grant_type=refresh_token&refresh_token=" + SettingsSP().loadSetting(SettingsSP.KEY_REFRESH_TOKEN, "NOT_AVAILABLE"))
    }
}