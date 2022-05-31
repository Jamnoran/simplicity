package com.simplicity.simplicityaclientforreddit

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.APIInterface
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.RetrofitClientInstance
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.serializers.CommentSerializer
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.CommentResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class CommentsInstrumentedTest {
    private val TAG = "ExampleInstrumentedTest"

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.simplicity.simplicityaclientforreddit", appContext.packageName)

    }

    @Test
    fun testComments(){
        val latch = CountDownLatch(1)
        var countOfComments = 0
        val service = RetrofitClientInstance.getRetrofitInstanceWithCustomConverter(CommentResponse::class.java, CommentSerializer()).create(APIInterface::class.java)
        val call = service.getComments("WorkReform", "te84tc")
        call.enqueue(object : Callback<ArrayList<CommentResponse>> {
            override fun onResponse(
                call: Call<ArrayList<CommentResponse>>,
                response: Response<ArrayList<CommentResponse>>
            ) {
                    response.body().let { data ->
                        latch.countDown()
                        Log.i(TAG, "Data $data")
                        countOfComments++
                    }
            }

            override fun onFailure(call: Call<ArrayList<CommentResponse>>, t: Throwable) {
                Log.e(TAG, "Error : ", t)
                latch.countDown()
            }
        })
        latch.await()
        assert(countOfComments > 0)
    }

}