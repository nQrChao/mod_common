package com.chaoji.im.ui.xpop.demo

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.chaoji.base.base.action.ClickAction
import com.chaoji.base.base.action.KeyboardAction
import com.chaoji.common.R
import com.chaoji.other.xpopup.core.CenterPopupView

@SuppressLint("ViewConstructor")
class DemoXPopupCenter(context: Context, private var cancel: (() -> Unit)?, private var sure: (() -> Unit)?) :
    CenterPopupView(context), ClickAction, KeyboardAction {
    override fun getImplLayoutId(): Int = R.layout._demo_xpopup_center

    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        super.onCreate()
        setOnClickListener(R.id.tv_cancel, R.id.tv_confirm)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_confirm -> {
                sure?.invoke()
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