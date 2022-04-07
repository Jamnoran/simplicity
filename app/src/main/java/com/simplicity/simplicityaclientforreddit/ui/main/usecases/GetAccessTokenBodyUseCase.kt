package com.simplicity.simplicityaclientforreddit.ui.main.usecases

import okhttp3.MediaType
import okhttp3.RequestBody

class GetAccessTokenBodyUseCase {

    fun getBody(code: String): RequestBody {
        return RequestBody.create(
            MediaType.parse("application/x-www-form-urlencoded"),
            "grant_type=authorization_code&code=" + code +
                    "&redirect_uri=" + REDIRECT_URI)

    }

    companion object {
        const val REDIRECT_URI = "simplicity://com.simplicity/redirect"
    }

}