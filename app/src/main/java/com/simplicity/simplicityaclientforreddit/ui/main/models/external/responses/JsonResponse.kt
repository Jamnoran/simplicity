package com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses

import com.google.gson.annotations.SerializedName

class JsonResponse(
    @SerializedName("code")
    val data: Integer){ }