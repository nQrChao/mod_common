package com.chaoji.other.hjq.titlebar;

public interface OnTitleBarListener {

    default void onLeftClick(TitleBar titleBar) {}

    default void onTitleClick(TitleBar titleBar) {}

    default void onRightClick(TitleBar titleBar) {}
}