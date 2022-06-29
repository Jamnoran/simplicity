package com.simplicity.simplicityaclientforreddit.base

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simplicity.simplicityaclientforreddit.R

open class BaseAdapterFragment : BaseFragment() {
    private var _recyclerview: RecyclerView? = null

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        _recyclerview = view?.findViewById(R.id.recyclerView)
        _recyclerview?.let { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            val adapter = BaseAdapter()

            recyclerView.adapter = adapter
        }
    }

    fun submitList(list: ArrayList<Any>) {
        (_recyclerview?.adapter as BaseAdapter).submitListAny(list)
    }
}
