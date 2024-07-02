package com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.BsdLocationPermissionBinding
import com.example.alfaresto_customersapp.databinding.FragmentAddNewAddressBinding
import com.example.alfaresto_customersapp.ui.components.restoTab.address.addressList.AddressListViewModel
import com.example.alfaresto_customersapp.utils.location.LocationGpsUtility
import com.example.alfaresto_customersapp.utils.location.LocationGpsUtility.locationDialogIsShown
import com.example.alfaresto_customersapp.utils.location.LocationPermissions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog

class AddNewAddressFragment : Fragment() {

    private lateinit var binding: FragmentAddNewAddressBinding
    private val addNewAddressViewModel: AddNewAddressViewModel by activityViewModels()

    private lateinit var map: GoogleMap
    private lateinit var bottomSheetBinding: BsdLocationPermissionBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            mvMap.onCreate(savedInstanceState)
            mvMap.getMapAsync {
                map = it
                enableMyLocation()
                setOnMyLocationButtonClickListener()
                setMapIdleListener()
            }
        }
        onSaveButtonClicked()
        requestLocationPermissions()
    }

    private fun onSaveButtonClicked() {
        binding.run {
            btnSaveAddress.setOnClickListener {
                val addressLabel = etAddressLabel.text.toString()
                val addressDetail = etAddressDetail.text.toString()
                addNewAddressViewModel.saveAddressInDatabase(
                    addressLabel = addressLabel,
                    addressDetail = addressDetail
                )
                Navigation.findNavController(it).popBackStack()
            }
        }
    }

    private fun zoomToLatLng(latLng: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel)
        map.animateCamera(cameraUpdate)
    }

    private fun enableMyLocation() {
        if (LocationPermissions.checkPermission(requireContext())) return

        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
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
            addNewAddressViewModel.setChosenLatLng(newLocation)
        }
    }

    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            when {
                fineLocationGranted || coarseLocationGranted -> {
                    // Fine location access granted
                }

                else -> {
                    // No location access granted
                    if (!shouldShowLocationPermissionRationale()) {
                        showBottomSheetLocationPermission()
                    } else {
                        showDialogForPermission()
                    }
                }
            }
        }

    private fun shouldShowLocationPermissionRationale(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private fun requestLocationPermissions() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // You can use the API that requires the permission.
            }

            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                // You can use the API that requires the permission.
            }

            else -> {
                // Directly ask for the permission
                requestMultiplePermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun showDialogForPermission() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.dialog_for_permission_message))
            .setTitle(getString(R.string.permission_required))
            .setCancelable(false)
            .setPositiveButton(
                getString(R.string.ok)
            ) { dialog, _ ->
                requestMultiplePermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                dialog.dismiss()
            }
        builder.show()
    }

    private fun showBottomSheetLocationPermission() {
        bottomSheetBinding = BsdLocationPermissionBinding.inflate(layoutInflater)

        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheetBinding.root) // Use the binding's root view
        bottomSheetDialog.setOnCancelListener {
            requireActivity().finish()
        }
        bottomSheetDialog.show()

        bottomSheetBinding.btnToSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.setData(uri)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mvMap.onResume()
    }

    private fun checkAndShowGpsAlertMessage() {
        if (!LocationGpsUtility.isLocationEnabled(requireContext()) && !locationDialogIsShown) {
            LocationGpsUtility.buildAlertMessageNoGps(requireContext(), requireActivity())
        }
    }

    override fun onStop() {
        super.onStop()
        binding.mvMap.onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.mvMap.onStart()
    }

    override fun onPause() {
        super.onPause()
        binding.mvMap.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mvMap.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mvMap.onDestroy()
    }

    // cache
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mvMap.onSaveInstanceState(outState)
    }

    companion object {
        const val markersWidth = 100
        const val markersHeight = 100
        const val zoomLevel = 15f
    }

}