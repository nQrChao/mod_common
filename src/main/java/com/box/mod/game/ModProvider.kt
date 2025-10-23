package com.box.mod.game

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.box.common.data.model.ModInfosBean

/**
 * 应用服务提供者接口
 * ModActivity 依赖的所有 app 模块功能都在此声明
 */
interface ModProvider {

    /**
     * 获取 Application Context
     */
    fun getApplicationContext(): Context

    /**
     * 检查用户是否已登录
     */
    fun isUserLoggedIn(): Boolean

    /**
     * 获取用户的 UID
     * @return UID，如果未登录则返回空字符串或特定值
     */
    fun getUserUid(): String
    fun getUserName(): String

    /**
     * 获取用户的 Token
     * @return Token，如果未登录则返回空字符串或特定值
     */
    fun getUserToken(): String
    fun getUserAuth(): String

    /**
     * 启动登录页面
     * @param activity 上下文 Activity
     * @param loginLauncher 用于接收登录结果的启动器
     */
    fun startLoginActivity(activity: Activity)
    fun startLoginActivity(activity: Activity, loginLauncher: ActivityResultLauncher<Intent>)

    fun startBindPhone(activity: Activity, loginLauncher: ActivityResultLauncher<Intent>)
    fun startModLoginActivity(activity: Activity, loginLauncher: ActivityResultLauncher<Intent>)

    /**
     * 启动主页面 (MainActivity)
     * @param context 上下文
     */
    fun startMainActivity(context: Context)

    /**
     * 处理特定的跳转操作，替代原来的 jumpCallback
     * @param context 上下文
     * @param action 描述跳转的字符串，例如 "详情_123", "启动_456"
     */
    fun handleJumpAction(context: Context, action: String)

    /**
     * 获取 SPUtils 中存储的 MarketInit json 字符串
     */
    fun getMarketInitJson(): String
    fun userLogout(activity: Activity)
    fun logout()

    fun hasOneKeyLogin(): Boolean
    fun openPay(activity: Activity, price: String)

    fun startCurrencyFragment()

    fun getModInfos(): ModInfosBean

}

/**
 * 服务持有者，作为 library 模块访问 Provider 的唯一入口
 */
object ModComService {
    private lateinit var provider: ModProvider
    /**
     * 这个方法将由 app 模块在启动时调用，完成“注入”
     */
    fun init(provider: ModProvider) {
        this.provider = provider
    }
    /**
     * library 模块中的代码通过这个方法来获取 Provider 实例
     */
    fun get(): ModProvider {
        if (!::provider.isInitialized) {
            throw IllegalStateException("ModProvider has not been initialized. Please call ModService.init() in your Application class.")
        }
        return provider
    }
}