package com.box.common.ui.xpop

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.box.base.base.action.ClickAction
import com.box.base.base.action.KeyboardAction
import com.box.common.R
import com.box.other.xpopup.core.CenterPopupView

@SuppressLint("ViewConstructor")
class XPopupCenterCommonEditText(context: Context, var maxLength:Int, var title: String, var content: String, var hint:String, private var cancel: (() -> Unit)?, private var sure: ((key: String) -> Unit)?) :
    CenterPopupView(context), ClickAction, KeyboardAction {
    override fun getImplLayoutId(): Int = R.layout.xpopup_confirm_edittext

    private var et: EditText? = null

    override fun onCreate() {
        super.onCreate()
        findViewById<TextView>(R.id.tv_title)?.run {
            text = title
        }
        et = findViewById<EditText>(R.id.et_input)
        et?.setText(content)
        et?.hint = hint
        if (maxLength>0){
            et?.filters=(arrayOf<InputFilter>(LengthFilter(maxLength)))
        }
        if(title == "群公告" || title == "群简介"){
            et?.hint = "输入公告内容1-255字"
        }
        setOnClickListener(R.id.tv_cancel, R.id.tv_confirm)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_confirm -> {
                sure?.invoke(et?.text.toString().trim())
                dismiss()
            }
            R.id.tv_cancel -> {
                cancel?.invoke()
                dismiss()
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        hideKeyboard(this)
    }

}