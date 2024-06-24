package com.example.alfaresto_customersapp.ui.components.restoTab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.ui.components.restoTab.adapter.RestoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestoFragment : Fragment() {
    private lateinit var binding: FragmentRestoBinding
    private val viewModel: RestoViewModel by viewModels()
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

        val rvMenu = binding.rvMenu

        lifecycleScope.launch {
            viewModel.menus.collect { menus ->
                if (menus.isEmpty()) {
                    Log.d("MENU", "Menus is empty, waiting for data...")
                    // Optionally, you can show a loading state or handle the empty case
                    return@collect
                }

                adapter.submitMenuList(menus)

                rvMenu.adapter = adapter
                rvMenu.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }
}