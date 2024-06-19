package com.example.alfaresto_customersapp.ui.components.restoTab.address.addressList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.AddressItemBinding
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.ui.components.listener.AddressItemListener

class AddressListAdapter(private var addresses: List<Address>, private val context: Context) :
    RecyclerView.Adapter<AddressListAdapter.AddressListViewHolder>() {

    private var addressItemListener: AddressItemListener? = null

    inner class AddressListViewHolder(private val itemBinding: AddressItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(address: Address, position: Int) {
            itemBinding.run {
                tvAddressLabel.text = address.addressLabel
                tvAddressLabel2.text = address.addressLabel
                tvAddressDetail.text = address.address

                cvAddress.strokeColor = ContextCompat.getColor(context, R.color.dark_gray)
                if (address.isSelected) {
                    cvAddress.strokeColor = ContextCompat.getColor(context, R.color.alfa_orange)
                }

                cvAddress.setOnClickListener {
                    addressItemListener?.onAddressClicked(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AddressListViewHolder(AddressItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return addresses.size
    }

    override fun onBindViewHolder(holder: AddressListViewHolder, position: Int) {
        val product = addresses[position]
        holder.bindItem(product, position)
    }

    fun updateData(newItems: List<Address>) {
        addresses = newItems
        notifyDataSetChanged()
    }

    fun setItemListener(addressItemListener: AddressItemListener) {
        this.addressItemListener = addressItemListener
    }
}