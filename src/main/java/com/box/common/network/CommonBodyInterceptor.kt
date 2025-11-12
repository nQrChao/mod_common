package com.box.common.network

import com.box.com.BuildConfig
import com.box.common.utils.mmkv.MMKVConfig
import com.box.other.blankj.utilcode.util.AppUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException

class CommonBodyInterceptor : Interceptor {
    private val gson = Gson()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        // 检查是否为 POST 请求，以及是否为 application/json 类型
        if (originalRequest.method != "POST" || originalRequest.body == null) {
            return chain.proceed(originalRequest)
        }
        val originalBody = originalRequest.body
        val contentType = originalBody?.contentType()
        if (contentType == null || contentType.subtype != "json") {
            return chain.proceed(originalRequest)
        }

        // 获取原始的 body 内容 (JSON 字符串)
        val originalJson = bodyToString(originalBody)

        // 将原始 body 解析为 Map
        val jsonMap: MutableMap<String, Any?> = if (originalJson.isBlank()) {
            mutableMapOf()
        } else {
            // 使用 TypeToken 来正确解析为 Map
            val type = object : TypeToken<MutableMap<String, Any?>>() {}.type
            gson.fromJson(originalJson, type)
        }

        // 公共参数，未授权参数
        val commonParams = mutableMapOf<String, Any?>(
            "appName" to AppUtils.getAppName(),
            "appPackageName" to AppUtils.getAppPackageName(),
            "appVersionName" to AppUtils.getAppVersionName(),
            "appVersionCode" to AppUtils.getAppVersionCode(),
            "appSignaturesMD5" to AppUtils.getAppSignaturesMD5(),
            "appSignaturesSHA1" to AppUtils.getAppSignaturesSHA1(),
            "launchCount" to MMKVConfig.launchCount,
            "modId" to BuildConfig.MOD_ID,
            "modName" to BuildConfig.MOD_NAME,
            "modVasDollyId" to BuildConfig.MOD_VASID,
            "modAPIVersion" to BuildConfig.MOD_API_VERSION,
            "systemId" to 1
        )
        //授权后增加
        if (MMKVConfig.deviceOAID.isNotEmpty()) {
            commonParams["deviceOAID"] = MMKVConfig.deviceOAID
            commonParams["deviceModel"] = MMKVConfig.deviceInfos.deviceModel
            commonParams["deviceBRAND"] = MMKVConfig.deviceInfos.deviceBRAND
            commonParams["deviceVersionRelease"] = MMKVConfig.deviceInfos.deviceVersionRelease
            commonParams["deviceVersionSDKInt"] = MMKVConfig.deviceInfos.deviceVersionSDKInt
            commonParams["deviceSupportedABIS0"] = MMKVConfig.deviceInfos.deviceSupportedABIS0
            commonParams["deviceIMEI"] = MMKVConfig.deviceInfos.deviceIMEI
            commonParams["deviceGUID"] = MMKVConfig.deviceInfos.deviceGUID
            commonParams["deviceCanvas"] = MMKVConfig.deviceInfos.deviceCanvas
            commonParams["deviceUniqueDeviceId"] = MMKVConfig.deviceInfos.deviceUniqueDeviceId
            commonParams["deviceAndroidID"] = MMKVConfig.deviceInfos.deviceAndroidID
            commonParams["deviceMacAddress"] = MMKVConfig.deviceInfos.deviceMacAddress
            commonParams["deviceManufacturer"] = MMKVConfig.deviceInfos.deviceManufacturer
            commonParams["deviceSDKVersionName"] = MMKVConfig.deviceInfos.deviceSDKVersionName
            commonParams["deviceSDKVersionCode"] = MMKVConfig.deviceInfos.deviceSDKVersionCode
            commonParams["devicePseudoID"] = MMKVConfig.deviceInfos.devicePseudoID
        }

        // 将公共参数添加到 Map 中
        // (如果 key 相同，公共参数会覆盖原始参数)
        jsonMap.putAll(commonParams)
        // 将合并后的 Map 转换回 JSON 字符串
        val newJson = gson.toJson(jsonMap)
        // 创建新的 RequestBody
        val newBody = newJson.toRequestBody(contentType)
        // 构建新的 Request 并继续请求
        val newRequest = originalRequest.newBuilder()
            .post(newBody)
            .build()

        return chain.proceed(newRequest)
    }

    /**
     * 辅助函数：将 RequestBody 转换为 String
     */
    private fun bodyToString(requestBody: RequestBody?): String {
        return try {
            val buffer = Buffer()
            requestBody?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            ""
        }
    }
}