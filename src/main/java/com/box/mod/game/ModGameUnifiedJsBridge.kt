package com.box.mod.game

import android.webkit.JavascriptInterface
import com.box.base.base.AppScope
import com.box.common.appContext
import com.box.common.data.model.MarketInit
import com.box.common.getOAIDWithCoroutines
import com.box.other.blankj.utilcode.util.GsonUtils
import kotlinx.coroutines.launch
import com.box.other.blankj.utilcode.util.Logs // 确保你的Library能访问这个Log类

/**
 * 统一的双向 Bridge。
 * 它只依赖于 ModGameBridgeHost 接口和 CoroutineScope。
 */
class ModGameUnifiedJsBridge(
    private val host: ModGameBridgeHost,
) {
    val TAG = ModGameUnifiedJsBridge::class.java.simpleName

    //================================================================
    // Region: JS -> Native
    //================================================================

    @JavascriptInterface
    fun loadingCompleted(msg: String?) {
        Logs.e("loadingCompleted:$msg")
        val goTestValue = host.getGoTestValue()
        if (goTestValue in 1..2) {
            val status = if (goTestValue == 2) "1" else "0"
            marketInit(status)
        } else {
            val marketInitJson = host.getMarketInitJson()
            if (marketInitJson.isNullOrEmpty()) {
                marketInit("0")
            } else {
                val marketInitData = GsonUtils.fromJson(marketInitJson, MarketInit::class.java)
                marketInit(marketInitData.status.toString())
            }
        }
    }

    @JavascriptInterface
    fun appLogin(msg: String?) {
        Logs.e("${TAG}:appLogin:$msg")
        enterLogin("ok")
    }

    @JavascriptInterface
    fun showMainView(msg: String?) {
        Logs.e("${TAG}:showMainView:$msg")
        host.onStartXDMain()
    }

    @JavascriptInterface
    fun openGame(msg: String) {
        Logs.e("${TAG}:openGame:$msg")
        host.onHandleJumpAction("启动_${msg.toInt()}")
    }

    @JavascriptInterface
    fun exchangeTimes(msg: String) {
        Logs.e("${TAG}:exchangeTimes:$msg")
        if (host.onCheckUserLogin()) {
            host.exchangeType = msg
            val uid = host.getUserUid()
            val token = host.getUserToken()
            host.onPostCocosExchange(msg, uid, token)
        }
    }

    @JavascriptInterface
    fun getGameList(msg: String?) {
        Logs.e("${TAG}:getGameList:$msg")
        if (msg == "1" || msg == "2") {
            host.onFetchGameList(msg)
        }
    }

    @JavascriptInterface
    fun showGameList(msg: String?) {
        Logs.e("${TAG}:showGameList:$msg")
        host.onFetchGameList("3") // 自定义一个类型 "3" 代表 showGameList
    }

    @JavascriptInterface
    fun startWebView(msg: String) {
        host.getPrivacyPolicyUrl()?.let { host.onOpenWebView(it) }
        Logs.e("${TAG}:startWebView:$msg")
//        val url = msg.ifEmpty { host.getPrivacyPolicyUrl() }
//        if (!url.isNullOrEmpty()) {
//            host.onOpenWebView(url)
//        }
    }

    @JavascriptInterface
    fun showToast(msg: String?) {
        Logs.e("${TAG}:showToast:$msg")
        host.onShowToast(msg)
    }

    @JavascriptInterface
    fun getDeviceId(msg: String?): String {
        Logs.e("${TAG}:getDeviceId:$msg")
        AppScope.applicationScope.launch {
            try {
                val oaid = getOAIDWithCoroutines()
                host.setOaid(oaid)
                takeDeviceId(oaid) // 将获取到的ID传给H5
            } catch (e: Throwable) {
                Logs.e("${TAG}:getDeviceId process failed", e)
            }
        }
        return host.getOaid() // 立即返回当前已有的OAID
    }

    @JavascriptInterface
    fun getAndroidId(msg: String?): String {
        Logs.e("${TAG}:getAndroidId:$msg")
        return host.getAndroidId()
    }

    @JavascriptInterface
    fun getTGID(msg: String?): String {
        Logs.e("${TAG}:getTGID:$msg")
        return host.getTgid()
    }

    @JavascriptInterface
    fun getPackageName(msg: String?): String {
        Logs.e("${TAG}:getPackageName:$msg")
        return host.getPackageName()
    }

    // 其他 JS 调用的接口 ...
    @JavascriptInterface
    fun getUserInfo(msg: String?) { /* 空实现或调用 host 接口 */
    }

    @JavascriptInterface
    fun openAdvertisement(msg: String?) { /* 空实现或调用 host 接口 */
    }

    //================================================================
    // Region: Native -> JS
    //================================================================

    fun marketInit(marketInit: String) {
        postToJs("marketInit('$marketInit')")
    }

    fun sendGameList1(gameList: String) {
        postToJs("getGameList1('$gameList')")
    }


    fun sendGameList2(gameList: String) {
        postToJs("getGameList2('$gameList')")
    }

    fun exchangeResult(exchangeType: String) {
        postToJs("exchangeResult('$exchangeType')")
    }

    fun takeDeviceId(oaid: String) {
        postToJs("takeDeviceId('$oaid')")
    }
    fun takeTGID(id: String) {
        postToJs("takeTGID('$id')")
    }

    fun takeAndroidId(id: String) {
        postToJs("takeAndroidId('$id')")
    }

    fun takePackageName(packageName: String) {
        postToJs("takePackageName('$packageName')")
    }

    fun enterLogin(userInfo: String) {
        Logs.e("${TAG}:enterLogin:$userInfo")
        host.getHandler().post {
            host.onInitFloatView() // 这个需要特殊处理，因为它在调用JS之前执行
            host.getWebView().evaluateJavascript("javascript:enterLogin('$userInfo')", null)
        }
    }

    fun startRecordCallback(boolean: Boolean) {
        Logs.e("${TAG}:startRecordCallback:$boolean")
        if (boolean) {
            host.getHandler().post {
                host.getWebView().loadUrl("javascript:startRecordCallback('1')")
            }
        }
    }

    /** 统一的JS调用方法 */
    private fun postToJs(script: String) {
        val fullScript = "javascript:$script"
        Logs.e("${TAG}:$fullScript:$script")
        host.getHandler().post {
            host.getWebView().evaluateJavascript(fullScript, null)
        }
    }
}