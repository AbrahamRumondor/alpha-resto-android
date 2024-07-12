package com.example.alfaresto_customersapp.ui.components.orderSummaryPage.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderSummaryAddressBinding
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener

class OrderAddressViewHolder(
    private val view: OrderSummaryAddressBinding,
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
                val noAddressDesc = "We need you address to be able to \nperform transaction"
                tvAddressLabel.text = noAddressLabel
                tvAddressLocation.text = noAddressDesc
            }

            clAddressView.setOnClickListener {
                itemListener?.onAddressClicked(position)
            }
        }
    }
}
