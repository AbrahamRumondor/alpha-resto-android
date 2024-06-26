package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alfaresto_customersapp.databinding.OrderHistoryDetailBinding

class OrderHistoryDetailFragment : Fragment() {
    private lateinit var binding: OrderHistoryDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrderHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}