package com.box.other.hjq.titlebar.style;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.box.other.hjq.titlebar.SelectorDrawable;
import com.box.other.hjq.titlebar.TitleBarSupport;
import com.box.common.R;


public class TransparentBarStyle extends CommonBarStyle {

    @Override
    public Drawable getBackButtonDrawable(Context context) {
        return TitleBarSupport.getDrawable(context, R.drawable.bar_arrows_left_white);
    }

    @Override
    public Drawable getLeftTitleBackground(Context context) {
        return new SelectorDrawable.Builder()
                .setDefault(new ColorDrawable(0x00000000))
                .setFocused(new ColorDrawable(0x22000000))
                .setPressed(new ColorDrawable(0x22000000))
                .build();
    }

    @Override
    public Drawable getRightTitleBackground(Context context) {
        return new SelectorDrawable.Builder()
                .setDefault(new ColorDrawable(0x00000000))
                .setFocused(new ColorDrawable(0x22000000))
                .setPressed(new ColorDrawable(0x22000000))
                .build();
    }

    @Override
    public Drawable getTitleBarBackground(Context context) {
        return new ColorDrawable(0x00000000);
    }

    @Override
    public ColorStateList getTitleColor(Context context) {
        return ColorStateList.valueOf(0xFFFFFFFF);
    }

    @Override
    public ColorStateList getLeftTitleColor(Context context) {
        return ColorStateList.valueOf(0xFFFFFFFF);
    }

    @Override
    public ColorStateList getRightTitleColor(Context context) {
        return ColorStateList.valueOf(0xFFFFFFFF);
    }

    @Override
    public Drawable getLineDrawable(Context context) {
        return new ColorDrawable(0x00000000);
    }
}