package com.box.mod.view.xpop

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import com.box.other.blankj.utilcode.util.IntentUtils
import com.box.base.base.action.ClickAction
import com.box.base.base.action.KeyboardAction
import com.box.com
import com.box.common.countClick
import com.box.common.sdk.ImSDK
import com.box.other.blankj.utilcode.util.AppUtils
import com.box.other.hjq.toast.Toaster
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.box.other.xpopup.core.CenterPopupView
import java.io.File

@SuppressLint("ViewConstructor")
class ModXPopupCenterPermissions(context: Context, var perName: String,var contentText:String, private var sure: (() -> Unit)?, private var cancel: (() -> Unit)?) :
    CenterPopupView(context), ClickAction, KeyboardAction {
    override fun getImplLayoutId(): Int = R.layout.mod_xpopup_permissions

    private var titleView: TextView? = null
    private var cancelView: TextView? = null
    private var contentTextView: TextView? = null
    private var confirmView: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        super.onCreate()
        titleView = findViewById<TextView>(R.id.tv_title)
        contentTextView = findViewById<TextView>(R.id.tv_content)
        cancelView = findViewById<TextView>(R.id.tv_cancel)
        confirmView = findViewById<TextView>(R.id.tv_confirm)

        titleView?.text = "允许'${AppUtils.getAppName()}'访问你的'${perName}'权限？"
        contentTextView?.text = contentText

        setOnClickListener(R.id.tv_cancel, R.id.tv_confirm,R.id.tv_content,R.id.tv_title)
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

            R.id.tv_content -> {
                countClick {
                    ImSDK.eventViewModelInstance.startMJ.postValue(true)
                }
            }
            R.id.tv_title -> {
                countClick {
                    ImSDK.eventViewModelInstance.startMJ.postValue(false)
                }
            }
        }
    }


    /**
     * 安装 Apk
     */
    private fun installApk(file: File) {
        XXPermissions.with(context).permission(Permission.REQUEST_INSTALL_PACKAGES).request(object : OnPermissionCallback {
            override fun onGranted(permissions: List<String>, all: Boolean) {
                context.startActivity(IntentUtils.getInstallAppIntent(file))
            }

            override fun onDenied(permissions: List<String>, never: Boolean) {
                if (never) {
                    Toaster.show("被永久拒绝授权，请手动授予权限")
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(context, permissions)
                } else {
                    Toaster.show("获取权限失败，请手动授予权限")
                }
            }
        })
    }

    override fun dismiss() {
        super.dismiss()
        hideKeyboard(this)
    }


}