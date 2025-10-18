package com.box.common.network

import com.box.common.appViewModel
import com.box.common.utils.MMKVUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

class SendHeadInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        //builder.addHeader("operationID", System.currentTimeMillis().toString() + "").build()
        val url= chain.request().url.encodedPath
        if (chain.request().url.toString().startsWith(ApiService.D_API_URL)){
            if (url.startsWith("/api")){
                appViewModel.userInfo.value?.let {
                    builder.addHeader("token", it.token).build()
                }
            }
        }else{
            appViewModel.userInfo.value?.let {
                builder.addHeader("Authorization", "Bearer "+it.token).build()
            }
        }

        if(url.contains("userlogout")){
            MMKVUtil.getJwtToken()?.let {
                builder.addHeader("Jwt-Token", it).build()
            }
        }

        //builder.addHeader("deviceID", UUID.randomUUID().toString()).build()
      //  builder.addHeader("isLogin", CacheUtil.isLogin().toString()).build()
        return chain.proceed(builder.build())
    }

}