package com.simplicity.simplicityaclientforreddit.ui.main.fragments.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplicity.simplicityaclientforreddit.ui.main.io.room.RoomDB
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AuthenticationViewModel : ViewModel() {

    private val _post = MutableLiveData<RedditPost>()

    fun post() : MutableLiveData<RedditPost>{
        return _post
    }

    fun parsePost(json: String) {
        _post.postValue(Gson().fromJson(json, RedditPost::class.java))
    }


    fun testDatabase(redditPost: RedditPost) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = RoomDB()

//            db.toReadPost(redditPost)?.let{
//                Log.i("DetailViewModel","Post to save to db: $it")
//                db.insertPost(it)
//            }
            db.deleteAllOlderThanAWeek()

            for(post in db.getAll()){
                Log.i("DetailViewModel", "Post in db: $post")
            }
        }

    }
}