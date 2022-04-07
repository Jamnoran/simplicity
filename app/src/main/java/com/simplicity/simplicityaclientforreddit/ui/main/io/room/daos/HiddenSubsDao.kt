package com.simplicity.simplicityaclientforreddit.ui.main.io.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.HiddenSubs

@Dao
interface HiddenSubsDao {
    @Query("SELECT * FROM HiddenSubs")
    fun getAll(): List<HiddenSubs>

    @Query("SELECT * FROM HiddenSubs WHERE sub LIKE :sub LIMIT 1")
    fun findBSub(sub: String): HiddenSubs?

    @Insert
    fun insertAll(vararg hiddenSub: HiddenSubs)

    @Delete
    fun delete(hiddenSub: HiddenSubs)
}