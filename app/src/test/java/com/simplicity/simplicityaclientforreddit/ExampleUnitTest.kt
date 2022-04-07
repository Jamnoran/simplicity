package com.simplicity.simplicityaclientforreddit

import com.simplicity.simplicityaclientforreddit.utils.media.VideoHelper
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testRegExp(){
        val url = "https://v.redd.it/rv9jnqqoryp81/DASH_1080.mp4?source=fallback"
        assertEquals("https://v.redd.it/rv9jnqqoryp81/DASH_audio.mp4?source=fallback", VideoHelper.getAudioUrl(url))
    }
}
