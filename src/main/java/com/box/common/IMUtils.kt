package com.box.common

// 请确保在文件顶部导入这个函数
import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.StatFs
import android.os.SystemClock
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.box.common.BuildConfig
import com.box.common.data.PictureElem
import com.box.common.data.model.ModTradeGoodDetailBean
import com.box.common.glide.GlideApp
import com.box.common.sdk.ApkUtils
import com.box.common.sdk.ImSDK
import com.box.common.sdk.appViewModel
import com.box.common.utils.GetFilePathFromUri
import com.box.common.utils.MMKVUtil
import com.box.common.utils.totp.PasscodeGenerator
import com.box.other.blankj.utilcode.util.ActivityUtils
import com.box.other.blankj.utilcode.util.AppUtils
import com.box.other.blankj.utilcode.util.ColorUtils
import com.box.other.blankj.utilcode.util.DeviceUtils
import com.box.other.blankj.utilcode.util.GsonUtils
import com.box.other.blankj.utilcode.util.ImageUtils
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.blankj.utilcode.util.RomUtils
import com.box.other.blankj.utilcode.util.ScreenUtils
import com.box.other.blankj.utilcode.util.StringUtils
import com.box.other.blankj.utilcode.util.Utils
import com.box.other.cnoaid.oaid.DeviceID
import com.box.other.cnoaid.oaid.DeviceIdentifier
import com.box.other.cnoaid.oaid.IGetter
import com.box.other.hjq.toast.Toaster
import com.box.other.xpopup.XPopup
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.phantomvk.identifier.IdentifierManager
import com.phantomvk.identifier.functions.Consumer
import com.phantomvk.identifier.model.IdConfig
import com.phantomvk.identifier.model.IdentifierResult
import com.phantomvk.identifier.model.MemoryConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern
import kotlin.coroutines.resume
import kotlin.random.Random
import kotlin.system.exitProcess


fun getDraft(conversationID: String): String? {
    return if (MMKVUtil.getConversationDraft(conversationID) == "") null else MMKVUtil.getConversationDraft(
        conversationID
    )
}


fun getUserShowName(remark: String?, nickName: String? = ""): String {
    remark?.let {
        if (TextUtils.isEmpty(it)) {
            return nickName ?: ""
        }
        return remark
    }
    return nickName ?: ""
}


fun getSingleSequence(
    groupId: String?, nickName: String?, uid: String?,
    txt: String?
): CharSequence {
    return buildClickAndColorSpannable(
        SpannableStringBuilder(txt),
        nickName!!,
        object : ClickableSpan() {
            override fun onClick(widget: View) {
                // toPersonDetail()
            }

        })
}

fun getSequence(txt: String?, name: String?): CharSequence {
    return buildClickAndColorSpannable(
        SpannableStringBuilder(txt),
        name!!,
        object : ClickableSpan() {
            override fun onClick(widget: View) {
                // toPersonDetail()
                //Toaster.show("A")
            }

        })
}


fun buildClickAndColorSpannables(
    spannableString: SpannableStringBuilder,
    tags: MutableList<String>,
    clickableSpan: ClickableSpan?
): CharSequence {
    //val reString : CharSequence
    for (tag in tags) {
        val colorSpan = ForegroundColorSpan(ColorUtils.getColor(R.color.colorPrimary))
        val start = spannableString.toString().indexOf(tag)
        val end = spannableString.toString().indexOf(tag) + tag.length
        spannableString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        if (start >= 0) {
//            val end = spannableString.toString().indexOf(tag) + tag.length
//            if (null != clickableSpan) spannableString.setSpan(
//                clickableSpan,
//                start,
//                end,
//                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
//            )
//            spannableString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        }
    }
    return spannableString
}


fun buildClickAndColorSpannable(
    spannableString: SpannableStringBuilder,
    tag: String,
    clickableSpan: ClickableSpan?
): CharSequence {
    return buildClickAndColorSpannable(spannableString, tag, R.color.colorPrimary, clickableSpan)
}

fun buildClickAndColorSpannable(
    spannableString: SpannableStringBuilder,
    @ColorRes colorId: Int,
    clickableSpan: ClickableSpan?
): CharSequence {
    val colorSpan = ForegroundColorSpan(ColorUtils.getColor(colorId))
    if (spannableString.isNotEmpty()) {
        val end = spannableString.length
        if (null != clickableSpan) spannableString.setSpan(
            clickableSpan,
            0,
            end,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannableString.setSpan(colorSpan, 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannableString
}

fun buildClickAndColorSpannable(
    spannableString: SpannableStringBuilder,
    tag: String,
    @ColorRes colorId: Int,
    clickableSpan: ClickableSpan?
): CharSequence {
    val colorSpan = ForegroundColorSpan(ColorUtils.getColor(colorId))
    val start = spannableString.toString().indexOf(tag)
    if (start >= 0) {
        val end = spannableString.toString().indexOf(tag) + tag.length
        if (null != clickableSpan) spannableString.setSpan(
            clickableSpan,
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannableString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannableString
}


fun parseTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

fun isEmoji(string: String): Boolean {
    val p = Pattern.compile("[^\\u0000-\\uFFFF]")
    val m = p.matcher(string)
    return m.find()
}


private const val clickCount = 10
private const val clickTime: Long = 2000
private var clickHits = LongArray(clickCount)
fun countClick(commit: (() -> Unit)) {
    System.arraycopy(clickHits, 1, clickHits, 0, clickHits.size - 1)
    clickHits[clickHits.size - 1] = SystemClock.uptimeMillis()
    if (clickHits[0] >= SystemClock.uptimeMillis() - clickTime) {
        commit.invoke()
    }
}

@SuppressLint("SimpleDateFormat")
fun generateTotpNumber(key: String) {
    //数组重新初始化
    val currentDate = Date()
    val date = SimpleDateFormat("yyyyMMddhhmm")
    val dataString = date.format(currentDate)
    val totpNum: String = PasscodeGenerator.generateTotpNum(key, dataString)
    Toaster.show(totpNum)
}

@SuppressLint("SimpleDateFormat")
fun verifyTOTP(Key: String?, otp: String?): Boolean {
    val currentDate = Date()
    val date = SimpleDateFormat("yyyyMMddhhmm")
    val dataString = date.format(currentDate)
    return PasscodeGenerator.generateTotpNum(Key!!, dataString) == otp
}


const val min = 1
const val max = 9999999
fun getIntUUID(): Int {
    return Random.nextInt(min, max + 1)
}

val uriNotification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
val mRingNotification: Ringtone = RingtoneManager.getRingtone(appContext, uriNotification)
fun playNotification() {
    if (!mRingNotification.isPlaying)
        mRingNotification.play()
}

val uriRingtone: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
val mRingtone: Ringtone = RingtoneManager.getRingtone(appContext, uriRingtone)
fun playRingtone() {
    if (!mRingtone.isPlaying)
        mRingtone.play()
}

fun stopRingtone() {
    if (mRingtone.isPlaying) {
        mRingtone.stop()
    }
}


fun hasNotificationPermission(): Boolean {
    val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // 在Android 8.0及以上版本
        notificationManager.areNotificationsEnabled()
    } else {
        // 在Android 7.0及以下版本
        NotificationManagerCompat.from(appContext).areNotificationsEnabled()
    }
}

fun loadAssetFileAsString(context: Context, fileName: String): String {
    // 如何使用：
    // 在你的 Activity 或其他需要的地方调用
    // val fileContent = loadAssetFileAsString(this, "path/to/your/file_in_assets.txt")
    // val fileContent = loadAssetFileAsString(applicationContext, "path/to/your/file_in_assets.assets")
    var inputStream: InputStream? = null
    var content = ""
    try {
        // 获取 AssetManager
        val assetManager = context.assets

        // 打开文件，fileName 是相对于 library assets 目录的路径
        inputStream = assetManager.open(fileName)

        // 读取文件内容
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)

        // 将字节数组转换为 String（假设是文本文件）
        content = String(buffer, Charsets.UTF_8) // 或者使用其他合适的编码

    } catch (e: IOException) {
        e.printStackTrace()
        content = ""
    } finally {
        try {
            inputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return content
}

fun loadPicture(url: String, onError: (() -> Unit)? = null, onReady: (() -> Unit)? = null): RequestBuilder<*> {

    return GlideApp.with(ImSDK.instance.getContext())
        .load(url)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onError?.let {
                    it.invoke()
                }
                return onError != null
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onReady?.let {
                    it.invoke()
                }
                return false
            }
        }).placeholder(R.mipmap.ic_chat_photo)
        .error(R.mipmap.ic_chat_photo)
}

fun loadPicture(pic: ModTradeGoodDetailBean.PicList, onError: (() -> Unit)? = null, onReady: (() -> Unit)? = null): RequestBuilder<*> {
    return GlideApp.with(ImSDK.instance.getContext())
        .load(pic.pic_path)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onError?.let {
                    it.invoke()
                }
                return onError != null
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onReady?.let {
                    it.invoke()
                }
                return false
            }
        }).placeholder(R.mipmap.ic_chat_photo)
        .error(R.mipmap.ic_chat_photo)
}

fun loadPicture(elem: PictureElem, onError: (() -> Unit)? = null, onReady: (() -> Unit)? = null): RequestBuilder<*> {
    var url: String? = ""
    val filePath: String = elem.sourcePath
    if (GetFilePathFromUri.fileIsExists(filePath)) url = filePath

    if (TextUtils.isEmpty(url)) {
        elem.snapshotPicture?.let {
            url = it.url + "?key=" + ImSDK.appViewModelInstance.userInfo.value
        }
    }
    if (TextUtils.isEmpty(url)) {
        elem.sourcePicture?.let {
            url = it.url + "?key=" + ImSDK.appViewModelInstance.userInfo.value
        }
    }
    return GlideApp.with(ImSDK.instance.getContext())
        .load(url)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onError?.let {
                    it.invoke()
                }
                return onError != null
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onReady?.let {
                    it.invoke()
                }
                return false
            }
        }).placeholder(R.mipmap.ic_chat_photo)
        .error(R.mipmap.ic_chat_photo)
}

fun toBrowser(url: String) {
    val intent = Intent()
    intent.setAction("android.intent.action.VIEW")
    val contentUrl = Uri.parse(url)
    intent.setData(contentUrl)
    ActivityUtils.startActivity(intent)
}

fun toWeChat(customerServiceUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(customerServiceUrl))
    try {
        ActivityUtils.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toaster.show("无法打开，请确保已安装微信")
    }
}

fun randomColor(): Int = Color.rgb(
    Random.nextInt(122, 222),
    Random.nextInt(122, 222),
    Random.nextInt(122, 222)
)

private fun getUserAgent(context: Context): String {
    val systemUa = System.getProperty("http.agent")
    var result = """
            ${WebSettings.getDefaultUserAgent(context)}
            $systemUa
            """.trimIndent()
    if (StringUtils.isEmpty(result)) {
        result = "unknown"
    }
    return result
}

private const val BYTE_FACTOR = 1024
private fun getTotalInternalMemorySize(): Float {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    val blockSize = stat.blockSizeLong
    val totalBlocks = stat.blockCountLong
    val valInBytes = totalBlocks * blockSize
    return valInBytes.toFloat() / (BYTE_FACTOR * BYTE_FACTOR * BYTE_FACTOR)
}

suspend fun getDetailedInformation(context: Context, type: Boolean): StringBuilder {
    val builder = StringBuilder()
    builder.append("♡♡♡\t：\t")
    builder.append("\n\nTGID\t：\t").append(ApkUtils.getTgid())
    builder.append("\nAPP_ID\t：\t").append(BuildConfig.APP_UPDATE_ID)
    builder.append("\n\n应用名称\t\t：\t").append(AppUtils.getAppName())
    builder.append("\n应用包名\t\t：\t").append(AppUtils.getAppPackageName())
    builder.append("\n版本代码\t\t：\t").append(AppUtils.getAppVersionCode())
    builder.append("\n版本名称\t\t：\t").append(AppUtils.getAppVersionName())

    builder.append("\n设备品牌\t\t：\t").append(Build.BRAND)
    builder.append("\n设备型号\t\t：\t").append(DeviceUtils.getModel())
    builder.append("\n制造厂商\t\t：\t").append(DeviceUtils.getManufacturer())
    builder.append("\n屏幕宽高\t\t：\t").append(ScreenUtils.getScreenWidth()).append(" x ")
        .append(ScreenUtils.getScreenHeight())
    builder.append("\n安卓版本\t\t：\t").append(Build.VERSION.RELEASE)
    builder.append("\n存储大小\t\t：\t").append(getTotalInternalMemorySize()).append(" Gb")
    builder.append("\nROOT???\t\t：\t").append(DeviceUtils.isDeviceRooted())
    builder.append("\nSDK版本\t\t：\t").append(Build.VERSION.SDK_INT)
    builder.append("\nCPU架构\t\t：\t").append(Build.SUPPORTED_ABIS[0])
    builder.append("\nROM版本\t\t：\t").append(RomUtils.getRomInfo().version)
    builder.append("\nappKey：\t").append(getAppKey(appContext))
        .append("\nappSecret：\t").append(BuildConfig.GAME_O_ID)

    if (type) {
        var oaid = appViewModel.oaid
        if (StringUtils.isEmpty(oaid)) {
            ImSDK.instance.initCNOAID()
            oaid = getOAIDWithCoroutines(appContext)
        }
        builder.append("\nOAID???：\t").append(oaid)
            .append("\nGUID???：\t").append(DeviceIdentifier.getGUID(appContext) ?: "")
            .append("\nCANVAS?：\t").append(DeviceIdentifier.getCanvasFingerprint() ?: "")
            .append("\nAndroidId：\t").append(DeviceUtils.getAndroidID() ?: "")
            .append("\nOAID???：\t").append(BuildConfig.GAME_O_ID)
            .append("\nOAID???：\t").append(BuildConfig.GAME_O_ID)
    }

    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val packageInfo = context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.GET_PERMISSIONS
        )
        builder.append("\n\n捕获时间\t\t：\t").append(dateFormat.format(Date()))
        builder.append("\n最近安装\t\t：\t")
            .append(dateFormat.format(Date(packageInfo.lastUpdateTime)))
        builder.append("\n首次安装\t\t：\t")
            .append(dateFormat.format(Date(packageInfo.firstInstallTime)))
        val permissions = listOf(*packageInfo.requestedPermissions)
        if (permissions.contains(Permission.READ_EXTERNAL_STORAGE)) {
            builder.append("\n读取权限\t\t：\t").append(
                if (XXPermissions.isGranted(
                        context,
                        Permission.READ_EXTERNAL_STORAGE
                    )
                ) "√" else "╳"
            )
        }
        if (permissions.contains(Permission.WRITE_EXTERNAL_STORAGE)) {
            builder.append("\n写入权限\t\t：\t").append(
                if (XXPermissions.isGranted(
                        context,
                        Permission.WRITE_EXTERNAL_STORAGE
                    )
                ) "√" else "╳"
            )
        }
        if (permissions.contains(Permission.REQUEST_INSTALL_PACKAGES)) {
            builder.append("\n安装权限\t\t：\t").append(
                if (XXPermissions.isGranted(
                        context,
                        Permission.REQUEST_INSTALL_PACKAGES
                    )
                ) "√" else "╳"
            )
        }
        builder.append("\n\nSignaturesMD5\t：\n").append(AppUtils.getAppSignaturesMD5())
        builder.append("\n\nSignaturesSHA1\t：\n").append(AppUtils.getAppSignaturesSHA1()[0])
        builder.append("\n\nSignaturesSHA256\t：\n").append(AppUtils.getAppSignaturesSHA256()[0])
    } catch (error: PackageManager.NameNotFoundException) {
        error.printStackTrace()
        return builder
    }
    builder.append("\n\nU.AGENT\t\t：\t").append(getUserAgent(context))

    return builder
}


// 使用 inline 关键字可以避免创建额外的函数调用和 Lambda 对象，性能无损耗。
inline fun runOnBuildConfig(
    onDebug: () -> Unit,
    onRelease: () -> Unit
) {
    if (BuildConfig.DEBUG) {
        onDebug()
    } else {
        onRelease()
    }
}

fun parseWithSplitToList(text: String): List<String>? {
    val parts = text.split("_", limit = 2)
    return if (parts.size == 2) parts else null
}


fun getAppKey(context: Context): String? {
    var appKey: String? = null
    try {
        val applicationInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        appKey = applicationInfo.metaData?.getString("app_key")
        Log.e("MyApp", "Successfully retrieved app_key: $appKey")
    } catch (e: PackageManager.NameNotFoundException) {
        Log.e("MyApp", "Failed to load meta-data, NameNotFound: " + e.message)
    } catch (e: NullPointerException) {
        Log.e("MyApp", "Failed to load meta-data, NullPointer: " + e.message)
    }
    return appKey
}

fun localExit(context: Context) {
    XPopup.Builder(context)
        .isDestroyOnDismiss(true)
        .hasStatusBar(true)
        .animationDuration(5)
        .navigationBarColor(ColorUtils.getColor(R.color.xpop_shadow_color))
        .isLightStatusBar(true)
        .hasNavigationBar(true)
        .asConfirm(
            "提示", "是否退出" + AppUtils.getAppName(),
            "取消", "确定",
            {
                ActivityUtils.finishAllActivities()
                exitProcess(0)
            }, null, false, R.layout.xpopup_confirm
        ).show()
}

fun loadRoundImage(view: ImageView, resId: Int?, radiusInDp: Int) {
    val density = appContext.resources.displayMetrics.density
    val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

    GlideApp.with(appContext)
        .load(resId)
        .transform(CenterCrop(), RoundedCorners(radiusInPixels))
        .error(com.box.common.R.drawable.status_error_ic)
        .into(view)
}

fun loadRoundImage(view: ImageView, url: String?, radiusInDp: Int) {
    if (url.isNullOrEmpty()) {
        view.setImageResource(R.drawable.status_error_ic)
    } else {
        val density = appContext.resources.displayMetrics.density
        val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

        GlideApp.with(appContext)
            .load(url)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            .error(R.drawable.status_error_ic)
            .into(view)
    }
}


/**
 * 获取 OAID 的方法 (回调)
 */
private const val MAX_RETRY = 5
private const val RETRY_DELAY_MS = 500L
fun getOAIDWithRetry(
    context: Context,
    onSuccess: (oaid: String) -> Unit
) {
    val currentMethodName = Thread.currentThread().stackTrace[1].methodName
    var retryCount = 0
    val handler = Handler(Looper.getMainLooper())

    fun attempt() {
        DeviceID.getOAID(context, object : IGetter {
            override fun onOAIDGetComplete(oaid: String) {
                val dealOAID = dealOAID(oaid)
                Logs.e("getOAIDWithRetry...OAID Get Complete: $dealOAID")
                onSuccess(dealOAID)
                appViewModel.oaid = dealOAID
            }

            override fun onOAIDGetError(error: Exception) {
                retryCount++
                if (retryCount < MAX_RETRY) {
                    Logs.e("getOAIDWithRetry...Retrying OAID... (Attempt $retryCount), id is error: ${error.message}")
                    handler.postDelayed(::attempt, RETRY_DELAY_MS)
                } else {
                    val pseudoID = DeviceIdentifier.getPseudoID()
                    appViewModel.oaid = pseudoID
                    Logs.e("getOAIDWithRetry...Failed to get OAID after $MAX_RETRY attempts...pseudoID:$pseudoID")
                    onSuccess(pseudoID)
                }
            }
        })
    }

    attempt()
}


/**
 * 封装单次获取OAID的结果，用于在循环中传递成功或失败信息
 */
private sealed class OaidAttemptResult {
    data class Success(val oaid: String) : OaidAttemptResult()
    data class Error(val exception: Exception) : OaidAttemptResult()
}

/**
 * 获取 OAID 的方法 (协程)
 */


/**
 * 获取 OAID 的方法 (协程)
 */
suspend fun getOAIDWithCoroutines(application: Application): String {
    val maxRetry = 3
    var retryCount = 0
    var oaid = ""
    while (retryCount < maxRetry) {
        Logs.e("getOAIDWithCoroutines", "正在进行第 ${retryCount + 1} 次尝试...")
        val result = withTimeoutOrNull(2000L) {
            suspendCancellableCoroutine<OaidAttemptResult> { continuation ->
                val consumer = object : Consumer {
                    override fun onSuccess(result: IdentifierResult) {
                        oaid = result.oaid
                        Logs.d("OAID_SDK_CALLBACK", "onOAIDGetComplete 回调被触发! result: ${GsonUtils.toJson(result)}")
                        continuation.resume(OaidAttemptResult.Success(dealOAID(oaid)))
                    }

                    override fun onError(msg: String, throwable: Throwable?) {
                        Logs.e("OAID_SDK_CALLBACK", "onOAIDGetError 回调被触发!", msg + "...result: ${GsonUtils.toJson(throwable)}")
                        continuation.resume(OaidAttemptResult.Error(Exception(msg)))
                    }
                }

                IdentifierManager.build()
                    .enableAsyncCallback(true) // 可选：在异步线程执行结果回调，默认为关闭
                    .enableExperimental(true)
                    .enableVerifyLimitAdTracking(true)
                    .setIdConfig(
                        IdConfig(
                            isAaidEnabled = true,
                            isVaidEnabled = true,
                            isGoogleAdsIdEnabled = true  // 可选: 使用GoogleAdsId作为备选，默认关闭
                        )
                    )
                    .setMemoryConfig(MemoryConfig(false))
                    .subscribe(consumer)

//                DeviceID.getOAID(application, object : IGetter {
//                    override fun onOAIDGetComplete(oaid: String) {
//                        if (continuation.isActive) {
//                            Logs.d("OAID_SDK_CALLBACK", "onOAIDGetComplete 回调被触发! oaid: $oaid")
//                            continuation.resume(OaidAttemptResult.Success(dealOAID(oaid)))
//                        }
//                    }
//
//                    override fun onOAIDGetError(error: Exception) {
//                        if (continuation.isActive) {
//                            Logs.e("OAID_SDK_CALLBACK", "onOAIDGetError 回调被触发!", error)
//                            continuation.resume(OaidAttemptResult.Error(error))
//                        }
//                    }
//                })
            }
        } // 如果5秒内没有回调，这里的 result 将会是 null

        if (result == null) {
            retryCount++
            Logs.e("getOAIDWithCoroutines", "[超时] 第 $retryCount 次尝试，SDK在5秒内未响应。")
            if (retryCount < maxRetry) {
                delay(1000L)
            }
            continue
        }

        when (result) {
            is OaidAttemptResult.Success -> {
                oaid = dealOAID(result.oaid)
                appViewModel.oaid = oaid
                Logs.e("getOAIDWithCoroutines...OAID Get Complete: $oaid")
                return oaid
            }

            is OaidAttemptResult.Error -> {
                retryCount++
                if (retryCount < maxRetry) {
                    Logs.e("getOAIDWithCoroutines...Retrying OAID... (Attempt $retryCount), id is error: ${result.exception.message}")
                    delay(2000L)
                }
            }
        }
    }

    if (StringUtils.isEmpty(oaid)) {
        oaid = DeviceIdentifier.getPseudoID()
    }

    Logs.e("getOAIDWithCoroutines...Failed to get OAID after $maxRetry attempts...pseudoID:$oaid")
    return oaid
}

private fun dealOAID(OAID: String): String {
    return if (OAID.contains("00000000") || OAID.isNullOrEmpty()) {
        DeviceIdentifier.getPseudoID()
    } else {
        OAID
    }
}


fun checkInstallInHoursDifference(hours: Int): Boolean {
    try {
        val packageInfo = appContext.packageManager.getPackageInfo(appContext.packageName, PackageManager.GET_PERMISSIONS)
        val firstInstallTimeMillis: Long = packageInfo.firstInstallTime
        val lastUpdateTimeMillis: Long = packageInfo.lastUpdateTime
        val currentTimeMillis: Long = System.currentTimeMillis()
        val diffInMillis: Long = currentTimeMillis - lastUpdateTimeMillis
        val diffInHours: Long = diffInMillis / (1000 * 60 * 60)
        Logs.e("应用首次安装时间: ${Date(firstInstallTimeMillis)}")
        Logs.e("应用最后安装时间: ${Date(lastUpdateTimeMillis)}")
        Logs.e("当前时间: ${Date(currentTimeMillis)}")
        Logs.e("时间差（小时）: $diffInHours")
        return diffInHours > hours
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        Logs.e("找不到指定的应用包名: $appContext.packageName")
        return false
    }
}

fun checkInstallInMinuteDifference(minutes: Int): Boolean {
    try {
        val packageInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
        val firstInstallTimeMillis: Long = packageInfo.firstInstallTime
        val lastUpdateTimeMillis: Long = packageInfo.lastUpdateTime
        val currentTimeMillis: Long = System.currentTimeMillis()
        val diffInMillis: Long = currentTimeMillis - lastUpdateTimeMillis
        val diffInMinutes: Long = diffInMillis / (1000 * 60)
        Logs.e("应用首次安装时间: ${Date(firstInstallTimeMillis)}")
        Logs.e("应用最后更新时间: ${Date(lastUpdateTimeMillis)}")
        Logs.e("当前时间: ${Date(currentTimeMillis)}")
        Logs.e("时间差（分钟）: $diffInMinutes")
        return diffInMinutes > minutes
    } catch (e: PackageManager.NameNotFoundException) {
        // 捕获找不到包名的异常
        e.printStackTrace()
        Logs.e("找不到指定的应用包名: ${appContext.packageName}")
        return false
    }
}

fun getCommonParams() {
    if (appViewModel.commonParams.value?.isEmpty() == true) {
        val commonParams = mutableMapOf<String, String>()
        commonParams["plat_id"] = "4"
        commonParams["client_type"] = "1"
        commonParams["tgid"] = ApkUtils.getTgid()
        commonParams["oldtgid"] = ApkUtils.getTgid()
        commonParams["appid"] = BuildConfig.APP_UPDATE_ID
        commonParams["api_version"] = BuildConfig.API_VERSION
        commonParams["packagename"] = Utils.getApp().packageName
        commonParams["mac"] = DeviceUtils.getMacAddress()
        commonParams["android_infos"] =
            DeviceUtils.getModel() + "|" + Build.BRAND + "|" + Build.VERSION.RELEASE + "|" + Build.VERSION.SDK_INT.toString() + "|" + AppUtils.getAppName() + "|" + Build.SUPPORTED_ABIS[0] + "|" + AppUtils.getAppSignaturesSHA1()[0]
        commonParams["oaid"] = appViewModel.oaid
        commonParams["imei"] = DeviceIdentifier.getIMEI(appContext) ?: ""
        commonParams["guid"] = DeviceIdentifier.getGUID(appContext) ?: ""
        commonParams["canvas"] = DeviceIdentifier.getCanvasFingerprint() ?: ""
        commonParams["device_id"] = DeviceUtils.getUniqueDeviceId()
        commonParams["androidid"] = DeviceUtils.getAndroidID() ?: ""
        appViewModel.commonParams.value = commonParams
    }
}


/**
 * 将网络图片保存到相册
 * @param imageUrl 图片的网络 URL
 */
fun saveNetworkImageToAlbum(imageUrl: String) {
    // 使用 Glide 下载图片并将其作为 Bitmap 加载到内存
    Glide.with(Utils.getApp())
        .asBitmap()
        .load(imageUrl)
        .into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                // 图片下载成功，现在可以调用 ImageUtils 来保存了
                val savedFile: File? = ImageUtils.save2Album(
                    resource,
                    AppUtils.getAppName(), // 相册文件夹名称
                    Bitmap.CompressFormat.JPEG, // 保存格式
                    100, // 图片质量
                    true // 保存后是否回收 Bitmap
                )
                if (savedFile != null) {
                    Toaster.show("图片已成功保存到相册: ${savedFile.absolutePath}")
                } else {
                    Toaster.show("图片保存失败")
                }
            }
        })
}