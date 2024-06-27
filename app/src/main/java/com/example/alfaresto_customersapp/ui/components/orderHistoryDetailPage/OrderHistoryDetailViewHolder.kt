package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alfaresto_customersapp.databinding.OrderHistoryDetailBinding
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.ui.components.listener.HistoryItemListener
import com.google.firebase.firestore.FirebaseFirestore

class OrderHistoryDetailViewHolder(
    private var binding: OrderHistoryDetailBinding,
    private val itemListener: HistoryItemListener?,
    private val firestore: FirebaseFirestore
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(orderItem: OrderItem) {
        binding.run {
            val menuId = orderItem.id
            val docRef = firestore.collection("menus").document(menuId)

            docRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val imageUrl = document.getString("menu_image")
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(android.R.drawable.ic_menu_report_image)
                        .into(ivMenuImage)
                } else {
                    ivMenuImage.setImageResource(android.R.drawable.ic_menu_report_image)
                }
            }.addOnFailureListener {
                ivMenuImage.setImageResource(android.R.drawable.ic_menu_report_image)
            }
            tvMenuName.text = orderItem.menuName
            tvMenuPrice.text = orderItem.menuPrice.toString()
            tvMenuQty.text = orderItem.quantity.toString()
            tvTotal.text = (orderItem.menuPrice * orderItem.quantity).toString()
            tvOrderId.text = orderItem.id
        }
    }
}