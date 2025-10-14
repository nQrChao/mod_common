package com.chaoji.other.scwang.smart.refresh.layout.listener;


import androidx.annotation.NonNull;

import com.chaoji.other.scwang.smart.refresh.layout.api.RefreshLayout;

/**
 * 刷新监听器
 */
public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
