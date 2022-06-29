package com.simplicity.simplicityaclientforreddit.ui.main.viewholders

import android.util.Log
import android.view.View
import android.widget.TextView
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.base.BaseViewHolderItem
import com.simplicity.simplicityaclientforreddit.base.Mapper

class TextViewHolder(var wrapper: TextViewData) : BaseViewHolderItem(wrapper) {

    override fun getLayout() = R.layout.list_item_text

    override fun bind(itemView: View) {
        view = itemView
        view.findViewById<TextView>(R.id.text_value).text = wrapper.value
        view.setOnClickListener {
            Log.i("TextViewHolder", "OnClick")
            wrapper.onClick()
        }
    }
}
// fun Mapper.mapToTextViewHolderList(list: List<String>): ArrayList<TextViewHolder> {
//    val wrappedList = ArrayList<TextViewHolder>()
//    for (textValue in list) {
//        wrappedList.add(
//            TextViewHolder(
//                TextViewData(textValue, null)
//            )
//        )
//    }
//    return wrappedList
// }

fun Mapper.mapToTextViewHolderList(list: List<String>, onClick: (textValue: String) -> Unit): ArrayList<TextViewHolder> {
    val wrappedList = ArrayList<TextViewHolder>()
    for (textValue in list) {
        wrappedList.add(
            TextViewHolder(
                TextViewData(textValue) { onClick(textValue) }
            )
        )
    }
    return wrappedList
}
