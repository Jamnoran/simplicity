package com.simplicity.simplicityaclientforreddit.base

import android.content.Context
import android.view.View
import java.util.*

abstract class BaseViewHolderItem(open val data: BaseData) {
    lateinit var view: View

    val id: String = generateId()

    abstract fun getLayout(): Int

    abstract fun bind(itemView: View)

    fun string(stringRes: Int): String {
        return view.context.getString(stringRes)
    }

    fun context(): Context? {
        return view.context
    }
}

fun generateId(): String {
    return Random().nextLong().toString()
}

open class BaseData
