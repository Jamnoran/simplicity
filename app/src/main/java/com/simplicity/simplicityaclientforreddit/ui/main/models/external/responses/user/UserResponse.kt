package com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.user

import com.google.gson.annotations.SerializedName


data class UserResponse (
  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var data : User?   = User()
)