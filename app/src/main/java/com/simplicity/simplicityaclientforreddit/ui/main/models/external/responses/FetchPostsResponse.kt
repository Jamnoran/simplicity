package com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses

import com.google.gson.annotations.SerializedName
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost

class FetchPostsResponse(
    @SerializedName("data")
    val data: Data
){
    class Data(
        @SerializedName("after")
        val after: String,
        @SerializedName("dist")
        val dist: Int,
        @SerializedName("children")
        val children: List<RedditPost>
    )
}