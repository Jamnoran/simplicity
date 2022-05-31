package com.simplicity.simplicityaclientforreddit.ui.main.fragments.webview

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.ui.main.Global
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.WebViewFragment
import com.simplicity.simplicityaclientforreddit.base.BaseFragment

class WebViewActivity : AppCompatActivity() {

    var currentFragment: BaseFragment? = null
    var previousFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        Global.applicationContext = applicationContext
        if (savedInstanceState == null) {
            val url = intent.getStringExtra("URL")
            url?.let{ startFragment(WebViewFragment.newInstance(it)) }
//            startFragment(WebViewFragment.newInstance("https://i.imgur.com/MBtV8jD.gifv"))
        }
    }

    fun startFragment(fragment: BaseFragment){
        previousFragment = currentFragment
        currentFragment = fragment
        currentFragment?.let{
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, it)
                .commitNow()
        }
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        Log.i("MainActivity", "onKeyDown $keyCode ${event.toString()}")
//        return if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
//            currentFragment?.let { it ->
//                if(!it.onKeyDown(keyCode, event)){
//                    Log.i("MainActivity", "Got back that current screen cant handle")
//                    goBack(keyCode, event)
//                }
//            }
//            true
//        } else super.onKeyDown(keyCode, event)
//    }

    private fun goBack() {
        previousFragment?.let{ pFrag ->
            startFragment(pFrag)
            previousFragment = null
            return
        }
        finish()
    }

    private fun goBack(keyCode: Int, event: KeyEvent) {
        previousFragment?.let{ pFrag ->
            startFragment(pFrag)
            previousFragment = null
            return
        }
        super.onKeyDown(keyCode, event)
    }
}