package com.example.alfaresto_customersapp.ui.components.restoTab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu.ListAllMenuFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestoFragment : Fragment() {

    private lateinit var binding: FragmentRestoBinding
    private val restoViewModel: RestoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addMenuBtn.setOnClickListener {
            lifecycleScope.launch {
                restoViewModel.insertMenu("2", 2)
                Log.d("RestoFragment", "Menu added")
            }
        }

        binding.getMenuBtn.setOnClickListener {
            lifecycleScope.launch {
                restoViewModel.getCart().observe(viewLifecycleOwner) {
                    Log.d("RestoFragment", "Cart: $it")
                }
            }
        }

        binding.allMenuBtn.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, ListAllMenuFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

}