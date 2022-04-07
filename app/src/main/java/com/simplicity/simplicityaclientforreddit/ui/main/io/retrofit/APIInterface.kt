package com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit

import com.simplicity.simplicityaclientforreddit.ui.main.models.external.AccessToken
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.FetchPostsResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.JsonResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.CommentResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @Headers("User-Agent: Sample App")
    @POST("api/v1/access_token")
    fun accessToken(@Header("Authorization") authorization: String, @Body body: RequestBody): Call<AccessToken>

    @GET("r/all/.json")
    fun getPost(@Query(value = "after") after: String?): Call<FetchPostsResponse>

    /**
     * include_over_18=on / off
     */
    @GET("r/all/.json")
    fun getPosts(@Query(value = "after") after: String?, @Query("include_over_18") includeOver18: String?): Call<FetchPostsResponse>

    @GET("r/valheim/.json")
    fun getSubRedditPosts(@Query(value = "after") after: String?): Call<FetchPostsResponse>

//    @GET("r/SluttyConfessions/.json")
//    fun getSluttySubRedditPosts(@Query(value = "after") after: String?): Call<JsonResponse>
    @GET("r/gonewild/.json")
    fun getSluttySubRedditPosts(@Query(value = "after") after: String?): Call<FetchPostsResponse>

    //https://www.reddit.com/r/sweden/comments/tej2nj.json
    @GET("r/{subreddit}/comments/{id}.json")
    fun getComments(@Path(value = "subreddit") subreddit: String, @Path(value = "id") id: String): Call<ArrayList<CommentResponse>>
}