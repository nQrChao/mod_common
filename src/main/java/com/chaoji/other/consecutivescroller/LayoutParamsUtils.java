package com.chaoji.other.consecutivescroller;

import android.view.ViewGroup;

public class LayoutParamsUtils {

    /**
     * 使子view的topMargin和bottomMargin属性无效
     */
    public static void invalidTopAndBottomMargin(ViewGroup.MarginLayoutParams params){
        if (params != null) {
            params.topMargin = 0;
            params.bottomMargin = 0;
        }
    }
}
