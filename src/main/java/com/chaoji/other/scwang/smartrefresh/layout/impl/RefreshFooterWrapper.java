package com.chaoji.other.scwang.smartrefresh.layout.impl;

import android.annotation.SuppressLint;
import android.view.View;

import com.chaoji.other.scwang.smartrefresh.layout.api.RefreshFooter;
import com.chaoji.other.scwang.smartrefresh.layout.internal.InternalAbstract;

/**
 * 刷新底部包装
 */
@SuppressLint("ViewConstructor")
public class RefreshFooterWrapper extends InternalAbstract implements RefreshFooter {

    public RefreshFooterWrapper(View wrapper) {
        super(wrapper);
    }

}
