package com.box.common.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.box.base.base.action.ClickAction
import com.box.base.base.action.KeyboardAction
import com.box.com.R
import com.box.other.xpopup.core.CenterPopupView

@SuppressLint("ViewConstructor")
class XPopupCenterAuthorize(context: Context, private var cancel: (() -> Unit)?, private var sure: (() -> Unit)?) :
    CenterPopupView(context), ClickAction, KeyboardAction {
    override fun getImplLayoutId(): Int = R.layout.xpopup_applets_authorize

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