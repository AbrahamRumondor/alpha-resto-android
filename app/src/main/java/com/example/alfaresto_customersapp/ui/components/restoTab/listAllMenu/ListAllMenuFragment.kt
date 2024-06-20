package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListAllMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListAllMenuAdapter()
        binding.listAllMenuRv.adapter = adapter
        binding.listAllMenuRv.layoutManager = GridLayoutManager(requireContext(), 2)

        lifecycleScope.launch {
            viewModel.menuList.collectLatest {
                adapter.submitData(it)
                Log.d("MENU", "onViewCreated: $it")
            }
        }
    }
}