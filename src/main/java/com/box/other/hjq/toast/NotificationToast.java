package com.box.other.hjq.toast;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class NotificationToast extends SystemToast {

    private static boolean sHookService;

    public NotificationToast(Application application) {
        super(application);
    }

    @Override
    public void show() {
        hookNotificationService();
        super.show();
    }

    @SuppressLint({"DiscouragedPrivateApi", "PrivateApi"})
    @SuppressWarnings({"JavaReflectionMemberAccess", "SoonBlockedPrivateApi"})
    private static void hookNotificationService() {
        if (sHookService) {
            return;
        }
        sHookService = true;
        try {
            Method getService = Toast.class.getDeclaredMethod("getService");
            getService.setAccessible(true);
            final Object iNotificationManager = getService.invoke(null);
            if (iNotificationManager == null) {
                return;
            }

            if (Proxy.isProxyClass(iNotificationManager.getClass()) &&
                    Proxy.getInvocationHandler(iNotificationManager) instanceof NotificationServiceProxy) {
                return;
            }
            Object iNotificationManagerProxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{Class.forName("android.app.INotificationManager")},
                    new NotificationServiceProxy(iNotificationManager));
            Field sService = Toast.class.getDeclaredField("sService");
            sService.setAccessible(true);
            sService.set(null, iNotificationManagerProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}