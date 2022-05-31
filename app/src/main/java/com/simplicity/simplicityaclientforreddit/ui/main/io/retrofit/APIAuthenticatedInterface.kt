package com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit

import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.JsonResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.search.SearchRedditResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.user.User
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.user.UserResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.user.posts.UserPostsResponse
import retrofit2.Call
import retrofit2.http.*

interface APIAuthenticatedInterface {
    @GET("api/v1/me")
    fun userMe(): Call<User>

    @FormUrlEncoded
    @POST("api/vote")
    fun upVote(@Field("id") id: String, @Field("dir") dir: String = "1"): Call<JsonResponse>

    @FormUrlEncoded
    @POST("api/vote")
    fun downVote(@Field("id") id: String, @Field("dir") dir: String = "-1"): Call<JsonResponse>

    @FormUrlEncoded
    @POST("api/vote")
    fun clearVote(@Field("id") id: String, @Field("dir") dir: String = "0"): Call<JsonResponse>

    @GET("api/search_reddit_names")
    fun searchReddits(@Query(value = "query") query: String,
                      @Query(value = "exact") exact: Boolean = false,
                      @Query(value = "include_over_18") includeOver18: Boolean = true,
                      @Query(value = "include_unadvertisable") includeUnadvertisable: Boolean = true
    ): Call<SearchRedditResponse>

    @GET("user/{username}/about/")
    fun getUser(@Path(value = "username") username: String): Call<UserResponse>

    @GET("user/{username}/submitted/")
    fun getUserPosts(@Path(value = "username") username: String, @Query(value = "after") after: String?): Call<UserPostsResponse>
}