package com.example.alfaresto_customersapp.ui.service.pushNotification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.domain.model.Token
import com.example.alfaresto_customersapp.ui.components.MainActivity
import com.example.alfaresto_customersapp.utils.singleton.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class PushNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var context: Context
    private lateinit var notificationChannel: NotificationChannel
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private lateinit var notificationManagerCompat: NotificationManagerCompat
    lateinit var builder: NotificationCompat.Builder
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToFirestore(token)
        UserInfo.USER_TOKEN = token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val deepLink = message.data.getValue("link")
        val title = message.data.getValue("title")
        val body = message.data.getValue("body")
        Log.d("notiv", "${title}...${body}...${deepLink}")
//        handleDeepLink(deepLink)
        createNotification(deepLink, title, body)
    }

    private fun createNotification(link: String, title: String, body: String) {
        notificationManagerCompat =
            NotificationManagerCompat.from(this.applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManagerCompat.createNotificationChannel(notificationChannel)

            builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(body)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_logo))
        } else {
            builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(body)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_logo))
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("link", link)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        builder.setContentIntent(pendingIntent)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManagerCompat.notify(4321, builder.build())
    }


    private fun handleDeepLink(deepLink: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink)).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Timber.d("notiv", "Failed to handle deep link: $deepLink")
        }
    }

    private fun sendTokenToFirestore(token: String) {
        val db = FirebaseFirestore.getInstance()

        val documentId = db.collection("users").document("amnRLCt7iYGogz6JRxi5")
            .collection("tokens").document().id

        val userToken = Token(userToken = token)
        db.collection("users").document("amnRLCt7iYGogz6JRxi5")
            .collection("tokens").document(documentId)
            .set(userToken)
            .addOnSuccessListener {
                Timber.tag("test").d("FCM token added to Firestore")
            }
            .addOnFailureListener { e ->
                Timber.tag("test").w(e, "Error adding FCM token to Firestore")
            }
    }
}