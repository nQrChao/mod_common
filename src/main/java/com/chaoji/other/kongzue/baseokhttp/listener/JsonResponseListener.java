package com.chaoji.other.kongzue.baseokhttp.listener;

import com.chaoji.other.kongzue.baseokhttp.exceptions.DecodeJsonException;
import com.chaoji.other.kongzue.baseokhttp.listener.BaseResponseListener;
import com.chaoji.other.kongzue.baseokhttp.util.JsonMap;

public abstract class JsonResponseListener implements BaseResponseListener {
    
    @Override
    public void response(Object response, Exception error) {
        if (error == null) {
            JsonMap data = new JsonMap(response.toString());
            if (data!=null && !data.isEmpty()) {
                onResponse(data, error);
            } else {
                onResponse(new JsonMap(), new DecodeJsonException(response.toString()));
            }
        } else {
            onResponse(new JsonMap(), error);
        }
    }
    
    public abstract void onResponse(JsonMap main, Exception error);
}
