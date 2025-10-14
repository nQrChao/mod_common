package com.chaoji.other.kongzue.baseokhttp.listener;

import android.content.Context;

import com.chaoji.other.kongzue.baseokhttp.listener.BaseResponseInterceptListener;

public abstract class ResponseInterceptListener implements BaseResponseInterceptListener {
    
    @Override
    public boolean response(Context context, String url, String response, Exception error) {
        return onResponse(context, url, response, error);
    }
    
    public abstract boolean onResponse(Context context, String url, String response, Exception error);
}
