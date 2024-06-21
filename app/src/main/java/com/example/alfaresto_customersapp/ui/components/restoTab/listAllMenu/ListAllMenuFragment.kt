package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alfaresto_customersapp.databinding.FragmentListAllMenuBinding
import com.example.alfaresto_customersapp.ui.components.listener.MenuListener
import com.example.alfaresto_customersapp.ui.components.restoTab.adapter.RestoAdapter
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

        binding.listAllMenuRv.adapter = adapter
        setMenusAdapterButtons()
        binding.listAllMenuRv.layoutManager = GridLayoutManager(requireContext(), 2)

        lifecycleScope.launch {
            viewModel.menuList.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setMenusAdapterButtons() {
        adapter.setItemListener(object : MenuListener {
            override fun onAddItemClicked(position: Int,  menuId: String) {

            }

            override fun onDecreaseItemClicked(position: Int, menuId: String) {

            }
        })
    }
}