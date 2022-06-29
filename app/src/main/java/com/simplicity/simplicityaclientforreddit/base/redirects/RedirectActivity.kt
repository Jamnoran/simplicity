package com.simplicity.simplicityaclientforreddit.base.redirects

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.simplicity.simplicityaclientforreddit.MainActivity
import com.simplicity.simplicityaclientforreddit.ui.main.Global
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP

class RedirectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check to see if this Activity is the root activity
        Global.applicationContext = applicationContext
        saveData(intent)
        if (isTaskRoot) {
            // This Activity is the only Activity, so
            //  the app wasn't running. So start the app from the
            //  beginning (redirect to MainActivity)
            val mainIntent = intent // Copy the Intent used to launch me
            // Launch the real root Activity (launch Intent)
            mainIntent.setClass(this, MainActivity::class.java)
            // I'm done now, so finish()
            startActivity(mainIntent)
            finish()
        } else {
            // App was already running, so just finish, which will drop the user
            //  in to the activity that was at the top of the task stack
            setResult(MainActivity.AUTHENTICATION_DONE)
            finish()
        }
    }

    private fun saveData(intent: Intent?) {
        Log.i(TAG, "Saving authentication data ${intent?.data}")
        intent?.data?.let {
            val uri: Uri = it
            if (uri.getQueryParameter("error") != null) {
                val error: String = uri.getQueryParameter("error")!!
                Log.e(TAG, "An error has occurred : $error")
            } else {
                uri.getQueryParameter("state")?.let { state ->
                    uri.getQueryParameter("code")?.let { code ->
                        Log.i(TAG, "Stored this code $code and this state $state")
                        SettingsSP().saveSetting(SettingsSP.KEY_CODE, code)
                        SettingsSP().saveSetting(SettingsSP.KEY_STATE, state)
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "RedirectActivity"
    }
}
