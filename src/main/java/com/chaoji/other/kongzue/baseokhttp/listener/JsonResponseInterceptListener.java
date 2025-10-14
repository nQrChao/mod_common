package com.chaoji.other.kongzue.baseokhttp.listener;

import android.content.Context;

import com.chaoji.other.kongzue.baseokhttp.listener.BaseResponseInterceptListener;
import com.chaoji.other.kongzue.baseokhttp.util.JsonMap;

public abstract class JsonResponseInterceptListener implements BaseResponseInterceptListener {
    
    @Override
    public boolean response(Context context, String url, String response, Exception error) {
        return onResponse(context, url, new JsonMap(response), error);
    }
    
    public abstract boolean onResponse(Context context, String url, JsonMap response, Exception error);
}
