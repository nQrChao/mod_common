package com.box.other.hjq.toast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.widget.Toast;

import com.box.other.hjq.toast.config.IToast;
import com.box.other.hjq.toast.config.IToastStrategy;
import com.box.other.hjq.toast.config.IToastStyle;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ToastStrategy implements IToastStrategy {

    public static final int SHOW_STRATEGY_TYPE_IMMEDIATELY = 0;

    public static final int SHOW_STRATEGY_TYPE_QUEUE = 1;

    /** Handler 对象 */
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private static final int DEFAULT_DELAY_TIMEOUT = 200;

    private Application mApplication;

    private WeakReference<IToast> mToastReference;

    private final int mShowStrategyType;

    private final Object mShowMessageToken = new Object();
    private final Object mCancelMessageToken = new Object();
    private volatile long mLastShowToastMillis;

    public ToastStrategy() {
        this(ToastStrategy.SHOW_STRATEGY_TYPE_IMMEDIATELY);
    }

    @Override
    public void registerStrategy(Application application) {
        mApplication = application;
    }

    public ToastStrategy(int type) {
        mShowStrategyType = type;
        switch (mShowStrategyType) {
            case SHOW_STRATEGY_TYPE_IMMEDIATELY:
            case SHOW_STRATEGY_TYPE_QUEUE:
                break;
            default:
                throw new IllegalArgumentException("Please don't pass non-existent toast show strategy");
        }
    }

    @Override
    public IToast createToast(ToastParams params) {
        Activity foregroundActivity = getForegroundActivity();
        IToast toast;
        if (VERSION.SDK_INT >= VERSION_CODES.M &&
                Settings.canDrawOverlays(mApplication)) {
            toast = new GlobalToast(mApplication);
        } else if (!params.crossPageShow && isActivityAvailable(foregroundActivity)) {
            toast = new ActivityToast(foregroundActivity);
        } else if (VERSION.SDK_INT == VERSION_CODES.N_MR1) {
            toast = new SafeToast(mApplication);
        } else if (VERSION.SDK_INT < VERSION_CODES.Q &&
                !areNotificationsEnabled(mApplication)) {
            toast = new NotificationToast(mApplication);
        } else {
            toast = new SystemToast(mApplication);
        }
        if (isSupportToastStyle(toast) || !onlyShowSystemToastStyle()) {
            diyToastStyle(toast, params.style);
        }
        return toast;
    }

    @Override
    public void showToast(ToastParams params) {
        switch (mShowStrategyType) {
            case SHOW_STRATEGY_TYPE_IMMEDIATELY: {
                HANDLER.removeCallbacksAndMessages(mShowMessageToken);
                long uptimeMillis = SystemClock.uptimeMillis() + params.delayMillis + (params.crossPageShow ? 0 : DEFAULT_DELAY_TIMEOUT);
                HANDLER.postAtTime(new ShowToastRunnable(params), mShowMessageToken, uptimeMillis);
                break;
            }
            case SHOW_STRATEGY_TYPE_QUEUE: {
                long showToastMillis = SystemClock.uptimeMillis() + params.delayMillis + (params.crossPageShow ? 0 : DEFAULT_DELAY_TIMEOUT);
                long waitMillis = generateToastWaitMillis(params);
                if (showToastMillis < (mLastShowToastMillis + waitMillis)) {
                    showToastMillis = mLastShowToastMillis + waitMillis;
                }
                HANDLER.postAtTime(new ShowToastRunnable(params), mShowMessageToken, showToastMillis);
                mLastShowToastMillis = showToastMillis;
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void cancelToast() {
        HANDLER.removeCallbacksAndMessages(mCancelMessageToken);
        long uptimeMillis = SystemClock.uptimeMillis();
        HANDLER.postAtTime(new CancelToastRunnable(), mCancelMessageToken, uptimeMillis);
    }

    protected boolean isSupportToastStyle(IToast toast) {
        return toast instanceof CustomToast || VERSION.SDK_INT < VERSION_CODES.R ||
                mApplication.getApplicationInfo().targetSdkVersion < VERSION_CODES.R;
    }

    protected void diyToastStyle(IToast toast, IToastStyle<?> style) {
        toast.setView(style.createView(mApplication));
        toast.setGravity(style.getGravity(), style.getXOffset(), style.getYOffset());
        toast.setMargin(style.getHorizontalMargin(), style.getVerticalMargin());
    }

    protected int generateToastWaitMillis(ToastParams params) {
        if (params.duration == Toast.LENGTH_SHORT) {
            return 1000;
        } else if (params.duration == Toast.LENGTH_LONG) {
            return 1500;
        }
        return 0;
    }

    private class ShowToastRunnable implements Runnable {

        private final ToastParams mToastParams;

        private ShowToastRunnable(ToastParams params) {
            mToastParams = params;
        }

        @Override
        public void run() {
            IToast toast = null;
            if (mToastReference != null) {
                toast = mToastReference.get();
            }

            if (toast != null) {
                toast.cancel();
            }
            toast = createToast(mToastParams);
            mToastReference = new WeakReference<>(toast);
            toast.setDuration(mToastParams.duration);
            toast.setText(mToastParams.text);
            toast.show();
        }
    }

    private class CancelToastRunnable implements Runnable {

        @Override
        public void run() {
            IToast toast = null;
            if (mToastReference != null) {
                toast = mToastReference.get();
            }

            if (toast == null) {
                return;
            }
            toast.cancel();
        }
    }

    protected boolean onlyShowSystemToastStyle() {
        return isChangeEnabledCompat(147798919L);
    }

    @SuppressLint("PrivateApi")
    protected boolean isChangeEnabledCompat(long changeId) {
        if (VERSION.SDK_INT < VERSION_CODES.R) {
            return true;
        }
        try {
            Class<?> clazz = Class.forName("android.app.compat.CompatChanges");
            Method method = clazz.getMethod("isChangeEnabled", long.class);
            method.setAccessible(true);
            return Boolean.parseBoolean(String.valueOf(method.invoke(null, changeId)));
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("PrivateApi")
    protected boolean areNotificationsEnabled(Context context) {
        return context.getSystemService(NotificationManager.class).areNotificationsEnabled();

    }

    /**
     * 获取前台的 Activity
     */
    protected Activity getForegroundActivity() {
        return ActivityStack.getInstance().getForegroundActivity();
    }

    /**
     * Activity 是否可用
     */
    protected boolean isActivityAvailable(Activity activity) {
        if (activity == null) {
            return false;
        }

        if (activity.isFinishing()) {
            return false;
        }

        return !activity.isDestroyed();
    }
}