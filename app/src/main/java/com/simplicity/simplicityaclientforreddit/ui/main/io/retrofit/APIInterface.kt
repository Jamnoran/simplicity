package com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit

import com.simplicity.simplicityaclientforreddit.ui.main.models.external.AccessToken
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.FetchPostsResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.CommentResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.user.UserResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @Headers("User-Agent: Sample App")
    @POST("api/v1/access_token")
    fun accessToken(@Header("Authorization") authorization: String, @Body body: RequestBody): Call<AccessToken>

    /**
     * default should be all
     * include_over_18=on / off
     */
    @GET("r/{subreddit}/.json")
    fun getPosts(@Path(value = "subreddit") subreddit: String = "all", @Query(value = "after") after: String?, @Query("include_over_18") includeOver18: String?): Call<FetchPostsResponse>

    //https://www.reddit.com/r/sweden/comments/tej2nj.json
    @GET("r/{subreddit}/comments/{id}.json")
    fun getComments(@Path(value = "subreddit") subreddit: String, @Path(value = "id") id: String): Call<ArrayList<CommentResponse>>

}