package com.example.alfaresto_customersapp.ui.components.orderSummaryPage.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.OrderSummaryPaymentMethodBinding
import com.example.alfaresto_customersapp.ui.components.listener.OrderSummaryItemListener
import com.example.alfaresto_customersapp.ui.components.orderSummaryPage.OrderSummaryFragment.Companion.COD
import com.example.alfaresto_customersapp.ui.components.orderSummaryPage.OrderSummaryFragment.Companion.GOPAY
import com.example.alfaresto_customersapp.utils.singleton.UserInfo.USER_PAYMENT_METHOD

class PaymentMethodViewHolder(
    private val view: OrderSummaryPaymentMethodBinding,
    private val itemListener: OrderSummaryItemListener?,
    private val position: Int
) : RecyclerView.ViewHolder(view.root) {

    fun bind() {
        view.run {
            if (USER_PAYMENT_METHOD != null) {
                ivIconAction.setImageResource(R.drawable.ic_down)
                rgPaymentMethod.visibility = View.VISIBLE

                when (USER_PAYMENT_METHOD) {
                    COD -> {
                        rbCod.isChecked = true
                    }

                    GOPAY -> {
                        rbGopay.isChecked = true
                    }
                }
            }

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