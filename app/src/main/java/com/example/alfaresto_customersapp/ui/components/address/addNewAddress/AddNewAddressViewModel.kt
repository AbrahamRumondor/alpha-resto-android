package com.example.alfaresto_customersapp.ui.components.address.addNewAddress

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.remote.response.osrm.RouteResponse
import com.example.alfaresto_customersapp.domain.error.OsrmCallback
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.network.NetworkUtils
import com.example.alfaresto_customersapp.domain.repository.OsrmApiRepository
import com.example.alfaresto_customersapp.domain.usecase.resto.RestaurantUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.utils.singleton.UserInfo.USER_ID
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class AddNewAddressViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val osrmApiRepository: OsrmApiRepository,
    private val restaurantUseCase: RestaurantUseCase
//    private val context: Context
) : ViewModel() {
    private val _chosenLatLng = MutableLiveData<LatLng?>()
    private var chosenLatLng: LiveData<LatLng?> = _chosenLatLng

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            userUseCase.getCurrentUser().collectLatest { user ->
                USER_ID = user.id
            }
        }
    }

    fun setChosenLatLng(latlng: LatLng?) {
        _chosenLatLng.value = latlng
    }

    suspend fun saveAddressInDatabase(
        addressLabel: String?,
        addressDetail: String?,
        onResult: (Boolean) -> Unit
    ) {
        val restoLatLng = restaurantUseCase.getRestaurant()?.let {
            LatLng(it.latitude, it.longitude)
        }

        val homeLatLng = _chosenLatLng.value

        val firebaseContext = Firebase.firestore.app.applicationContext

        if (addressLabel.isNullOrEmpty() || addressDetail.isNullOrEmpty() || restoLatLng == null || homeLatLng == null) {
            Toast.makeText(
                firebaseContext,
                firebaseContext.getString(R.string.failed_create_address),
                Toast.LENGTH_LONG
            ).show()
            onResult(false)
            return
        }

        if (NetworkUtils.isConnectedToNetwork.value == false) {
            Toast.makeText(
                firebaseContext,
                firebaseContext.getString(R.string.no_internet),
                Toast.LENGTH_LONG
            ).show()
            onResult(false)
            return
        }

        val validAddressDistance = isValidAddressDistance(home = homeLatLng, driver = restoLatLng)
        if (!validAddressDistance) {
            Toast.makeText(
                firebaseContext,
                firebaseContext.getString(R.string.too_far_address),
                Toast.LENGTH_LONG
            ).show()
            onResult(false)
            return
        }

        try {
            val newAddress = Address(
                label = addressLabel,
                address = addressDetail,
                latitude = homeLatLng.latitude,
                longitude = homeLatLng.longitude
            )
            userUseCase.makeNewAddress(newAddress)
            Toast.makeText(
                firebaseContext,
                firebaseContext.getString(R.string.new_address_success),
                Toast.LENGTH_SHORT
            ).show()
            onResult(true)
        } catch (e: Exception) {
            Toast.makeText(
                firebaseContext,
                e.message.toString(),
                Toast.LENGTH_LONG
            ).show()
            onResult(false)
        }
    }

    private fun getRoute(home: LatLng, driver: LatLng, osrmCallback: OsrmCallback) {
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

    private suspend fun getRouteDistance(home: LatLng, driver: LatLng): Double =
        suspendCancellableCoroutine { continuation ->
            getRoute(home, driver, object : OsrmCallback {
                override fun onSuccess(routeResponse: RouteResponse?) {
                    routeResponse?.let {
                        continuation.resume(it.routes[0].distance)
                    }
                        ?: continuation.resumeWithException(IllegalArgumentException("Route response is null"))
                }

                override fun onFailure(string: String?) {
                    continuation.resumeWithException(Exception(string))
                }
            })
        }

    private suspend fun isValidAddressDistance(home: LatLng, driver: LatLng): Boolean = try {
        val distance = getRouteDistance(home, driver)
        distance < 40000
    } catch (e: Exception) {
        false
    }
}