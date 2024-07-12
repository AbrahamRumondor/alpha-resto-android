package com.example.alfaresto_customersapp.ui.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.ui.components.MainActivity
import timber.log.Timber

@Suppress("DEPRECATION")
class NotificationForegroundService : Service() {

    private lateinit var notificationChannel: NotificationChannel
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private lateinit var notificationManagerCompat: NotificationManagerCompat
    lateinit var builder: NotificationCompat.Builder
    private var customView: RemoteViews? = null
    private val handler = Handler()
    private val delayMillis: Long = 2 * 60 * 1000

    override fun onCreate() {
        super.onCreate()
        notificationManagerCompat = NotificationManagerCompat.from(this)
        createNotification()

        handler.postDelayed({
            stopForeground(true)
            stopSelf()
        }, delayMillis)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val orderId = intent?.getStringExtra("orderId")
        val shipmentId = intent?.getStringExtra("shipmentId")
        val orderStatus = intent?.getStringExtra("orderStatus")

        updateNotification(orderId, shipmentId, orderStatus)
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

    private fun updateNotification(orderId: String?, shipmentId: String?, orderStatus: String?) {

        Timber.tag("NULLL").d(orderId + ", " + shipmentId + ", " + orderStatus)

        orderId?.let { order ->
            shipmentId?.let { shipment ->
                orderStatus?.let { orderStatus ->

                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        putExtra("navigate_to_fragment", orderStatus)
                        putExtra("orderId", order)
                        putExtra("shipmentId", shipment)
                    }
                    val pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    builder.setContentIntent(pendingIntent)

                    customView?.setTextViewText(R.id.notification_title, orderStatus)
                    when (orderStatus) {
                        "On Delivery" -> {
                            customView?.setImageViewResource(
                                R.id.iv_dot_one,
                                R.drawable.point_round_orange
                            )
                            customView?.setImageViewResource(
                                R.id.iv_dot_two,
                                R.drawable.point_round_orange
                            )
                            customView?.setImageViewResource(
                                R.id.iv_dot_three,
                                R.drawable.point_round_orange
                            )
                            customView?.setImageViewResource(
                                R.id.iv_dot_four,
                                R.drawable.point_round
                            )

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

                            setAnimationVisibility("On Delivery")
                        }

                        "Delivered" -> {
                            customView?.setImageViewResource(
                                R.id.iv_dot_one,
                                R.drawable.point_round_orange
                            )
                            customView?.setImageViewResource(
                                R.id.iv_dot_two,
                                R.drawable.point_round_orange
                            )
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

                            setAnimationVisibility("Delivered")
                        }

                        else -> { // ON PROCESS
                            customView?.setImageViewResource(
                                R.id.iv_dot_one,
                                R.drawable.point_round_orange
                            )
                            customView?.setImageViewResource(
                                R.id.iv_dot_two,
                                R.drawable.point_round_orange
                            )
                            customView?.setImageViewResource(
                                R.id.iv_dot_three,
                                R.drawable.point_round
                            )
                            customView?.setImageViewResource(
                                R.id.iv_dot_four,
                                R.drawable.point_round
                            )
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

                            setAnimationVisibility("On Process")
                        }
                    }

                    if (ActivityCompat.checkSelfPermission(
                            this@NotificationForegroundService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    notificationManagerCompat.notify(1234, builder.build())
                    startForeground(1234, builder.build())
                }
            }
        }
    }

    private fun setAnimationVisibility(item: String) {
        when (item) {
            "On Process" -> {
                customView?.setViewVisibility(
                    R.id.pb_delivered,
                    View.GONE
                )
                customView?.setViewVisibility(
                    R.id.pb_on_order,
                    View.GONE
                )
                customView?.setViewVisibility(
                    R.id.pb_on_process,
                    View.VISIBLE
                )
                customView?.setViewVisibility(
                    R.id.pb_on_delivery,
                    View.GONE
                )

                customView?.setViewVisibility(
                    R.id.iv_delivered,
                    View.VISIBLE
                )
                customView?.setViewVisibility(
                    R.id.iv_on_order,
                    View.VISIBLE
                )
                customView?.setViewVisibility(
                    R.id.iv_on_process,
                    View.GONE
                )
                customView?.setViewVisibility(
                    R.id.iv_on_delivery,
                    View.VISIBLE
                )
            }

            "On Delivery" -> {
                customView?.setViewVisibility(
                    R.id.pb_delivered,
                    View.GONE
                )
                customView?.setViewVisibility(
                    R.id.pb_on_order,
                    View.GONE
                )
                customView?.setViewVisibility(
                    R.id.pb_on_process,
                    View.GONE
                )
                customView?.setViewVisibility(
                    R.id.pb_on_delivery,
                    View.VISIBLE
                )

                customView?.setViewVisibility(
                    R.id.iv_delivered,
                    View.VISIBLE
                )
                customView?.setViewVisibility(
                    R.id.iv_on_order,
                    View.VISIBLE
                )
                customView?.setViewVisibility(
                    R.id.iv_on_process,
                    View.VISIBLE
                )
                customView?.setViewVisibility(
                    R.id.iv_on_delivery,
                    View.GONE
                )
            }

            "Delivered" -> {
                customView?.setViewVisibility(
                    R.id.pb_delivered,
                    View.VISIBLE
                )
                customView?.setViewVisibility(
                    R.id.pb_on_order,
                    View.GONE
                )
                customView?.setViewVisibility(
                    R.id.pb_on_process,
                    View.GONE
                )
                customView?.setViewVisibility(
                    R.id.pb_on_delivery,
                    View.GONE
                )

                customView?.setViewVisibility(
                    R.id.iv_delivered,
                    View.GONE
                )
                customView?.setViewVisibility(
                    R.id.iv_on_order,
                    View.VISIBLE
                )
                customView?.setViewVisibility(
                    R.id.iv_on_process,
                    View.VISIBLE
                )
                customView?.setViewVisibility(
                    R.id.iv_on_delivery,
                    View.VISIBLE
                )
            }
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
