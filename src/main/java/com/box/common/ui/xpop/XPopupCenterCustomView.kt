package com.box.common.ui.xpop

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.box.com.R
import com.box.other.xpopup.core.CenterPopupView
import com.box.other.xpopup.interfaces.OnCancelListener
import com.box.other.xpopup.interfaces.OnConfirmListener
import com.box.other.xpopup.util.XPopupUtils

@SuppressLint("ViewConstructor")
open class XPopupCenterCustomView(context: Context, bindLayoutId: Int) :
    CenterPopupView(context), View.OnClickListener {
    override fun getImplLayoutId(): Int {
        return if (bindLayoutId != 0) bindLayoutId else R.layout.xpopup_confirm
    }

    private var cancelListener: OnCancelListener? = null
    private var confirmListener: OnConfirmListener? = null
    private var tv_title: TextView? = null
    private var tv_content: TextView? = null
    private var tv_cancel: TextView? = null
    private var tv_confirm: TextView? = null
    var title: CharSequence? = null
    var content: CharSequence? = null
    private var hint: CharSequence? = null
    private var cancelText: CharSequence? = null
    private var confirmText: CharSequence? = null
    private var et_input: EditText? = null
    private var divider1: View? = null
    private var divider2: View? = null
    private var isHideCancel = false


    /**
     * @param context
     * @param bindLayoutId layoutId 要求布局中必须包含的TextView以及id有：tv_title，tv_content，tv_cancel，tv_confirm
     */
    init {
        this.bindLayoutId = bindLayoutId
        addInnerContent()
    }


    override fun onCreate() {
        super.onCreate()
        tv_title = findViewById(R.id.tv_title)
        tv_content = findViewById(R.id.tv_content)
        tv_cancel = findViewById(R.id.tv_cancel)
        tv_confirm = findViewById(R.id.tv_confirm)
        tv_content?.movementMethod = LinkMovementMethod.getInstance()
        et_input = findViewById(R.id.et_input)
        divider1 = findViewById(R.id.xpopup_divider1)
        divider2 = findViewById(R.id.xpopup_divider2)
        tv_cancel?.setOnClickListener(this)
        tv_confirm?.setOnClickListener(this)
        if (!TextUtils.isEmpty(title)) {
            tv_title?.text = title
        } else {
            XPopupUtils.setVisible(tv_title, false)
        }
        if (!TextUtils.isEmpty(content)) {
            tv_content?.text = content
        } else {
            XPopupUtils.setVisible(tv_content, false)
        }
        if (!TextUtils.isEmpty(cancelText)) {
            tv_cancel?.text = cancelText
        }
        if (!TextUtils.isEmpty(confirmText)) {
            tv_confirm?.text = confirmText
        }
        if (isHideCancel) {
            XPopupUtils.setVisible(tv_cancel, false)
            XPopupUtils.setVisible(divider2, false)
        }
        applyTheme()
    }


    val titleTextView: TextView
        get() = findViewById(R.id.tv_title)
    val contentTextView: TextView
        get() = findViewById(R.id.tv_content)
    val cancelTextView: TextView
        get() = findViewById(R.id.tv_cancel)
    val confirmTextView: TextView
        get() = findViewById(R.id.tv_confirm)


    fun setListener(confirmListener: OnConfirmListener?, cancelListener: OnCancelListener?): XPopupCenterCustomView {
        this.cancelListener = cancelListener
        this.confirmListener = confirmListener
        return this
    }

    fun setTitleContent(title: CharSequence?, content: CharSequence?, hint: CharSequence?): XPopupCenterCustomView {
        this.title = title
        this.content = content
        this.hint = hint
        return this
    }

    fun setCancelText(cancelText: CharSequence?): XPopupCenterCustomView {
        this.cancelText = cancelText
        return this
    }

    fun setConfirmText(confirmText: CharSequence?): XPopupCenterCustomView {
        this.confirmText = confirmText
        return this
    }

    override fun onClick(v: View) {
        if (v === tv_cancel) {
            if (cancelListener != null) cancelListener!!.onCancel()
            dismiss()
        } else if (v === tv_confirm) {
            if (confirmListener != null) confirmListener!!.onConfirm()
            if (popupInfo.autoDismiss) dismiss()
        }
    }

    override fun getMaxHeight(): Int {
        if (popupInfo == null) return 0
        return if (popupInfo.maxHeight == 0) (XPopupUtils.getAppHeight(context) * 0.8).toInt() else popupInfo.maxHeight
    }
}