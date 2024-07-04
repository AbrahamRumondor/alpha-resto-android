package com.example.alfaresto_customersapp.ui.components.restoTab

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.databinding.BsdLocationPermissionBinding
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.domain.error.FirestoreCallback
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener
import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
import com.example.alfaresto_customersapp.ui.components.restoTab.adapter.RestoAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestoFragment : Fragment() {
    private lateinit var binding: FragmentRestoBinding
    private val viewModel: RestoViewModel by activityViewModels()
    private val adapter by lazy { RestoAdapter() }

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
            viewModel.menus.collectLatest { menus ->
                if (menus.isEmpty()) {
//                    Toast.makeText(requireContext(), "Menu is not available", Toast.LENGTH_LONG).show()
                    return@collectLatest
                }

                viewModel.cart.collectLatest { cart ->
                    if (cart.isEmpty()) {
                        setRestoAdapterButtons(cart)
                        adapter.submitMenuList(menus)

                        return@collectLatest
                    }

                    val updatedMenus = menus.map { menu ->
                        val cartItem = cart.find { it.menuId == menu.id }
                        if (cartItem != null) {
                            menu.copy(orderCartQuantity = cartItem.menuQty)
                        } else {
                            menu
                        }
                    }

                    setRestoAdapterButtons(cart)
                    adapter.submitMenuList(updatedMenus)

                    viewModel.cartCount.collectLatest {
                        binding.tvCartCount.text = it.toString()
                        binding.rlCart.visibility = if (it != 0) View.VISIBLE else View.INVISIBLE
                    }
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

    private fun setRestoAdapterButtons(cart: List<CartEntity>?) {
        adapter.setItemListener(object : MenuListener {
            override fun onAddItemClicked(position: Int, menuId: String) {
                val item: CartEntity? = cart?.find { it.menuId == menuId }
                viewModel.addOrderQuantity(menuId, item)
                adapter.notifyItemChanged(position)
            }

            override fun onDecreaseItemClicked(position: Int, menuId: String) {
                val item: CartEntity? = cart?.find { it.menuId == menuId }
                viewModel.decreaseOrderQuantity(menuId, item)
                adapter.notifyItemChanged(position)
            }
        })
    }

    private fun logoutValidation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", false)
            apply()
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
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.setData(uri)
            startActivity(intent)
        }
    }

}