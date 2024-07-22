package com.example.alfaresto_customersapp.ui.components.restoPage

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.databinding.BsdLocationPermissionBinding
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.domain.callbacks.FirestoreCallback
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.data.network.NetworkUtils
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.ui.components.addressPage.addNewAddress.AddNewAddressFragment.Companion.schemePackage
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener
import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
import com.example.alfaresto_customersapp.ui.components.restoPage.adapter.RestoAdapter
import com.example.alfaresto_customersapp.utils.singleton.Constants
import com.example.alfaresto_customersapp.utils.singleton.UserInfo
import com.example.alfaresto_customersapp.utils.singleton.UserInfo.USER_ADDRESS
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestoFragment : Fragment() {
    private lateinit var binding: FragmentRestoBinding
    private val viewModel: RestoViewModel by activityViewModels()
    private val adapter by lazy { RestoAdapter() }

    private var latestUpdatedMenus: List<Menu>? = null

    private lateinit var bottomSheetBinding: BsdLocationPermissionBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMenu.let {
            it.adapter = adapter
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.loadingLayout.root.visibility =
                    if (isLoading) View.VISIBLE else View.GONE
            }
        }

        viewModel.getUserFromDB(object : FirestoreCallback {
            override fun onSuccess(user: User?) {
                if (user != null) {
                    binding.tvGreetings.text = getString(R.string.greetings, user.name)
                }
            }

            override fun onFailure(exception: Exception) {
                val greetingsGuest =
                    "${getString(R.string.greetings)}, ${getString(R.string.guest)}"
                binding.tvGreetings.text = greetingsGuest
            }

        })

        lifecycleScope.launch {
            // Combine the menus and cart flows
            combine(viewModel.menus, viewModel.cart) { menus, cart ->
                Pair(menus, cart)
            }.collectLatest { (menus, cart) ->
                // Process the combined data
                val updatedMenus = menus.map { menu ->
                    val cartItem = cart.find { it.menuId == menu.id }


                    Log.d("test", "menu: ${menu.stock}")
                    if (cartItem != null) {
                        menu.copy(orderCartQuantity = cartItem.menuQty)
                    } else {
                        menu
                    }
                }

                updateMenusInRv(
                    latestUpdatedMenus,
                    updatedMenus
                )

                setRestoAdapterButtons(cart, updatedMenus)
                Log.d("MENU", "1: $updatedMenus")

                // Update the cart count
                viewModel.cartCount.collectLatest { cartCount ->
                    binding.tvCartCount.text = cartCount.toString()
                    binding.rlCart.visibility = if (cartCount != 0) View.VISIBLE else View.INVISIBLE
                }

                // Show a message if the menu is empty
                delay(500)
                if (menus.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.menu_not_available),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }


        binding.btnAllMenu.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_resto_fragment_to_list_all_menu_fragment)
        }

        binding.btnCart.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_resto_fragment_to_order_summary_fragment)
        }

        binding.toolbar.btnLogout.setOnClickListener {
            viewModel.deleteAllCartItems()
            logoutValidation()
        }

        adapter.setItemClickListener { menu ->
            val action = RestoFragmentDirections.actionRestoFragmentMenuToDetailFragment(
                menuId = menu.id,
                name = menu.name,
                price = menu.price,
                description = menu.description,
                image = menu.image
            )
            Navigation.findNavController(requireView()).navigate(action)
        }

        checkNotificationPermission(true)
    }


    private fun updateMenusInRv(oldList: List<Menu>?, newList: List<Menu>) {
        adapter.submitMenuList(newList)

        if (oldList == null) {
            adapter.notifyItemRangeChanged(0,newList.size)
            latestUpdatedMenus = newList
            return
        }

        if (newList.size > oldList.size) {
            val newMenusCount = newList.size - oldList.size
            adapter.notifyItemRangeInserted(oldList.size, newMenusCount)
        }

        for (i in oldList.indices) {
            if (oldList[i] != newList[i]) {
                adapter.notifyItemChanged(i)
            }
        }

        latestUpdatedMenus = newList
    }

    private fun setRestoAdapterButtons(cart: List<CartEntity>?, updatedMenus: List<Menu>) {
        adapter.setItemListener(object : MenuListener {
            override fun onAddItemClicked(position: Int, menuId: String) {
                if (!noInternetConnection()) {
                    val item: CartEntity? = cart?.find { it.menuId == menuId }
                    viewModel.addOrderQuantity(requireContext(), menuId, item)
                }
            }

            override fun onDecreaseItemClicked(position: Int, menuId: String) {
                if (!noInternetConnection()) {
                    val item: CartEntity? = cart?.find { it.menuId == menuId }
                    viewModel.decreaseOrderQuantity(menuId, item)
                }
            }
        })
    }

    fun noInternetConnection(): Boolean {
        return if (NetworkUtils.isConnectedToNetwork.value == false) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
            true
        } else {
            false
        }
    }

    private fun logoutValidation() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.logout_confirmation_title))
            .setMessage(getString(R.string.logout_confirmation_message))
            .setPositiveButton(getString(R.string.logout)) { _, _ ->
                logout()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun logout() {
        Firebase.messaging.deleteToken()

        val sharedPreferences =
            requireContext().getSharedPreferences(Constants.login, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(Constants.isLoggedIn, false)
            apply()
        }

        UserInfo.run {
            USER_TOKEN = ""
            USER_ADDRESS = Address()
            USER_ID = ""
            USER_PAYMENT_METHOD = ""
            SHIPMENT = MutableLiveData(Shipment())
        }

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun checkNotificationPermission(firstTime: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && firstTime) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissions()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val notificationGranted = permissions[Manifest.permission.POST_NOTIFICATIONS] ?: false

            when {
                notificationGranted -> {
                    // notification access granted
                }

                else -> {
//                    // No location access granted
//                    if (!shouldShowLocationPermissionRationale()) {
//                        showBottomSheetLocationPermission()
//                    } else {
//                        showDialogForPermission()
//                    }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun shouldShowLocationPermissionRationale(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.POST_NOTIFICATIONS
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermissions() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                // You can use the API that requires the permission.
            }

            else -> {
                requestMultiplePermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showDialogForPermission() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.dialog_for_permission_message_notification))
            .setTitle(getString(R.string.permission_required))
            .setCancelable(false)
            .setPositiveButton(
                getString(R.string.ok)
            ) { dialog, _ ->
                requestMultiplePermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
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
            it.dismiss()
        }
        bottomSheetDialog.show()

        bottomSheetBinding.btnToSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts(schemePackage, requireActivity().packageName, null)
            intent.setData(uri)
            startActivity(intent)
        }
    }
}