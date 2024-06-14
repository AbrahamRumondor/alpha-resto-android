package com.example.alfaresto_customersapp.ui.components.restoTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.ui.components.restoTab.adapter.RestoAdapter

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

        val menuRv = binding.menuRv

        menuRv.adapter = adapter
        menuRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val menuList = viewModel.getMenuList()
        adapter.submitRestoList(menuList)
    }

}