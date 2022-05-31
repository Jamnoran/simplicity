package com.simplicity.simplicityaclientforreddit.ui.main.io.room

import androidx.room.Room
import com.simplicity.simplicityaclientforreddit.ui.main.Global.applicationContext
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.HiddenSubs
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.ReadPost

class RoomDB {
    private val DATABASE_NAME = "simplicity_database"

    fun getAll(): List<ReadPost> {
        val db = db()
        val dao = db.readPostDao()
        val result = dao.getAll()
        db.close()
        return result
    }

    fun getTotalRowsInDatabase(): Int{
        val db = db()
        val dao = db.readPostDao()
        val result = dao.countOfPostsInDatabase()
        db.close()
        return result
    }

    fun getReadPost(post: ReadPost): ReadPost? {
        val db = db()
        val dao = db.readPostDao()
        val result = dao.findByUUID(post.uuid)
        db.close()
        return result
    }

    fun insertPost(post: ReadPost){
        val db = db()
        val dao = db.readPostDao()
        dao.insertAll(post)
        db.close()
    }

    fun deleteAllOlderThanAWeek(){
        val compareTime = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        val db = db()
        val dao = db.readPostDao()
        dao.deleteAllOlderThanAWeek(compareTime)
        db.close()
    }

    fun deleteAll(){
        val db = db()
        val dao = db.readPostDao()
        dao.deleteAll()
        db.close()
    }

    fun isSubHidden(subName: String): Boolean{
        val db = db()
        val dao = db.hiddenSubsDao()
        val result =  dao.findBSub(subName) != null
        db.close()
        return result
    }

    fun hideSub(subName: String){
        val db = db()
        val dao = db.hiddenSubsDao()
        dao.insertAll(HiddenSubs(null, subName))
        db.close()
    }

    // -----------   Helper methods:

    fun toReadPost(it: RedditPost): ReadPost? {
        var date = System.currentTimeMillis()
        it.data.id?.let{ uuid ->
            it.data.url?.let{ url ->
                return ReadPost(null, uuid, url, date)
            }
        }
        return null
    }

    private fun db(): AppDatabase {
        return  Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, DATABASE_NAME
        ).addMigrations(MIGRATION_2_3).build()
    }

}