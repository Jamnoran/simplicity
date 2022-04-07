package com.simplicity.simplicityaclientforreddit.ui.main.fragments.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.simplicity.simplicityaclientforreddit.databinding.AuthenticationFragmentBinding
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.custom.BaseFragment
import android.content.Intent
import android.net.Uri
import android.util.Log


class AuthenticationFragment : BaseFragment() {
    lateinit var binding: AuthenticationFragmentBinding

    private val AUTH_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
            "&response_type=code&state=%s&redirect_uri=%s&" +
            "duration=permanent&scope=identity,edit,flair,history,mysubreddits,privatemessages,read,report,save,submit,subscribe,vote,creddits"

    private val CLIENT_ID = "5IyfyqHZKHflfxTAKUj3zg"

    private val REDIRECT_URI = "simplicity://com.simplicity/redirect"

    private val STATE = "MY_RANDOM_STRING_1"


    companion object {
        fun newInstance() = AuthenticationFragment()
    }

    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = AuthenticationFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)


        startSignIn()
    }

    private fun startSignIn() {
        val url = String.format(AUTH_URL, CLIENT_ID, STATE, REDIRECT_URI)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    fun getAccessToken(code: String) {
            Log.i("AuthenticationFragment", "Code is $code")
    }

    fun isCorrectState(state: String): Boolean {
        return state == STATE
    }

}