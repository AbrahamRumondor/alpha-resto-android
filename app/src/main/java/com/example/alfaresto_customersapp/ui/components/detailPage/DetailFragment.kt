package com.example.alfaresto_customersapp.ui.components.detailPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alfaresto_customersapp.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private var quantity = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnBack.setOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
            }

            btnAddToCart.setOnClickListener {
                btnAddToCart.visibility = View.GONE
                llQuantity.visibility = View.VISIBLE
                this@DetailFragment.quantity = 1
                tvQuantity.text = quantity.toString()
            }

            btnMinus.setOnClickListener {
                if (quantity > 1) {
                    quantity--
                    tvQuantity.text = quantity.toString()
                } else {
                    btnAddToCart.visibility = View.VISIBLE
                    llQuantity.visibility = View.GONE
                }
            }

            btnPlus.setOnClickListener {
                quantity++
                tvQuantity.text = quantity.toString()
            }
        }
    }
}
