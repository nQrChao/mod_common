package com.chaoji.im.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.chaoji.other.blankj.utilcode.util.ActivityUtils
import com.chaoji.other.blankj.utilcode.util.AppUtils
import com.chaoji.other.blankj.utilcode.util.IntentUtils
import com.chaoji.other.hjq.toast.Toaster
import java.io.File

class UtilShare private constructor() {
    private object InstanceHolder {
        val instance = UtilShare()
    }

    /**
     * 分享到微信好友
     */
    fun shareTextToWeChat(text: String) {
        //判断是否安装微信，如果没有安装微信 又没有判断就直达微信分享是会挂掉的
        if (!AppUtils.isAppInstalled( "com.tencent.mm")) {
            Toaster.show( "您还没有安装微信")
            return
        }

        if (TextUtils.isEmpty(text)) {
            Toaster.show( "内容不能为空")
            return
        }

        try {
            val intent = Intent()
            intent.component = ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI")
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, text)
            intent.type = "text/plain"
            ActivityUtils.startActivity(Intent.createChooser(intent, "分享"))
        }catch (e: Exception) {
            e.printStackTrace()
            Toaster.show( "分享失败")
        }
    }


    fun shareText(url: String, info: String?) {
        ActivityUtils.startActivity(IntentUtils.getShareTextIntent(String.format(url, info)))
    }

    fun shareFile(activity: Activity, path: String) {
        val file = File(path)
        if (file.exists()) {
            val share = Intent(Intent.ACTION_SEND)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val contentUri = FileProvider.getUriForFile(activity, AppUtils.getAppPackageName() + ".provider", file)
                share.putExtra(Intent.EXTRA_STREAM, contentUri)
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            }
            share.type = "application/vnd.ms-excel" //此处可发送多种文件
            share.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            ActivityUtils.startActivity(Intent.createChooser(share, "分享文件"))
        } else {
            Toaster.show("分享文件不存在")
        }

    }

    fun shareImage(path: String?) {
        ActivityUtils.startActivity(IntentUtils.getShareImageIntent(path))

    }

    companion object {
        @JvmStatic
        val instance: UtilShare
            get() = InstanceHolder.instance
    }
}