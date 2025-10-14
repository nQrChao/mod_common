package com.box.other.kongzue.baseokhttp.listener;

import okhttp3.MultipartBody;

public interface MultipartBuilderInterceptor {
    
    MultipartBody.Builder interceptMultipartBuilder(MultipartBody.Builder multipartBuilder);
}
