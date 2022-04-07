package com.simplicity.simplicityaclientforreddit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.APIInterface
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.RetrofitClientInstance
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.AccessToken
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetAccessTokenBodyUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetAccessTokenAuthenticationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    private val TAG = "MainViewModel"


    private val _accessToken = MutableLiveData<String>()
    private val _refreshToken = MutableLiveData<String>()
    private val _expiresIn = MutableLiveData<Int>()
    private val _scope = MutableLiveData<String>()
    private val _tokenType = MutableLiveData<String>()

    fun accessToken(): MutableLiveData<String>{
        return _accessToken
    }

    fun refreshToken(): MutableLiveData<String>{
        return _refreshToken
    }

    fun expiresIn(): MutableLiveData<Int>{
        return _expiresIn
    }

    fun scope(): MutableLiveData<String>{
        return _scope
    }

    fun tokenType(): MutableLiveData<String>{
        return _tokenType
    }

    fun initApplication() {

    }

    private fun authenticate(code: String) {
        Log.i(TAG, "Getting reddit posts with this cursor: $code")

        val service = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        val call = service.accessToken(GetAccessTokenAuthenticationUseCase().getAuth(), GetAccessTokenBodyUseCase().getBody(code))
        call.enqueue(object : Callback<AccessToken> {
            override fun onResponse(
                call: Call<AccessToken>,
                response: Response<AccessToken>
            ) {
                viewModelScope.launch(Dispatchers.IO) {
                    response.body()?.let { data ->
                        Log.i(TAG, "Data in response: $data")
                        data.accessToken?.let{ _accessToken.postValue(it) }
                        data.refreshToken?.let{ _refreshToken.postValue(it) }
                        data.expiresIn?.let{ _expiresIn.postValue(it) }
                        data.scope?.let{ _scope.postValue(it) }
                        data.tokenType?.let{ _tokenType.postValue(it) }
                    }
                }
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                Log.e(TAG, "Error : ", t)
            }
        })
    }

    fun resumeWithAuthenticationShowing() {
        val code = SettingsSP().loadSetting(SettingsSP.KEY_CODE, "Default")
        Log.i("MainActivity", "Send authentication request to reddit with code: $code")
        code?.let {
            authenticate(it)
        }
    }

    fun setDeviceInfo(widthPixels: Int, heightPixels: Int) {
        SettingsSP().saveSetting(SettingsSP.KEY_DEVICE_HEIGHT, heightPixels)
        SettingsSP().saveSetting(SettingsSP.KEY_DEVICE_WIDTH, widthPixels)
    }

}