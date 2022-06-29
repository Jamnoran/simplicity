package com.simplicity.simplicityaclientforreddit

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util.RedditPostListenerImpl
import com.simplicity.simplicityaclientforreddit.ui.main.listeners.RedditPostListener
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.text.GetFormattedTextUseCase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class FormattingInstrumentedTest {
    private val TAG = "FormattingInstrumentedTest"
    private val listener = object : RedditPostListener {
        override fun voteUp(post: RedditPost) {}

        override fun voteDown(post: RedditPost) { }

        override fun linkClicked(post: RedditPost) { }

        override fun commentsClicked(post: RedditPost) { }

        override fun redditLinkClicked(post: RedditPost) { }

        override fun subRedditClicked(post: RedditPost) { }

        override fun authorClicked(post: RedditPost) { }

        override fun hideSubClicked(post: RedditPost) { }

        override fun directLinkClicked(link: String) { }

        override fun directAuthorClicked(author: String) { }

        override fun directRedditClicked(reddit: String) { }
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.simplicity.simplicityaclientforreddit", appContext.packageName)

    }

    @Test
    fun testFormattingAndSign(){

        val stringToFormat = "Test &amp;x200B;with text before &amp; after"
        val result = GetFormattedTextUseCase(listener).execute(stringToFormat)
        assert(result.contains("&"))
    }

    @Test
    fun testFormatting(){
        val stringToFormat = "Test *italic* **bold** \n#Title \n##Title2\n####Title3\nnew line \n&gt;This is quote \n&#x200B;Edit: Testing a little"
        val result = GetFormattedTextUseCase(listener).execute(stringToFormat)
        assert(result.contains("&"))
    }

    @Test
    fun testFullFormatting(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val json = appContext.resources.openRawResource(R.raw.post_desc).bufferedReader().use { it.readText() }
        val post = Gson().fromJson(json, RedditPost::class.java)
        val result = GetFormattedTextUseCase(listener).execute(post.data.selftext!!)
        Log.i(TAG, "Result->  $result")
        assert(result.contains("&"))
    }

}