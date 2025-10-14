package com.box.other.hjq.toast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;


final class ToastImpl {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private final CustomToast mToast;

    private WindowLifecycle mWindowLifecycle;

    private final String mPackageName;

    private boolean mShow;

    private boolean mGlobalShow;

    ToastImpl(Activity activity, CustomToast toast) {
        this((Context) activity, toast);
        mGlobalShow = false;
        mWindowLifecycle = new WindowLifecycle((activity));
    }

    ToastImpl(Application application, CustomToast toast) {
        this((Context) application, toast);
        mGlobalShow = true;
        mWindowLifecycle = new WindowLifecycle(application);
    }

    private ToastImpl(Context context, CustomToast toast) {
        mToast = toast;
        mPackageName = context.getPackageName();
    }

    boolean isShow() {
        return mShow;
    }

    void setShow(boolean show) {
        mShow = show;
    }


    void show() {
        if (isShow()) {
            return;
        }
        if (isMainThread()) {
            mShowRunnable.run();
        } else {
            HANDLER.removeCallbacks(mShowRunnable);
            HANDLER.post(mShowRunnable);
        }
    }

    void cancel() {
        if (!isShow()) {
            return;
        }
        HANDLER.removeCallbacks(mShowRunnable);
        if (isMainThread()) {
            mCancelRunnable.run();
        } else {
            HANDLER.removeCallbacks(mCancelRunnable);
            HANDLER.post(mCancelRunnable);
        }
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private void trySendAccessibilityEvent(View view) {
        final Context context = view.getContext();
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            return;
        }
        int eventType = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        AccessibilityEvent event;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            event = new AccessibilityEvent();
            event.setEventType(eventType);
        } else {
            event = AccessibilityEvent.obtain(eventType);
        }
        event.setClassName(Toast.class.getName());
        event.setPackageName(context.getPackageName());
        view.dispatchPopulateAccessibilityEvent(event);
        accessibilityManager.sendAccessibilityEvent(event);
    }

    private final Runnable mShowRunnable = new Runnable() {

        @SuppressLint("WrongConstant")
        @Override
        public void run() {
            
            WindowManager windowManager = mWindowLifecycle.getWindowManager();
            if (windowManager == null) {
                return;
            }

            final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            params.packageName = mPackageName;
            params.gravity = mToast.getGravity();
            params.x = mToast.getXOffset();
            params.y = mToast.getYOffset();
            params.verticalMargin = mToast.getVerticalMargin();
            params.horizontalMargin = mToast.getHorizontalMargin();
            params.windowAnimations = mToast.getAnimationsId();

            if (mGlobalShow) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    params.flags &= ~WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
                } else {
                    params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                }
            }

            try {
                windowManager.addView(mToast.getView(), params);
                HANDLER.postDelayed(() -> cancel(), mToast.getDuration() == Toast.LENGTH_LONG ?
                        mToast.getLongDuration() : mToast.getShortDuration());
                mWindowLifecycle.register(ToastImpl.this);
                setShow(true);
                trySendAccessibilityEvent(mToast.getView());
            } catch (IllegalStateException | WindowManager.BadTokenException e) {
                e.printStackTrace();
            }
        }
    };

    private final Runnable mCancelRunnable = new Runnable() {

        @Override
        public void run() {

            try {
                WindowManager windowManager = mWindowLifecycle.getWindowManager();
                if (windowManager == null) {
                    return;
                }

                windowManager.removeViewImmediate(mToast.getView());

            } catch (IllegalArgumentException e) {
                // 如果当前 WindowManager 没有附加这个 View 则会抛出异常
                // java.lang.IllegalArgumentException: View=android.widget.TextView not attached to window manager
                e.printStackTrace();
            } finally {
                // 反注册生命周期管控
                mWindowLifecycle.unregister();
                // 当前没有显示
                setShow(false);
            }
        }
    };
}