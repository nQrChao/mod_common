package com.chaoji.other.scwang.smartrefresh.layout.listener;




import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.chaoji.other.scwang.smartrefresh.layout.api.RefreshLayout;
import com.chaoji.other.scwang.smartrefresh.layout.constant.RefreshState;

/**
 * 刷新状态改变监听器
 * Created by scwang on 2017/5/26.
 */
public interface OnStateChangedListener {
    /**
     * 【仅限框架内调用】状态改变事件 {@link RefreshState}
     * @param refreshLayout RefreshLayout
     * @param oldState 改变之前的状态
     * @param newState 改变之后的状态
     */
    @RestrictTo({RestrictTo.Scope.LIBRARY,RestrictTo.Scope.LIBRARY_GROUP,RestrictTo.Scope.SUBCLASSES})
    void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState);
}
