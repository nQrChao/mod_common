package com.chaoji.base.base.action

import androidx.annotation.StringRes
import com.chaoji.other.hjq.toast.Toaster
interface ToastAction {

    fun toast(text: CharSequence?) {
        Toaster.show(text)
    }
    fun toast(@StringRes id: Int) {
        Toaster.show(id)
    }
    fun toast(`object`: Any?) {
        Toaster.show(`object`)
    }
}