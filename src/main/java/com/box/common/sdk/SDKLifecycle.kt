package com.box.common.sdk

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.box.other.blankj.utilcode.util.Logs
import com.box.common.utils.MMKVUtil
import java.util.concurrent.CopyOnWriteArrayList

class SDKLifecycle : Application.ActivityLifecycleCallbacks {
    val CHECK_DELAY: Long = 500
    val TAG: String = SDKLifecycle::class.java.name
    var activityList: MutableList<Activity> =mutableListOf()
    interface Listener {
        fun onBecameForeground()
        fun onBecameBackground()
    }

    private var instance: SDKLifecycle? = null
    private var foreground = false
    private var paused = true
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val listeners: MutableList<Listener> = CopyOnWriteArrayList()
    private var check: Runnable? = null

    companion object {
        val instance: SDKLifecycle by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SDKLifecycle()
        }
    }
    fun init(application: Application): SDKLifecycle? {
        if (instance == null) {
            instance = SDKLifecycle()
            application.registerActivityLifecycleCallbacks(instance)
        }
        return instance
    }

    operator fun get(application: Application): SDKLifecycle? {
        if (instance == null) {
            init(application)
        }
        return instance
    }

    operator fun get(ctx: Context): SDKLifecycle? {
        if (instance == null) {
            val appCtx: Context = ctx.applicationContext
            if (appCtx is Application) {
                init(appCtx as Application)
            }
            throw IllegalStateException(
                "Foreground is not initialised and " +
                        "cannot obtain the Application object"
            )
        }
        return instance
    }

    fun get(): SDKLifecycle? {
        checkNotNull(instance) {
            "Foreground is not initialised - invoke " +
                    "at least once with parameterised init/get"
        }
        return instance
    }

    fun isForeground(): Boolean {
        return foreground
    }

    fun isBackground(): Boolean {
        return !foreground
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
       // Logs.e("online:onActivityCreated==${activity}")
        // 禁止字体大小随系统设置变化
        val resources = activity.resources
        if (resources != null && resources.configuration.fontScale != MMKVUtil.getFontScale()) {
            val configuration = resources.configuration
            configuration.fontScale = MMKVUtil.getFontScale()
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        activityList.add(activity)
    }


    override fun onActivityStarted(activity: Activity) {
        //Logs.e("online:onActivityStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        paused = false
        val wasBackground = !foreground
        foreground = true
        if (check != null) handler.removeCallbacks(check!!)
        if (wasBackground) {
            //Logs.e("went foreground")
            for (l in listeners) {
                try {
                    l.onBecameForeground()
                } catch (exc: Exception) {
                    //Logs.e("Listener threw exception!:$exc")
                }
            }
        } else {
            //Logs.e("still foreground")
        }
        //Logs.e("online:onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        paused = true
        if (check != null) handler.removeCallbacks(check!!)
        handler.postDelayed(Runnable {
            if (foreground && paused) {
                foreground = false
                //Logs.e("went background")
                for (l in listeners) {
                    try {
                        l.onBecameBackground()
                    } catch (exc: java.lang.Exception) {
                        Logs.e("Listener threw exception!:$exc")
                    }
                }
            } else {
                Logs.e("still foreground")
            }
        }.also { check = it }, CHECK_DELAY)
       // Logs.e("online:onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        //Logs.e("online:onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        //Logs.e("online:onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        //Logs.e("online:onActivityDestroyed")
        activityList.remove(activity)
    }

}