package com.example.alfaresto_customersapp.ui.components.thankYouPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentThankYouBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThankYouFragment : Fragment() {

    private lateinit var binding: FragmentThankYouBinding
    private val args: ThankYouFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThankYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            val checkoutStatus = args.checkoutStatus

            tvOrderTitle.apply {
                text = if (checkoutStatus) getString(R.string.order_success) else getString(R.string.order_failed)
            }

            tvOrderBody.apply {
                text = if (checkoutStatus) getString(R.string.please_wait_order) else ""
            }

            btnProceed.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_thankYouFragment_to_restoFragment)
            }
        }
    }
}