package com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit

import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.JsonResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.user.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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
}