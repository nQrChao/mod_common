package com.chaoji.other.hjq.toast;

import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

@SuppressWarnings("deprecation")
final class SafeHandler extends Handler {

    private final Handler mHandler;

    SafeHandler(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void handleMessage(final Message msg) {
        try {
            mHandler.handleMessage(msg);
        } catch (WindowManager.BadTokenException | IllegalStateException e) {
            // android.view.WindowManager$BadTokenException：Unable to add window -- token android.os.BinderProxy is not valid; is your activity running?
            // java.lang.IllegalStateException：View android.widget.TextView has already been added to the window manager.
            e.printStackTrace();
        }
    }
}