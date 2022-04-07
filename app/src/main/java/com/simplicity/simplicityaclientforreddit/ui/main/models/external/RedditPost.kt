package com.simplicity.simplicityaclientforreddit.ui.main.models.external

import com.google.gson.annotations.SerializedName

data class RedditPost(
    var data: Data
) {
    data class Data(
        var id: String,
        var name: String,
        var title: String? = null,
        @SerializedName("author_fullname")
        var authorFullName: String? = null,
        var author: String? = null,
        var domain: String? = null,
        @SerializedName("num_comments")
        var numComments: Int = 0,
        var subreddit: String,
        @SerializedName("subreddit_name_prefixed")
        var subredditPrefixed: String? = null,
        var thumbnail: String? = null,
        var url: String? = null,
        var permalink: String? = null,
        var selftext: String? = null,
        @SerializedName("url_overridden_by_dest")
        var urlOverriddenByDest: String? = null,
        var over_18: Boolean? = null,
        var ups: Int = 0,
        var downs: Int = 0,
        var score: Int = 0,
        var media: Media?,
        var tournament_data: TournamentData?,
        @SerializedName("media_metadata")
        var mediaMetadata: HashMap<String, MediaMetadata>,
        @SerializedName("secure_media_embed")
        var secureMediaEmbed: SecureMediaEmbed?,
        var is_video: Boolean,
        var is_gallery: Boolean,
        @SerializedName("gallery_data")
        var galleryData: GalleryData,
        @SerializedName("post_hint")
        var postHint: String?,
        var preview: Preview?
    )
}