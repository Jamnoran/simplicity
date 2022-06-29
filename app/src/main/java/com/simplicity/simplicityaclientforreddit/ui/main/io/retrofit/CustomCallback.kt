package com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection

interface CustomCallback<T> : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        when (response.code()) {
            HttpsURLConnection.HTTP_OK,
            HttpsURLConnection.HTTP_CREATED,
            HttpsURLConnection.HTTP_ACCEPTED,
            HttpsURLConnection.HTTP_NOT_AUTHORITATIVE ->
                response.body()?.apply { onSuccess(this) }
            HttpURLConnection.HTTP_UNAUTHORIZED,
            HttpURLConnection.HTTP_FORBIDDEN
            -> onUnauthorized()
            else -> onFailed(Throwable("Default " + response.code() + " " + response.message()))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        // handle error mechanism
        t.apply { onFailed(this) }
    }
    fun onSuccess(responseBody: T)
    fun onUnauthorized()
    fun onFailed(throwable: Throwable)
}

interface CustomCallbackList<T> : Callback<ArrayList<T>> {
    override fun onResponse(call: Call<ArrayList<T>>, response: Response<ArrayList<T>>) {
        when (response.code()) {
            HttpsURLConnection.HTTP_OK,
            HttpsURLConnection.HTTP_CREATED,
            HttpsURLConnection.HTTP_ACCEPTED,
            HttpsURLConnection.HTTP_NOT_AUTHORITATIVE ->
                response.body()?.apply { onSuccess(this) }
            HttpURLConnection.HTTP_UNAUTHORIZED,
            HttpURLConnection.HTTP_FORBIDDEN
            -> onUnauthorized()
            else -> onFailed(Throwable("Default " + response.code() + " " + response.message()))
        }
    }

    override fun onFailure(call: Call<ArrayList<T>>, t: Throwable) {
        // handle error mechanism
        t.apply { onFailed(this) }
    }
    fun onSuccess(responseBody: ArrayList<T>)
    fun onUnauthorized()
    fun onFailed(throwable: Throwable)
}
