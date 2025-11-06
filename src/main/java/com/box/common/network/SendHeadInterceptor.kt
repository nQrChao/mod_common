package com.box.common.network

import com.box.common.appViewModel
import com.box.common.utils.mmkv.MMKVConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class SendHeadInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        //builder.addHeader("operationID", System.currentTimeMillis().toString() + "").build()
        val url= chain.request().url.encodedPath
        if (chain.request().url.toString().startsWith(ApiService.D_API_URL)){
            if (url.startsWith("/api")){
                appViewModel.modUserInfo.value?.let {
                    builder.addHeader("token", it.token).build()
                }
            }
        }else{
            appViewModel.modUserInfo.value?.let {
                builder.addHeader("Authorization", "Bearer "+it.token).build()
            }
        }

        if(url.contains("userlogout")){
            MMKVConfig.userToken.let {
                builder.addHeader("Jwt-Token", it).build()
            }
        }
        //builder.addHeader("deviceID", UUID.randomUUID().toString()).build()
      //  builder.addHeader("isLogin", CacheUtil.isLogin().toString()).build()
        return chain.proceed(builder.build())
    }

}