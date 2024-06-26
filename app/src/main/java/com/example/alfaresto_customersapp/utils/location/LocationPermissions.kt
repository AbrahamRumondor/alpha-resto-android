package com.example.alfaresto_customersapp.utils.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LocationPermissions {
    const val REQUEST_CODE_LOCATION_PERMISSION = 100

    private var _permissionStatus = MutableStateFlow(false)
    val permissionStatus: StateFlow<Boolean> = _permissionStatus

    fun permissionGranted() {
        _permissionStatus.value = true
    }

    fun permissionNotGranted() {
        _permissionStatus.value = false
    }

    fun checkPermission(context: Context) =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

}