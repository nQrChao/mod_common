package com.chaoji.mod.game

import android.content.Context
import android.os.Handler
import android.webkit.WebView

/**
 * Bridge宿主接口 (The Contract)
 *
 * 定义了 Bridge 层需要 App 层（宿主）提供的所有能力。
 * UnifiedJsBridge 将只依赖于这个接口，从而实现与 App 模块的完全解耦。
 */
interface ModGameBridgeHost {
    // --- 核心组件提供 ---
    fun getWebView(): WebView
    fun getHandler(): Handler
    fun getAppContext(): Context

    // --- 需要App层执行的动作 ---
    fun onStartXDMain()
    fun onCheckUserLogin(): Boolean
    fun onHandleJumpAction(action: String)
    fun onShowToast(message: String?)
    fun onOpenWebView(url: String)
    fun onInitFloatView()
    fun onFetchGameList(type: String) // 统一替代 postInfoAppApi325/326/327
    fun onPostCocosExchange(msg: String, uid: String, token: String)
    fun onStartBindPhoneActivity(launcher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>)
    fun onStartLoginActivity(launcher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>)

    // --- 需要从App层获取的数据 ---
    fun getGoTestValue(): Int?
    fun getMarketInitJson(): String?
    fun getUserUid(): String
    fun getUserToken(): String
    fun getPrivacyPolicyUrl(): String?
    fun getOaid(): String
    fun getAndroidId(): String
    fun getTgid(): String
    fun getPackageName(): String

    // --- 需要向App层写入/更新的数据 ---
    fun setOaid(oaid: String)
    var exchangeType: String // 用于读写的属性
}