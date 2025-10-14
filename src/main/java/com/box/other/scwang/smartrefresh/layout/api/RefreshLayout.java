package com.box.other.scwang.smartrefresh.layout.api;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.box.other.scwang.smartrefresh.layout.impl.ScrollBoundaryDeciderAdapter;
import com.box.other.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.box.other.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.box.other.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.box.other.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.box.other.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

/**
 * 刷新布局
 * interface of the refresh layout
 * Created by scwang on 2017/5/26.
 */
@SuppressWarnings("UnusedReturnValue")
public interface RefreshLayout extends com.box.other.scwang.smart.refresh.layout.api.RefreshLayout {

    /**
     * Set the footer of RefreshLayout.
     * 设置指定的 Footer
     * @param footer RefreshFooter 刷新尾巴
     * @return RefreshLayout
     */
    RefreshLayout setRefreshFooter(@NonNull RefreshFooter footer);

    /**
     * Set the footer of RefreshLayout.
     * 设置指定的 Footer
     * @param footer RefreshFooter 刷新尾巴
     * @param width the width in px, can use MATCH_PARENT and WRAP_CONTENT.
     *              宽度 可以使用 MATCH_PARENT, WRAP_CONTENT
     * @param height the height in px, can use MATCH_PARENT and WRAP_CONTENT.
     *               高度 可以使用 MATCH_PARENT, WRAP_CONTENT
     * @return RefreshLayout
     */
    RefreshLayout setRefreshFooter(@NonNull RefreshFooter footer, int width, int height);

    /**
     * Set the header of RefreshLayout.
     * 设置指定的 Header
     * @param header RefreshHeader 刷新头
     * @return RefreshLayout
     */
    RefreshLayout setRefreshHeader(@NonNull RefreshHeader header);

    /**
     * Set the header of RefreshLayout.
     * 设置指定的 Header
     * @param header RefreshHeader 刷新头
     * @param width the width in px, can use MATCH_PARENT and WRAP_CONTENT.
     *              宽度 可以使用 MATCH_PARENT, WRAP_CONTENT
     * @param height the height in px, can use MATCH_PARENT and WRAP_CONTENT.
     *               高度 可以使用 MATCH_PARENT, WRAP_CONTENT
     * @return RefreshLayout
     */
    RefreshLayout setRefreshHeader(@NonNull RefreshHeader header, int width, int height);

    /**
     * Set refresh listener separately.
     * 单独设置刷新监听器
     * @param listener OnRefreshListener 刷新监听器
     * @return RefreshLayout
     */
    RefreshLayout setOnRefreshListener(OnRefreshListener listener);

    /**
     * Set load more listener separately.
     * 单独设置加载监听器
     * @param listener OnLoadMoreListener 加载监听器
     * @return RefreshLayout
     */
    RefreshLayout setOnLoadMoreListener(OnLoadMoreListener listener);

    /**
     * Set refresh and load listeners at the same time.
     * 同时设置刷新和加载监听器
     * @param listener OnRefreshLoadMoreListener 刷新加载监听器
     * @return RefreshLayout
     */
    RefreshLayout setOnRefreshLoadMoreListener(OnRefreshLoadMoreListener listener);

    /**
     * Set up a multi-function listener.
     * Recommended {@link SimpleMultiPurposeListener}
     * 设置多功能监听器
     * 建议使用 {@link SimpleMultiPurposeListener}
     * @param listener OnMultiPurposeListener 多功能监听器
     * @return RefreshLayout
     */
    RefreshLayout setOnMultiPurposeListener(OnMultiPurposeListener listener);

    /**
     * Set the scroll boundary Decider, Can customize when you can refresh.
     * Recommended {@link ScrollBoundaryDeciderAdapter}
     * 设置滚动边界判断器
     * 建议使用 {@link ScrollBoundaryDeciderAdapter}
     * @param boundary ScrollBoundaryDecider 判断器
     * @return RefreshLayout
     */
    RefreshLayout setScrollBoundaryDecider(ScrollBoundaryDecider boundary);

    /**
     * Get header of RefreshLayout
     * 获取当前 Header
     * @return RefreshLayout
     */
    @Nullable
    RefreshHeader getRefreshHeader();

    /**
     * Get footer of RefreshLayout
     * 获取当前 Footer
     * @return RefreshLayout
     */
    @Nullable
    RefreshFooter getRefreshFooter();

//    /**
//     * Get the current state of RefreshLayout
//     * 获取当前状态
//     * @return RefreshLayout
//     */
//    @NonNull
//    RefreshState getState();

}
