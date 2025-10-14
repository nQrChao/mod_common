package com.box.common.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit

object PollingUrl {
    private var BASE_URL = ApiService.HTTP_RELEASE_URLS[0] + "/"

    private val okHttpClient = OkHttpClient.Builder().build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}