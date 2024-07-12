package com.example.alfaresto_customersapp.ui.components.listAllMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentListAllMenuBinding
import com.example.alfaresto_customersapp.data.network.NetworkUtils
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener
import com.example.alfaresto_customersapp.ui.components.listAllMenu.adapter.ListAllMenuAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListAllMenuFragment : Fragment() {
    private lateinit var binding: FragmentListAllMenuBinding
    private val viewModel: ListAllMenuViewModel by viewModels()
    private val adapter by lazy { ListAllMenuAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListAllMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupSearch()
        setupCartNavigation()
        setMenusAdapterButtons()
        loadData()

        lifecycleScope.launch {
            viewModel.cartCount.collectLatest {
                binding.tvCartCount.text = it.toString()
                binding.rlCart.visibility = if (it > 0) View.VISIBLE else View.INVISIBLE
            }
        }

        setConnectionBehaviour()
        binding.inclInternet.btnInetTryAgain.setOnClickListener {
            setConnectionBehaviour()
        }
    }

    private fun setConnectionBehaviour() {
        if (NetworkUtils.isConnectedToNetwork.value == false) {
            binding.inclInternet.root.visibility = View.VISIBLE
            binding.clBase.visibility = View.GONE
            Toast.makeText(requireContext(), "No internet", Toast.LENGTH_SHORT).show()
        } else {
            binding.inclInternet.root.visibility = View.GONE
            binding.clBase.visibility = View.VISIBLE
        }
    }

    private fun setupView() {
        binding.rvListAllMenu.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvListAllMenu.adapter = adapter
    }

    private fun setupSearch() {
        binding.svSearchMenu.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.setSearchQuery(it)
                    binding.svSearchMenu.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.setSearchQuery(null)
                }
                return true
            }
        })

        binding.svSearchMenu.setOnCloseListener {
            viewModel.setSearchQuery(null)
            false
        }
    }

    private fun setupCartNavigation() {
        binding.btnCart.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_list_all_menu_fragment_to_order_summary_fragment)
        }
    }

    private fun setMenusAdapterButtons() {
        adapter.setItemListener(object : MenuListener {
            override fun onAddItemClicked(position: Int, menuId: String) {
                if (!noInternetConnection()) {
                    viewModel.getCartByMenuId(menuId) {
                        viewModel.addOrderQuantity(menuId, it)
                        adapter.notifyItemChanged(position)
                    }
                }
            }

            override fun onDecreaseItemClicked(position: Int, menuId: String) {
                if (!noInternetConnection()) {
                    viewModel.getCartByMenuId(menuId) {
                        viewModel.decreaseOrderQuantity(it)
                        adapter.notifyItemChanged(position)
                    }
                }
            }
        })

        adapter.setItemClickListener { menu ->
            val action = ListAllMenuFragmentDirections.actionListAllMenuFragmentToDetailFragment(
                menuId = menu.id,
                name = menu.name,
                price = menu.price,
                description = menu.description,
                image = menu.image
            )
            Navigation.findNavController(requireView()).navigate(action)
        }
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

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.menuList.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}