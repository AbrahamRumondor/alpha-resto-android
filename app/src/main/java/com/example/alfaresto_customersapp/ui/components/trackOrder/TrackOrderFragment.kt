package com.example.alfaresto_customersapp.ui.components.trackOrder

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.remote.response.RouteResponse
import com.example.alfaresto_customersapp.databinding.ActivityTrackBinding
import com.example.alfaresto_customersapp.databinding.FragmentOrderHistoryBinding
import com.example.alfaresto_customersapp.databinding.FragmentTrackOrderBinding
import com.example.alfaresto_customersapp.domain.error.OsrmCallback
import com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress.AddNewAddressFragment
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.OrderSummaryViewModel
import com.example.alfaresto_customersapp.ui.components.trackOrder.chat.ChatActivity
import com.example.alfaresto_customersapp.utils.location.LocationPermissions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil

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
            }

            btnChat.setOnClickListener {
                val intent = Intent(requireContext(), ChatActivity::class.java)
                startActivity(intent)
            }

        }


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

    private fun zoomToLatLng(latLng: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
            latLng,
            AddNewAddressFragment.zoomLevel
        )
        map.animateCamera(cameraUpdate)
    }

    private fun setOnFocusLocationButtonClickListener() {
        binding.ivFocus.setOnClickListener {
            zoomToLatLng(LatLng(-6.200000, 106.816666))
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