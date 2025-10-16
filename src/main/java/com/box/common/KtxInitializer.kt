package com.box.common

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.startup.InitializationProvider
import androidx.startup.Initializer
import com.box.base.ext.lifecycle.KtxAppLifeObserver
import com.box.base.ext.lifecycle.KtxLifeCycleCallBack
import com.box.base.network.NetworkStateReceive

// 全局 Application Context
val appContext: Application by lazy { KtxManager.app }

// 用于持有 Context 和其他全局实例
object KtxManager {
    lateinit var app: Application
    // 配置项
    var watchAppLife = true
    var watchActivityLife = true

    fun install(application: Application) {
        if (::app.isInitialized) {
            return // 防止重复初始化
        }
        app = application
        // 注册网络状态接收器
        listenNetworkState(app)
        // 注册生命周期回调
        if (watchActivityLife) {
            application.registerActivityLifecycleCallbacks(KtxLifeCycleCallBack())
        }
        if (watchAppLife) {
            ProcessLifecycleOwner.get().lifecycle.addObserver(KtxAppLifeObserver)
        }
    }

    private fun listenNetworkState(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = NetworkStateReceive()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }
}

class KtxInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        val application = context.applicationContext as Application

        try {
            val providerInfo = context.packageManager.getProviderInfo(
                ComponentName(context, InitializationProvider::class.java),
                PackageManager.GET_META_DATA
            )
            val metaData = providerInfo.metaData
            if (metaData != null) {
                // 将配置传递给 KtxManager
                KtxManager.watchActivityLife = metaData.getBoolean("watchActivityLife", true)
                KtxManager.watchAppLife = metaData.getBoolean("watchAppLife", true)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // 正常情况下不会发生，但加上保护更安全
            e.printStackTrace()
        }

        KtxManager.install(application)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}