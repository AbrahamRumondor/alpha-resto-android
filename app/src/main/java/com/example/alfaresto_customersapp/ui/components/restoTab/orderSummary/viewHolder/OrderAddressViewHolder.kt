package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderAddressBinding
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.ui.components.listener.ItemListener

class OrderAddressViewHolder(
    private val view: OrderAddressBinding,
    private val onAddressClicked: ItemListener?
) : RecyclerView.ViewHolder(view.root) {

    fun bind(address: Address?) {
        view.run {
            if (address != null) {
                tvAddressLabel.text = address.addressLabel
                tvAddressLocation.text = address.address
            }
        }
    }
}
