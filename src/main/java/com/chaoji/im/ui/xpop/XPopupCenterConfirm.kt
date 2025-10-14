package com.chaoji.im.ui.xpop

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.chaoji.common.R
import com.chaoji.other.xpopup.core.CenterPopupView
import com.chaoji.other.xpopup.interfaces.OnCancelListener
import com.chaoji.other.xpopup.interfaces.OnConfirmListener
import com.chaoji.other.xpopup.util.XPopupUtils

@SuppressLint("ViewConstructor")
open class XPopupCenterConfirm(context: Context, bindLayoutId: Int) :
    CenterPopupView(context), View.OnClickListener {
    override fun getImplLayoutId(): Int = if (bindLayoutId != 0) bindLayoutId else R.layout.xpopup_confirm


    private var cancelListener: OnCancelListener? = null
    private var confirmListener: OnConfirmListener? = null
    private var tvTitle: TextView? = null
    private var etInput: EditText? = null
    private var tvCancel: TextView? = null
    private var tvContent: TextView? = null
    private var tvConfirm: TextView? = null

    var title: CharSequence? = null
    var content: CharSequence? = null
    private var hint: CharSequence? = null
    private var cancelText: CharSequence? = null
    private var confirmText: CharSequence? = null

    private var divider1: View? = null
    private var divider2: View? = null
    private var isHideCancel = false


    init {
        this.bindLayoutId = bindLayoutId
        addInnerContent()
    }


    override fun onCreate() {
        super.onCreate()
        tvTitle = findViewById(R.id.tv_title)
        tvContent = findViewById(R.id.tv_content)
        tvCancel = findViewById(R.id.tv_cancel)
        tvConfirm = findViewById(R.id.tv_confirm)
        tvContent?.movementMethod = LinkMovementMethod.getInstance()
        etInput = findViewById(R.id.et_input)
        divider1 = findViewById(R.id.xpopup_divider1)
        divider2 = findViewById(R.id.xpopup_divider2)
        tvCancel?.setOnClickListener(this)
        tvConfirm?.setOnClickListener(this)
        if (!TextUtils.isEmpty(title)) {
            tvTitle?.text = title
        } else {
            XPopupUtils.setVisible(tvTitle, false)
        }
        if (!TextUtils.isEmpty(content)) {
            tvContent?.text = content
        } else {
            XPopupUtils.setVisible(tvContent, false)
        }
        if (!TextUtils.isEmpty(cancelText)) {
            tvCancel?.text = cancelText
        }
        if (!TextUtils.isEmpty(confirmText)) {
            tvConfirm?.text = confirmText
        }
        if (isHideCancel) {
            XPopupUtils.setVisible(tvCancel, false)
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


    fun setListener(confirmListener: OnConfirmListener?, cancelListener: OnCancelListener?): XPopupCenterConfirm {
        this.cancelListener = cancelListener
        this.confirmListener = confirmListener
        return this
    }

    fun setTitleContent(title: CharSequence?, content: CharSequence?, hint: CharSequence?): XPopupCenterConfirm {
        this.title = title
        this.content = content
        this.hint = hint
        return this
    }

    fun setCancelText(cancelText: CharSequence?): XPopupCenterConfirm {
        this.cancelText = cancelText
        return this
    }

    fun setConfirmText(confirmText: CharSequence?): XPopupCenterConfirm {
        this.confirmText = confirmText
        return this
    }

    override fun onClick(v: View) {
        if (v === tvCancel) {
            if (cancelListener != null) cancelListener!!.onCancel()
            dismiss()
        } else if (v === tvConfirm) {
            if (confirmListener != null) confirmListener!!.onConfirm()
            if (popupInfo.autoDismiss) dismiss()
        }
    }

    override fun getMaxHeight(): Int {
        if (popupInfo == null) return 0
        return if (popupInfo.maxHeight == 0) (XPopupUtils.getAppHeight(context) * 0.8).toInt() else popupInfo.maxHeight
    }
}