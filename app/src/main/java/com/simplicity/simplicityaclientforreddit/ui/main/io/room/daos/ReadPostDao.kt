package com.simplicity.simplicityaclientforreddit.ui.main.io.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.ReadPost

@Dao
interface ReadPostDao {
    @Query("SELECT * FROM ReadPost")
    fun getAll(): List<ReadPost>

    @Query("SELECT * FROM ReadPost WHERE id IN (:ReadPostIds)")
    fun loadAllByIds(ReadPostIds: IntArray): List<ReadPost>

    @Query("SELECT * FROM ReadPost WHERE url LIKE :url LIMIT 1")
    fun findByUrl(url: String): ReadPost

    @Query("SELECT * FROM ReadPost WHERE uuid LIKE :uuid LIMIT 1")
    fun findByUUID(uuid: String): ReadPost

    @Query("DELETE FROM ReadPost WHERE date <= :compareTime")
    fun deleteAllOlderThanAWeek(compareTime: Long)

    @Query("DELETE FROM ReadPost")
    fun deleteAll()

    @Query("SELECT COUNT(id) FROM ReadPost")
    fun countOfPostsInDatabase(): Int

    @Insert
    fun insertAll(vararg readPosts: ReadPost)

    @Delete
    fun delete(readPost: ReadPost)
}