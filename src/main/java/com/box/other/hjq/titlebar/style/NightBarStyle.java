package com.box.other.hjq.titlebar.style;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.box.other.hjq.titlebar.SelectorDrawable;
import com.box.other.hjq.titlebar.TitleBarSupport;
import com.box.com.R;

public class NightBarStyle extends CommonBarStyle {

    @Override
    public Drawable getBackButtonDrawable(Context context) {
        return TitleBarSupport.getDrawable(context, R.drawable.bar_arrows_left_white);
    }

    @Override
    public Drawable getLeftTitleBackground(Context context) {
        return new SelectorDrawable.Builder()
                .setDefault(new ColorDrawable(0x00000000))
                .setFocused(new ColorDrawable(0x66FFFFFF))
                .setPressed(new ColorDrawable(0x66FFFFFF))
                .build();
    }

    @Override
    public Drawable getRightTitleBackground(Context context) {
        return new SelectorDrawable.Builder()
                .setDefault(new ColorDrawable(0x00000000))
                .setFocused(new ColorDrawable(0x66FFFFFF))
                .setPressed(new ColorDrawable(0x66FFFFFF))
                .build();
    }

    @Override
    public Drawable getTitleBarBackground(Context context) {
        return new ColorDrawable(0xFF000000);
    }

    @Override
    public ColorStateList getTitleColor(Context context) {
        return ColorStateList.valueOf(0xEEFFFFFF);
    }

    @Override
    public ColorStateList getLeftTitleColor(Context context) {
        return ColorStateList.valueOf(0xCCFFFFFF);
    }

    @Override
    public ColorStateList getRightTitleColor(Context context) {
        return ColorStateList.valueOf(0xCCFFFFFF);
    }

    @Override
    public Drawable getLineDrawable(Context context) {
        return new ColorDrawable(0xFFFFFFFF);
    }
}