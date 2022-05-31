package com.simplicity.simplicityaclientforreddit.ui.main.fragments.search.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.databinding.SearchFragmentBinding
import com.simplicity.simplicityaclientforreddit.databinding.SearchListSubRedditBinding
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.search.SearchActivity
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.subreddits.GetSubRedditIntentUseCase

class SearchFragment : Fragment() {

    lateinit var binding: SearchFragmentBinding

    companion object {
        fun newInstance() = SearchFragment()
        private const val TAG = "SearchFragment"
    }

    private lateinit var viewModel: SeachViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = SearchFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SeachViewModel::class.java)
        viewModel.subreddits().observe(requireActivity()) { observeSubreddits(it) }
        viewModel.isFetching().observe(requireActivity()) { observeFetching(it) }
        setUpListeners()
        binding.loading.visibility = View.GONE
    }

    private fun observeFetching(it: Boolean) {
        if(it){
            binding.loading.visibility = View.VISIBLE
        }else{
            binding.loading.visibility = View.GONE
        }
    }

    private fun setUpListeners() {
        binding.searchTextInputField.addTextChangedListener { text ->
            Log.i(TAG, "Text is changed to ${text.toString()}")
            viewModel.searchInputChanged(text.toString())
        }
        binding.nsfwListButton.setOnClickListener {
            viewModel.fetchNsfwSubReddits()
        }
    }

    private fun observeSubreddits(list: List<String>) {
        Log.i(TAG, "---- First call to observeSubreddits")
        binding.subRedditsLayout.removeAllViews()
        for(reddit in list){
            addSubRedditToView(reddit)
        }
        Log.i(TAG, "---- Added all subreddits")
    }

    private fun addSubRedditToView(subreddit: String) {
        val subredditLayout = SearchListSubRedditBinding.inflate(layoutInflater)
        subredditLayout.subReddit.text = getString(R.string.sub_reddit, subreddit)
        subredditLayout.root.setOnClickListener {
            viewModel.subRedditClicked(subreddit)
            (activity as SearchActivity).startActivityWithAnimation(GetSubRedditIntentUseCase(subreddit, requireActivity()).execute())
        }
        binding.subRedditsLayout.addView(subredditLayout.root)
    }

}