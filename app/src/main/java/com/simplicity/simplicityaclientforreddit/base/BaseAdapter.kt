package com.simplicity.simplicityaclientforreddit.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter : ListAdapter<BaseViewHolderItem, BaseViewHolder>(BaseItemDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BaseViewHolder(layoutInflater.inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    override fun getItemViewType(position: Int): Int = getItem(position).getLayout()

    fun submitListAny(list: ArrayList<Any>) {
        submitList(list as ArrayList<BaseViewHolderItem>)
    }

    object BaseItemDiff : DiffUtil.ItemCallback<BaseViewHolderItem>() {
        override fun areContentsTheSame(oldItem: BaseViewHolderItem, newItem: BaseViewHolderItem): Boolean {
            return oldItem.data == newItem.data
        }
        override fun areItemsTheSame(oldItem: BaseViewHolderItem, newItem: BaseViewHolderItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

open class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: BaseViewHolderItem) {
        item.bind(this.itemView)
    }
}
