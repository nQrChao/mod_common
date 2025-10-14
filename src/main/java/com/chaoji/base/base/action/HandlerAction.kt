package com.chaoji.base.base.action

import android.os.Handler
import android.os.Looper
import android.os.SystemClock


interface HandlerAction {

    companion object {
        val HANDLER: Handler = Handler(Looper.getMainLooper())
    }

    fun getHandler(): Handler {
        return HANDLER
    }

    fun post(runnable: Runnable): Boolean {
        return postDelayed(runnable, 0)
    }

    fun postDelayed(runnable: Runnable, delayMillis: Long): Boolean {
        return postAtTime(runnable, SystemClock.uptimeMillis() + if (delayMillis < 0) 0 else delayMillis)
    }

    fun postAtTime(runnable: Runnable, uptimeMillis: Long): Boolean {
        return HANDLER.postAtTime(runnable, this, uptimeMillis)
    }

    fun removeCallbacks(runnable: Runnable) {
        HANDLER.removeCallbacks(runnable)
    }

    fun removeCallbacks() {
        // 移除和当前对象相关的消息回调
        HANDLER.removeCallbacksAndMessages(this)
    }
}