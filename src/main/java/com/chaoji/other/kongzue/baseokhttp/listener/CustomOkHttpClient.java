package com.chaoji.other.kongzue.baseokhttp.listener;

import okhttp3.OkHttpClient;

public interface CustomOkHttpClient {
    
    OkHttpClient customBuilder(OkHttpClient builder);
}
