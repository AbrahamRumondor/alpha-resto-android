package com.example.alfaresto_customersapp.ui.components.trackOrder

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.data.remote.response.RouteResponse
import com.example.alfaresto_customersapp.domain.error.OsrmCallback
import com.example.alfaresto_customersapp.domain.error.RealtimeLocationCallback
import com.example.alfaresto_customersapp.domain.error.TrackDistanceCallback
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.repository.OsrmApiRepository
import com.example.alfaresto_customersapp.domain.usecase.order.OrderUseCase
import com.example.alfaresto_customersapp.domain.usecase.resto.RestaurantUseCase
import com.example.alfaresto_customersapp.domain.usecase.shipment.ShipmentUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TrackOrderViewModel @Inject constructor(
    private val osrmApiRepository: OsrmApiRepository,
    private val orderUseCase: OrderUseCase,
    private val shipmentUseCase: ShipmentUseCase
) : ViewModel() {

    private var _order: MutableLiveData<List<Order>> = MutableLiveData()
    val order: LiveData<List<Order>> = _order

    private var _shipment: MutableLiveData<Shipment> = MutableLiveData()
    val shipment: LiveData<Shipment> = _shipment

    init {
        getOrder()
    }

    private fun getOrder() {
        viewModelScope.launch {
            val result = orderUseCase.getOrders()
            _order.postValue(result.value) // Update LiveData with fetched data
        }
    }

    fun getShipmentById(id: String) {
        viewModelScope.launch {
            shipmentUseCase.getShipmentById(id).observeForever {
                _shipment.postValue(it)
            }
        }
    }

    fun getRoute(home: LatLng, driver: LatLng, osrmCallback: OsrmCallback) {
        val call = osrmApiRepository.getRoute(
            profile = "car", // Replace with your desired profile
            coordinates = "${driver.longitude},${driver.latitude};${home.longitude},${home.latitude}",
            alternatives = false,
            steps = true,
            geometries = "polyline", // Choose your desired geometry format
            overview = "full",
            annotations = true
        )

        call.enqueue(object : retrofit2.Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                osrmCallback.onSuccess(response.body())
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                osrmCallback.onFailure(t.message)
            }

        })
    }

    fun getRouteFocusBounds(home: LatLng, driver: LatLng): LatLngBounds {
        val boundsBuilder = LatLngBounds.Builder()
        boundsBuilder.include(
            home
        )
        boundsBuilder.include(
            driver
        )

        return boundsBuilder.build()
    }

    fun getLocationUpdates(callback: RealtimeLocationCallback) {
        val database =
            FirebaseDatabase.getInstance("https://final-project-alfa-default-rtdb.asia-southeast1.firebasedatabase.app")

        database.getReference("driver_location").addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val latitude = dataSnapshot.child("latitude").getValue(Double::class.java)
                val longitude = dataSnapshot.child("longitude").getValue(Double::class.java)

                if (latitude != null && longitude != null) {
                    callback.onSuccess(
                        LatLng(latitude, longitude)
                    )
                } else {
                    callback.onFailure("Location data is missing")
                    Timber.tag("Firebase").d("Location data is missing")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onFailure(databaseError.message)
                Timber.tag("Firebase").w(databaseError.toException(), "loadPost:onCancelled")
            }

        })
    }

    fun getTimeEstimation(duration: Double): String {
        return (duration / 60.00).toInt().toString() + " min"
    }

}