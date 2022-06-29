package com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit

import androidx.lifecycle.viewModelScope
import com.simplicity.simplicityaclientforreddit.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class CustomResponse<T>(val viewModel: BaseViewModel) : CustomCallback<T> {
    override fun onSuccess(responseBody: T) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            success(responseBody)
        }
        viewModel.isFetching.postValue(false)
    }

    abstract fun success(responseBody: T)

    override fun onUnauthorized() {
        viewModel.unAuthorizedError.postValue(Unit)
        viewModel.isFetching.postValue(false)
    }

    override fun onFailed(throwable: Throwable) {
        viewModel.networkError.postValue(Unit)
        viewModel.isFetching.postValue(false)
    }

}
abstract class CustomResponseList<T>(val viewModel: BaseViewModel) : CustomCallbackList<T> {
    override fun onSuccess(responseBody: ArrayList<T>) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            success(responseBody)
        }
        viewModel.isFetching.postValue(false)
    }

    abstract fun success(responseBody: ArrayList<T>)

    override fun onUnauthorized() {
        viewModel.unAuthorizedError.postValue(Unit)
        viewModel.isFetching.postValue(false)
    }

    override fun onFailed(throwable: Throwable) {
        viewModel.networkError.postValue(Unit)
        viewModel.isFetching.postValue(false)
    }
}
