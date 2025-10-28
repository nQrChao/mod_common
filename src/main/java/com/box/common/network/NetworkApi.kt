package com.box.common.network

import android.annotation.SuppressLint
import android.os.Build
import android.text.TextUtils
import com.box.base.network.BaseNetworkApi
import com.box.base.network.CacheInterceptor
import com.box.base.network.LogInterceptor
import com.box.com.BuildConfig
import com.box.common.AppInit.application
import com.box.common.MMKVConfig
import com.box.common.appContext
import com.box.common.network.Des.signKey
import com.box.other.blankj.utilcode.util.AppUtils
import com.box.other.blankj.utilcode.util.DeviceUtils
import com.box.other.blankj.utilcode.util.GsonUtils
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.cnoaid.oaid.DeviceIdentifier
import com.box.other.franmontiel.persistentcookiejar.PersistentCookieJar
import com.box.other.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.box.other.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.box.other.jessyan.retrofiturlmanager.RetrofitUrlManager
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.KeyStore
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.SortedMap
import java.util.TreeMap
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

var apiUrlNetworkCode: Int = -1

var serverUrl: String = ""
    private set

// 底层的 Service Holder
private var apiServiceHolder: ApiService? = null

// 添加一个互斥锁，用于保证初始化过程的线程安全
private val initMutex = Mutex()

/**
 * 全局唯一的 ApiService 实例。
 *
 * **重要：在使用此实例之前，必须先调用并成功等待 [initializeNetwork] 挂起函数完成。**
 * 直接访问而未初始化会导致 [IllegalStateException]。
 *
 * ### 正确使用模式
 * 在一个协程中先调用 `initializeNetwork()`，然后再访问 `apiService`。
 * 例如，在 ViewModel 的某个方法中：
 * ```
 * fun fetchData() {
 * viewModelScope.launch {
 * try {
 * initializeNetwork() // 1. 确保网络已初始化
 * // 2. 此处可以安全地使用 apiService
 * val result = apiService.someApiCall()
 * // ... 处理结果
 * } catch (e: Exception) {
 * // 处理初始化或网络请求失败
 * }
 * }
 * }
 * ```
 *
 * @throws IllegalStateException 如果在 [initializeNetwork] 完成前访问此实例。
 */
val apiService: ApiService
    get() {
        return apiServiceHolder
            ?: throw IllegalStateException("ApiService 尚未初始化。请先调用并等待 initializeNetwork() 完成。")
    }


open class NetworkApi : BaseNetworkApi() {
    companion object {
        val INSTANCE: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApi()
        }
    }

    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            cache(Cache(File(appContext.cacheDir, "cxk_cache"), 10 * 1024 * 1024))
            cookieJar(cookieJar)
            addInterceptor(CacheInterceptor())
            addInterceptor(TokenOutInterceptor())
            if (BuildConfig.LOG_ENABLE) {
                addInterceptor(LogInterceptor())
            }
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)

            // ========================= 关键安全修复 =========================
            //  在商用项目中，绝不能无条件信任所有SSL证书，这会使应用易受中间人攻击。
            //  以下逻辑确保只在调试(Debug)模式下为了方便抓包等操作而信任所有证书，
            //  在发布(Release)模式下，则使用安卓系统默认的安全信任机制。
            if (BuildConfig.DEBUG) {
                hostnameVerifier { _, _ -> true }
                getX509TrustManager()?.let { sslSocketFactory(getSSLSocketFactory(), it) }
                Logs.w("NetworkApi", "SSL SECURITY WARNING: Trusting all certificates in DEBUG mode.")
            }
            // 在 Release 构建中，此处不添加任何自定义的 sslSocketFactory 和 hostnameVerifier，
            // OkHttp 将自动使用系统默认的、安全的 SSL 配置。
            // ===============================================================
        }
        return builder
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        }
    }

    private val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(appContext))
    }

    // ========================= 已恢复并加固的工具方法 =========================

    private fun stringToMD5(s: String): String? {
        val hexDigits = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        )
        return try {
            val btInput = s.toByteArray()
            val mdInst = MessageDigest.getInstance("MD5")
            mdInst.update(btInput)
            val md = mdInst.digest()
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (i in 0 until j) {
                val byte0 = md[i]
                str[k++] = hexDigits[byte0.toInt() ushr 4 and 0xf]
                str[k++] = hexDigits[byte0.toInt() and 0xf]
            }
            String(str)
        } catch (e: Exception) {
            // **[健壮性修复]** 使用统一的日志框架记录错误，而不是直接打印堆栈信息。
            Logs.e("MD5", "Failed to generate MD5 hash.", e)
            null
        }
    }

    // **[代码优化]** 使用更高效和简洁的 Kotlin 写法。
    private fun mapToString(map: Map<String, String>): String {
        return map.entries.joinToString("&") { (k, v) -> "$k=$v" }
    }

    private fun getSignKey(params: Map<String, String>): String? {
        val newParams: SortedMap<String, String> = TreeMap()
        params.forEach { (key, value) ->
            try {
                // 如果参数值不为空，则进行URL编码；否则，保留为空字符串。
                val processedValue = if (!TextUtils.isEmpty(value)) {
                    URLEncoder.encode(value, "UTF-8").replace("*", "%2A")
                } else {
                    ""
                }
                newParams[key] = processedValue
            } catch (e: UnsupportedEncodingException) {
                Logs.e("getSignKey", "Error encoding parameter value for key: $key", e)
                return null // URL编码失败，直接返回null，终止签名流程。
            }
        }

        // **[安全警告]** 将密钥硬编码在代码中(Des.signKey)是极不安全的做法，容易被反编译获取。
        // 建议：将密钥存储在 BuildConfig 中，并通过 local.properties 文件注入，或者存储在NDK的C/C++代码中以提高安全性。
        val signString = mapToString(newParams) + signKey
        Logs.d("getSignKey", "String to be signed (before MD5): $signString")
        return stringToMD5(signString)
    }


    /**
     * 创建虚拟账号postData
     */
    fun createVirtualUserPostData(params: MutableMap<String, String>): String? {
        val targetParams = getVirtualUserCommonMap(params)
        val mapData = try {
            URLEncoder.encode(Des.encode(mapToString(targetParams)), "UTF-8")
        } catch (_: Exception) {
            return null
        }
        return mapData
    }

    private fun getVirtualUserCommonMap(params: MutableMap<String, String>): Map<String, String> {
        val commonParams = mutableMapOf<String, String>()
        commonParams.putAll(params)
        commonParams["virtual"] = "1"
        return getCommonMap(commonParams)
    }

    /**
     * 创建账号postData
     */
    fun createPostData(params: MutableMap<String, String>): String? {
        val targetParams = getCommonMap(params)
        Logs.d("createPostData", "Parameters with common data: ${mapToString(targetParams)}")
        val mapData = try {
            URLEncoder.encode(Des.encode(mapToString(targetParams)), "UTF-8")
        } catch (e: Exception) {
            // 捕获所有可能的异常，包括加密和URL编码。
            Logs.e("createPostData", "Failed to DES encode or URLEncode data.", e)
            return null // 如果加密或编码失败，返回null。
        }
        Logs.d("createPostData", "Final encrypted and encoded data: $mapData")
        return mapData
    }

    fun createInitPostData(params: MutableMap<String, String>): String? {
        val targetParams = getMarketInitCommonMap(params)
        Logs.d("createPostData", "Parameters with common data: ${mapToString(targetParams)}")
        val mapData = try {
            URLEncoder.encode(Des.encode(mapToString(targetParams)), "UTF-8")
        } catch (e: Exception) {
            // 捕获所有可能的异常，包括加密和URL编码。
            Logs.e("createPostData", "Failed to DES encode or URLEncode data.", e)
            return null // 如果加密或编码失败，返回null。
        }
        Logs.d("createPostData", "Final encrypted and encoded data: $mapData")
        return mapData
    }

    private fun getCommonMap(params: MutableMap<String, String>): Map<String, String> {
        val commonParams = mutableMapOf<String, String>()
        // 先放入业务参数
        commonParams.putAll(params)
        commonParams["appName"]  = AppUtils.getAppName()
        commonParams["appPackageName"]= AppUtils.getAppPackageName()
        commonParams["appVersionName"] = AppUtils.getAppVersionName()
        commonParams["appVersionCode"] = AppUtils.getAppVersionCode().toString()
        commonParams["appSignaturesMD5"] = AppUtils.getAppSignaturesMD5()[0]
        commonParams["appSignaturesSHA1"] = AppUtils.getAppSignaturesSHA1()[0]
        commonParams["modId"] = BuildConfig.MOD_ID
        commonParams["modName"] = BuildConfig.MOD_NAME
        commonParams["modVasDollyId"] = BuildConfig.MOD_VASID
        commonParams["modAPIVersion"] = BuildConfig.MOD_API_VERSION
        commonParams["systemId"] = "1"
        if (MMKVConfig.modInfos != null) {
            commonParams["deviceOAID"] = MMKVConfig.deviceOAID
            commonParams["deviceModel"] = DeviceUtils.getModel()
            commonParams["deviceBRAND"] = Build.BRAND
            commonParams["deviceVersionRelease"]= Build.VERSION.RELEASE
            commonParams["deviceVersionSDKInt"] = Build.VERSION.SDK_INT.toString()
            commonParams["deviceSupportedABIS0"] = Build.SUPPORTED_ABIS[0]
            commonParams["deviceIMEI"] = DeviceIdentifier.getIMEI(appContext) ?: ""
            commonParams["deviceGUID"] = DeviceIdentifier.getGUID(application) ?: ""
            commonParams["deviceCanvas"] = DeviceIdentifier.getCanvasFingerprint() ?: ""
            commonParams["deviceUniqueDeviceId"] = DeviceUtils.getUniqueDeviceId()
            commonParams["deviceAndroidID"]= DeviceUtils.getAndroidID()
            commonParams["deviceMacAddress"] = DeviceUtils.getMacAddress()
            commonParams["deviceManufacturer"]  = DeviceUtils.getManufacturer()
            commonParams["deviceSDKVersionName"]  = DeviceUtils.getSDKVersionName()
            commonParams["deviceSDKVersionCode"]  = DeviceUtils.getSDKVersionCode().toString()
            commonParams["devicePseudoID"]  = DeviceIdentifier.getPseudoID()

        }
        commonParams["sign"] = getSignKey(commonParams) ?: ""
        Logs.e("getCommonMap：${GsonUtils.toJson(commonParams)}")
        return commonParams
    }


    private fun getMarketInitCommonMap(params: MutableMap<String, String>): Map<String, String> {
        val commonParams = mutableMapOf<String, String>()
        // 先放入业务参数
        commonParams.putAll(params)
        commonParams["appName"]  = AppUtils.getAppName()
        commonParams["appPackageName"]= AppUtils.getAppPackageName()
        commonParams["appVersionName"] = AppUtils.getAppVersionName()
        commonParams["appVersionCode"] = AppUtils.getAppVersionCode().toString()
        commonParams["appSignaturesMD5"] = AppUtils.getAppSignaturesMD5()[0]
        commonParams["appSignaturesSHA1"] = AppUtils.getAppSignaturesSHA1()[0]
        commonParams["modId"] = BuildConfig.MOD_ID
        commonParams["modName"] = BuildConfig.MOD_NAME
        commonParams["modVasDollyId"] =  BuildConfig.MOD_VASID
        commonParams["modAPIVersion"] = BuildConfig.MOD_API_VERSION
        commonParams["systemId"] = "1"
        commonParams["sign"] = getSignKey(commonParams) ?: ""
        Logs.e("getCommonMap：${GsonUtils.toJson(commonParams)}")
        return commonParams
    }


    // ====================================================================

    // 在调试模式下使用的SSL相关方法
    private fun getSSLSocketFactory(): SSLSocketFactory {
        return try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, getTrustManager(), SecureRandom())
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun getTrustManager(): Array<TrustManager> {
        return arrayOf(
            @SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {}
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }
        )
    }

    private fun getX509TrustManager(): X509TrustManager? {
        return try {
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + trustManagers.contentToString()
            }
            trustManagers[0] as X509TrustManager
        } catch (e: Exception) {
            Logs.e("getX509TrustManager", "Failed to get trust manager", e)
            null
        }
    }
}

/**
 * 初始化网络配置，包括轮询可用服务器URL并创建[apiService]实例。
 *
 * 这是一个线程安全的、幂等的挂起函数。
 * - **幂等**：无论被调用多少次，真正的网络初始化逻辑只会执行一次。
 * - **线程安全**：使用互斥锁确保并发调用不会引发问题。
 * - **挂起函数**：调用者必须在协程中调用并等待其完成。
 */
suspend fun initializeNetwork() {
    if (apiServiceHolder != null) {
        return
    }
    initMutex.withLock {
        if (apiServiceHolder != null) {
            return
        }

        try {
            var validServerUrl: String? = null
            var validResponseCode: Int = -1

            withContext(Dispatchers.IO) {
                for (i in ApiService.HTTP_RELEASE_URLS) {
                    val fullUrlToCheck = "$i/ok.txt"
                    Logs.d("NetworkTest", "Checking URL: $fullUrlToCheck")
                    val responseCode = checkPollingUrl(fullUrlToCheck)
                    if (responseCode == 200) {
                        validResponseCode = responseCode
                        validServerUrl = i
                        Logs.d("NetworkTest", "Found valid URL: $validServerUrl")
                        break
                    }
                }

                serverUrl = validServerUrl ?: ApiService.D_API_URL
                apiUrlNetworkCode = validResponseCode
                Logs.d("NetworkTest", "Final SERVER_URL: $serverUrl, Network Code: $apiUrlNetworkCode")

                RetrofitUrlManager.getInstance().putDomain("SERVER_URL", serverUrl)
                RetrofitUrlManager.getInstance().putDomain("API_URL", "https://4319g.yize01.com")


                apiServiceHolder = NetworkApi.INSTANCE.getApi(ApiService::class.java, serverUrl)
            }
        } catch (e: Exception) {
            Logs.e("NetworkInit", "FATAL: Network initialization failed unexpectedly.", e)
            throw e
        }
    }
}

private suspend fun checkPollingUrl(url: String): Int {
    return withContext(Dispatchers.IO) {
        try {
            val response = PollingUrl.instance.checkUrl(url)
            if (response.isSuccessful) {
                Logs.d("NetworkTest", "URL '$url' is accessible (Response Code: ${response.code()})")
                response.code()
            } else {
                Logs.e("NetworkTest", "URL '$url' is not accessible (Response Code: ${response.code()})")
                -1
            }
        } catch (e: Exception) {
            Logs.e("NetworkTest", "Error checking URL '$url': ${e.message}")
            -2
        }
    }
}
