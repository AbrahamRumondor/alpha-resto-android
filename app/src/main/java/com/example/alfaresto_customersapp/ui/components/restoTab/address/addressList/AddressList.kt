package com.example.alfaresto_customersapp.ui.components.restoTab.address.addressList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentAddressListBinding
import com.example.alfaresto_customersapp.ui.components.listener.AddressItemListener
import com.example.alfaresto_customersapp.utils.user.UserConstants.USER_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressList : Fragment() {

    private lateinit var binding: FragmentAddressListBinding
    private lateinit var addressAdapter: AddressListAdapter
    private val addressListViewModel: AddressListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddressAdapter()
        setButtonNewAddress()
    }

    private fun setButtonNewAddress() {
        binding.btnNewAddress.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_addressList_to_addNewAddressFragment)
        }
    }

    private fun setupAddressAdapter() {
        addressListViewModel.fetchAllAddresses("amnRLCt7iYGogz6JRxi5")
        addressAdapter =
            AddressListAdapter(addressListViewModel.userAddressFlow.value, requireContext())
        binding.rvAddressList.adapter = addressAdapter

        lifecycleScope.launch {
            addressListViewModel.userAddressFlow.collect { data ->
                addressAdapter.updateData(data)
            }
        }

        setAddressListener()
    }

    private fun setAddressListener() {
        addressAdapter.setItemListener(object : AddressItemListener {
            override fun onAddressClicked(position: Int, addressId: String) {
                addressListViewModel.setAnAddress(addressId)
                val otherAddress = addressListViewModel.updateAddress(position)
                otherAddress?.let {previousSelected->
                    addressAdapter.notifyItemChanged(previousSelected)
                }
                addressAdapter.notifyItemChanged(position)
            }
        })
    }

}