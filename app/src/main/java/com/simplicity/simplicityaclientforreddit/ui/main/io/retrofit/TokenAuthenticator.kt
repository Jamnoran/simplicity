package com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit

import android.util.Log
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetAccessTokenAuthenticationUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetRefreshTokenBodyUseCase
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator() : Authenticator {

    private val TAG = "TokenAuthenticator"

    override fun authenticate(route: Route?, response: Response): Request? {

        // This is a synchronous call
        val updatedToken = getNewToken()

        return updatedToken?.let {
            Log.i(TAG, "Updated token to : $it")
            return response.request().newBuilder()
                .header("Authorization", "bearer $it")
                .build()
        }
    }

//    override fun authenticate(route: Route?, response: Response): Request? {
//        return runBlocking {
//            val service = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
//            val call = service.accessToken(GetAccessTokenAuthenticationUseCase().getAuth(), GetAccessTokenBodyUseCase().getBody(code))
//            call.enqueue(object : Callback<AccessToken> {
//                override fun onResponse(
//                    call: Call<AccessToken>,
//                    response: retrofit2.Response<AccessToken>
//                ) {
//                        response.body()?.let { data ->
//                            Log.i(TAG, "Data in response: $data")
//                            data.accessToken?.let{ _accessToken.postValue(it) }
//                            data.refreshToken?.let{ _refreshToken.postValue(it) }
//                            data.expiresIn?.let{ _expiresIn.postValue(it) }
//                            data.scope?.let{ _scope.postValue(it) }
//                            data.tokenType?.let{ _tokenType.postValue(it) }
//                        }
//
//                    SettingsSP().saveSetting(SettingsSP.KEY_ACCESS_TOKEN, tokenResponse.value.access_token)
//                    SettingsSP().saveSetting(SettingsSP.KEY_REFRESH_TOKEN, tokenResponse.value.refresh_token)
//                    return response.request.newBuilder()
//                        .header("Authorization", "Bearer ${tokenResponse.value.access_token}")
//                        .build()
//                }
//
//                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
//                    Log.e(TAG, "Error : ", t)
//                }
//            })
//
//
//
//
// //            when (val tokenResponse = getUpdatedToken()) {
// //                is Resource.Success -> {
// //                    SettingsSP().saveSetting(SettingsSP.KEY_ACCESS_TOKEN, tokenResponse.value.access_token)
// //                    SettingsSP().saveSetting(SettingsSP.KEY_REFRESH_TOKEN, tokenResponse.value.refresh_token)
// //                    response.request.newBuilder()
// //                        .header("Authorization", "Bearer ${tokenResponse.value.access_token}")
// //                        .build()
// //                }
// //                else -> null
// //            }
//        }
//    }

    private fun getNewToken(): String? {
        val service = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        val call = service.accessToken(GetAccessTokenAuthenticationUseCase().getAuth(), GetRefreshTokenBodyUseCase().getBody())
        val refreshTokenResponse = call.execute().body()
        refreshTokenResponse?.let { response ->
            response.refreshToken?.let {
                SettingsSP().saveSetting(SettingsSP.KEY_REFRESH_TOKEN, it)
            }
            response.accessToken?.let {
                SettingsSP().saveSetting(SettingsSP.KEY_ACCESS_TOKEN, it)
                return it
            }
        }
//            call.enqueue(object : Callback<AccessToken> {
//                override fun onResponse(
//                    call: Call<AccessToken>,
//                    response: retrofit2.Response<AccessToken>
//                ) {
//                        response.body()?.let { data ->
//                            Log.i(TAG, "Data in response: $data")
//                            data.accessToken?.let{ _accessToken.postValue(it) }
//                            data.refreshToken?.let{ _refreshToken.postValue(it) }
//                            data.expiresIn?.let{ _expiresIn.postValue(it) }
//                            data.scope?.let{ _scope.postValue(it) }
//                            data.tokenType?.let{ _tokenType.postValue(it) }
//                        }
//
//                    SettingsSP().saveSetting(SettingsSP.KEY_ACCESS_TOKEN, tokenResponse.value.access_token)
//                    SettingsSP().saveSetting(SettingsSP.KEY_REFRESH_TOKEN, tokenResponse.value.refresh_token)
//                }
//
//                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
//                    Log.e(TAG, "Error : ", t)
//                }
//            })
//        val refreshTokenRequest = RefreshTokenRequest(SharedPreferenceHelper.refreshToken)
//        val call = ApiFactory.retrofit(BuildConfig.BASEURL).create(PostDataInterface::class.java)
//            .refreshToken(refreshTokenRequest)
//        val authTokenResponse = call?.execute()?.body()
//
//        if (authTokenResponse?.status == 0){
//            //Logout User
//            AuthUtility.logout(true)
//        }
//
//        return authTokenResponse?.data?.token
        return null
    }
}
