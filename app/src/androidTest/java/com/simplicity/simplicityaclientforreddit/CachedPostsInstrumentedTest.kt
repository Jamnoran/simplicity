package com.simplicity.simplicityaclientforreddit

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.simplicity.simplicityaclientforreddit.ui.main.Global
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.cachedPosts.AddCachedPostUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.cachedPosts.GetCachedPostUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.cachedPosts.RemoveAllCachedPostUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.cachedPosts.RemoveCachedPostUseCase
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CachedPostsInstrumentedTest {
    private val TAG = "CachedPostsInstrumentedTest"

    @Test
    fun addCachedPost() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Global.applicationContext = appContext
        RemoveAllCachedPostUseCase().execute()
        val previousSize = GetCachedPostUseCase().execute().size
        assert(previousSize == 0)
        val post = getPost(R.raw.post_desc)
        AddCachedPostUseCase(post).execute()
        assert(GetCachedPostUseCase().execute().size > previousSize)
        RemoveCachedPostUseCase(post).execute()
        assert(GetCachedPostUseCase().execute().isEmpty())
    }

    private fun getPost(postDesc: Int): RedditPost {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val json = appContext.resources.openRawResource(postDesc).bufferedReader().use { it.readText() }
        return Gson().fromJson(json, RedditPost::class.java)
    }
}