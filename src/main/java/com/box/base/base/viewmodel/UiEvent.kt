package com.box.base.base.viewmodel

/**
 * 定义一次性的 UI 事件
 * 使用 sealed interface (密封接口) 可以确保 when 表达式的详尽性
 */
sealed interface UiEvent {
    data class ShowLoading(val message: String = "请求网络中") : UiEvent
    object DismissLoading : UiEvent
    object NavigateBack : UiEvent // 例如：返回事件
    data class ShowToast(val message: String) : UiEvent // 例如：弹出 Toast 事件
}