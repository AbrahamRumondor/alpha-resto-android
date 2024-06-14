package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentOrderSummaryBinding
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Menu

class OrderSummaryFragment : Fragment() {

    private lateinit var binding: FragmentOrderSummaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            val address = Address(
                address = "Kantor",
                addressLabel = "Jl. Alam Sutera Boulevard No.Kav. 21, Pakulonan, Kec. Serpong Utara, Kota Tangerang Selatan, Banten 15325",
                addressID = "adfi90sdjaaf98uf",
                latitude = 0.0,
                longitude = 0.0
            )

            val menu1 = Menu(
                menuName = "Pizza AB",
                menuPrice = 15000,
                menuDesc = "daging cincang dan keju",
                orderCartQuantity = 2,
            )

            val menu2 = Menu(
                menuName = "Taco AB",
                menuPrice = 10000,
                menuDesc = "daging cincang dan keju",
                orderCartQuantity = 1,
            )

            val menu3 = Menu(
                menuName = "Kebab AB",
                menuPrice = 5000,
                menuDesc = "daging cincang dan keju",
                orderCartQuantity = 3,
            )

            val list = mutableListOf(menu1, menu2, menu3)

            var total = 0
            var totalItem = 0
            list.map {
                total += it.menuPrice
                totalItem++
            }

            val sendList = mutableListOf(
                address,
                menu1,
                menu2,
                menu3,
                Pair(totalItem, total),
                "payment_method",
                "checkout"
            )

            val orderAdapter = OrderSummaryAdapter(sendList)

            rvOrderSummary.adapter = orderAdapter
        }
    }
}