package com.box.other.hjq.toast;

import android.app.Activity;

public class ActivityToast extends CustomToast {

    /** Toast 实现类 */
    private final ToastImpl mToastImpl;

    public ActivityToast(Activity activity) {
        mToastImpl = new ToastImpl(activity, this);
    }

    @Override
    public void show() {
        // 替换成 WindowManager 来显示
        mToastImpl.show();
    }

    @Override
    public void cancel() {
        // 取消 WindowManager 的显示
        mToastImpl.cancel();
    }
}