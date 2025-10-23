package com.box.common.network

import android.util.Log
import com.box.common.MMKVConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * 自定义头部参数拦截器，接收heads
 */
class ResponseHeadInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse: Response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            e.message?.let { Log.d("Http Error: %s", it) }
            throw e
        }
        val url = originalResponse.request.url.toString()
        if (url.contains("api/v1/accountslogin")) {
            originalResponse.headers["Jwt-Token"]?.let {
                MMKVConfig.userToken = it
            }
        }
        return originalResponse
    }

}