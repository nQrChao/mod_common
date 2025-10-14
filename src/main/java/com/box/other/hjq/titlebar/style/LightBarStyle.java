package com.box.other.hjq.titlebar.style;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.box.common.R;
import com.box.other.hjq.titlebar.SelectorDrawable;
import com.box.other.hjq.titlebar.TitleBarSupport;

public class LightBarStyle extends CommonBarStyle {

    @Override
    public Drawable getTitleBarBackground(Context context) {
        return new ColorDrawable(0xFFFFFFFF);
    }

    @Override
    public Drawable getLeftTitleBackground(Context context) {
        return new SelectorDrawable.Builder()
                .setDefault(new ColorDrawable(0x00000000))
                .setFocused(new ColorDrawable(0x0C000000))
                .setPressed(new ColorDrawable(0x0C000000))
                .build();
    }

    @Override
    public Drawable getRightTitleBackground(Context context) {
        return new SelectorDrawable.Builder()
                .setDefault(new ColorDrawable(0x00000000))
                .setFocused(new ColorDrawable(0x0C000000))
                .setPressed(new ColorDrawable(0x0C000000))
                .build();
    }

    @Override
    public Drawable getBackButtonDrawable(Context context) {
        return TitleBarSupport.getDrawable(context, R.drawable.arrows_left_b_ic);
    }

    @Override
    public ColorStateList getTitleColor(Context context) {
        return ColorStateList.valueOf(0xFF222222);
    }

    @Override
    public ColorStateList getLeftTitleColor(Context context) {
        return ColorStateList.valueOf(0xFF666666);
    }

    @Override
    public ColorStateList getRightTitleColor(Context context) {
        return ColorStateList.valueOf(0xFFA4A4A4);
    }

    @Override
    public Drawable getLineDrawable(Context context) {
        return new ColorDrawable(0xFFECECEC);
    }
}