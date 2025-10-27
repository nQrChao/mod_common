package com.box.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.box.base.base.AppScope
import com.box.com.R
import com.box.com.BuildConfig
import com.box.common.data.model.ModInfosBean
import com.box.common.event.AppViewModel
import com.box.common.event.EventViewModel
import com.box.common.event.modEvent
import com.box.common.sdk.SDKLifecycle
import com.box.common.sdk.VasDollyUtils
import com.box.common.ui.activity.crash.CrashHandler
import com.box.common.utils.DirUtils
import com.box.common.utils.logsE
import com.box.common.utils.logsW
import com.box.common.utils.other.MaterialHeader
import com.box.common.utils.other.SmartBallPulseFooter
import com.box.other.blankj.utilcode.util.AppUtils
import com.box.other.blankj.utilcode.util.ColorUtils
import com.box.other.blankj.utilcode.util.DeviceUtils
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.blankj.utilcode.util.PathUtils
import com.box.other.cnoaid.oaid.DeviceIdentifier
import com.box.other.hjq.toast.Toaster
import com.box.other.scwang.smart.refresh.layout.SmartRefreshLayout
import com.box.other.scwang.smart.refresh.layout.api.RefreshLayout
import com.box.other.xpopup.XPopup
import com.phantomvk.identifier.IdentifierManager
import com.phantomvk.identifier.log.Logger
import com.phantomvk.identifier.log.TraceLevel
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.coroutines.cancellation.CancellationException

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
        this.application = app
        initLogging()
        if (isInitialized) {
            logsW("App has already been initialized.")
            return
        }
        isInitialized = true
        MMKV.initialize(app, PathUtils.getInternalAppFilesPath() + "/boxMMKV")
        Toaster.init(app)

        VasDollyUtils.initVasId(app)
        CrashHandler.register(app)
        initViewModelsAndEvents()
        initUIComponents()
        //initCNOAID()
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
        if (fontScale != MMKVConfig.fontScale) {
            MMKVConfig.fontScale = fontScale
        }
    }

    private fun initViewModelsAndEvents() {
        modEvent.init()
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
            logsE("AppInit has not been initialized yet. Cannot init CN_OAID.")
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
        logsE("CNOAID has been initialized on-demand after user consent.")
        getOAID()
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

    private fun getOAID() {
        AppScope.applicationScope.launch {
            try {
                //getCommonParams()
                val oaid = getOAIDWithCoroutines()
                appViewModel.oaid = oaid
                MMKVConfig.modelOAID = oaid
                getModInfos()
                Logs.e("getOAIDWithCoroutines---getOAID:$oaid")
            } catch (e: CancellationException) {
                Logs.e("Coroutine was cancelled. This is why adActive() was not called.", e)
                throw e
            } catch (e: Throwable) {
                Logs.e("getOAID process failed with a non-cancellation error", e)
            } finally {
                withContext(NonCancellable) {
                    Logs.d("Running final, non-cancellable actions.")
                }
            }
        }
    }

    fun getModInfos(): ModInfosBean {
        val modInfosBean = ModInfosBean()
        modInfosBean.deviceModel = DeviceUtils.getModel()
        modInfosBean.deviceBRAND = Build.BRAND
        modInfosBean.deviceVersionRelease = Build.VERSION.RELEASE
        modInfosBean.deviceVersionSDKInt = Build.VERSION.SDK_INT.toString()
        modInfosBean.deviceSupportedABIS0 = Build.SUPPORTED_ABIS[0]
        modInfosBean.deviceIMEI = DeviceIdentifier.getIMEI(appContext) ?: ""
        modInfosBean.deviceGUID = DeviceIdentifier.getGUID(application) ?: ""
        modInfosBean.deviceCanvas = DeviceIdentifier.getCanvasFingerprint() ?: ""
        modInfosBean.deviceUniqueDeviceId = DeviceUtils.getUniqueDeviceId()
        modInfosBean.deviceAndroidID = DeviceUtils.getAndroidID()
        modInfosBean.deviceMacAddress = DeviceUtils.getMacAddress()
        modInfosBean.deviceManufacturer = DeviceUtils.getManufacturer()
        modInfosBean.deviceSDKVersionName = DeviceUtils.getSDKVersionName()
        modInfosBean.deviceSDKVersionCode = DeviceUtils.getSDKVersionCode().toString()
        modInfosBean.devicePseudoID = DeviceIdentifier.getPseudoID()
        modInfosBean.deviceOAID = MMKVConfig.modelOAID
        modInfosBean.appName = AppUtils.getAppName()
        modInfosBean.appPackageName = AppUtils.getAppPackageName()
        modInfosBean.appVersionName = AppUtils.getAppVersionName()
        modInfosBean.appVersionCode = AppUtils.getAppVersionCode().toString()
        modInfosBean.appSignaturesMD5 = AppUtils.getAppSignaturesMD5()[0]
        modInfosBean.appSignaturesSHA1 = AppUtils.getAppSignaturesSHA1()[0]
        modInfosBean.modId = ""
        modInfosBean.modName = ""
        modInfosBean.modVasDollyId = ""
        modInfosBean.modAPIVersion = BuildConfig.API_VERSION
        modInfosBean.systemId = "1"
        MMKVConfig.modInfos = modInfosBean
        return modInfosBean
    }
}