package com.example.alfaresto_customersapp.ui.components.trackOrder

import androidx.lifecycle.ViewModel
import com.example.alfaresto_customersapp.data.remote.response.RouteResponse
import com.example.alfaresto_customersapp.domain.error.OsrmCallback
import com.example.alfaresto_customersapp.domain.repository.FcmApiRepository
import com.example.alfaresto_customersapp.domain.repository.OsrmApiRepository
import com.example.alfaresto_customersapp.domain.usecase.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.auth.AuthUseCase
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TrackOrderViewModel @Inject constructor(
    private val osrmApiRepository: OsrmApiRepository
) : ViewModel() {

    fun getRoute(osrmCallback: OsrmCallback){
        val call = osrmApiRepository.getRoute(
            profile = "car", // Replace with your desired profile
            coordinates = "106.816666,-6.200000;107.609810,-6.914744",
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

    fun getRouteFocusBounds(): LatLngBounds {
        val boundsBuilder = LatLngBounds.Builder()
        boundsBuilder.include(
            LatLng(-6.200000,106.816666)
        )
        boundsBuilder.include(
            LatLng(-6.914744,107.609810)
        )

        return boundsBuilder.build()
    }

    fun getDriverLatLng(): Pair<String, LatLng> {
        return Pair("Driver", LatLng(-6.200000, 106.816666))
    }

    fun getUserAddressLatLng(): Pair<String, LatLng>  {
        return Pair("Me",LatLng(-6.914744,107.609810))
    }
}