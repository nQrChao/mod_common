package com.chaoji.other.hjq.toast.config;


import com.chaoji.other.hjq.toast.ToastParams;

public interface IToastInterceptor {

    /**
     * 根据显示的文本决定是否拦截该 Toast
     */
    boolean intercept(ToastParams params);
}