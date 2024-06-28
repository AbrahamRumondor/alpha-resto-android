package com.example.alfaresto_customersapp.ui.components.restoTab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.domain.error.FirestoreCallback
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener
import com.example.alfaresto_customersapp.ui.components.restoTab.adapter.RestoAdapter
import com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu.ListAllMenuFragment
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

//        viewModel.getToken()

        binding.rvMenu.let {
            it.adapter = adapter
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
                        binding.rvMenu.adapter = adapter
                        setRestoAdapterButtons(it)
                        adapter.submitMenuList(menus)
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
                    binding.rvMenu.adapter = adapter
                    adapter.submitMenuList(updatedMenus)
                }
            }
        }

        binding.allMenuBtn.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_restoFragment_to_listAllMenuFragment)
        }

        binding.ivIconCart.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_restoFragment_to_orderSummaryFragment)
        }
    }

    private fun setRestoAdapterButtons(cart: List<CartEntity>?) {
        adapter.setItemListener(object : MenuListener {
            override fun onAddItemClicked(position: Int, menuId: String) {
                var item: CartEntity? = null
                item = cart?.find { it.menuId == menuId }
                viewModel.addOrderQuantity(menuId, item)
                adapter.notifyItemChanged(position)
            }

            override fun onDecreaseItemClicked(position: Int, menuId: String) {
                var item: CartEntity? = null
                item = cart?.find { it.menuId == menuId }
                viewModel.decreaseOrderQuantity(menuId, item)
                adapter.notifyItemChanged(position)
            }
        })
    }

}