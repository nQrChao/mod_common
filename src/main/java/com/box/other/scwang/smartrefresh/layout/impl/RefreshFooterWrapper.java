package com.box.other.scwang.smartrefresh.layout.impl;

import android.annotation.SuppressLint;
import android.view.View;

import com.box.other.scwang.smartrefresh.layout.api.RefreshFooter;
import com.box.other.scwang.smartrefresh.layout.internal.InternalAbstract;

/**
 * 刷新底部包装
 */
@SuppressLint("ViewConstructor")
public class RefreshFooterWrapper extends InternalAbstract implements RefreshFooter {

    public RefreshFooterWrapper(View wrapper) {
        super(wrapper);
    }

}
