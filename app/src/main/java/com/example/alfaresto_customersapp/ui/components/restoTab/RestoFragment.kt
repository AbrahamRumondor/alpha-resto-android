package com.example.alfaresto_customersapp.ui.components.restoTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu.ListAllMenuFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestoFragment : Fragment() {

    private lateinit var binding: FragmentRestoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAllMenu.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, ListAllMenuFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

}