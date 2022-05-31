package com.simplicity.simplicityaclientforreddit.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val isFetching = MutableLiveData<Boolean>()

    fun isFetching() : LiveData<Boolean> {
        return isFetching
    }

    fun setIsFetching(value: Boolean){
        isFetching.postValue(value)
    }

}