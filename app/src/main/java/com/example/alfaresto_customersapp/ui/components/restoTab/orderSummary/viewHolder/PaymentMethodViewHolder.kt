package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.databinding.OrderSummaryPaymentMethodBinding
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener

class PaymentMethodViewHolder (
    private val view: OrderSummaryPaymentMethodBinding,
    private val itemListener: OrderSummaryItemListener?,
    private val position: Int
) : RecyclerView.ViewHolder(view.root) {

    fun bind() {
        view.run {
            ivIconAction.setOnClickListener {
                itemListener?.onPaymentMethodClicked(this)
            }

            rgPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
                itemListener?.onRadioButtonClicked(position, checkedId)
            }

            cvPaymentMethod.setOnClickListener {
                itemListener?.onPaymentMethodClicked(this)
            }
        }
    }
}