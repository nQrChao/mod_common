package com.chaoji.im.sdk

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDex
import com.chaoji.base.base.action.ActivityAction
import com.chaoji.common.BuildConfig
import com.chaoji.common.R
import com.chaoji.im.event.AppViewModel
import com.chaoji.im.event.EventViewModel
import com.chaoji.im.event.imEvent
import com.chaoji.im.ui.activity.crash.CrashHandler
import com.chaoji.im.utils.MMKVUtil
import com.chaoji.im.utils.UtilDir
import com.chaoji.im.utils.other.MaterialHeader
import com.chaoji.im.utils.other.SmartBallPulseFooter
import com.chaoji.other.blankj.utilcode.util.ActivityUtils
import com.chaoji.other.blankj.utilcode.util.AppUtils
import com.chaoji.other.blankj.utilcode.util.ColorUtils
import com.chaoji.other.blankj.utilcode.util.DeviceUtils
import com.chaoji.other.blankj.utilcode.util.FileUtils
import com.chaoji.other.blankj.utilcode.util.Logs
import com.chaoji.other.blankj.utilcode.util.PathUtils
import com.chaoji.other.cnoaid.oaid.DeviceIdentifier
import com.chaoji.other.hjq.toast.Toaster
import com.chaoji.other.scwang.smart.refresh.layout.SmartRefreshLayout
import com.chaoji.other.scwang.smart.refresh.layout.api.RefreshLayout
import com.chaoji.other.xpopup.XPopup
import com.phantomvk.identifier.IdentifierManager
import com.phantomvk.identifier.log.Logger
import com.phantomvk.identifier.log.TraceLevel
import com.tencent.mmkv.MMKV
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import kotlin.random.Random


val appViewModel: AppViewModel by lazy { ImSDK.appViewModelInstance }
val eventViewModel: EventViewModel by lazy { ImSDK.eventViewModelInstance }

class ImSDK : ActivityAction, LifecycleOwner, ViewModelStoreOwner {
    override val lifecycle: Lifecycle = LifecycleRegistry(this)
    var netConnected = true
    lateinit var sdkApplication: Application
    private var isInitialized = false // 添加一个标志位来跟踪初始化状态
    private var mFactory: ViewModelProvider.Factory? = null
    override val viewModelStore: ViewModelStore = ViewModelStore()
    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    companion object {
        private var flag = false
        var isCreateMainActivity = false
        lateinit var mainActivityRef: WeakReference<Activity>
        lateinit var eventViewModelInstance: EventViewModel
        lateinit var appViewModelInstance: AppViewModel
        val instance: ImSDK by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ImSDK()
        }
    }

    init {
        if (!flag) {
            flag = true
        } else {
            throw Throwable("SingleTon is being attacked.")
        }
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

    fun initCNOAID() {
        IdentifierManager.Builder(sdkApplication)
            .setDebug(BuildConfig.DEBUG)
            .setExecutor(Executors.newFixedThreadPool(1))// 可选: 设置自定义ThreadPoolExecutor
            .setLogger(logger)
            .setMergeRequests(false) // 可选：合并多个请求，默认关闭
            .build()
        //DeviceIdentifier.register(sdkApplication);
    }


    fun init(application: Application): ImSDK {
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        if (!isInitialized) {
            sdkApplication = application
            isInitialized = true
            Logs.d("ImSDK", "ImSDK initialized with application: $sdkApplication")
        } else {
            Logs.w("ImSDK", "ImSDK already initialized.")
        }

        CrashHandler.register(application)
        Toaster.init(application)
        MultiDex.install(application)
        MMKV.initialize(PathUtils.getInternalAppFilesPath() + "/immune")
        SDKLifecycle.instance.init(application)
        SDKLifecycle.instance.get()?.addListener(object : SDKLifecycle.Listener {
            override fun onBecameForeground() {
                appViewModel.onBecameBackground = false
                //Logs.e("当前程序切换到前台")
                if (isCreateMainActivity) {
                    if (ActivityUtils.isActivityAlive(mainActivityRef.get())) {
                        //Logs.e("MainActivity_LIVE")
                    } else {
                        AppUtils.relaunchApp(true)
                        //Logs.e("MainActivity_OVER")
                    }
                }
            }

            override fun onBecameBackground() {
                appViewModel.onBecameBackground = true
                //Logs.e("当前程序切换到后台")
            }
        })
        //sdkApplication.registerActivityLifecycleCallbacks(lifecycle)
        appViewModelInstance = getAppViewModelProvider()[AppViewModel::class.java]
        eventViewModelInstance = getAppViewModelProvider()[EventViewModel::class.java]
        imEvent.init()
        // 设置全局的 Header 构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context, _: RefreshLayout ->
            MaterialHeader(context).setColorSchemeColors(
                ContextCompat.getColor(
                    context,
                    R.color.common_accent_color
                )
            )
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context: Context, _: RefreshLayout ->
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

        val fileDir: String = UtilDir.getGameFileDir()
        FileUtils.createOrExistsDir(fileDir)
        UtilDir.APK_FILE_PATH = fileDir
        FileUtils.createOrExistsDir(UtilDir.getAppFileMediaDir())


        //设置 log 总开关
        //Logs.getConfig().setLogSwitch(BuildConfig.LOG_ENABLE)
        Logs.getConfig().setLogSwitch(BuildConfig.DEBUG)
        //设置 log 文件开关
        Logs.getConfig().setLog2FileSwitch(BuildConfig.DEBUG)
        //设置 log 文件前缀
        Logs.getConfig().setFilePrefix(AppUtils.getAppName())
        //设置 log 文件存储目录
        Logs.getConfig().setDir(UtilDir.getLogDir())
        //设置 log 边框开关
        Logs.getConfig().setBorderSwitch(true)
        //设置 log 头部信息开关
        Logs.getConfig().setLogHeadSwitch(true)
        //设置 log 单一 tag 开关（为美化 AS 3.1 的 Logcat）
        Logs.getConfig().setSingleTagSwitch(false)
//        if (!StringUtils.isEmpty(MMKVUtil.getShouQuan())) {
//            initUmeng(application)
//        }
        return this
    }


    fun getApplication(): Application {
        if (!isInitialized) {
            throw IllegalStateException("ImSDK not initialized. Call init() in Application.onCreate()")
        }
        return sdkApplication
    }


    private object CommonSingletonHolder {
        val holder = ImSDK()
    }

    override fun getContext(): Context {
        return sdkApplication;
    }


    private fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory =
                sdkApplication.let { ViewModelProvider.AndroidViewModelFactory.getInstance(it) }
        }
        return mFactory as ViewModelProvider.Factory
    }


    fun setAppFontScale(fontScale: Float) {
        val activityList: List<Activity> = SDKLifecycle.instance.activityList
        for (activity in activityList) {
            val resources = activity.resources
            if (resources != null) {
                val configuration = resources.configuration
                configuration.fontScale = fontScale
                resources.updateConfiguration(configuration, resources.displayMetrics)
                activity.recreate()
                if (fontScale != MMKVUtil.getFontScale()) {
                    MMKVUtil.setFontScale(fontScale)
                }
            }
        }
    }

    fun initUmeng(mContext: Context?) {
        val uMengAppKey = "5dcb6ea94ca357a1ed000cee"
        val channel = "CHANNAL_" + ApkUtils.getTgid()
        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        UMConfigure.init(mContext, uMengAppKey, channel, UMConfigure.DEVICE_TYPE_PHONE, null)
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
        //Logs.e("initUmeng\nuMengAppKey = $uMengAppKey\nchannel = $channel")
    }

    fun initDeviceInfo() {
        appViewModel.deviceMac = DeviceUtils.getMacAddress()
        appViewModel.deviceId = DeviceUtils.getUniqueDeviceId()
        appViewModel.deviceAndroidId = DeviceUtils.getAndroidID()
    }


    fun generateNicknameChineseChars(count: Int = 4): String {
        val result = StringBuilder()
        val random = Random.Default
        // 更常用的汉字的 Unicode 范围 (可以根据需要调整)
        val min = 0x4E00 // U+4E00: 一
        val max = 0x9FFF // U+9FFF: 龧 (这个范围仍然比较大，您可以根据需要进一步调整)
        for (i in 0 until count) {
            val codePoint = random.nextInt(max - min + 1) + min
            result.append(codePoint.toChar())
        }
        return result.toString()
    }

    fun generateNicknameLikeChineseChars(count: Int = 4): String {
        val result = StringBuilder()
        val random = Random.Default
        // 更常用的、更适合昵称的汉字列表
        val nicknameCharacters = listOf(
            "清", "风", "明", "月", "夏", "日", "微", "光", "落", "雪", "无", "声", "浅", "语", "嫣", "然",
            "星", "辰", "入", "梦", "暖", "阳", "初", "升", "静", "夜", "思", "语", "花", "开", "半", "雅",
            "云", "淡", "风", "轻", "细", "雨", "蒙", "蒙", "皓", "月", "当", "空", "繁", "星", "点", "点",
            "晨", "曦", "初", "露", "夕", "阳", "西", "下", "林", "间", "小", "溪", "山", "谷", "幽", "兰",
            "水", "墨", "丹", "青", "素", "笺", "锦", "瑟", "玲", "珑", "婉", "约", "娉", "婷", "皎", "洁",
            "安", "然", "若", "素", "悠", "然", "自", "得", "宁", "静", "致", "远", "心", "之", "所", "向",
            "陌", "上", "花", "开", "执", "手", "相", "看", "两", "不", "厌", "唯", "愿", "君", "安", "好",
            "笑", "靥", "如", "花", "眼", "眸", "似", "水", "回", "眸", "一笑", "百", "媚", "生", "六", "宫",
            "黛", "眉", "微", "蹙", "朱", "唇", "轻", "启", "素", "手", "纤", "纤", "步", "履", "轻", "盈",
            "冰", "霜", "傲", "骨", "寒", "梅", "吐", "艳", "松", "柏", "长", "青", "竹", "报", "平", "安",
            "海", "阔", "天", "空", "鱼", "翔", "浅", "底", "鹰", "击", "长", "空", "雁", "过", "留", "声",
            "清", "晨", "薄", "雾", "午", "后", "斜", "阳", "黄", "昏", "暮", "色", "夜", "幕", "降", "临",
            "红", "粉", "佳", "人", "温", "润", "如", "玉", "翩", "翩", "少年", "意", "气", "风", "发",
            "含", "情", "脉", "脉", "两", "心", "相", "悦", "天", "涯", "海", "角", "永", "不", "分", "离",
            "快", "乐", "无", "忧", "幸", "福", "安", "康", "顺", "心", "如", "意", "万", "事", "胜", "意",
            "真", "诚", "待", "人", "友", "善", "和", "睦", "乐", "于", "助", "人", "慷", "慨", "大", "方",
            "聪", "明", "伶", "俐", "慧", "心", "巧", "思", "博", "学", "多", "才", "才", "华", "横", "溢",
            "勇", "敢", "果", "断", "坚", "韧", "不", "拔", "积", "极", "进", "取", "奋", "发", "向", "上",
            "温", "文", "尔", "雅", "谈", "笑", "风", "生", "风", "度", "翩", "翩", "气", "宇", "轩", "昂",
            "诗", "情", "画", "意", "琴", "棋", "书", "画", "样", "样", "精", "通", "多", "才", "多", "艺",
            "率", "性", "真", "诚", "天", "真", "烂", "漫", "活", "泼", "可", "爱", "古", "灵", "精", "怪",
            "独", "具", "慧", "眼", "别", "出", "心", "裁", "巧", "夺", "天", "工", "独", "树", "一", "帜"
        )

        for (i in 0 until count) {
            val randomIndex = random.nextInt(nicknameCharacters.size)
            result.append(nicknameCharacters[randomIndex])
        }
        return result.toString()
    }

}