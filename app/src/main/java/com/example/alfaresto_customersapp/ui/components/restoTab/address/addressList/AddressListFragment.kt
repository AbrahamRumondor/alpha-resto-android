package com.example.alfaresto_customersapp.ui.components.restoTab.address.addressList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentAddressListBinding
import com.example.alfaresto_customersapp.ui.components.listener.AddressItemListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressListFragment : Fragment() {
    private lateinit var binding: FragmentAddressListBinding
    private val addressAdapter by lazy { AddressListAdapter() }
    private val addressListViewModel: AddressListViewModel by activityViewModels()

    private var hasAddress = false

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

        lifecycleScope.launch {
            addressListViewModel.isLoading.collectLatest { isLoading ->
                binding.loadingLayout.progressBar.visibility =
                    if (isLoading) View.VISIBLE else View.GONE
            }
        }

        hasNoAddress()
    }

    private fun hasNoAddress() {
        lifecycleScope.launch {
            delay(2000)
            if (!hasAddress) {
                Toast.makeText(requireContext(), "No addresses found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addressToolbar() {
        binding.apply {
            toolbar.btnLogout.visibility = View.GONE
            toolbar.btnBack.visibility = View.VISIBLE
            toolbar.btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
            toolbar.tvToolbarText.visibility = View.VISIBLE
            toolbar.ivToolbarTitle.visibility = View.GONE
        }
    }

    private fun setButtonNewAddress() {
        binding.btnNewAddress.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_address_list_to_add_new_address_fragment)
        }
    }

    private fun setupAddressAdapter() {
        binding.rvAddressList.adapter = addressAdapter

        lifecycleScope.launch {
            addressListViewModel.userAddresses.collect { data ->
                if (data.isNotEmpty()) hasAddress = true
                addressAdapter.updateData(data)
                addressAdapter.notifyItemChanged(data.size - 1)
            }
        }

        setAddressListener()
    }

    private fun setAddressListener() {
        addressAdapter.setItemListener(object : AddressItemListener {
            override fun onAddressClicked(position: Int, addressId: String) {
                addressListViewModel.setAnAddress(addressId)
                val otherAddress = addressListViewModel.updateAddress(position)
                otherAddress?.let { previousSelected ->
                    addressAdapter.notifyItemChanged(previousSelected)
                }
                addressAdapter.notifyItemChanged(position)
            }
        })
    }

}