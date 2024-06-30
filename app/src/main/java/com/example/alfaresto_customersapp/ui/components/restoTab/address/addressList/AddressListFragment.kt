package com.example.alfaresto_customersapp.ui.components.restoTab.address.addressList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentAddressListBinding
import com.example.alfaresto_customersapp.ui.components.listener.AddressItemListener
import com.example.alfaresto_customersapp.utils.user.UserConstants.USER_ID
import kotlinx.coroutines.launch

class AddressListFragment : Fragment() {

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

        addressToolbar()
        setupAddressAdapter()
        setButtonNewAddress()
    }

    private fun addressToolbar() {
        binding.apply {
            toolbar.btnLogout.visibility = View.GONE
            toolbar.btnBack.visibility = View.VISIBLE
            toolbar.btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
            toolbar.tvToolbarAddress.visibility = View.VISIBLE
            toolbar.tvToolbarTitle.visibility = View.GONE
        }
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
                addressListViewModel.setAnAddress(USER_ID, addressId)
                val otherAddress = addressListViewModel.updateAddress(position)
                otherAddress?.let { previousSelected ->
                    addressAdapter.notifyItemChanged(previousSelected)
                }
                addressAdapter.notifyItemChanged(position)
            }
        })
    }

}