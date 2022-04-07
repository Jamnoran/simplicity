package com.simplicity.simplicityaclientforreddit.ui.main.models.internal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReadPost(
    @PrimaryKey
    val id: Int?,
    @ColumnInfo(name = "uuid") val uuid: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "date") val date: Long
) {

}