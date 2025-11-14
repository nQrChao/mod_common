package com.box.common.network

import com.box.common.appViewModel
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class SendHeadInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val url = chain.request().url.encodedPath
        if (chain.request().url.toString().startsWith(ApiService.D_API_URL)) {
            if (url.startsWith("/api")) {
                appViewModel.modUserInfo.value?.let {
                    builder.addHeader("token", it.token).build()
                    builder.addHeader("Authorization", it.token).build()
                }
            }
        }
        if (url.contains("userCenter")) {
            appViewModel.modUserInfo.value?.let {
                builder.addHeader("Authorization", it.token).build()
            }

        }
        appViewModel.modInitBean.value?.let {
            builder.addHeader("groupId", it.groupId).build()
        }
        return chain.proceed(builder.build())
    }

}