package com.example.alfaresto_customersapp.ui.components.address.addNewAddress

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.BsdLocationPermissionBinding
import com.example.alfaresto_customersapp.databinding.FragmentAddNewAddressBinding
import com.example.alfaresto_customersapp.data.network.NetworkUtils
import com.example.alfaresto_customersapp.utils.location.LocationPermissions
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class AddNewAddressFragment : Fragment() {

    private lateinit var binding: FragmentAddNewAddressBinding
    private val addNewAddressViewModel: AddNewAddressViewModel by activityViewModels()

    private lateinit var map: GoogleMap
    private lateinit var bottomSheetBinding: BsdLocationPermissionBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private var isSaveClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLocationPermissions()
        binding.mvMap.onCreate(savedInstanceState)
        inputAction()
        setConnectionBehaviour()
        binding.inclInternet.btnInetTryAgain.setOnClickListener {
            setConnectionBehaviour()
        }
    }

    private fun inputAction() {
        binding.apply {
            etAddressLabel.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    binding.etAddressDetail.requestFocus()
                    true
                } else {
                    false
                }
            }
            etAddressDetail.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
                ) {
                    val imm =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setConnectionBehaviour() {
        if (NetworkUtils.isConnectedToNetwork.value == false) {
            binding.inclInternet.root.visibility = View.VISIBLE
            binding.clBase.visibility = View.GONE
            Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT)
                .show()
        } else {
            binding.inclInternet.root.visibility = View.GONE
            binding.clBase.visibility = View.VISIBLE
        }
    }

    private fun setUpMap() {
        binding.run {
            mvMap.getMapAsync {
                map = it
                enableMyLocation()
                setOnMyLocationButtonClickListener()
                setMapIdleListener()
            }
        }
        onSaveButtonClicked()
    }

    private fun onSaveButtonClicked() {
        binding.run {
            btnSaveAddress.setOnClickListener {
                if (!isSaveClicked) {
                    isSaveClicked = true
                    val addressLabel = etAddressLabel.text.toString()
                    val addressDetail = etAddressDetail.text.toString()
                    lifecycleScope.launch {
                        addNewAddressViewModel.saveAddressInDatabase(
                            addressLabel = addressLabel,
                            addressDetail = addressDetail
                        ) {
                            if (it) {
                                Navigation.findNavController(binding.root).popBackStack()
                            } else {
                                isSaveClicked = false
                            }
                        }
                    }
                }
            }
        }
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
                    setUpMap()
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
                setUpMap()
                // You can use the API that requires the permission.
            }

            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                setUpMap()
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

    private fun hasLocationPermissions(context: Context): Boolean {
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        return coarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
                fineLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun showBottomSheetLocationPermission() {
        bottomSheetBinding = BsdLocationPermissionBinding.inflate(layoutInflater)

        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheetBinding.root) // Use the binding's root view
        bottomSheetDialog.setOnCancelListener {
            if (!hasLocationPermissions(requireContext())) {
                Navigation.findNavController(binding.root).popBackStack()
            } else {
                requestLocationPermissions()
                it.dismiss()
            }
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
    }

}