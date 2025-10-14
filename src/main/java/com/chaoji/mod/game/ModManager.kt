package com.chaoji.mod.game

import android.app.Application

/**
 * 服务管理者，单例模式
 * 用于持有 app 模块提供的服务实现
 */
object ModManager {
    const val GAME_URL = "file:///android_asset/game/index.html"
    const val LOGIN_OK = 1518
    const val LOGIN_CANCEL = 1519
    const val BIND_PHONE_OK = 1520
    const val BIND_PHONE_CANCEL = 1521
    //const val GAME_URL = "http://192.168.28.2:7456/"
    private var _provider: ModProvider? = null

    val provider: ModProvider
        get() = _provider
            ?: throw IllegalStateException("IAppServiceProvider has not been initialized. Please call ModManager.initialize() in your Application class.")

    /**
     * 在 app 模块的 Application.onCreate() 中调用此方法进行初始化
     */
    fun initialize(application: Application, provider: ModProvider) {
        _provider = provider
    }
}