package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplicity.simplicityaclientforreddit.MainActivity
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.APIAuthenticatedInterface
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.APIInterface
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.RetrofitClientInstance
import com.simplicity.simplicityaclientforreddit.ui.main.io.room.RoomDB
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.FetchPostsResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.JsonResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.enums.PostType
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.FilterPostsUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetPostTypeUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.InitApplicationUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.RemoveOldPostsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListViewModel : ViewModel() {

    private val TAG = "ListViewModel"
    private val _activePosts = ArrayList<RedditPost>()
    private val _preLoadedPosts = ArrayList<RedditPost>()
    private var _cursor: String = ""
    private var fetching = false
    private var nsfwSettings = true
    private var sfwSettings = true
    private var subReddit: String? = null

    private val _redditPostsLiveData = MutableLiveData<ArrayList<RedditPost>>()
    private val _redditPostCount = MutableLiveData<Int>()
    private val _requireUpdateSettingsValues = MutableLiveData<Unit>()
    private val _scrollToNextItem = MutableLiveData<Unit>()
    private val _openCommentsFragment = MutableLiveData<RedditPost>()
    private val _hideSubLiveData = MutableLiveData<Unit>()
    private val _subRedditClicked = MutableLiveData<String>()
    private val _authorClicked = MutableLiveData<String>()
    private val _browserUrl = MutableLiveData<String>()
    private val _webViewActivity = MutableLiveData<String>()
    private val _redditUrl = MutableLiveData<String>()

    fun posts() : MutableLiveData<ArrayList<RedditPost>>{
        return _redditPostsLiveData
    }

    fun count(): MutableLiveData<Int>{
        return _redditPostCount
    }

    fun requireSettingsUpdate(): LiveData<Unit> {
        return _requireUpdateSettingsValues
    }

    fun scrollToNextItem(): LiveData<Unit> {
        return _scrollToNextItem
    }

    fun hideSub(): LiveData<Unit> {
        return _hideSubLiveData
    }

    fun subRedditClicked(): LiveData<String> {
        return _subRedditClicked
    }

    fun authorClicked(): LiveData<String> {
        return _authorClicked
    }

    fun redditUrlClicked(): LiveData<String> {
        return _redditUrl
    }

    fun webViewActivityClicked(): LiveData<String> {
        return _webViewActivity
    }

    fun browserUrlClicked(): LiveData<String> {
        return _browserUrl
    }

    fun openCommentsFragment(): LiveData<RedditPost>{
        return _openCommentsFragment
    }

    fun fetchPosts() {
        if (!fetching) {
            // check if we have cached results
            if(_preLoadedPosts.isNotEmpty()){
                // post those to list
                addPreloadedToList()
            }
            // Fetch more
            fetchPosts(_cursor)
        }
    }

    fun setSettingsValues(nsfw: Boolean, sfw: Boolean){
        nsfwSettings = nsfw
        sfwSettings = sfw
    }

    fun hideSub(subName: String){
        _hideSubLiveData.postValue(Unit)
        viewModelScope.launch(Dispatchers.IO) {
            val db = RoomDB()
            db.hideSub(subName)
        }
    }

    fun initSession(firstTimeApplicationStarted: Boolean) {
        _requireUpdateSettingsValues.postValue(Unit)
        viewModelScope.launch(Dispatchers.IO) {
            InitApplicationUseCase().init(firstTimeApplicationStarted)
            RemoveOldPostsUseCase().removeOld()
        }
    }

    fun scrollToNext(nextPosition: Int) {
        if(nextPosition >= (_activePosts.size - 3)){
            fetchPosts()
        }
        _scrollToNextItem.postValue(Unit)
    }

    fun upVote(post: RedditPost) {
        post.data.name.let{ id ->
            val service = RetrofitClientInstance.getRetrofitAuthenticatedInstance().create(
                APIAuthenticatedInterface::class.java)
            val call = service.upVote(id)
            call.enqueue(object : Callback<JsonResponse> {
                override fun onResponse(
                    call: Call<JsonResponse>,
                    response: Response<JsonResponse>
                ) {
                    response.body().let { data ->
                        Log.i(TAG, "Data $data")
                    }
                }

                override fun onFailure(call: Call<JsonResponse>, t: Throwable) {
                    Log.e(TAG, "Error : ", t)
                }
            })
        }
    }

    fun downVote(post: RedditPost) {
        post.data.name.let{ id ->
            val service = RetrofitClientInstance.getRetrofitAuthenticatedInstance().create(
                APIAuthenticatedInterface::class.java)
            val call = service.downVote(id)
            call.enqueue(object : Callback<JsonResponse> {
                override fun onResponse(
                    call: Call<JsonResponse>,
                    response: Response<JsonResponse>
                ) {
                    response.body().let { data ->
                        Log.i(TAG, "Data $data")
                    }
                }

                override fun onFailure(call: Call<JsonResponse>, t: Throwable) {
                    Log.e(TAG, "Error : ", t)
                }
            })
        }
    }

    fun commentsClicked(post: RedditPost) {
        _openCommentsFragment.postValue(post)
    }

    fun subRedditClicked(subreddit: String) {
        _subRedditClicked.postValue(subreddit)
    }
    fun authorClicked(author: String) {
        _authorClicked.postValue(author)
    }

    fun linkClicked(post: RedditPost) {
        when(GetPostTypeUseCase().execute(post.data)){
            PostType.IMGUR_LINK -> {
                post.data.url?.let{ link -> _browserUrl.postValue(link) }
//                it.data.url?.let{ (activity as MainActivity).startWebViewActivity(it) }
                return
            }
            PostType.LINK -> {
                post.data.url?.let{ url ->
                    _webViewActivity.postValue(url)
//                    (activity as MainActivity).startWebViewActivity(url)
                }
                return
            }
            PostType.IMAGE -> post.data.permalink?.let{ _redditUrl.postValue(it) }
            PostType.RICH_VIDEO -> post.data.permalink?.let{ _redditUrl.postValue(it) }
            PostType.IS_VIDEO -> post.data.permalink?.let{ _redditUrl.postValue(it) }
            PostType.GALLERY -> post.data.permalink?.let{ _redditUrl.postValue(it) }
            PostType.NONE -> {
                when {
                    post.data.url != null -> {
                        _webViewActivity.postValue(post.data.url!!)
                    }
                    post.data.permalink != null -> {
                        post.data.permalink?.let{ _redditUrl.postValue(it) }
                    }
                    else -> {
                        Log.i("ListFragment", "Could not find click method for this post $post")
                    }
                }
            }
            PostType.TOURNAMENT -> {}
        }
    }

    fun redditLinkClicked(post: RedditPost) {
        post.data.permalink?.let{ _redditUrl.postValue(it) }
    }

    fun setSubReddit(it: String) {
        subReddit = it
    }


    private fun fetchPosts(cursor: String) {
        _requireUpdateSettingsValues.postValue(Unit)
        fetching = true
        Log.i(TAG, "Getting reddit posts with this cursor: $cursor")
        val service = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        val call = service.getPosts(cursor, "on")
        call.enqueue(object : Callback<FetchPostsResponse> {
            override fun onResponse(
                call: Call<FetchPostsResponse>,
                response: Response<FetchPostsResponse>
            ) {
                viewModelScope.launch(Dispatchers.IO) {
                    response.body()?.data?.let { data ->
                        _cursor = data.after
                        processFetchedPosts(data.children)
                    }
                    displayToast()
                    if(_preLoadedPosts.size < 8){
                        // Fetching next set of posts.
                        fetchPosts(_cursor)
                    }else{
                        fetching = false
                    }
                }
            }

            override fun onFailure(call: Call<FetchPostsResponse>, t: Throwable) {
                Log.e(TAG, "Error : ", t)
                fetching = false
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