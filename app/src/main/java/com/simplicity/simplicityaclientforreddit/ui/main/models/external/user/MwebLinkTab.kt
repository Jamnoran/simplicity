package com.simplicity.simplicityaclientforreddit.ui.main.models.external.user

import com.google.gson.annotations.SerializedName


data class MwebLinkTab (

  @SerializedName("owner"         ) var owner        : String? = null,
  @SerializedName("variant"       ) var variant      : String? = null,
  @SerializedName("experiment_id" ) var experimentId : Int?    = null

)