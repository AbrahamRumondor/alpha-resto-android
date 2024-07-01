package com.example.alfaresto_customersapp.domain.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.ui.components.trackOrder.TrackOrderViewModel
import com.example.alfaresto_customersapp.utils.user.UserConstants.SHIPMENT_STATUS
import kotlinx.coroutines.launch
import okhttp3.internal.notify

class NotificationForegroundService : Service() {

    private lateinit var notificationChannel: NotificationChannel
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private lateinit var notificationManagerCompat: NotificationManagerCompat
    lateinit var builder: NotificationCompat.Builder
    private var customView: RemoteViews? = null

    override fun onCreate() {
        super.onCreate()
        notificationManagerCompat = NotificationManagerCompat.from(this)
        createNotification()
        updateNotification()
        startForeground(1234, builder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Handle incoming intents or data updates here
        updateNotification()
        return START_STICKY
    }

    private fun createNotification() {
        notificationManagerCompat =
            NotificationManagerCompat.from(this.applicationContext)

        customView = RemoteViews(this.packageName, R.layout.progress_notification_tray)

        customView?.let { view ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel =
                    NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManagerCompat.createNotificationChannel(notificationChannel)

                builder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setCustomContentView(view)
                    .setCustomBigContentView(view)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_logo))
            } else {
                builder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setCustomContentView(view)
                    .setCustomBigContentView(view)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_logo))
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManagerCompat.notify(1234, builder.build())
    }


    private fun updateNotification() {
        SHIPMENT_STATUS.let {
            customView?.setTextViewText(R.id.notification_title, it)
            when (it) {
                "On Delivery" -> {
                    customView?.setImageViewResource(R.id.iv_dot_one, R.drawable.point_round_orange)
                    customView?.setImageViewResource(R.id.iv_dot_two, R.drawable.point_round_orange)
                    customView?.setImageViewResource(
                        R.id.iv_dot_three,
                        R.drawable.point_round_orange
                    )
                    customView?.setImageViewResource(R.id.iv_dot_four, R.drawable.point_round)

                    customView?.setImageViewResource(
                        R.id.v_line_one,
                        R.drawable.rectangle_line_orange
                    )
                    customView?.setImageViewResource(
                        R.id.v_line_two,
                        R.drawable.rectangle_line_orange
                    )
                    customView?.setTextViewText(
                        R.id.notification_text,
                        getText(R.string.on_delivery)
                    )
                    customView?.setImageViewResource(
                        R.id.v_line_three,
                        R.drawable.rectangle_line
                    )
                }

                "Delivered" -> {
                    customView?.setImageViewResource(R.id.iv_dot_one, R.drawable.point_round_orange)
                    customView?.setImageViewResource(R.id.iv_dot_two, R.drawable.point_round_orange)
                    customView?.setImageViewResource(
                        R.id.iv_dot_three,
                        R.drawable.point_round_orange
                    )
                    customView?.setImageViewResource(
                        R.id.iv_dot_four,
                        R.drawable.point_round_orange
                    )
                    customView?.setImageViewResource(
                        R.id.v_line_one,
                        R.drawable.rectangle_line_orange
                    )
                    customView?.setImageViewResource(
                        R.id.v_line_two,
                        R.drawable.rectangle_line_orange
                    )
                    customView?.setImageViewResource(
                        R.id.v_line_three,
                        R.drawable.rectangle_line_orange
                    )
                    customView?.setTextViewText(
                        R.id.notification_text,
                        getText(R.string.delivered)
                    )
                }

                else -> { // ON PROCESS
                    customView?.setImageViewResource(R.id.iv_dot_one, R.drawable.point_round_orange)
                    customView?.setImageViewResource(R.id.iv_dot_two, R.drawable.point_round_orange)
                    customView?.setImageViewResource(R.id.iv_dot_three, R.drawable.point_round)
                    customView?.setImageViewResource(R.id.iv_dot_four, R.drawable.point_round)
                    customView?.setImageViewResource(
                        R.id.v_line_one,
                        R.drawable.rectangle_line_orange
                    )
                    customView?.setImageViewResource(
                        R.id.v_line_two,
                        R.drawable.rectangle_line
                    )
                    customView?.setImageViewResource(
                        R.id.v_line_three,
                        R.drawable.rectangle_line
                    )
                    customView?.setTextViewText(
                        R.id.notification_text,
                        getText(R.string.on_process)

                    )
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notificationManagerCompat.notify(1234, builder.build())
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up resources if needed
    }
}
