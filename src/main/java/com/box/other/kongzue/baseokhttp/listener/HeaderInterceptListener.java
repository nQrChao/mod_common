package com.box.other.kongzue.baseokhttp.listener;

import android.content.Context;

import com.box.other.kongzue.baseokhttp.util.Parameter;

public interface HeaderInterceptListener {
    
    Parameter onIntercept(Context context, String url, Parameter header);
    
}
