package com.example.alfaresto_customersapp.ui.components.detailFoodPage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.alfaresto_customersapp.databinding.FragmentDetailFoodBinding
import com.example.alfaresto_customersapp.data.network.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFoodFragment : Fragment() {

    private lateinit var binding: FragmentDetailFoodBinding
    private val args: DetailFoodFragmentArgs by navArgs()
    private val viewModel: DetailFoodViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.menu.observe(viewLifecycleOwner) { menu ->
            binding.apply {
                tvFoodName.text = menu.name
                tvFoodPrice.text = menu.formattedPrice()
                tvFoodDesc.text = menu.description
                Glide.with(this@DetailFoodFragment)
                    .load(menu.image)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(ivFood)
            }
        }

        viewModel.getMenuDetail(args.menuId)

        binding.apply {
            btnBack.setOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
            }
            tvRestoEmail.setOnClickListener {
                val email = tvRestoEmail.text.toString()
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                }
                startActivity(Intent.createChooser(emailIntent, "Send email"))
            }
            tvRestoPhone.setOnClickListener {
                val phoneNumber = tvRestoPhone.text.toString()
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                startActivity(dialIntent)
            }
            tvRestoAddress.setOnClickListener {
                val address = tvRestoAddress.text.toString()
                val addressIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
                }
                addressIntent.setPackage("com.google.android.apps.maps")
                startActivity(addressIntent)
            }
        }

        setConnectionBehaviour()
        binding.inclInternet.btnInetTryAgain.setOnClickListener {
            setConnectionBehaviour()
        }
    }

    private fun setConnectionBehaviour() {
        if (NetworkUtils.isConnectedToNetwork.value == false){
            binding.inclInternet.root.visibility = View.VISIBLE
            binding.clBase.visibility = View.GONE
            Toast.makeText(requireContext(), "No internet", Toast.LENGTH_SHORT).show()
        } else {
            binding.inclInternet.root.visibility = View.GONE
            binding.clBase.visibility = View.VISIBLE
        }
    }
}