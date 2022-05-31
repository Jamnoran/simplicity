package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.base.BasePostsListViewModel
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.APIInterface
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.RetrofitClientInstance
import com.simplicity.simplicityaclientforreddit.ui.main.io.room.RoomDB
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.FetchPostsResponse
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.FilterPostsUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.InitApplicationUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.RemoveOldPostsUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.cachedPosts.AddCachedPostUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.cachedPosts.GetCachedPostUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListViewModel : BasePostsListViewModel() {

    private val TAG = "ListViewModel"
    private var nsfwSettings = true
    private var sfwSettings = true
    private var subReddit: String? = null

    private val _redditPostsLiveData = MutableLiveData<ArrayList<RedditPost>>()
    private val _redditPostCount = MutableLiveData<Int>()
    private val _requireUpdateSettingsValues = MutableLiveData<Unit>()
    private val _showErrorMessage = MutableLiveData<Int>()

    fun posts() : LiveData<ArrayList<RedditPost>>{
        return _redditPostsLiveData
    }

    fun count(): LiveData<Int>{
        return _redditPostCount
    }

    fun requireSettingsUpdate(): LiveData<Unit> {
        return _requireUpdateSettingsValues
    }

    fun showError(): LiveData<Int>{
        return _showErrorMessage
    }

    override fun fetchPosts() {
        if (isFetching().value == false || isFetching().value == null) {
            // check if we have cached results
                Log.i(TAG, "Preloaded array has a size of ${_preLoadedPosts.size}")
            if(_preLoadedPosts.isNotEmpty()){
                // post those to list
                addPreloadedToList()
            }
            // Fetch more
            _cursor?.let{
                fetchPosts(it)
            }
        }
    }

    fun setSettingsValues(nsfw: Boolean, sfw: Boolean){
        nsfwSettings = nsfw
        sfwSettings = sfw
    }

    fun initSession(firstTimeApplicationStarted: Boolean) {
        _requireUpdateSettingsValues.postValue(Unit)
        viewModelScope.launch(Dispatchers.IO) {
            InitApplicationUseCase().init(firstTimeApplicationStarted)
            RemoveOldPostsUseCase().removeOld()
        }
        if(subReddit == null) {
            _preLoadedPosts.addAll(GetCachedPostUseCase().execute())
        }
    }

    fun setSubReddit(it: String) {
        _preLoadedPosts.clear()
        subReddit = it
    }

    private fun fetchPosts(cursor: String) {
        _requireUpdateSettingsValues.postValue(Unit)
        setIsFetching(true)
        Log.i(TAG, "Getting reddit posts with this cursor: $cursor")
        val service = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        val call = service.getPosts(subReddit?: "all", cursor, "on")
        call.enqueue(object : Callback<FetchPostsResponse> {
            override fun onResponse(
                call: Call<FetchPostsResponse>,
                response: Response<FetchPostsResponse>
            ) {
                viewModelScope.launch(Dispatchers.IO) {
                    if(response.isSuccessful){
                        response.body()?.data?.let { data ->
                            _cursor = data.after
                            processFetchedPosts(data.children)
                            if(data.after == null || data.after.isEmpty()){
                                Log.i(TAG, "There is no posts to show.")
                                _showErrorMessage.postValue(R.string.error_empty)
                            }
                        }
                        displayToast()
                        if(_preLoadedPosts.size < 8){
                            // Fetching next set of posts.
                            _cursor?.let{
                                fetchPosts(it)
                            }
                        }else{
                            setIsFetching(false)
                        }
                    }else{
                        _showErrorMessage.postValue(R.string.error_network)
                        setIsFetching(false)
                    }
                }
            }

            override fun onFailure(call: Call<FetchPostsResponse>, t: Throwable) {
                Log.e(TAG, "Error : ", t)
                setIsFetching(false)
            }
        })
    }

    private fun displayToast() {
        Log.i(TAG, "Showing [${_activePosts.size}] posts")
        val count = RoomDB().getTotalRowsInDatabase()
        Log.i(TAG, "Count of rows in database after downloading: $count")
        _redditPostCount.postValue(count)
    }

    private fun processFetchedPosts(children: List<RedditPost>) {
        for(child in children){
            if(FilterPostsUseCase().canDisplay(child, nsfwSettings, sfwSettings)){
                _preLoadedPosts.add(child)
                if(subReddit == null){ // We are in /all feed, we can cache these posts safely
                    AddCachedPostUseCase(child).execute()
                }
            }
        }
        if(_activePosts.size < 8){ // Nothing has been displayed before, show what we have and fetch more.
            addPreloadedToList()
        }
    }

    private fun addPreloadedToList() {
        _activePosts.addAll(_preLoadedPosts)
        _redditPostsLiveData.postValue(_activePosts)
        _preLoadedPosts.clear()
    }

}