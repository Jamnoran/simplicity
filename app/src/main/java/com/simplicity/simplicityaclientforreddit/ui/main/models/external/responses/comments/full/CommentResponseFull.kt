package com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.full

import com.google.gson.annotations.SerializedName


data class CommentResponseFull (
  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var commentResponseData : CommentResponseDataFull?   = CommentResponseDataFull()
)