package com.chaoji.im.ui.activity

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import com.chaoji.other.blankj.utilcode.util.AppUtils
import com.chaoji.other.blankj.utilcode.util.ColorUtils
import com.chaoji.other.blankj.utilcode.util.DeviceUtils.isTablet
import com.chaoji.other.blankj.utilcode.util.ResourceUtils
import com.chaoji.base.base.action.BundleAction
import com.chaoji.base.base.activity.BaseVmDbActivity
import com.chaoji.base.base.viewmodel.BaseViewModel
import com.chaoji.base.network.NetState
import com.chaoji.other.hjq.titlebar.TitleBar
import com.chaoji.common.R
import com.chaoji.common.databinding.CommonActivityCrashBinding
import com.chaoji.other.immersionbar.BarHide
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.PrintWriter
import java.io.StringWriter
import java.net.InetAddress
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.min

class CommonActivityCrash : BaseVmDbActivity<CommonActivityCrash.Model, CommonActivityCrashBinding>(), BundleAction {
    override fun layoutId(): Int = R.layout.common_activity_crash

    private var stackTrace: String? = null

    companion object {
        private const val INTENT_KEY_IN_THROWABLE: String = "throwable"

        /** 系统包前缀列表 */
        private val SYSTEM_PACKAGE_PREFIX_LIST: Array<String> = arrayOf(
            "android", "com.android",
            "androidx", "com.google.android", "java", "javax", "dalvik", "kotlin"
        )

        /** 报错代码行数正则表达式 */
        private val CODE_REGEX: Pattern = Pattern.compile("\\(\\w+\\.\\w+:\\d+\\)")

        fun start(application: Application, throwable: Throwable?) {
            if (throwable == null) {
                return
            }
            val intent = Intent(application, CommonActivityCrash::class.java)
            intent.putExtra(INTENT_KEY_IN_THROWABLE, throwable)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.startActivity(intent)
        }
    }


    override fun createObserver() {

    }

    override fun onNetworkStateChanged(it: NetState) {
    }


    override fun initView(savedInstanceState: Bundle?) {
        try {
            val throwable: Throwable = getSerializable(INTENT_KEY_IN_THROWABLE) ?: return
            mViewModel.titleT.value = throwable.javaClass.simpleName
            setStatusBarDark()
            getTitleBar()?.setTitleColor(ColorUtils.getColor(R.color.red))
            getTitleBar()?.run {
                leftIcon=  ResourceUtils.getDrawable(R.drawable.info_ic)
                rightIcon=  ResourceUtils.getDrawable(R.drawable.reboot_ic)
            }
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter)
            throwable.printStackTrace(printWriter)
            throwable.cause?.printStackTrace(printWriter)
            stackTrace = stringWriter.toString()
            val matcher: Matcher = CODE_REGEX.matcher(stackTrace!!)
            val spannable = SpannableStringBuilder(stackTrace)
            if (spannable.isNotEmpty()) {
                while (matcher.find()) {
                    val start: Int = matcher.start() + "(".length
                    val end: Int = matcher.end() - ")".length
                    var codeColor: Int = Color.parseColor("#999999")
                    val lineIndex: Int = stackTrace!!.lastIndexOf("at ", start)
                    if (lineIndex != -1) {
                        val lineData: String = spannable.subSequence(lineIndex, start).toString()
                        if (TextUtils.isEmpty(lineData)) {
                            continue
                        }
                        var highlight = true
                        for (packagePrefix: String? in SYSTEM_PACKAGE_PREFIX_LIST) {
                            if (lineData.startsWith("at $packagePrefix")) {
                                highlight = false
                                break
                            }
                        }
                        if (highlight) {
                            codeColor = Color.parseColor("#287BDE")
                        }
                    }
                    spannable.setSpan(ForegroundColorSpan(codeColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannable.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                mDataBinding.tvCrashMessage.text = spannable
            }
            val displayMetrics: DisplayMetrics = resources.displayMetrics
            val screenWidth: Int = displayMetrics.widthPixels
            val screenHeight: Int = displayMetrics.heightPixels
            val smallestWidth: Float = min(screenWidth, screenHeight) / displayMetrics.density
            val targetResource: String?
            when {
                displayMetrics.densityDpi > 480 -> {
                    targetResource = "xxxhdpi"
                }

                displayMetrics.densityDpi > 320 -> {
                    targetResource = "xxhdpi"
                }

                displayMetrics.densityDpi > 240 -> {
                    targetResource = "xhdpi"
                }

                displayMetrics.densityDpi > 160 -> {
                    targetResource = "hdpi"
                }

                displayMetrics.densityDpi > 120 -> {
                    targetResource = "mdpi"
                }

                else -> {
                    targetResource = "ldpi"
                }
            }
            val builder: StringBuilder = StringBuilder()
            builder.append("设备品牌：\t").append(Build.BRAND)
                .append("\n设备型号：\t").append(Build.MODEL)
                .append("\n设备类型：\t").append(if (isTablet()) "平板" else "手机")

            builder.append("\n屏幕宽高：\t").append(screenWidth).append(" x ").append(screenHeight)
                .append("\n屏幕密度：\t").append(displayMetrics.densityDpi)
                .append("\n密度像素：\t").append(displayMetrics.density)
                .append("\n应用名称：\t").append(AppUtils.getAppName())
                .append("\n应用包名：\t").append(AppUtils.getAppPackageName())
            builder.append("\n应用版本：\t").append(AppUtils.getAppVersionName())
                .append("\n版本代码：\t").append(AppUtils.getAppVersionCode())

            builder.append("\n安卓版本：\t").append(Build.VERSION.RELEASE)
                .append("\nAPI 版本：\t").append(Build.VERSION.SDK_INT)
                .append("\nCPU 架构：\t").append(Build.SUPPORTED_ABIS[0])

            try {
                val dateFormat = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
                val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                builder.append("\n首次安装：\t")
                    .append(dateFormat.format(Date(packageInfo.firstInstallTime)))
                    .append("\n最近安装：\t").append(dateFormat.format(Date(packageInfo.lastUpdateTime)))
                    .append("\n崩溃时间：\t").append(dateFormat.format(Date()))
                val permissions: MutableList<String> = mutableListOf(*packageInfo.requestedPermissions)
                if (permissions.contains(Permission.READ_EXTERNAL_STORAGE) ||
                    permissions.contains(Permission.WRITE_EXTERNAL_STORAGE)
                ) {
                    builder.append("\n存储权限：\t").append(
                        if (XXPermissions.isGranted(this, *Permission.Group.STORAGE)) "已获得" else "未获得"
                    )
                }
                if (permissions.contains(Permission.ACCESS_FINE_LOCATION) ||
                    permissions.contains(Permission.ACCESS_COARSE_LOCATION)
                ) {
                    builder.append("\n定位权限：\t")
                    if (XXPermissions.isGranted(this, Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)) {
                        builder.append("精确、粗略")
                    } else {
                        when {
                            XXPermissions.isGranted(this, Permission.ACCESS_FINE_LOCATION) -> {
                                builder.append("精确")
                            }

                            XXPermissions.isGranted(this, Permission.ACCESS_COARSE_LOCATION) -> {
                                builder.append("粗略")
                            }

                            else -> {
                                builder.append("未获得")
                            }
                        }
                    }
                }
                if (permissions.contains(Permission.CAMERA)) {
                    builder.append("\n相机权限：\t")
                        .append(if (XXPermissions.isGranted(this, Permission.CAMERA)) "已获得" else "未获得")
                }
                if (permissions.contains(Permission.RECORD_AUDIO)) {
                    builder.append("\n录音权限：\t").append(
                        if (XXPermissions.isGranted(this, Permission.RECORD_AUDIO)) "已获得" else "未获得"
                    )
                }
                if (permissions.contains(Permission.SYSTEM_ALERT_WINDOW)) {
                    builder.append("\n悬浮窗权限：\t").append(
                        if (XXPermissions.isGranted(this, Permission.SYSTEM_ALERT_WINDOW)) "已获得" else "未获得"
                    )
                }
                if (permissions.contains(Permission.REQUEST_INSTALL_PACKAGES)) {
                    builder.append("\n安装包权限：\t").append(
                        if (XXPermissions.isGranted(this, Permission.REQUEST_INSTALL_PACKAGES)) "已获得" else "未获得"
                    )
                }
                if (permissions.contains(Manifest.permission.INTERNET)) {
                    builder.append("\n当前网络访问：\t")

                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            InetAddress.getByName("www.baidu.com")
                            builder.append("正常")
                        } catch (ignored: UnknownHostException) {
                            builder.append("异常")
                        }
                        lifecycleScope.launch(Dispatchers.Main) {
                            mDataBinding.tvCrashInfo.text = builder
                        }
                    }
                } else {
                    mDataBinding.tvCrashInfo.text = builder
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onLeftClick(view: TitleBar) {
        mDataBinding.dlCrashDrawer.openDrawer(GravityCompat.START)
    }

    override fun onRightClick(view: TitleBar) {
        AppUtils.relaunchApp(true)
    }


    override fun getBundle(): Bundle? {
        return intent.extras
    }


    class Model: BaseViewModel(barHid = BarHide.FLAG_HIDE_BAR,title = "发现异常",isStatusBarEnabled = true) {

    }
}