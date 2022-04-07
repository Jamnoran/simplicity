package com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments

import com.google.gson.annotations.SerializedName


data class Replies (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var repliesData : RepliesData?   = RepliesData()

)