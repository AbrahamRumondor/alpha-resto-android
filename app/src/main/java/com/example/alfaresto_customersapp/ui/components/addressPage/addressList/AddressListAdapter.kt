package com.example.alfaresto_customersapp.ui.components.addressPage.addressList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.AddressItemBinding
import com.example.alfaresto_customersapp.databinding.FragmentAddressListBinding
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.ui.components.listener.AddressItemListener
import com.example.alfaresto_customersapp.utils.singleton.UserInfo

class AddressListAdapter(
    private val addressBinding: FragmentAddressListBinding
) : RecyclerView.Adapter<AddressListAdapter.AddressListViewHolder>() {

    private var addresses: List<Address> = listOf()
    private var addressItemListener: AddressItemListener? = null

    inner class AddressListViewHolder(private val itemBinding: AddressItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(address: Address, position: Int) {
            itemBinding.run {
                tvAddressLabel2.text = address.label
                tvAddressDetail.text = address.address

                cvAddress.strokeColor = root.context.getColor(R.color.dark_gray)
                if (address.isSelected) {
                    cvAddress.strokeColor = root.context.getColor(R.color.alfa_orange)
                }

                cvAddress.setOnClickListener {
                    UserInfo.USER_ADDRESS = address
                    addressItemListener?.onAddressClicked(position, addressId = address.id)
                    chooseAddress()
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
        notifyItemRangeChanged(0, newItems.size)
    }

    fun chooseAddress() {
        addressBinding.btnChooseAddress.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                it.findNavController().popBackStack()
            }
        }
    }

    fun setItemListener(addressItemListener: AddressItemListener) {
        this.addressItemListener = addressItemListener
    }
}