package com.simplicity.simplicityaclientforreddit

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.simplicity.simplicityaclientforreddit.base.BaseFragment
import com.simplicity.simplicityaclientforreddit.databinding.MainActivityBinding
import com.simplicity.simplicityaclientforreddit.ui.main.Global
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.authentication.AuthenticationFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.detail.DetailFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.ListFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.menu.SideMenuBar
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.webview.WebViewActivity
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.firebase.FireBaseLogUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.subreddits.GetSubRedditIntentUseCase


class MainActivity : AppCompatActivity() {
    companion object {
        const val AUTHENTICATION_DONE = 1001
        const val KEY_SUBREDDIT = "subreddit"
    }
    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel

    private var _subreddit: String? = null
    private var _currentFragment: BaseFragment? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(binding.root)
        Global.applicationContext = applicationContext

        if (savedInstanceState == null) {
            intent.extras?.let{
                _subreddit = it.getString(KEY_SUBREDDIT)
            }
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
        SideMenuBar(this, binding.navigationDrawer).init()

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Log.i("MainActivity", "Refresh token ${SettingsSP().loadSetting(SettingsSP.KEY_REFRESH_TOKEN, "Default")}")
        mFirebaseAnalytics?.let{ FireBaseLogUseCase(it).execute("app_started","MainActivity", "logged_in_false") }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.i("MainActivity", "onKeyDown $keyCode $event")
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            _currentFragment?.let { it ->
                if(!it.onKeyDown(keyCode, event) || _subreddit != null){
                    goBack()
                }
            }
            false
        } else super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        if(_currentFragment is AuthenticationFragment){
            viewModel.resumeWithAuthenticationShowing()
            goBack()
        }
    }

    fun subRedditClicked(subreddit: String) {
//        AddSubRedditVisitedUseCase(subreddit).execute()
//        viewModel.fetchListOfVisitedSubReddits()
//        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtra(KEY_SUBREDDIT, subreddit)
//        startActivityWithAnimation(intent)

        startActivityWithAnimation(GetSubRedditIntentUseCase(subreddit, this).execute())
        viewModel.fetchListOfVisitedSubReddits()
    }

    fun startActivityWithAnimation(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    fun startWebViewActivity(url: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.apply { putExtra("URL", url) }
        startActivity(intent)
    }

    private fun navigateToScreen() {
        startFragment(ListFragment.newInstance(_subreddit))
//            startFragment(DetailFragment.newInstance())
//            startWebViewActivity("https://i.imgur.com/MBtV8jD.gifv")
//            startWebViewActivity("https://www.redditmedia.com/mediaembed/ptnj5n")
//            startWebViewActivity("https://i.imgur.com/gK1zKGP.gifv")
//            startWebViewActivity("http://jamnoran.se/test2.html")

//        val intent = Intent(this, UserDetailActivity::class.java)
//        intent.putExtra(UserDetailActivity.KEY_USER_NAME, "PantyNectar")
//        startActivityWithAnimation(intent)
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

    private fun startFragment(fragment: BaseFragment){
        _currentFragment = fragment
        _currentFragment?.let{
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, it)
                .commitNow()
        }
    }

    private fun goBack() {
        finish()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    fun closeDrawer() {
        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawers()
    }
}