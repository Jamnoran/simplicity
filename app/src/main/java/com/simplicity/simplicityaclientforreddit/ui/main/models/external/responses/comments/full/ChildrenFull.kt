package com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.full

import com.google.gson.annotations.SerializedName
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.ChildrenData


data class ChildrenFull (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var childrenData : ChildrenData?   = ChildrenData()

)