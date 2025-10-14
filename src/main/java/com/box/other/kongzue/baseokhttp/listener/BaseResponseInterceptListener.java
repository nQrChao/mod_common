package com.box.other.kongzue.baseokhttp.listener;

import android.content.Context;

public interface BaseResponseInterceptListener {

    boolean response(Context context, String url, String response, Exception error);
}
