package com.simplicity.simplicityaclientforreddit.ui.main.usecases

import com.simplicity.simplicityaclientforreddit.ui.main.io.room.RoomDB

class RemoveOldPostsUseCase {
    fun removeOld(){
        val db = RoomDB()
        db.deleteAllOlderThanAWeek()
    }
}