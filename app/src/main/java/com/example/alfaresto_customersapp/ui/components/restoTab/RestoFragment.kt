package com.example.alfaresto_customersapp.ui.components.restoTab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.domain.error.FirestoreCallback
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener
import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
import com.example.alfaresto_customersapp.ui.components.restoTab.adapter.RestoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestoFragment : Fragment() {
    private lateinit var binding: FragmentRestoBinding
    private val viewModel: RestoViewModel by activityViewModels()
    private val adapter by lazy { RestoAdapter() }

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
                binding.loadingLayout.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
            viewModel.menus.collect { menus ->
                if (menus.isEmpty()) {
                    Log.d("MENU", "Menus is empty, waiting for data...")
                    return@collect
                }
                viewModel.cart.collectLatest {
                    if (it.isEmpty()) {
                        Log.d("test", "NO DATA")
                        setRestoAdapterButtons(it)
                        adapter.submitMenuList(menus)
                        adapter.notifyItemChanged(menus.size - 1)

                        return@collectLatest
                    }

                    val updatedMenus = menus.map { menu ->
                        val cartItem = it.find { cart -> cart.menuId == menu.id }
                        if (cartItem != null) {
                            menu.copy(orderCartQuantity = cartItem.menuQty)
                        } else {
                            menu
                        }
                    }

                    setRestoAdapterButtons(it)
                    adapter.submitMenuList(updatedMenus)
                    adapter.notifyItemChanged(updatedMenus.size - 1)
                }
            }
        }

        binding.btnAllMenu.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_restoFragment_to_listAllMenuFragment)
        }

        binding.btnCart.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_restoFragment_to_orderSummaryFragment)
        }

        binding.toolbar.btnLogout.setOnClickListener {
            logoutValidation()
        }
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
}