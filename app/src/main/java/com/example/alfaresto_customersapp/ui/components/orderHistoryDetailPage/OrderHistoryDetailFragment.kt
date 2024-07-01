package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.databinding.OrderHistoryDetailBinding
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.OrderSummaryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderHistoryDetailFragment : Fragment() {

    private lateinit var binding: OrderHistoryDetailBinding
    private val args: OrderHistoryDetailFragmentArgs by navArgs()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrderHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = auth.currentUser
        val orderID = args.orderId

        val userId = user?.uid.toString()
        Log.d("OrderHistoryDetail", "User ID: $userId")

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        fetchOrderDetails(orderID)
    }

    private fun fetchOrderDetails(orderID: String) {
        val orderRef = FirebaseFirestore.getInstance().collection("orders").document(orderID)

        orderRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val orderDate = document.getDate("order_date")
                val totalPrice = document.getDouble("total_price")
                val address = document.getString("full_address")
                val userName = document.getString("user_name")
                val userPhone = document.getString("user_phone")
                val formattedTotalPrice = String.format("Rp ", totalPrice)

                if (orderDate != null && totalPrice != null) {
                    binding.apply {
                        tvUserName.text = userName
                        tvUserPhone.text = userPhone
                        tvOrderId.text = orderID
                        tvOrderDate.text = orderDate.toString()
                        tvTotalPrice.text = formattedTotalPrice
                        tvUserAddress.text = address

                        rvOrderItems.layoutManager = LinearLayoutManager(requireContext())
                    }
                    fetchOrderItems(orderID)
                    Log.d("OrderHistoryDetail", "Username: ${userName}")
                    Log.d("OrderHistoryDetail", "User phone: ${userPhone}")
                    Log.d("OrderHistoryDetail", "Order id: ${orderID} fetched successfully")
                    Log.d("OrderHistoryDetail", "Order date: ${orderDate}")
                    Log.d("OrderHistoryDetail", "Order total price: ${totalPrice}")
                    Log.d("OrderHistoryDetail", "Order full address: ${address}")
                } else {
                    Log.e("OrderHistoryDetail", "Order document does not exist")
                }
            } else {
                Log.e("OrderHistoryDetail", "Failed to fetch order document")
            }
        }.addOnFailureListener { e ->
            Log.e("OrderHistoryDetail", "Error fetching order document", e)
        }
    }

    private fun fetchOrderItems(orderID: String) {
        val orderItemsRef = FirebaseFirestore.getInstance().collection("orders")
            .document(orderID).collection("order_items")

        orderItemsRef.get().addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val orderItems = documents.map { doc ->
                    val orderItemId = doc.getString("order_item_id") ?: ""
                    val menuName = doc.getString("menu_name") ?: ""
                    val menuPrice = doc.getDouble("menu_price") ?: 0.0
                    val quantity = doc.getLong("quantity")?.toInt() ?: 0
                    val formattedMenuPrice = menuPrice.toInt()

                    OrderItem(orderItemId, menuName, formattedMenuPrice, quantity)
                }

                fetchMenuDetails(orderItems)
            } else {
                Log.e("OrderHistoryDetail", "No order items found")
            }
        }.addOnFailureListener { e ->
            Log.e("OrderHistoryDetail", "Error fetching order items", e)
        }
    }


    private fun fetchMenuDetails(orderItems: List<OrderItem>) {
        val menuRef = FirebaseFirestore.getInstance().collection("menus")
        val itemsWithMenuDetails = mutableListOf<Pair<OrderItem, Menu>>()

        menuRef.whereEqualTo("menu_name", orderItems.get(0).menuName).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val menu = documents.first().toObject(Menu::class.java)
                    itemsWithMenuDetails.add(orderItems.get(0) to menu)
                    if (itemsWithMenuDetails.size == orderItems.size) {
                        binding.rvOrderItems.adapter = OrderItemsAdapter(itemsWithMenuDetails)
                    }
                } else {
                    Log.e(
                        "OrderHistoryDetail",
                        "Menu item not found for ${orderItems.get(0).menuName}"
                    )
                }
            }.addOnFailureListener { e ->
                Log.e("OrderHistoryDetail", "Error fetching menu item", e)
            }
    }
}