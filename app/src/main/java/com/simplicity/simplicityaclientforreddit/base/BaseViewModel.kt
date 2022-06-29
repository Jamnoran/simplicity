package com.simplicity.simplicityaclientforreddit.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.APIInterface
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.RetrofitClientInstance
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.serializers.CommentSerializer
import java.lang.reflect.Type

open class BaseViewModel : ViewModel() {
    val isFetching = MutableLiveData<Boolean>()
    val networkError = MutableLiveData<Unit>()
    val unAuthorizedError = MutableLiveData<Unit>()

    fun API(
        typeOfApi: Int,
        responseType: Type,
        commentSerializer: CommentSerializer?,
        `interface`: Class<APIInterface>
    ): APIInterface {
        isFetching.postValue(true)
        return RetrofitClientInstance.getRetrofitInstanceWithCustomConverter(
            responseType,
            commentSerializer
        ).create(`interface`)
    }

    fun isFetching(): LiveData<Boolean> {
        return isFetching
    }

    fun setIsFetching(value: Boolean) {
        isFetching.postValue(value)
    }

    fun networkError(): LiveData<Unit> {
        return networkError
    }
    fun unAuthorizedError(): LiveData<Unit> {
        return unAuthorizedError
    }
}
