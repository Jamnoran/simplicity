package com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.full

import com.google.gson.annotations.SerializedName
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.Children


data class RepliesDataFull (
  @SerializedName("after"      ) var after     : String?             = null,
  @SerializedName("dist"       ) var dist      : String?             = null,
  @SerializedName("modhash"    ) var modhash   : String?             = null,
  @SerializedName("geo_filter" ) var geoFilter : String?             = null,
  @SerializedName("children"   ) var children  : ArrayList<Children> = arrayListOf(),
  @SerializedName("before"     ) var before    : String?             = null
)