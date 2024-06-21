package com.example.alfaresto_customersapp.ui.components.orderHistoryTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.databinding.FragmentOrderHistoryBinding
import com.example.alfaresto_customersapp.domain.dummy.orderList.orders

class OrderHistoryFragment : Fragment() {

    private lateinit var binding: FragmentOrderHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OrderHistoryAdapter()

        binding.orderHistoryRv.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        adapter.submitList(orders)
    }

}