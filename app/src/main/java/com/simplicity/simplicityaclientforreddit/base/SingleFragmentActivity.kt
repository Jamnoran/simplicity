package com.simplicity.simplicityaclientforreddit.base

import android.os.Bundle
import android.util.Log
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.comments.CommentsFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.menu.values.Keys

class SingleFragmentActivity : BaseActivity() {
    private val TAG: String = "SingleFragmentActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_fragment)

        intent.extras?.let{
            val fragmentToStart = it.getString(KEY_FRAGMENT)
            if(fragmentToStart?.equals(VALUE_COMMENT) == true){
                Log.i(TAG, "Starting CommentFragment with ${it.getString(Keys.KEY_SUB_REDDIT)}  -- ${it.getString(Keys.KEY_ID)}")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CommentsFragment.newInstance(it.getString(Keys.KEY_SUB_REDDIT)!!, it.getString(Keys.KEY_ID)!!))
                    .commitNow()
            }
        }

//        setSupportActionBar(binding.toolbar);
//        val navController =
//            Navigation.findNavController(this, R.id.nav_host_fragment_content_single_fragment)
//        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration!!)
    }

    companion object {
        const val KEY_FRAGMENT = "fragment_key"
        const val VALUE_COMMENT = "fragment_comment"
        const val VALUE_AUTHENTICATION = "fragment_authentication"
    }

}