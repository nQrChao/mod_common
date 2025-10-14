package com.box.other.nbnotification

import com.box.other.blankj.utilcode.util.AppUtils
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.nbnotification.notification.bar.NotificationBar
import com.box.other.nbnotification.notification.foreground.ForegroundNotificationText

object TextNotificationExecute {
    fun execute(builder: NBNotification.Builder) {
        when (builder.notificationType) {
            NBNotificationType.FRONT_AND_NOTIFICATION_BAR -> {
                NotificationBar.notify(builder)
                ForegroundNotificationText.notify(builder)
            }
            NBNotificationType.FRONT -> {
                ForegroundNotificationText.notify(builder)
            }
            NBNotificationType.NOTIFICATION_BAR -> {
                NotificationBar.notify(builder)
            }
            NBNotificationType.FRONT_INTERLACE_NOTIFICATION_BAR -> {
                if (!AppUtils.isAppForeground()) {
                    Logs.e("TextNotificationExecute","后台通知")
                    NotificationBar.notify(builder)
                } else {
                    Logs.e("TextNotificationExecute","前台通知")
                    ForegroundNotificationText.notify(builder)
                }
            }
        }

    }
}