package com.box.common.ui.activity.crash

import android.app.*
import android.os.Process
import kotlin.system.exitProcess

class CrashHandler private constructor(private val application: Application) : Thread.UncaughtExceptionHandler {

    companion object {
        private const val CRASH_FILE_NAME: String = "crash_file"
        private const val KEY_CRASH_TIME: String = "key_crash_time"
        fun register(application: Application) {
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler(application))
        }
    }

    private val nextHandler: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()

    init {
        if ((javaClass.name == nextHandler?.javaClass?.name)) {
            throw IllegalStateException("are you sb?")
        }
    }

    @Suppress("ApplySharedPref")
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
//        val sharedPreferences: SharedPreferences = application.getSharedPreferences(
//            CRASH_FILE_NAME, Context.MODE_PRIVATE)
//        val currentCrashTime: Long = System.currentTimeMillis()
//        val lastCrashTime: Long = sharedPreferences.getLong(KEY_CRASH_TIME, 0)
//        sharedPreferences.edit().putLong(KEY_CRASH_TIME, currentCrashTime).commit()

//        val deadlyCrash: Boolean = currentCrashTime - lastCrashTime < 1000 * 60 * 5
//        if (BuildConfig.DEBUG) {
//            CrashActivity.start(application, throwable)
//        } else {
//            if (!deadlyCrash) {
//                RestartActivity.start(application)
//            }
//        }
        CommonActivityCrash.start(application, throwable)
        // 不去触发系统的崩溃处理（com.android.internal.os.RuntimeInit$KillApplicationHandler）
        if (nextHandler != null && !nextHandler.javaClass.name
                .startsWith("com.android.internal.os")) {
            nextHandler.uncaughtException(thread, throwable)
        }
        // 杀死进程（这个事应该是系统干的，但是它会多弹出一个崩溃对话框，所以需要我们自己手动杀死进程）
        Process.killProcess(Process.myPid())
        exitProcess(10)
    }
}