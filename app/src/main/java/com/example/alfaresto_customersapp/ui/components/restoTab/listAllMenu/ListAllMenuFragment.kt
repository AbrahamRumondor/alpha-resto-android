package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alfaresto_customersapp.databinding.FragmentListAllMenuBinding
import com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu.adapter.ListAllMenuAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListAllMenuFragment : Fragment() {

    private lateinit var binding: FragmentListAllMenuBinding
    private val viewModel: ListAllMenuViewModel by viewModels()
    private val adapter = ListAllMenuAdapter()

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

            adapter.setItemClickListener {menu ->
                val action = ListAllMenuFragmentDirections.actionListAllMenuFragmentToFoodDetailFragment (
                    menuId = menu.id,
                    name = menu.name,
                    price = menu.price,
                    description = menu.description,
                    image = menu.image
                )
                Navigation.findNavController(requireView()).navigate(action)
            }

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
                adapter.submitData(it)
            }
        }
    }
}