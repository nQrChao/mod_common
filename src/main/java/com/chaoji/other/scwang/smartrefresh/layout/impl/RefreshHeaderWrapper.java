package com.chaoji.other.scwang.smartrefresh.layout.impl;

import android.annotation.SuppressLint;
import android.view.View;

import com.chaoji.other.scwang.smartrefresh.layout.api.RefreshHeader;
import com.chaoji.other.scwang.smartrefresh.layout.internal.InternalAbstract;

/**
 * 刷新头部包装
 */
@SuppressLint("ViewConstructor")
public class RefreshHeaderWrapper extends InternalAbstract implements RefreshHeader {

    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }

}
