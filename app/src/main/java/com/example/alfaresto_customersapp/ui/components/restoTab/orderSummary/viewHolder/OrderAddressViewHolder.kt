package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder

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
                tvAddressLabel.text = address.addressLabel
                tvAddressLocation.text = address.address
            }

            clAddressView.setOnClickListener {
                itemListener?.onAddressClicked(position)
            }
        }
    }
}
