package com.simplicity.simplicityaclientforreddit

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.simplicity.simplicityaclientforreddit.ui.main.Global
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.subreddits.AddSubRedditVisited
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.subreddits.GetSubRedditVisited
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SubredditsInstrumentedTest {
    private val TAG = "FormattingInstrumentedTest"

    @Test
    fun addSubredditVisitedTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Global.applicationContext = appContext
        val previousSize = GetSubRedditVisited().execute().size
        AddSubRedditVisited("gonewild").execute()
        assert(GetSubRedditVisited().execute().size > previousSize)
    }

}