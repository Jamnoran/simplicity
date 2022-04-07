package com.simplicity.simplicityaclientforreddit.ui.main.fragments.menu

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.switchmaterial.SwitchMaterial
import com.simplicity.simplicityaclientforreddit.MainActivity
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.databinding.NavigationDrawerBinding
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.authentication.AuthenticationFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.menu.values.Keys
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.subreddits.GetSubRedditVisited

class NavigationBar(var activity: MainActivity, var binding: NavigationDrawerBinding) {

    fun init() {
        setUpProfile()
        setUpPreviousVisitedSubreddits()
        setUpSwitch(binding.switchNsfw, SettingsSP().loadSetting(Keys.NSFW, true), Keys.NSFW)
        setUpSwitch(binding.switchSfw, SettingsSP().loadSetting(Keys.SFW, true), Keys.SFW)
    }

    private fun setUpProfile() {
        val accessToken = SettingsSP().loadSetting(SettingsSP.KEY_ACCESS_TOKEN, "")
        Log.i("NavigationBar", "AccessToken $accessToken")
        if (accessToken.isNotEmpty()) {
            showLoggedIn(accessToken)
        } else {
            showConnectButton()
        }
        binding.authenticateButton.setOnClickListener {
            activity.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawers()
            activity.startFragment(AuthenticationFragment())
        }
    }

    private fun showConnectButton() {
        binding.authenticateButton.visibility = View.VISIBLE
        binding.profileName.visibility = View.GONE
    }

    private fun showLoggedIn(accessToken: String) {
        binding.authenticateButton.visibility = View.GONE
        binding.profileName.let {
            it.visibility = View.VISIBLE
            it.text = accessToken
        }
    }

    private fun setUpPreviousVisitedSubreddits() {
        for (subreddit in GetSubRedditVisited().execute()) {
            addSubRedditLink(subreddit)
        }
    }

    private fun addSubRedditLink(subreddit: String) {
        val layout = activity.layoutInflater.inflate(R.layout.menu_itemsub_reddit, null)
        layout.findViewById<TextView>(R.id.sub_reddit_name)?.let {
            it.text = subreddit
            it.setOnClickListener {
                activity.subRedditClicked(subreddit)
            }
        }
        binding.previousVisitedHolder.addView(layout)
    }

    private fun setUpSwitch(switch: SwitchMaterial, originalValue: Boolean, key: String) {
        switch.isChecked = originalValue
        switch.setOnCheckedChangeListener { _, value ->
            SettingsSP().saveSetting(key, value)
        }
    }
}