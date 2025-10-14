package com.chaoji.im.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;


import com.chaoji.other.blankj.utilcode.util.ScreenUtils;
import com.chaoji.common.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {
    /**
     * 主线程handler
     */
    public final static Handler UIHandler = new Handler(Looper.getMainLooper());

    //目标项是否在最后一个可见项之后
    public static  boolean mShouldScroll;
    //记录目标项位置
    public  static int mToPosition;
    /**
     * 滑动到指定位置
     */
    public static  void smoothMoveToPosition(RecyclerView mRecyclerView,  int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem =
            mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }



    /**
     *  finish routes
     * @param routes
     */
    public static  void finishRoute(String... routes) {
//        for (String route : routes) {
//            Postcard postcard = ARouter.getInstance().build(route);
//            LogisticsCenter.completion(postcard);
//            ActivityManager.finishActivity(postcard.getDestination());
//        }
    }
    public static void stringBindForegroundColorSpan(TextView textView, String data,
                                                     String target) {
        stringBindForegroundColorSpan(textView, data, target, R.color.colorPrimary);
    }

    /**
     * 设置带背景的目标文字
     *
     * @param textView
     * @param data     数据
     * @param target   目标文字
     */
    public static void stringBindForegroundColorSpan(TextView textView, String data,
                                                     String target, int bgColor) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(data);
        String searchContent = target.toLowerCase(Locale.ROOT);
        data = data.toLowerCase(Locale.ROOT);
        int start = data.indexOf(searchContent);
        if (start == -1) {
            textView.setText(spannableString);
            return;
        }
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(bgColor);
        spannableString.setSpan(colorSpan, start, start + searchContent.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception ignored) {}
        return versionName;
    }

    public static String md5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static int dp2px(float dp) {
        float scale = ScreenUtils.getScreenDensity();
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(float px) {
        float scale =  ScreenUtils.getScreenDensity();
        return (int) (px / scale + 0.5f);
    }

    //收起键盘
    public static void hideKeyboard(Context context, View v) {
        InputMethodManager imm =
            (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    //弹出键盘
    public static void pushKeyboard(Context context) {
        InputMethodManager inputMethodManager =
            (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //软键盘是否弹出
    public static boolean isShowKeyboard(Context context) {
        InputMethodManager imm =
            (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //获取状态信息
        return imm.isActive();//true 打开
    }

    /**
     * 判断是否是字母
     *
     * @param str 传入字符串
     * @return 是字母返回true，否则返回false
     */
    public static boolean isAlpha(String str) {
        if (TextUtils.isEmpty(str)) return false;
        return str.matches("[a-zA-Z]+");
    }

    /**
     * 设置全屏
     *
     * @param activity
     */
    public static void setFullScreen(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }


//
//    public static void permission(Context context, OnGrantedListener onGrantedListener,
//                                  boolean hasPermission, String... permissions) {
//        if (hasPermission)
//            onGrantedListener.onGranted();
//        else {
//            XXPermissions.with(context)
//                .permission(permissions)
//                .request((permissions1, allGranted) -> {
//                    if (allGranted){
//                        onGrantedListener.onGranted();
//                    }
//                });
//        }
//    }
//
//    public interface OnGrantedListener {
//        void onGranted();
//    }

    /***
     * 判断字符串是否为全部空格
     * @param sc
     * @return
     */
    public static Boolean isBlank(CharSequence sc) {
        if (sc != null && sc.length() > 0) {
            for (int i = 0; i < sc.length(); i++) {
                if (!Character.isWhitespace(sc.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }


    public static String containsLink(String text) {
        StringBuilder links=new StringBuilder();
        // 正则表达式模式匹配URL链接
        String pattern = "(http|https)://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(/\\S*)?";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(text);
        while (matcher.find()) {
            links.append(matcher.group());
        }
        return links.toString();
    }

    /**
     * (x,y)是否在view的区域内
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    public static boolean isTouchPointInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left && x <= right) {
            return true;
        }
        return false;
    }


}

