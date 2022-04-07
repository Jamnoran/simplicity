package com.simplicity.simplicityaclientforreddit.ui.main.fragments.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.databinding.TestFragmentBinding
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.custom.BaseFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.test.models.MediaObject
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.test.util.Resources
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.test.util.VerticalSpacingItemDecorator


class TestFragment : BaseFragment() {
    lateinit var binding: TestFragmentBinding
    companion object {
        fun newInstance() = TestFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = TestFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        testMethod()
    }

    private fun testMethod() {
        initRecyclerView()

    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecorator = VerticalSpacingItemDecorator(10)
        binding.recyclerView.addItemDecoration(itemDecorator)
        val mediaObjects = ArrayList<MediaObject>()
        mediaObjects.addAll(Resources.MEDIA_OBJECTS)
        binding.recyclerView.setMediaObjects(mediaObjects)
        val adapter = VideoPlayerRecyclerAdapter(mediaObjects, initGlide())
        binding.recyclerView.adapter = adapter
    }

    private fun initGlide(): RequestManager? {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.white_background)
            .error(R.drawable.white_background)
        return Glide.with(this)
            .setDefaultRequestOptions(options)
    }
}