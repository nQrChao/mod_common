package com.chaoji.other.kongzue.baseokhttp.listener;

import okhttp3.OkHttpClient;

public interface CustomOkHttpClientBuilder {
    
    OkHttpClient.Builder customBuilder(OkHttpClient.Builder builder);
}
