package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentListAllMenuBinding
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener
import com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu.adapter.ListAllMenuAdapter
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

        binding.apply {
            svSearchMenu.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

            svSearchMenu.setOnCloseListener {
                viewModel.setSearchQuery(null)
                false
            }
        }

        loadData()
    }

    private fun setupView() {
        binding.rvListAllMenu.let {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.menuList.collectLatest {
                Log.d("test", it.toString())
                adapter.submitData(it)
            }
        }

        binding.rvListAllMenu.adapter = adapter
        setMenusAdapterButtons()

        binding.btnCart.setOnClickListener {
            Log.d("listall", "cart clicked")
            Navigation.findNavController(requireView())
                .navigate(R.id.action_listAllMenuFragment_to_orderSummaryFragment)
        }
    }

    private fun setMenusAdapterButtons() {
        adapter.setItemListener(object : MenuListener {
            override fun onAddItemClicked(position: Int, menuId: String) {
                viewModel.getCartByMenuId(menuId) {
                    viewModel.addOrderQuantity(menuId, it)
                    adapter.notifyItemChanged(position)
                }
            }

            override fun onDecreaseItemClicked(position: Int, menuId: String) {
                viewModel.getCartByMenuId(menuId) {
                    viewModel.decreaseOrderQuantity(menuId, it)
                    adapter.notifyItemChanged(position)
                }
            }
        })
    }
}