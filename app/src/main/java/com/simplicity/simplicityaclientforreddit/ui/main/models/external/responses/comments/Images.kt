package com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments

import com.google.gson.annotations.SerializedName


data class Images (

  @SerializedName("source"      ) var source      : Source?                = Source(),
  @SerializedName("resolutions" ) var resolutions : ArrayList<Resolutions> = arrayListOf(),
  @SerializedName("id"          ) var id          : String?                = null

)