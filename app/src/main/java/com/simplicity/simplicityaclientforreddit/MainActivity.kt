package com.simplicity.simplicityaclientforreddit

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.simplicity.simplicityaclientforreddit.databinding.MainActivityBinding
import com.simplicity.simplicityaclientforreddit.ui.main.Global
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.authentication.AuthenticationFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.custom.BaseFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.ListFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.menu.NavigationBar
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.test.TestFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.webview.WebViewActivity
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP


class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel

    var currentFragment: BaseFragment? = null
    var previousFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(binding.root)
        Global.applicationContext = applicationContext

        if (savedInstanceState == null) {
            setUpObservables()
            viewModel.initApplication()
            val displayMetrics = DisplayMetrics()
            this.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            viewModel.setDeviceInfo(displayMetrics.widthPixels, displayMetrics.heightPixels)
            navigateToScreen()
//            val code = SettingsSP(this).loadSetting(SettingsSP.KEY_CODE, "Default")
//            if(!code.equals("Default")){
//                code?.let{
//                    viewModel.authenticateRetro(it)
//                }
//            }
        }
        setUpNavigationBar()

        Log.i("MainActivity", "Refresh token ${SettingsSP().loadSetting(SettingsSP.KEY_REFRESH_TOKEN, "Default")}")
    }

    private fun navigateToScreen() {
        startFragment(ListFragment.newInstance())
//        startFragment(TestFragment.newInstance())
//            startFragment(DetailFragment.newInstance())
//            startWebViewActivity("https://i.imgur.com/MBtV8jD.gifv")
//            startWebViewActivity("https://www.redditmedia.com/mediaembed/ptnj5n")
//            startWebViewActivity("https://i.imgur.com/gK1zKGP.gifv")
//            startWebViewActivity("http://jamnoran.se/test2.html")
    }

    private fun setUpObservables() {
        viewModel.accessToken().observe(this) { observeAccessToken(it) }
        viewModel.refreshToken().observe(this) { observeRefreshToken(it) }
    }

    private fun observeAccessToken(it: String) {
        SettingsSP().saveSetting(SettingsSP.KEY_ACCESS_TOKEN, it)
    }

    private fun observeRefreshToken(it: String) {
        SettingsSP().saveSetting(SettingsSP.KEY_REFRESH_TOKEN, it)
    }

    private fun setUpNavigationBar() {
        NavigationBar(this, binding.navigationDrawer).init()
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

    fun startWebViewActivity(url: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.apply { putExtra("URL", url) }
        startActivity(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.i("MainActivity", "onKeyDown $keyCode $event")
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            currentFragment?.let { it ->
                if(!it.onKeyDown(keyCode, event)){
                    goBack(keyCode, event)
                }
            }
            false
        } else super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        if(currentFragment is AuthenticationFragment){
            viewModel.resumeWithAuthenticationShowing()
            goBack()
        }
    }

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

    fun subRedditClicked(subreddit: String) {
        startFragment(ListFragment.newInstance())
    }


    companion object {
        const val AUTHENTICATION_DONE = 1001
    }
}