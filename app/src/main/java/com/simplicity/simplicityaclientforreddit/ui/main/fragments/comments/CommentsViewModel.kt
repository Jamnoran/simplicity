package com.simplicity.simplicityaclientforreddit.ui.main.fragments.comments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplicity.simplicityaclientforreddit.base.BasePostsListViewModel
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.APIInterface
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.RetrofitClientInstance
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.serializers.CommentSerializer
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.FetchPostsResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.CommentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentsViewModel : BasePostsListViewModel() {

    private val TAG: String = "CommentsViewModel"

    private val _comments = MutableLiveData<CommentResponse>()
    private val _loading = MutableLiveData<Boolean>()

    fun comments() : LiveData<CommentResponse> {
        return _comments
    }

    fun loading() : LiveData<Boolean>{
        return _loading
    }

    fun fetchComments(subreddit: String, postId: String) {
        _loading.postValue(true)
        val service = RetrofitClientInstance.getRetrofitInstanceWithCustomConverter(CommentResponse::class.java, CommentSerializer()).create(APIInterface::class.java)
        val call = service.getComments(subreddit, postId)
        call.enqueue(object : Callback<ArrayList<CommentResponse>> {
            override fun onResponse(
                call: Call<ArrayList<CommentResponse>>,
                response: Response<ArrayList<CommentResponse>>
            ) {
                viewModelScope.launch(Dispatchers.IO) {
                    response.body()?.let { commentsResponse ->
                        _comments.postValue(commentsResponse[1])
                    }
                }
                _loading.postValue(false)
            }

            override fun onFailure(call: Call<ArrayList<CommentResponse>>, t: Throwable) {
                Log.e(TAG, "Error : ", t)
                _loading.postValue(false)
            }
        })
    }
}