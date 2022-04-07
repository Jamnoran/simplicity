package com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments

import com.google.gson.annotations.SerializedName


data class Children (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var childrenData : ChildrenData?   = ChildrenData()

)