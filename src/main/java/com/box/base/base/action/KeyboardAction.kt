package com.box.base.base.action

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


interface KeyboardAction {

    fun showKeyboard(view: View?) {
        if (view == null) {
            return
        }
        val manager: InputMethodManager = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager? ?: return
        manager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    fun hideKeyboard(view: View?) {
        if (view == null) {
            return
        }
        val manager: InputMethodManager = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager? ?: return
        manager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun toggleSoftInput(view: View?) {
        if (view == null) {
            return
        }
        val manager: InputMethodManager = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager? ?: return
        manager.toggleSoftInput(0, 0)
    }
}