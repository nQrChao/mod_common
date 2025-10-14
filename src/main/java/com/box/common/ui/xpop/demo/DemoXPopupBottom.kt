package com.box.common.ui.xpop.demo

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.box.base.base.action.ClickAction
import com.box.base.base.action.KeyboardAction
import com.box.common.R
import com.box.other.xpopup.core.BottomPopupView

@SuppressLint("ViewConstructor")
class DemoXPopupBottom(context: Context, var logcation: String, private var cancel: (() -> Unit)?, private var sure: ((info: String) -> Unit)?) :
    BottomPopupView(context), ClickAction, KeyboardAction {
    override fun getImplLayoutId(): Int=  R.layout._demo_xpopup_bottom

    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        super.onCreate()

        setOnClickListener(R.id.tv_cancel, R.id.tv_confirm)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_confirm -> {
                sure?.invoke(logcation)
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