package com.box.other.scwang.smartrefresh.layout.footer;

import android.content.Context;
import android.util.AttributeSet;

import com.box.other.scwang.smart.refresh.layout.api.RefreshFooter;

/**
 * 虚假的 Footer
 * 用于 正真的 Footer 在 RefreshLayout 外部时，
 */
@SuppressWarnings("unused")
public class FalsifyFooter extends com.box.other.scwang.smart.refresh.header.FalsifyFooter implements RefreshFooter {

    //<editor-fold desc="FalsifyHeader">
    public FalsifyFooter(Context context) {
        this(context, null);
    }

    public FalsifyFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //</editor-fold>

}
