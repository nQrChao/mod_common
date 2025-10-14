package com.box.other.hjq.titlebar.style;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.box.other.hjq.titlebar.ITitleBarStyle;
import com.box.other.hjq.titlebar.TitleBarSupport;

public abstract class CommonBarStyle implements ITitleBarStyle {

    @Override
    public TextView createTitleView(Context context) {
        TextView titleView = newTitleView(context);
        titleView.setGravity(Gravity.CENTER_VERTICAL);
        titleView.setFocusable(true);
        titleView.setSingleLine();
        return titleView;
    }

    public TextView newTitleView(Context context) {
        return new TextView(context);
    }

    @Override
    public TextView createLeftView(Context context) {
        TextView leftView = newLeftView(context);
        leftView.setGravity(Gravity.CENTER_VERTICAL);
        leftView.setFocusable(true);
        leftView.setSingleLine();
        return leftView;
    }

    public TextView newLeftView(Context context) {
        return new TextView(context);
    }

    @Override
    public TextView createRightView(Context context) {
        TextView rightView = newRightView(context);
        rightView.setGravity(Gravity.CENTER_VERTICAL);
        rightView.setFocusable(true);
        rightView.setSingleLine();
        return rightView;
    }

    public TextView newRightView(Context context) {
        return new TextView(context);
    }

    @Override
    public View createLineView(Context context) {
        return new View(context);
    }

    @Override
    public Drawable getLeftTitleForeground(Context context) {
        return null;
    }

    @Override
    public Drawable getRightTitleForeground(Context context) {
        return null;
    }

    @Override
    public int getLeftHorizontalPadding(Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getTitleHorizontalPadding(Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getRightHorizontalPadding(Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getChildVerticalPadding(Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources().getDisplayMetrics());
    }

    @Override
    public CharSequence getTitle(Context context) {
        if (!(context instanceof Activity)) {
            return "";
        }

        CharSequence label = ((Activity) context).getTitle();
        if (TextUtils.isEmpty(label)) {
            return "";
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (!label.toString().equals(packageInfo.applicationInfo.loadLabel(packageManager).toString())) {
                return label;
            }
        } catch (PackageManager.NameNotFoundException ignored) {}

        return "";
    }

    @Override
    public CharSequence getLeftTitle(Context context) {
        return "";
    }

    @Override
    public CharSequence getRightTitle(Context context) {
        return "";
    }

    @Override
    public float getTitleSize(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics());
    }

    @Override
    public float getLeftTitleSize(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics());
    }

    @Override
    public float getRightTitleSize(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics());
    }

    @Override
    public Typeface getTitleTypeface(Context context, int style) {
        return TitleBarSupport.getTextTypeface(style);
    }

    @Override
    public Typeface getLeftTitleTypeface(Context context, int style) {
        return TitleBarSupport.getTextTypeface(style);
    }

    @Override
    public Typeface getRightTitleTypeface(Context context, int style) {
        return TitleBarSupport.getTextTypeface(style);
    }

    @Override
    public int getTitleStyle(Context context) {
        return Typeface.NORMAL;
    }

    @Override
    public int getLeftTitleStyle(Context context) {
        return Typeface.NORMAL;
    }

    @Override
    public int getRightTitleStyle(Context context) {
        return Typeface.NORMAL;
    }

    @Override
    public int getTitleIconGravity(Context context) {
        return Gravity.END;
    }

    @Override
    public int getLeftIconGravity(Context context) {
        return Gravity.START;
    }

    @Override
    public int getRightIconGravity(Context context) {
        return Gravity.END;
    }

    @Override
    public int getTitleIconPadding(Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getLeftIconPadding(Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getRightIconPadding(Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getTitleIconWidth(Context context) {
        return 0;
    }

    @Override
    public int getLeftIconWidth(Context context) {
        return 0;
    }

    @Override
    public int getRightIconWidth(Context context) {
        return 0;
    }

    @Override
    public int getTitleIconHeight(Context context) {
        return 0;
    }

    @Override
    public int getLeftIconHeight(Context context) {
        return 0;
    }

    @Override
    public int getRightIconHeight(Context context) {
        return 0;
    }

    @Override
    public TextUtils.TruncateAt getTitleOverflowMode(Context context) {
        return TextUtils.TruncateAt.MARQUEE;
    }

    @Override
    public TextUtils.TruncateAt getLeftTitleOverflowMode(Context context) {
        return null;
    }

    @Override
    public TextUtils.TruncateAt getRightTitleOverflowMode(Context context) {
        return null;
    }

    @Override
    public boolean isLineVisible(Context context) {
        return true;
    }

    @Override
    public int getLineSize(Context context) {
        return 1;
    }
}