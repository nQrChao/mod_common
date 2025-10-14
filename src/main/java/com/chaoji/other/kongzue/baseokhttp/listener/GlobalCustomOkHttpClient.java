package com.chaoji.other.kongzue.baseokhttp.listener;

import com.chaoji.other.kongzue.baseokhttp.HttpRequest;

import okhttp3.OkHttpClient;

public interface GlobalCustomOkHttpClient {
    
    OkHttpClient customBuilder(HttpRequest request, OkHttpClient builder);
}
