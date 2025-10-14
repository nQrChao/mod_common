package com.chaoji.mod.game.sdk

import android.app.Activity
import android.content.Context
import android.view.View

/**
 * 游戏渠道SDK的统一接口 (The Contract for Channel SDKs)
 * 定义了所有渠道SDK需要提供的通用能力。
 */
interface IGameChannelSDK {

    /**
     * 初始化SDK
     * @param context 通常是 Application Context 或 Activity
     * @param appId 每个渠道分配的应用ID
     */
    fun init(context: Context, appId: String)

    /**
     * 执行登录流程
     * @param activity 发起登录的 Activity
     * @param onLoginSuccess 整个登录流程（包括获取用户信息等）成功后的回调
     * @param onLoginFailure 登录失败的回调，返回错误信息和代码
     */
    fun doLogin(
        activity: Activity,
        onLoginSuccess: () -> Unit,
        onLoginFailure: (message: String, code: Int) -> Unit
    )


    /**
     * 响应退出事件
     * @param activity 发起退出的 Activity
     * @param onExitConfirm 用户确认退出后的回调，通常用于结束所有Activity和进程
     */
    fun onExit(activity: Activity, onExitConfirm: () -> Unit)

    fun initAd(activity: Activity)
    fun doAdSplash(activity: Activity, onAdShow: () -> Unit, onAdReady: (view: View) -> Unit, onAdClick: () -> Unit, onAdSkip: () -> Unit, onAdTimeOver: () -> Unit, onAdFailed: (message: String) ->
    Unit)
    fun doAdBanner(activity: Activity, onAdShow: () -> Unit, onAdReady: (view: View) -> Unit, onAdClick: () -> Unit,onAdSkip: () -> Unit, onAdTimeOver: () -> Unit, onAdFailed: (message: String) ->
                          Unit,onAdClose: () -> Unit)


}