package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderAddressBinding
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener

class OrderAddressViewHolder(
    private val view: OrderAddressBinding,
    private val itemListener: OrderSummaryItemListener?,
    private val position: Int
) : RecyclerView.ViewHolder(view.root) {

    fun bind(address: Address?) {
        view.run {
            if (address != null) {
                tvAddressLabel.text = address.label
                tvAddressLocation.text = address.address
            } else {
                val noAddressLabel = "Please choose an address"
                val noAddressDesc = "we need you address to be able to perform transaction"
                tvAddressLabel.text = noAddressLabel
                tvAddressLocation.text = noAddressDesc
            }

            clAddressView.setOnClickListener {
                Log.d("test", "HI")
                itemListener?.onAddressClicked(position)
            }
        }
    }
}
