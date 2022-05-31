package com.simplicity.simplicityaclientforreddit.ui.main.fragments.user

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.base.BaseActivity
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.user.main.UserDetailFragment

class UserDetailActivity : BaseActivity() {

    private var _username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_detail_activity)
        if (savedInstanceState == null) {
            intent.extras?.let{
                _username = it.getString(KEY_USER_NAME)
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, UserDetailFragment.newInstance(_username))
                .commitNow()
        }
    }

    companion object {
        const val KEY_USER_NAME = "username"
    }
}