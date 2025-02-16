package com.example.avito.tech.avito_tech_winter_2025.notification

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.example.avito.tech.avito_tech_winter_2025.receiver.MediaReceiver

class NotificationHelper {
    companion object {
        private const val CHANNEL_ID = "channel"
        const val NOTIFICATION_ID = 0

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    context.getString(com.example.avito.tech.avito_tech_winter_2025.R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        }


        fun createNotification(context: Context): Notification {

            val nextIntent =
                Intent(context, MediaReceiver::class.java).setAction(MediaReceiver.ACTION_NEXT)
            val nextPendingIntent =
                PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE)

            val prevIntent =
                Intent(context, MediaReceiver::class.java).setAction(MediaReceiver.ACTION_PREV)
            val prevPendingIntent =
                PendingIntent.getBroadcast(context, 0, prevIntent, PendingIntent.FLAG_IMMUTABLE)

            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_media_ff)
                    .setContentTitle("")
                    .setContentText("")
                    .setStyle(
                        MediaStyle()
                            .setShowActionsInCompactView(0, 1)
                    )
                    .addAction(R.drawable.ic_media_previous, "Prev", prevPendingIntent)
                    .addAction(R.drawable.ic_media_next, "Next", nextPendingIntent)
            return builder.build()
        }

    }
}