package com.simplicity.simplicityaclientforreddit

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.APIAuthenticatedInterface
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.APIInterface
import com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.RetrofitClientInstance
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.AccessToken
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.JsonResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.search.SearchRedditResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.user.User
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetAccessTokenAuthenticationUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetRefreshTokenBodyUseCase
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
class APIInstrumentedTest {
    private val TAG = "ExampleInstrumentedTest"

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.simplicity.simplicityaclientforreddit", appContext.packageName)

    }

    @Test
    fun testAuthenticatedRequest(){
        val latch = CountDownLatch(1)
        var user: User? = null
        val service = RetrofitClientInstance.getRetrofitAuthenticatedInstance().create(APIAuthenticatedInterface::class.java)
        val call = service.userMe()
        call.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                    response.body().let { data ->
                        latch.countDown()
                        user = data
                    }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(TAG, "Error : ", t)
                latch.countDown()
            }
        })
        latch.await()

        user?.let{
            assert(it.name == "Jamnoran")
        }
    }

    @Test
    fun testVote(){
        val latch = CountDownLatch(1)
        val service = RetrofitClientInstance.getRetrofitAuthenticatedInstance().create(APIAuthenticatedInterface::class.java)
        val call = service.upVote("t3_tekys8")
        call.enqueue(object : Callback<JsonResponse> {
            override fun onResponse(
                call: Call<JsonResponse>,
                response: Response<JsonResponse>
            ) {
                    response.body().let { data ->
                        latch.countDown()
                        Log.i(TAG, "Data $data")
                    }
            }

            override fun onFailure(call: Call<JsonResponse>, t: Throwable) {
                Log.e(TAG, "Error : ", t)
                latch.countDown()
            }
        })
        latch.await()
    }

    @Test
    fun testRefreshToken(){
        val latch = CountDownLatch(1)

        val service = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        val call = service.accessToken(GetAccessTokenAuthenticationUseCase().getAuth(), GetRefreshTokenBodyUseCase().getBody())
        call.enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken> ) {
                response.body().let { data ->
                    latch.countDown()
                    Log.i(TAG, "Data $data")
                }
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                Log.e(TAG, "Error : ", t)
                latch.countDown()
            }
        })
        latch.await()
    }


    @Test
    fun testSearch(){
        val latch = CountDownLatch(1)
        val service = RetrofitClientInstance.getRetrofitAuthenticatedInstance().create(APIAuthenticatedInterface::class.java)
        val call = service.searchReddits("gonewild")
        call.enqueue(object : Callback<SearchRedditResponse> {
            override fun onResponse(
                call: Call<SearchRedditResponse>,
                response: Response<SearchRedditResponse>
            ) {
                response.body().let { data ->
                    latch.countDown()
                    Log.i(TAG, "Data $data")
                }
            }

            override fun onFailure(call: Call<SearchRedditResponse>, t: Throwable) {
                Log.e(TAG, "Error : ", t)
                latch.countDown()
            }
        })
        latch.await()
    }
}