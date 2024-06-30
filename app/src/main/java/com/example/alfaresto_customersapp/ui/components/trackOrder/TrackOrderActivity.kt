package com.example.alfaresto_customersapp.ui.components.trackOrder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alfaresto_customersapp.databinding.ActivityTrackBinding
import com.example.alfaresto_customersapp.ui.components.trackOrder.chat.ChatActivity
import com.google.android.gms.maps.GoogleMap

class TrackOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackBinding
    private lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            mvTrack.onCreate(savedInstanceState)
            mvTrack.getMapAsync {
                map = it
                setOnMyLocationButtonClickListener()
                setMapIdleListener()
            }
        }


        binding.btnChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setOnMyLocationButtonClickListener() {
        map.setOnMyLocationButtonClickListener(
            GoogleMap.OnMyLocationButtonClickListener {
                return@OnMyLocationButtonClickListener false
            }
        )
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