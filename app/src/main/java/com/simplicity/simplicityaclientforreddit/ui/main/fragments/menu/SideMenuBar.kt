package com.simplicity.simplicityaclientforreddit.ui.main.fragments.menu

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.simplicity.simplicityaclientforreddit.MainActivity
import com.simplicity.simplicityaclientforreddit.MainViewModel
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.base.SingleFragmentActivity
import com.simplicity.simplicityaclientforreddit.databinding.NavigationDrawerBinding
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.menu.values.Keys
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.search.SearchActivity
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.user.User

class SideMenuBar(var activity: MainActivity, var binding: NavigationDrawerBinding) {
    private lateinit var viewModel: MainViewModel

    fun init() {
        viewModel = ViewModelProvider(activity).get(MainViewModel::class.java)
        setUpAuthentication()

        setUpListeners()
        viewModel.fetchListOfVisitedSubReddits()
        viewModel.user().observe(activity) { observeUser(it) }
        viewModel.visitedSubReddits().observe(activity) { observeVisitedSubReddits(it) }
    }

    fun update() {
        setUpAuthentication()
        viewModel.fetchListOfVisitedSubReddits()
    }

    private fun setUpListeners() {
        setUpSwitch(binding.switchNsfw, SettingsSP().loadSetting(Keys.NSFW, true), Keys.NSFW)
        binding.searchReddits.setOnClickListener {
            activity.closeDrawer()
            val intent = Intent(binding.root.context, SearchActivity::class.java)
            activity.startActivityWithAnimation(intent)
        }
    }

    private fun observeVisitedSubReddits(it: List<String>) {
        binding.previousVisitedHolder.removeAllViews()
        for (subreddit in it) {
            addSubRedditLink(subreddit)
        }
    }

    private fun observeUser(user: User) {
        binding.authenticateButton.visibility = View.GONE
        binding.profileSection.profileName.let {
            it.visibility = View.VISIBLE
            it.text = user.name
        }
    }

    private fun setUpAuthentication() {
        val accessToken = SettingsSP().loadSetting(SettingsSP.KEY_ACCESS_TOKEN, "")
        if (accessToken?.isEmpty() == true) {
            showAuthenticationButton()
            binding.authenticateButton.setOnClickListener {
                activity.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawers()
                val intent = Intent(activity, SingleFragmentActivity::class.java).apply {
                    putExtra(SingleFragmentActivity.KEY_FRAGMENT, SingleFragmentActivity.VALUE_AUTHENTICATION)
                }
                activity.startActivityWithAnimation(intent)
            }
        }
    }

    private fun showAuthenticationButton() {
        binding.authenticateButton.visibility = View.VISIBLE
        binding.profileSection.profileName.visibility = View.GONE
    }

    private fun addSubRedditLink(subreddit: String) {
        val layout = activity.layoutInflater.inflate(R.layout.menu_itemsub_reddit, null)
        layout.findViewById<TextView>(R.id.sub_reddit_name)?.let {
            it.text = subreddit
            it.setOnClickListener {
                activity.subRedditClicked(subreddit)
                activity.closeDrawer()
            }
        }
        layout.findViewById<Button>(R.id.remove_subreddit)?.let {
            it.setOnClickListener {
                viewModel.removeVisitedSubreddit(subreddit)
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
