package com.box.other.kongzue.baseokhttp.listener;

import okhttp3.OkHttpClient;

public interface CustomOkHttpClient {
    
    OkHttpClient customBuilder(OkHttpClient builder);
}
