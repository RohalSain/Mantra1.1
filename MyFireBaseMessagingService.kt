package com.developer.rohal.mantra

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFireBaseMessagingService: FirebaseMessagingService()
{

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        //pushNotification(remoteMessage)
        val int= Intent(this, MainActivity::class.java)
        int.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        var pendingIntent=PendingIntent.getActivity(this,0,int,PendingIntent.FLAG_ONE_SHOT)
        var notificationBuilder: NotificationCompat.Builder =NotificationCompat.Builder(this)
        notificationBuilder.setContentTitle("FCM notification")
        notificationBuilder.setContentText(remoteMessage?.notification?.body)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder.setContentIntent(pendingIntent)
        var notificationManger:NotificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManger.notify(0,notificationBuilder.build())
    }

    private fun pushNotification(remoteMessage: RemoteMessage?)
    {
      val pushNotifications=Intent(Config.Str)
      pushNotifications.putExtra("message",remoteMessage)
      LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotifications)
    }
}