package com.box.other.scwang.smartrefresh.layout.api;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * 默认全局初始化器
 */
public interface DefaultRefreshInitializer {
    void initialize(@NonNull Context context, @NonNull RefreshLayout layout);
}
