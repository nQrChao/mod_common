package com.box.base.base.viewmodel

import com.box.other.immersionbar.BarHide

/**
 * 定义基础 ViewModel 的 UI 状态
 * 使用 data class 可以方便地通过 copy() 方法来更新部分状态
 */
data class BaseUiState(
    val title: String = "",
    val leftTitle: String = "",
    val rightTitle: String = "",
    val isTitleLineVisible: Boolean = true,
    val barHideState: BarHide = BarHide.FLAG_SHOW_BAR,
    val isStatusBarEnabled: Boolean = true
)