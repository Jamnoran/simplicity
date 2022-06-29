package com.simplicity.simplicityaclientforreddit.base

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.authentication.AuthenticationFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.comments.CommentsFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.menu.values.Keys

class SingleFragmentActivity : BaseActivity() {
    private val TAG: String = "SingleFragmentActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_fragment)

        intent.extras?.let {
            when (it.getString(KEY_FRAGMENT)) {
                VALUE_COMMENT -> {
                    Log.i(
                        TAG,
                        "Starting CommentFragment with ${it.getString(Keys.KEY_SUB_REDDIT)}  -- ${
                        it.getString(Keys.KEY_ID)
                        }"
                    )
                    it.getString(Keys.KEY_SUB_REDDIT)?.let { subRedditKey ->
                        it.getString(Keys.KEY_ID)?.let { keyId ->
                            it.getString(Keys.KEY_AUTHOR)?.let { author ->
                                startFragment(
                                    CommentsFragment.newInstance(
                                        subRedditKey,
                                        keyId,
                                        author
                                    )
                                )
                            }
                        }
                    }
                }
                VALUE_AUTHENTICATION -> {
                    startFragment(AuthenticationFragment())
                }
                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val f: Fragment? =
            supportFragmentManager.findFragmentById(R.id.container)
        if (f is AuthenticationFragment) {
            Log.i(TAG, "OnResume is called and is authentication fragment, lets close this screen")
            finish()
        }
    }

    companion object {
        const val KEY_FRAGMENT = "fragment_key"
        const val VALUE_COMMENT = "fragment_comment"
        const val VALUE_AUTHENTICATION = "fragment_authentication"
    }
}
