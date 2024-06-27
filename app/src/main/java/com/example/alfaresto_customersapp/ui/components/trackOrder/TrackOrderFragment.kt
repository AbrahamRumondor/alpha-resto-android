package com.example.alfaresto_customersapp.ui.components.trackOrder

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.remote.response.RouteResponse
import com.example.alfaresto_customersapp.databinding.FragmentTrackOrderBinding
import com.example.alfaresto_customersapp.domain.error.OsrmCallback
import com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress.AddNewAddressFragment.Companion.markersHeight
import com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress.AddNewAddressFragment.Companion.markersWidth
import com.example.alfaresto_customersapp.ui.components.trackOrder.chat.ChatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackOrderFragment : Fragment() {

    private lateinit var binding: FragmentTrackOrderBinding
    private val trackOrderViewModel: TrackOrderViewModel by viewModels()
    private lateinit var map: GoogleMap

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
            mvTrack.onCreate(savedInstanceState)
            mvTrack.getMapAsync {
                map = it
                setOnFocusLocationButtonClickListener()
                setMapIdleListener()
                enableMyLocation()
                setRouting()
                setOrderMarker()
            }

            btnChat.setOnClickListener {
                val intent = Intent(requireContext(), ChatActivity::class.java)
                startActivity(intent)
            }

        }


    }

    private fun setOrderMarker() {
        val driver = trackOrderViewModel.getDriverLatLng()
        val user = trackOrderViewModel.getUserAddressLatLng()
        setMarkerOnMap(title = driver.first, latLng = driver.second, icon = R.drawable.ic_delivery_truck_24)
        setMarkerOnMap(title = user.first, latLng = user.second, icon = R.drawable.ic_house_24)
    }

    private fun setRouting() {
        trackOrderViewModel.getRoute(object : OsrmCallback {
            override fun onSuccess(routeResponse: RouteResponse?) {
                routeResponse?.let {
                    // Decode the polyline into list of LatLng points
                    val decodedPath =
                        decodePolyline(routeResponse.routes[0].geometry) // Assuming there's only one route

                    // Draw polyline on Google Map
                    val polylineOptions = PolylineOptions()
                    polylineOptions.color(Color.RED)
                    polylineOptions.width(12f)
                    polylineOptions.addAll(decodedPath)

                    map.addPolyline(polylineOptions)
                }
            }

            override fun onFailure(string: String?) {
                TODO("Not yet implemented")
            }
        })
    }

    fun decodePolyline(polyline: String): List<LatLng> {
        return PolyUtil.decode(polyline)
    }

    private fun enableMyLocation() {
        map.uiSettings.isMyLocationButtonEnabled = true
    }

    private fun zoomToLatLng() {
        val focusBounds = trackOrderViewModel.getRouteFocusBounds()
        val padding = 100
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(focusBounds, padding)
        map.moveCamera(cameraUpdate)
        map.animateCamera(cameraUpdate)
    }

    private fun setOnFocusLocationButtonClickListener() {
        binding.ivFocus.setOnClickListener {
            zoomToLatLng()
        }
    }

    private fun setMarkerOnMap(title: String, latLng: LatLng, icon: Int) {
        val iconDrawable = ContextCompat.getDrawable(requireContext(), icon)

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

            map.addMarker(markerOptions)
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