package com.example.alfaresto_customersapp.ui.components.restoTab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding

class RestoFragment : Fragment() {

    private lateinit var binding: FragmentRestoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            btnToOrderSummary.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_restoFragment_to_orderSummaryFragment)
            }
        }
    }

}