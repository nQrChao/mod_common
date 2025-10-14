package com.chaoji.mod.game.sdk

import android.app.Application

/**
 * 您的游戏SDK对外的统一入口和管理者
 */
object ModSdkManager {

    private var channelSdk: IGameChannelSDK? = null

    /**
     * 获取渠道SDK的实现。
     * @throws IllegalStateException 如果未初始化
     */
    fun getChannelSdk(): IGameChannelSDK {
        return channelSdk ?: throw IllegalStateException("ModSdkManager has not been initialized. Please call initialize() first.")
    }

    /**
     * 初始化整个游戏SDK。
     * 这个方法应该在您App的Application.onCreate()中被调用。
     * @param application Application实例
     * @param sdkImplementation 渠道SDK的具体实现类实例
     */
    fun initialize(application: Application, sdkImplementation: IGameChannelSDK) {
        // 保存渠道SDK的实现
        channelSdk = sdkImplementation

        // 在这里可以进行其他您SDK需要做的全局初始化操作...
    }
}