package com.simplicity.simplicityaclientforreddit.ui.main.io.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.simplicity.simplicityaclientforreddit.ui.main.io.room.daos.HiddenSubsDao
import com.simplicity.simplicityaclientforreddit.ui.main.io.room.daos.ReadPostDao
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.HiddenSubs
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.ReadPost

@Database(entities = [ReadPost::class, HiddenSubs::class], version = 3, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readPostDao(): ReadPostDao
    abstract fun hiddenSubsDao(): HiddenSubsDao
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `HiddenSubs` (`id` INTEGER, `sub` TEXT NOT NULL, " +
                "PRIMARY KEY(`id`))")
    }
}