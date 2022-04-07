package com.simplicity.simplicityaclientforreddit.ui.main

import android.content.SharedPreferences
import com.simplicity.simplicityaclientforreddit.ui.main.models.internal.Session
import com.google.gson.Gson

class SessionDatabase {
    val SESSIONS_DB_NAME = "sessions_db"
    val SESSIONS_NAME = "all_sessions_v7"
    var db = SessionDB(ArrayList())

    fun getSessions(): ArrayList<Session>{
        loadSessions()
        return db.sessions
    }

    fun updateSessions(sessions: ArrayList<Session>){
        db.sessions = sessions
        storeSessions()
    }

    fun createSession(): Session {
        loadSessions()
        val newSession = Session(getSessionId(), null, null, null, 0, null)
        db.sessions.add(0, newSession)
        storeSessions()
        return newSession
    }

    fun getCurrentSession(): Session? {
        loadSessions()
        return if (db.sessions.isNotEmpty()) {
            db.sessions.first()
        } else {
            return null
        }
    }
    fun getNextSession(): Session? {
        loadSessions()
        return if (db.sessions.size >= 2) {
            db.sessions[1]
        } else {
            return null
        }
    }

    fun loadSessions(){
//        Log.i("SessionDatabase", "Loading session database")
        val pref: SharedPreferences = Global.applicationContext.getSharedPreferences(SESSIONS_DB_NAME, 0) // 0 - for private mode
        if (pref.contains(SESSIONS_NAME)) {
            db = Gson().fromJson(pref.getString(SESSIONS_NAME, ""), SessionDB::class.java)
        }
    }

    fun storeSessions(){
//        Log.i("SessionDatabase", "Storing session database")
        val pref: SharedPreferences = Global.applicationContext.getSharedPreferences(SESSIONS_DB_NAME, 0) // 0 - for private mode
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putString(SESSIONS_NAME, Gson().toJson(db))
        editor.apply()
    }

    fun getSessionId(): String{
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }

    data class SessionDB(
        var sessions: ArrayList<Session>
    )
}