package com.example.alfaresto_customersapp.ui.components.trackOrder

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.remote.response.RouteResponse
import com.example.alfaresto_customersapp.databinding.FragmentTrackOrderBinding
import com.example.alfaresto_customersapp.domain.error.OsrmCallback
import com.example.alfaresto_customersapp.domain.error.RealtimeLocationCallback
import com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress.AddNewAddressFragment.Companion.markersHeight
import com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress.AddNewAddressFragment.Companion.markersWidth
import com.example.alfaresto_customersapp.ui.components.trackOrder.chat.ChatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackOrderFragment : Fragment() {

    private lateinit var binding: FragmentTrackOrderBinding
    private val trackOrderViewModel: TrackOrderViewModel by viewModels()
    private lateinit var map: GoogleMap
    private val args: TrackOrderFragmentArgs by navArgs()
    private var polylines: Polyline? = null
    private var driverMarker: Marker? = null
    private var myMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            trackOrderViewModel.order.observe(viewLifecycleOwner) { orderList ->

                Log.d("test", args.orderId)

                val order = orderList.find { it.id == args.orderId }

                Log.d("test", order.toString())

                order?.let { myOrder ->
                    val home = LatLng(myOrder.latitude, myOrder.longitude)

                    mvTrack.onCreate(savedInstanceState)
                    mvTrack.getMapAsync {
                        map = it
                        setMapIdleListener()
                        enableMyLocation()
                        setLocationUpdatesListener(home)
                    }
                }

                btnChat.setOnClickListener {
                    val intent = Intent(requireContext(), ChatActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setLocationUpdatesListener(home: LatLng) {
        trackOrderViewModel.getLocationUpdates(object : RealtimeLocationCallback {
            override fun onSuccess(driverLatLng: LatLng) {
                Log.d("test", driverLatLng.toString())
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
        val padding = 100
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