package com.simplicity.simplicityaclientforreddit.ui.main.fragments.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.simplicity.simplicityaclientforreddit.base.BasePostsListViewModel
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.APIInterface
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.CustomResponseList
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.serializers.CommentSerializer
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.CommentResponse

class CommentsViewModel : BasePostsListViewModel() {

    private val TAG: String = "CommentsViewModel"

    private val _comments = MutableLiveData<CommentResponse>()

    fun comments(): LiveData<CommentResponse> {
        return _comments
    }

    fun fetchComments(subreddit: String, postId: String) {
        val service = API(1, CommentResponse::class.java, CommentSerializer(), APIInterface::class.java)
        val call = service.getComments(subreddit, postId)
        call.enqueue(object : CustomResponseList<CommentResponse>(this) {
            override fun success(responseBody: ArrayList<CommentResponse>) {
                responseBody.let { commentsResponse ->
                    _comments.postValue(commentsResponse[1])
                }
            }
        })
    }
}
