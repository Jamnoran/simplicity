package com.simplicity.simplicityaclientforreddit.ui.main.fragments.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simplicity.simplicityaclientforreddit.base.BaseAdapterFragment
import com.simplicity.simplicityaclientforreddit.databinding.FragmentTestBinding


class TestFragment : BaseAdapterFragment() {
    private val TAG: String = "TestFragment"
    lateinit var binding: FragmentTestBinding

    companion object {
        fun newInstance() = TestFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        testList()
    }

    private fun testList() {
        // ArrayList of class ItemsViewModel
        val list = ArrayList<SampleViewHolder>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..10) {
            val data = SampleData("Test value $i")
            list.add(SampleViewHolder(data))
        }

        submitList(list = list as ArrayList<Any>)
    }
}
