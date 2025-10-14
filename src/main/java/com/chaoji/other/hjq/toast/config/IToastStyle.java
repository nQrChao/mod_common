package com.chaoji.other.hjq.toast.config;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

public interface IToastStyle<V extends View> {

    V createView(Context context);

    default int getGravity() {
        return Gravity.CENTER;
    }

    default int getXOffset() {
        return 0;
    }

    default int getYOffset() {
        return 0;
    }

    default float getHorizontalMargin() {
        return 0;
    }

    default float getVerticalMargin() {
        return 0;
    }
}