package com.simplicity.simplicityaclientforreddit.ui.main.fragments.custom

import android.view.KeyEvent
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {

    open fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}
