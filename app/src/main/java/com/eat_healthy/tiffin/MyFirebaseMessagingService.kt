package com.eat_healthy.tiffin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.ui.MainActivity
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.SharedPrefManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    companion object {
        const val notificationId=1
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (sharedPrefManager.getBooleanFromPreference("registered")){
            return
        }
        createNotification(
            message.notification?.title,
            message.notification?.body,
            message.notification?.imageUrl
        )
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun  createNotification(title:String?, body:String?,image:Uri?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        var largeBitmap: Bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.mealy_icon)
        CoroutineScope(Dispatchers.Main).launch {
            val builder : NotificationCompat.Builder
            if (image != null) {
                largeBitmap = withContext(Dispatchers.IO) {
                    Glide.with(this@MyFirebaseMessagingService).asBitmap().load(image).submit()
                        .get()
                }

                val bitmap = BitmapFactory.decodeResource(this@MyFirebaseMessagingService.resources, R.drawable.mealy_icon)
                 builder = NotificationCompat.Builder(this@MyFirebaseMessagingService, "1")
                    .setSmallIcon(R.drawable.ic_m)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(ContextCompat.getColor(this@MyFirebaseMessagingService,R.color.colorPrimary))
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
                    .setLargeIcon(bitmap)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
            } else {
                 builder = NotificationCompat.Builder(this@MyFirebaseMessagingService, "1")
                    .setSmallIcon(R.drawable.ic_m)
                    .setContentTitle(title)
                     .setContentText(body)
                    .setColor(ContextCompat.getColor(this@MyFirebaseMessagingService,R.color.colorPrimary))
                     .setStyle(NotificationCompat.BigTextStyle()
                         .bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "channelName"
                val descriptionText = "Channel Description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("1", name, importance).apply {
                    description = descriptionText
                }
                val notificationManager = this@MyFirebaseMessagingService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            with(NotificationManagerCompat.from(this@MyFirebaseMessagingService)) {
                notify(MyFirebaseMessagingService.notificationId,builder.build())
            }
        }
    }
}