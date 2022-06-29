package com.simplicity.simplicityaclientforreddit.ui.main.fragments.user.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simplicity.simplicityaclientforreddit.base.BasePostsListViewModel
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.APIAuthenticatedInterface
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.CustomCallback
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.RetrofitClientInstance
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.user.User
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.user.UserResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.user.posts.UserPostsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : BasePostsListViewModel() {

    private val _user = MutableLiveData<User>()
    private val _posts = MutableLiveData<ArrayList<RedditPost>>()

    private lateinit var _userName: String

    fun user(): LiveData<User> {
        return _user
    }
    fun posts(): LiveData<ArrayList<RedditPost>> {
        return _posts
    }

    fun setUserName(username: String) {
        _userName = username
    }

    fun fetchUser() {
        val service = RetrofitClientInstance.getRetrofitAuthenticatedInstance()
            .create(APIAuthenticatedInterface::class.java)
        val call = service.getUser(_userName)
        call.enqueue(object : CustomCallback<UserResponse> {
            override fun onSuccess(responseBody: UserResponse) {
                viewModelScope.launch(Dispatchers.IO) {
                    responseBody.data.let { data ->
                        _user.postValue(data)
                    }
                }
            }

            override fun onUnauthorized() {
                Log.e(TAG, "onUnauthorized")
                networkError.postValue(Unit)
            }

            override fun onFailed(throwable: Throwable) {
                Log.e(TAG, "Error: ", throwable)
            }
        }
        )
    }

    override fun fetchPosts() {
        if (isFetching().value == false || isFetching().value == null) {
            // check if we have cached results
            Log.i(TAG, "Preloaded array has a size of ${_preLoadedPosts.size}")
            if (_preLoadedPosts.isNotEmpty()) {
                // post those to list
                addPreloadedToList()
            }
            // Fetch more
            _cursor?.let {
                fetchUserPosts(it)
            }
        }
    }

    private fun fetchUserPosts(cursor: String) {
        setIsFetching(true)
        val service = RetrofitClientInstance.getRetrofitAuthenticatedInstance().create(APIAuthenticatedInterface::class.java)
        val call = service.getUserPosts(_userName, cursor)
        call.enqueue(object : Callback<UserPostsResponse> {
            override fun onResponse(
                call: Call<UserPostsResponse>,
                response: Response<UserPostsResponse>
            ) {
                viewModelScope.launch(Dispatchers.IO) {
                    response.body()?.data?.let { data ->
                        _cursor = data.after
                        processFetchedPosts(data.children)
                    }
                    setIsFetching(false)
                }
            }

            override fun onFailure(call: Call<UserPostsResponse>, t: Throwable) {
                Log.e(TAG, "Error : ", t)
                setIsFetching(false)
            }
        })
    }

    private fun processFetchedPosts(children: List<RedditPost>) {
        for (child in children) {
            _preLoadedPosts.add(child)
        }
        if (_activePosts.size < 8) { // Nothing has been displayed before, show what we have and fetch more.
            addPreloadedToList()
        }
    }

    private fun addPreloadedToList() {
        _activePosts.addAll(_preLoadedPosts)
        _posts.postValue(_activePosts)
        _preLoadedPosts.clear()
    }

    companion object {
        private const val TAG = "UserViewModel"
    }
}
