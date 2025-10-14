package com.chaoji.mod.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.chaoji.base.base.action.ActivityAction
import com.chaoji.base.base.action.ClickAction
import com.chaoji.common.R
import com.chaoji.common.databinding.DownFloatBind
import com.chaoji.im.appContext
import com.chaoji.im.glide.GlideApp
import com.chaoji.im.utils.floattoast.draggable.SpringScaleDraggable
import com.chaoji.im.data.model.ModModel
import com.chaoji.other.blankj.utilcode.util.FileUtils
import com.chaoji.other.blankj.utilcode.util.IntentUtils
import com.chaoji.other.blankj.utilcode.util.Logs
import com.chaoji.other.blankj.utilcode.util.PathUtils
import com.chaoji.other.blankj.utilcode.util.SnackbarUtils.dismiss
import com.chaoji.other.hjq.toast.Toaster
import com.chaoji.other.kongzue.baseokhttp.HttpRequest
import com.chaoji.other.kongzue.baseokhttp.listener.OnDownloadListener
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import java.io.File

@SuppressLint("ViewConstructor")
class DownFloatView(activity: Activity, private var icon: Drawable, private var fileUrl: String, private var onClickView: () -> Unit?, private var downOver: () -> Unit?) : LinearLayout(activity),
    ClickAction, ActivityAction {
    private var modModel: ModModel = ViewModelProvider(getActivity() as AppCompatActivity)[ModModel::class.java]
    private lateinit var floatBind: DownFloatBind

    init {
        onCreate()
    }

    fun onCreate() {
        floatBind = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.app_down_floatview,
            this,
            true
        )
        GlideApp.with(appContext)
            .load(icon)
            .transform(RoundedCorners(12))
            .error(R.drawable.tips_error_ic)
            .into(floatBind.floatImage)

        // 将点击事件设置在最外层的容器上
        floatBind.floatView.setOnClickListener { onClick(it) }

        downFile()
    }

    override fun onClick(view: View) {
        if (view === floatBind.floatView) {
            onClickView.invoke()
        }
    }

    /**
     * 根据传入的停靠位置，更新视图内部的UI元素。
     * @param side 停靠位置，值为 SpringHideDraggable.LEFT 或 SpringHideDraggable.RIGHT
     */
    //17 23
    fun updateViewForSide(side: Int) {
        val params = floatBind.floatImage.layoutParams as ViewGroup.MarginLayoutParams
        val rightMarginInPx = context.resources.getDimensionPixelSize(R.dimen.idp_17)
        val leftMarginInPx = context.resources.getDimensionPixelSize(R.dimen.idp_24)
        //val container = floatBind.floatView as? ViewGroup ?: return // 获取子元素的容器
        // 为了重新排序，我们先移除它们
        //container.removeView(bgView)
        if (side == SpringScaleDraggable.LEFT) {
            // 当停在左边时
            params.leftMargin = leftMarginInPx
            floatBind.floatImage.layoutParams = params
            //container.addView(bgView) // 再添加图标
        } else { // side == SpringHideDraggable.RIGHT
            // 当停在右边时
            params.leftMargin = rightMarginInPx
            floatBind.floatImage.layoutParams = params
//            floatBind.floatBg.scaleX  = 1f
            //container.addView(bgView) // 先添加图标
        }
    }

    private fun downFile() {
        XXPermissions.with(context).permission(Permission.REQUEST_INSTALL_PACKAGES).request(object : OnPermissionCallback {
            override fun onGranted(permissions: List<String>, all: Boolean) {
                val apkFile = FileUtils.getFileByPath(PathUtils.getInternalAppFilesPath() + File.separator + "xdqy.apk")
                if (apkFile.exists()) {
                    installApk(apkFile)
                    downOver.invoke()
                } else {
                    HttpRequest.DOWNLOAD(
                        context,
                        fileUrl,
                        apkFile,
                        object : OnDownloadListener {
                            override fun onDownloadSuccess(file: File) {
                                floatBind.downText.text = "下载完成"
                                Logs.e("update.android?.fileUrl:$fileUrl")
                                installApk(file)
                                floatBind.progressBar.progress = 100
                                dismiss()
                                downOver.invoke()
                            }

                            @SuppressLint("SetTextI18n")
                            override fun onDownloading(progress: Int) {
                                floatBind.downText.text = "正在下载 $progress%"
                                floatBind.progressBar.progress = progress
                            }

                            override fun onDownloadFailed(e: Exception) {
                                downOver.invoke()
                                Logs.e("下载失败", e.printStackTrace())
                            }
                        }
                    )
                }

            }

            override fun onDenied(permissions: List<String>, never: Boolean) {
                if (never) {
                    Toaster.show("被永久拒绝授权，请手动授予权限")
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(context, permissions)
                    downOver.invoke()
                } else {
                    Toaster.show("获取权限失败，请手动授予权限")
                    downOver.invoke()
                }
            }
        })
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
                    downOver.invoke()
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(context, permissions)
                } else {
                    Toaster.show("获取权限失败，请手动授予权限")
                    downOver.invoke()
                }
            }
        })
    }

    companion object {
        fun getView(activity: Activity, icon: Drawable, fileUrl: String, onClickView: () -> Unit?, downOver: () -> Unit?): DownFloatView {
            return DownFloatView(activity, icon, fileUrl, onClickView, downOver)
        }
    }
}
