package com.box.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.box.com.R
import com.box.com.BuildConfig
import com.box.common.event.AppViewModel
import com.box.common.event.EventViewModel
import com.box.common.event.imEvent
import com.box.common.sdk.SDKLifecycle
import com.box.common.ui.activity.crash.CrashHandler
import com.box.common.utils.DirUtils
import com.box.common.utils.MMKVUtil
import com.box.common.utils.loge
import com.box.common.utils.logw
import com.box.common.utils.other.MaterialHeader
import com.box.common.utils.other.SmartBallPulseFooter
import com.box.other.blankj.utilcode.util.AppUtils
import com.box.other.blankj.utilcode.util.ColorUtils
import com.box.other.blankj.utilcode.util.DeviceUtils
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.blankj.utilcode.util.PathUtils
import com.box.other.hjq.toast.Toaster
import com.box.other.scwang.smart.refresh.layout.SmartRefreshLayout
import com.box.other.scwang.smart.refresh.layout.api.RefreshLayout
import com.box.other.xpopup.XPopup
import com.phantomvk.identifier.IdentifierManager
import com.phantomvk.identifier.log.Logger
import com.phantomvk.identifier.log.TraceLevel
import com.tencent.mmkv.MMKV
import java.util.concurrent.Executors
object AppViewModelProvider : ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
    private val factory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(AppInit.application)
    }
    val appModel: AppViewModel by lazy {
        ViewModelProvider(this, factory)[AppViewModel::class.java]
    }
    val eventModel: EventViewModel by lazy {
        ViewModelProvider(this, factory)[EventViewModel::class.java]
    }
}

val appViewModel: AppViewModel get() = AppViewModelProvider.appModel
val eventViewModel: EventViewModel get() = AppViewModelProvider.eventModel
/**
 * 初始化器对象，实现线程安全单例。
 */
object AppInit {

    private var isInitialized = false
    lateinit var application: Application
        private set // 只允许内部修改

    fun init(app: Application) {
        if (isInitialized) {
            logw("App has already been initialized.")
            return
        }
        this.application = app
        isInitialized = true

        Toaster.init(application)
        CrashHandler.register(application)
        MMKV.initialize(PathUtils.getInternalAppFilesPath() + "/immune")

        initViewModelsAndEvents()
        initUIComponents()
        initLogging()
        initCNOAID()
        initLifecycleListener()
    }

    fun setAppFontScale(fontScale: Float) {
        if (!isInitialized) return
        val activityList: List<Activity> = SDKLifecycle.instance.activityList
        for (activity in activityList) {
            val resources = activity.resources
            if (resources != null) {
                val configuration = resources.configuration
                // 只有当字体大小变化时才更新并重建 Activity
                if (configuration.fontScale != fontScale) {
                    configuration.fontScale = fontScale
                    resources.updateConfiguration(configuration, resources.displayMetrics)
                    activity.recreate()
                }
            }
        }
        // 持久化存储
        if (fontScale != MMKVUtil.getFontScale()) {
            MMKVConfig.fontScale = fontScale
        }
    }




    private fun initViewModelsAndEvents() {
        imEvent.init()
    }

    private fun initUIComponents() {
        // 设置全局的 SmartRefreshLayout Header 和 Footer
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context).setColorSchemeColors(
                ContextCompat.getColor(context, R.color.common_accent_color)
            )
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            SmartBallPulseFooter(context)
        }

        SmartRefreshLayout.setDefaultRefreshInitializer { _: Context, layout: RefreshLayout ->
            layout.setEnableHeaderTranslationContent(true)
                .setEnableFooterTranslationContent(true)
                .setEnableFooterFollowWhenNoMoreData(true)
                .setEnableLoadMoreWhenContentNotFull(true)
                .setEnableOverScrollDrag(false)
        }

        XPopup.setIsLightStatusBar(true)
        XPopup.setNavigationBarColor(ColorUtils.getColor(R.color.white))
    }



    private fun initLogging() {
        Logs.getConfig().setLogSwitch(BuildConfig.DEBUG)
        //设置 log 文件开关
        Logs.getConfig().setLog2FileSwitch(BuildConfig.DEBUG)
        //设置 log 文件前缀
        Logs.getConfig().setFilePrefix(AppUtils.getAppName())
        //设置 log 文件存储目录
        Logs.getConfig().setDir(DirUtils.getLogDir())
        //设置 log 边框开关
        Logs.getConfig().setBorderSwitch(true)
        //设置 log 头部信息开关
        Logs.getConfig().setLogHeadSwitch(true)
        //设置 log 单一 tag 开关（为美化 AS 3.1 的 Logcat）
        Logs.getConfig().setSingleTagSwitch(false)
    }

    fun initCNOAID() {
        if (!isInitialized) {
            loge("AppInit has not been initialized yet. Cannot init CN_OAID.")
            return
        }

        val logger = object : Logger {
            override fun log(level: TraceLevel, tag: String, message: String, throwable: Throwable?) {
                when (level) {
                    TraceLevel.VERBOSE -> Log.v(tag, message, throwable)
                    TraceLevel.DEBUG -> Log.d(tag, message, throwable)
                    TraceLevel.INFO -> Log.i(tag, message, throwable)
                    TraceLevel.WARN -> Log.w(tag, message, throwable)
                    TraceLevel.ERROR -> Log.e(tag, message, throwable)
                    TraceLevel.ASSERT -> Log.wtf(tag, message, throwable)
                }
            }
        }

        DeviceUtils.getUniqueDeviceId()
        IdentifierManager.Builder(application)
            .setDebug(BuildConfig.DEBUG)
            .setExecutor(Executors.newFixedThreadPool(1))
            .setLogger(logger)
            .build()

        Log.i("AppInit", "CNOAID has been initialized on-demand after user consent.")
    }
    private fun initLifecycleListener() {
        SDKLifecycle.instance.init(application)
        SDKLifecycle.instance.get()?.addListener(object : SDKLifecycle.Listener {
            override fun onBecameForeground() {
                appViewModel.onBecameBackground = false
                // ...
            }

            override fun onBecameBackground() {
                appViewModel.onBecameBackground = true
                // ...
            }
        })
    }

    fun initUmeng(context: Context) {
        // ... 友盟初始化代码 ...
    }
}