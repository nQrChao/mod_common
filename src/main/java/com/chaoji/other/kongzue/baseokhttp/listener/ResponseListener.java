package com.chaoji.other.kongzue.baseokhttp.listener;

import com.chaoji.other.kongzue.baseokhttp.listener.BaseResponseListener;

public abstract class ResponseListener implements BaseResponseListener {
    
    @Override
    public void response(Object response, Exception error) {
        onResponse(response == null ? "" : response.toString(), error);
    }
    
    public abstract void onResponse(String main, Exception error);
}
