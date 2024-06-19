package com.example.alfaresto_customersapp.utils.location

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.alfaresto_customersapp.R


object LocationGpsUtility {

    var locationDialogIsShown = false

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun buildAlertMessageNoGps(context: Context, activity: FragmentActivity) {
        locationDialogIsShown = true
        val builder = AlertDialog.Builder(context)
        builder.setMessage(context.getString(R.string.dialog_for_enable_gps_message))
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.yes)) { dialog, id ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
                locationDialogIsShown = false
                dialog.dismiss()
            }
            .setNegativeButton(context.getString(R.string.no)) { dialog, id ->
                locationDialogIsShown = false
                dialog.dismiss()
                activity.finish()
            }

        val alert = builder.create()
        alert.show()
    }
}