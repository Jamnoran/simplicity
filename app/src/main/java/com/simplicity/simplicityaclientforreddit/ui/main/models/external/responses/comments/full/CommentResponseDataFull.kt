package com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.full

import com.google.gson.annotations.SerializedName


data class CommentResponseDataFull (

  @SerializedName("after"      ) var after     : String?             = null,
  @SerializedName("dist"       ) var dist      : Int?                = null,
  @SerializedName("modhash"    ) var modhash   : String?             = null,
  @SerializedName("geo_filter" ) var geoFilter : String?             = null,
  @SerializedName("children"   ) var children  : ArrayList<ChildrenFull> = arrayListOf(),
  @SerializedName("before"     ) var before    : String?             = null

)