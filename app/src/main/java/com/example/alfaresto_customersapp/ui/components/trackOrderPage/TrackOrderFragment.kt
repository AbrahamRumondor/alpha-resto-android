package com.example.alfaresto_customersapp.ui.components.trackOrderPage

import android.Manifest
import android.app.NotificationChannel
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.remote.response.osrm.RouteResponse
import com.example.alfaresto_customersapp.databinding.BsdLocationPermissionBinding
import com.example.alfaresto_customersapp.databinding.FragmentTrackOrderBinding
import com.example.alfaresto_customersapp.domain.callbacks.OsrmCallback
import com.example.alfaresto_customersapp.domain.callbacks.RealtimeLocationCallback
import com.example.alfaresto_customersapp.data.network.NetworkUtils
import com.example.alfaresto_customersapp.ui.components.addressPage.addNewAddress.AddNewAddressFragment.Companion.markersHeight
import com.example.alfaresto_customersapp.ui.components.addressPage.addNewAddress.AddNewAddressFragment.Companion.markersWidth
import com.example.alfaresto_customersapp.utils.singleton.UserInfo.SHIPMENT
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
//        checkNotificationPermission(true)

//        SHIPMENT.postValue(SHIPMENT.value?.copy(statusDelivery = "On Delivery"))

        binding.toolbar.apply {
            btnLogout.visibility = View.GONE
            btnBack.visibility = View.VISIBLE
            ivToolbarTitle.visibility = View.GONE
            tvToolbarText.visibility = View.VISIBLE
            tvToolbarText.text = getString(R.string.track_order)
        }

        val orderId = args.orderId
        binding.run {
            toolbar.btnBack.setOnClickListener {
                if (
                    findNavController().popBackStack(R.id.order_history_fragment, false)
                ) {
                    return@setOnClickListener
                } else {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_track_order_fragment_to_order_history_fragment)
                }
            }
            onEmbeddedBackPressed()

            trackOrderViewModel.order.observe(viewLifecycleOwner) { orderList ->
                val order = orderList.find { it.id == orderId }

                order?.let { myOrder ->
                    val home = LatLng(myOrder.latitude, myOrder.longitude)

                    tvOrderStatusBody.text = getText(R.string.on_process_status)

//                    trackOrderViewModel.getShipmentById(args.shipmentId)
                    tvOrderStatusBody.text = getString(R.string.on_delivery_text)

                    tvAddressDetail.text = myOrder.fullAddress

                    mvTrack.getMapAsync {
                        map = it
                        setMapIdleListener()
                        enableMyLocation()
                        setLocationUpdatesListener(home)
                    }
                }

                btnChat.setOnClickListener {
                    trackOrderViewModel.updateReadStatus(orderId)
                    val action = TrackOrderFragmentDirections.actionTrackOrderFragmentToChatFragment(orderId = orderId)
                    Navigation.findNavController(requireView()).navigate(action)
                }

                onStatusChangeToDelivery()
            }
        }

        setConnectionBehaviour()
        binding.inclInternet.btnInetTryAgain.setOnClickListener {
            setConnectionBehaviour()
        }
    }

    private fun setConnectionBehaviour() {
        if (NetworkUtils.isConnectedToNetwork.value == false) {
            binding.inclInternet.root.visibility = View.VISIBLE
            binding.clBase.visibility = View.GONE
            Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        } else {
            binding.inclInternet.root.visibility = View.GONE
            binding.clBase.visibility = View.VISIBLE
        }
    }

    private fun onEmbeddedBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (
                    findNavController().popBackStack(R.id.order_history_fragment, false)
                ) {
                    return
                } else {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_track_order_fragment_to_order_history_fragment)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun onStatusChangeToDelivery() {
        SHIPMENT.observe(viewLifecycleOwner) {
            if (it.orderID == args.orderId) {
                if (it.statusDelivery == getString(R.string.delivered_text)) {
                    val action =
                        TrackOrderFragmentDirections.actionTrackOrderFragmentToOrderHistoryDetailFragment(
                            orderId = args.orderId,
                            orderStatus = getString(R.string.delivered_text)
                        )
                    Navigation.findNavController(binding.root)
                        .navigate(action)
                }
            }
        }
    }

    private fun setLocationUpdatesListener(home: LatLng) {
        trackOrderViewModel.getLocationUpdates(object : RealtimeLocationCallback {
            override fun onSuccess(driverLatLng: LatLng) {
                zoomToLatLng(home, driverLatLng)

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
                    if (!shouldShowLocationPermissionRationale()) {
                        showDialogForPermission()
                    } else {
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

    override fun onResume() {
        super.onResume()
        binding.mvTrack.onResume()
    }

    // cache
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mvTrack.onSaveInstanceState(outState)
    }
}