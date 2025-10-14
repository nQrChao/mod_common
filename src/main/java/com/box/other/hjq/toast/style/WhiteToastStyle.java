package com.box.other.hjq.toast.style;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;

public class WhiteToastStyle extends BlackToastStyle {

    @Override
    protected int getTextColor(Context context) {
        return 0XBB000000;
    }

    @Override
    protected Drawable getBackgroundDrawable(Context context) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(0XFFEAEAEA);
        drawable.setCornerRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            10, context.getResources().getDisplayMetrics()));
        return drawable;
    }
}