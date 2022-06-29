package com.simplicity.simplicityaclientforreddit.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.firebase.FireBaseLogUseCase

open class BaseActivity : AppCompatActivity() {
    private val TAG: String = "BaseActivity"
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "onKeyDown $keyCode $event")
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            goBack()
            false
        } else super.onKeyDown(keyCode, event)
    }

    fun startActivityWithAnimation(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    fun logFirebaseEvent(event: String, tag: String, log: String) {
        mFirebaseAnalytics?.let { FireBaseLogUseCase(it).execute(event, tag, log) }
    }

    fun startFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
    }

    fun goBack() {
        finish()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    companion object {
    }
}
