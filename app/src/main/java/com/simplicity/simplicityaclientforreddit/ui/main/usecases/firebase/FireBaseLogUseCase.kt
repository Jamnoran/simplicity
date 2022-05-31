package com.simplicity.simplicityaclientforreddit.ui.main.usecases.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics


class FireBaseLogUseCase(val mFirebaseAnalytics: FirebaseAnalytics) {
    fun execute(event: String, tag: String, log: String) {
        val bundle = Bundle()
        bundle.putString(tag, log)
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        mFirebaseAnalytics.logEvent(event, bundle)
    }
}
