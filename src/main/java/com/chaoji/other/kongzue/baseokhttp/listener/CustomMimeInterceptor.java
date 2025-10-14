package com.chaoji.other.kongzue.baseokhttp.listener;

import com.chaoji.other.kongzue.baseokhttp.util.RequestInfo;

import java.io.File;

import okhttp3.Call;

public abstract class CustomMimeInterceptor {
    
    public String onUploadFileMimeInterceptor(File originFile) {
        return "";
    }
    
    public String onRequestMimeInterceptor(RequestInfo requestInfo,Call call){
        return "";
    }
}
