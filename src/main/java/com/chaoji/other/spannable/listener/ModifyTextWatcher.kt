package com.chaoji.other.spannable.listener

import android.text.Editable
import android.text.TextWatcher

/**
 * 允许修改已输入内容
 */
abstract class ModifyTextWatcher : TextWatcher {

    /** 是否为修改事件 */
    protected var isModifyEvent = false

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable) {
        if (!isModifyEvent) {
            isModifyEvent = true
            onModify(s)
            isModifyEvent = false
        }
    }

    /**
     * 每次输入完成会被触发, 在[onModify]中可以修改已输入内容, 且不会导致死循环
     */
    abstract fun onModify(s: Editable)
}