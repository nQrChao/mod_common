package com.chaoji.other.kongzue.baseokhttp.listener;

import com.chaoji.other.kongzue.baseokhttp.HttpRequest;

import okhttp3.OkHttpClient;

public interface GlobalCustomOkHttpClientBuilder {
    
    OkHttpClient.Builder customBuilder(HttpRequest request, OkHttpClient.Builder builder);
}
