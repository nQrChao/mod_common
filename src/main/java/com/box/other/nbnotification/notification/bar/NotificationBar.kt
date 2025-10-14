package com.box.other.nbnotification.notification.bar

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.box.common.appContext
import com.box.common.getIntUUID
import com.box.other.nbnotification.NBNotification


object NotificationBar {
    @SuppressLint("UnspecifiedImmutableFlag")
    fun notify(nbBuilder: NBNotification.Builder) {

        val channelId = nbBuilder.groupId.toString() + "_channel"
        val channelName = nbBuilder.appName
        val id = nbBuilder.groupId

        val application = appContext
        val manager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(notificationChannel)
        }
        val builder = NotificationCompat.Builder(application, channelId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(channelId)
        }

        builder.setContentTitle(nbBuilder.title)
            .setContentText(nbBuilder.content)
            .setLargeIcon(BitmapFactory.decodeResource(application.resources, nbBuilder.resId))
            .setSmallIcon(nbBuilder.resId)
            .setAutoCancel(nbBuilder.autoCancel)
            .setWhen(System.currentTimeMillis())

        if (nbBuilder.intent != null) {
            val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(appContext, getIntUUID(), nbBuilder.intent, PendingIntent.FLAG_IMMUTABLE)
            } else {
                PendingIntent.getActivity(appContext, getIntUUID(), nbBuilder.intent, PendingIntent.FLAG_ONE_SHOT)
            }
            builder.setContentIntent(pendingIntent)
//            builder.setContentIntent(
//                PendingIntent.getActivity(appContext, getIntUUID(), nbBuilder.intent, PendingIntent.FLAG_UPDATE_CURRENT)
//            )
        }
        manager.notify(id, builder.build())
//        NotificationManagerCompat.from(application).notify(
//            channelName,
//            id,
//            builder.build()
//        )
    }

}