package com.example.alfaresto_customersapp.ui.components.trackOrder

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.remote.response.RouteResponse
import com.example.alfaresto_customersapp.databinding.BsdLocationPermissionBinding
import com.example.alfaresto_customersapp.databinding.FragmentTrackOrderBinding
import com.example.alfaresto_customersapp.domain.error.OsrmCallback
import com.example.alfaresto_customersapp.domain.error.RealtimeLocationCallback
import com.example.alfaresto_customersapp.ui.components.restoTab.RestoFragmentDirections
import com.example.alfaresto_customersapp.domain.error.TrackDistanceCallback
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.service.NotificationForegroundService
import com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress.AddNewAddressFragment.Companion.markersHeight
import com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress.AddNewAddressFragment.Companion.markersWidth
import com.example.alfaresto_customersapp.utils.user.UserConstants.SHIPMENT_STATUS
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrackOrderFragment : Fragment() {

    private lateinit var binding: FragmentTrackOrderBinding
    private val trackOrderViewModel: TrackOrderViewModel by viewModels()
    private lateinit var map: GoogleMap
    private var polylines: Polyline? = null
    private val args: TrackOrderFragmentArgs by navArgs()
    private var driverMarker: Marker? = null
    private var myMarker: Marker? = null

    private lateinit var notificationChannel: NotificationChannel
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private lateinit var notificationManagerCompat: NotificationManagerCompat
    lateinit var builder: NotificationCompat.Builder
    private var customView: RemoteViews? = null
    private val progressMax = 100
    private var progressCurrent = 0

    private lateinit var bottomSheetBinding: BsdLocationPermissionBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackOrderBinding.inflate(inflater, container, false)
        binding.mvTrack.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SHIPMENT_STATUS = "On Process"


        val orderId = args.orderId
        binding.run {

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            trackOrderViewModel.order.observe(viewLifecycleOwner) { orderList ->

                Log.d("test", orderId)

                val order = orderList.find { it.id == orderId }

                Log.d("test", order.toString())

                order?.let { myOrder ->
                    val home = LatLng(myOrder.latitude, myOrder.longitude)

//                    trackOrderViewModel.getShipmentById(args.shipmentId)

                    tvOrderStatusBody.text = getText(R.string.on_process_status)
                    trackOrderViewModel.shipment.observe(viewLifecycleOwner) {
                        tvOrderStatusBody.text = it.statusDelivery
                    }

                    tvAddressDetail.text = myOrder.fullAddress

                    mvTrack.getMapAsync {
                        map = it
                        setMapIdleListener()
                        enableMyLocation()
                        setLocationUpdatesListener(home)
                    }
                }

                btnChat.setOnClickListener {
                    val action = TrackOrderFragmentDirections.actionTrackOrderFragmentToChatFragment(
                        orderId = orderId
                    )
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
        }
    }

    private fun setLocationUpdatesListener(home: LatLng) {
        trackOrderViewModel.getLocationUpdates(object : RealtimeLocationCallback {
            override fun onSuccess(driverLatLng: LatLng) {
                Log.d("test", "driver lat lng = ${driverLatLng.toString()}")
                setOnFocusLocationButtonClickListener(home, driverLatLng)
                setRouting(home = home, driver = driverLatLng)
            }

            override fun onFailure(string: String?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setRouting(home: LatLng, driver: LatLng) {
        trackOrderViewModel.getRoute(home = home, driver = driver, object : OsrmCallback {
            override fun onSuccess(routeResponse: RouteResponse?) {
                routeResponse?.let {
                    polylines?.remove()
                    // Decode the polyline into list of LatLng points
                    val decodedPath =
                        decodePolyline(routeResponse.routes[0].geometry) // Assuming there's only one route

                    // Draw polyline on Google Map
                    val polylineOptions = PolylineOptions()
                    polylineOptions.color(Color.RED)
                    polylineOptions.width(12f)
                    polylineOptions.addAll(decodedPath)

                    polylines = map.addPolyline(polylineOptions)
                    binding.tvEstimatedTime.text =
                        trackOrderViewModel.getTimeEstimation(routeResponse.routes[0].duration)
//                    observeNotificationLocationDistance(home, it.routes[0].distance)
                }

                setOrderMarker(home, driver)
            }

            override fun onFailure(string: String?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setOrderMarker(home: LatLng, driver: LatLng) {
        setMarkerOnMap(
            title = getString(R.string.driver),
            latLng = driver,
            icon = R.drawable.ic_delivery_truck_24,
            true
        )
        setMarkerOnMap(
            title = getString(R.string.me),
            latLng = home,
            icon = R.drawable.ic_house_24,
            false
        )
    }

    fun decodePolyline(polyline: String): List<LatLng> {
        return PolyUtil.decode(polyline)
    }

    private fun enableMyLocation() {
        map.uiSettings.isMyLocationButtonEnabled = true
    }

    private fun zoomToLatLng(home: LatLng, driver: LatLng) {
        val focusBounds = trackOrderViewModel.getRouteFocusBounds(home, driver)
        val padding = 300
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(focusBounds, padding)
        map.moveCamera(cameraUpdate)
        map.animateCamera(cameraUpdate)
    }

    private fun setOnFocusLocationButtonClickListener(home: LatLng, driver: LatLng) {
        binding.ivFocus.setOnClickListener {
            zoomToLatLng(home, driver)
        }
    }

    private fun setMarkerOnMap(title: String, latLng: LatLng, icon: Int, theDriverMarker: Boolean) {
        val iconDrawable = ContextCompat.getDrawable(requireContext(), icon)

        if (theDriverMarker) {
            driverMarker?.also {
                it.remove()
                driverMarker = null
            }
        } else {
            myMarker?.also {
                it.remove()
                myMarker = null
            }
        }

        iconDrawable?.let { theIcon ->

            val bitmap = Bitmap.createBitmap(
                theIcon.intrinsicWidth,
                theIcon.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)
            theIcon.setBounds(0, 0, canvas.width, canvas.height)
            theIcon.draw(canvas)

            val newIcon = Bitmap.createScaledBitmap(bitmap, markersWidth, markersHeight, true)

            val markerOptions = MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(newIcon))

            if (theDriverMarker) {
                driverMarker = map.addMarker(markerOptions)
            } else {
                myMarker = map.addMarker(markerOptions)
            }
        }
    }

    private fun setMapIdleListener() {
        map.setOnCameraIdleListener {
            val newLocation = map.cameraPosition.target
        }
    }

//    private fun createNotification() {
//        notificationManagerCompat =
//            NotificationManagerCompat.from(requireContext().applicationContext)
//
//        customView = RemoteViews(context?.packageName, R.layout.progress_notification_tray)
//
//        customView?.let { view ->
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                notificationChannel =
//                    NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
//                notificationChannel.enableLights(true)
//                notificationChannel.lightColor = Color.GREEN
//                notificationChannel.enableVibration(false)
//                notificationManagerCompat.createNotificationChannel(notificationChannel)
//
//                builder = NotificationCompat.Builder(requireContext(), channelId)
//                    .setSmallIcon(R.drawable.ic_launcher_background)
//                    .setCustomContentView(view)
//                    .setCustomBigContentView(view)
//                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_logo))
//            } else {
//                builder = NotificationCompat.Builder(requireContext())
//                    .setSmallIcon(R.drawable.ic_launcher_background)
//                    .setCustomContentView(view)
//                    .setCustomBigContentView(view)
//                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_logo))
//            }
//        }
//
////        builder.setProgress(progressMax, progressCurrent, false)
//        checkNotificationPermission(true)
//        notificationManagerCompat.notify(1234, builder.build())
//    }

//    private fun updateNotificationCustomView(shipment: Shipment) {
//        customView?.setTextViewText(R.id.notification_title, shipment.statusDelivery)
//        when (shipment.statusDelivery) {
//            "On Delivery" -> {
//                customView?.setImageViewResource(R.id.iv_dot_one, R.drawable.point_round_orange)
//                customView?.setImageViewResource(R.id.iv_dot_two, R.drawable.point_round_orange)
//                customView?.setImageViewResource(R.id.iv_dot_three, R.drawable.point_round_orange)
//                customView?.setImageViewResource(R.id.v_line_one, R.drawable.rectangle_line_orange)
//                customView?.setImageViewResource(R.id.v_line_two, R.drawable.rectangle_line_orange)
//                customView?.setTextViewText(
//                    R.id.notification_text,
//                    "Track your order progress here!"
//                )
//            }
//
//            "Delivered" -> {
//                customView?.setImageViewResource(R.id.iv_dot_one, R.drawable.point_round_orange)
//                customView?.setImageViewResource(R.id.iv_dot_two, R.drawable.point_round_orange)
//                customView?.setImageViewResource(R.id.iv_dot_three, R.drawable.point_round_orange)
//                customView?.setImageViewResource(R.id.iv_dot_four, R.drawable.point_round_orange)
//                customView?.setImageViewResource(R.id.v_line_one, R.drawable.rectangle_line_orange)
//                customView?.setImageViewResource(R.id.v_line_two, R.drawable.rectangle_line_orange)
//                customView?.setImageViewResource(
//                    R.id.v_line_three,
//                    R.drawable.rectangle_line_orange
//                )
//                customView?.setTextViewText(
//                    R.id.notification_text,
//                    "Track your order progress here!"
//                )
//            }
//
//            else -> { // ON PROCESS
//                customView?.setImageViewResource(R.id.iv_dot_one, R.drawable.point_round_orange)
//                customView?.setImageViewResource(R.id.iv_dot_two, R.drawable.point_round_orange)
//                customView?.setImageViewResource(R.id.v_line_one, R.drawable.rectangle_line_orange)
//                customView?.setTextViewText(
//                    R.id.notification_text,
//                    "Track your order progress here!"
//                )
//            }
//        }
//        checkNotificationPermission(true)
//        notificationManagerCompat.notify(1234, builder.build())
//    }

//    private fun observeNotificationLocationDistance(home: LatLng, distance: Double) {
//        checkNotificationPermission(false)
//        if (progressCurrent <= progressMax) {
//            trackOrderViewModel.getProgressPercentage(home, distance, object : TrackDistanceCallback {
//                override fun onSuccess(progressPercentage: Int) {
//                    Log.d("test", progressPercentage.toString())
//                    progressCurrent = progressPercentage
//                }
//
//                override fun onFailure(string: String?) {
//                    Toast.makeText(requireContext(), string, Toast.LENGTH_LONG).show()
//                }
//            })
////            builder.setProgress(progressMax, progressCurrent, false)
//            notificationManagerCompat.notify(1234, builder.build())
//        } else {
////            builder.setContentText("Download complete")
////                .setProgress(0, 0, false)
//            notificationManagerCompat.notify(1234, builder.build())
//        }
//    }

    private fun checkNotificationPermission(firstTime: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && firstTime) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissions()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val notificationGranted = permissions[Manifest.permission.POST_NOTIFICATIONS] ?: false

            when {
                notificationGranted -> {
                    // notification access granted
                }

                else -> {
                    // No location access granted
                    if (!shouldShowLocationPermissionRationale()) {
                        showBottomSheetLocationPermission()
                    } else {
                        showDialogForPermission()
                    }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun shouldShowLocationPermissionRationale(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.POST_NOTIFICATIONS
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermissions() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                // You can use the API that requires the permission.
            }

            else -> {
                requestMultiplePermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showDialogForPermission() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.dialog_for_permission_message_notification))
            .setTitle(getString(R.string.permission_required))
            .setCancelable(false)
            .setPositiveButton(
                getString(R.string.ok)
            ) { dialog, _ ->
                requestMultiplePermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                )
                dialog.dismiss()
            }
        builder.show()
    }

    private fun showBottomSheetLocationPermission() {
        bottomSheetBinding = BsdLocationPermissionBinding.inflate(layoutInflater)

        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheetBinding.root) // Use the binding's root view
        bottomSheetDialog.setOnCancelListener {
            it.dismiss()
        }
        bottomSheetDialog.show()

        bottomSheetBinding.btnToSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.setData(uri)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        binding.mvTrack.onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.mvTrack.onStart()
    }

    override fun onPause() {
        super.onPause()
        binding.mvTrack.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mvTrack.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mvTrack.onDestroy()
    }

    // cache
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mvTrack.onSaveInstanceState(outState)
    }

}