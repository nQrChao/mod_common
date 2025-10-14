package com.chaoji.other.hjq.toast;

import android.util.Log;


import com.chaoji.other.hjq.toast.config.IToastInterceptor;

import java.lang.reflect.Modifier;

public class ToastLogInterceptor implements IToastInterceptor {

    @Override
    public boolean intercept(ToastParams params) {
        printToast(params.text);
        return false;
    }

    protected void printToast(CharSequence text) {
        if (!isLogEnable()) {
            return;
        }

        StackTraceElement[] stackTraces = new Throwable().getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            // 获取代码行数
            int lineNumber = stackTrace.getLineNumber();
            if (lineNumber <= 0) {
                continue;
            }

            String className = stackTrace.getClassName();
            try {
                Class<?> clazz = Class.forName(className);
                if (!filterClass(clazz)) {
                    printLog("(" + stackTrace.getFileName() + ":" + lineNumber + ") " + text.toString());
                    // 跳出循环
                    break;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean isLogEnable() {
        return Toaster.isDebugMode();
    }

    protected void printLog(String msg) {
        Log.i("Toaster", msg);
    }

    protected boolean filterClass(Class<?> clazz) {
        return IToastInterceptor.class.isAssignableFrom(clazz) ||
                Toaster.class.equals(clazz) ||
                clazz.isInterface() ||
                Modifier.isAbstract(clazz.getModifiers());
    }
}