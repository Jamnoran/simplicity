package com.simplicity.simplicityaclientforreddit.ui.main.models.internal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HiddenSubs(
    @PrimaryKey
    val id: Int?,
    @ColumnInfo(name = "sub") val sub: String
) {

}