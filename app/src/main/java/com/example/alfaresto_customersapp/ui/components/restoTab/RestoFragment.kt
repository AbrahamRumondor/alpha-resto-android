package com.example.alfaresto_customersapp.ui.components.restoTab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.ui.components.restoTab.adapter.RestoAdapter
import com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu.ListAllMenuFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestoFragment : Fragment() {

    private lateinit var binding: FragmentRestoBinding
    private val viewModel: RestoViewModel by viewModels()
    private val adapter by lazy { RestoAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.allMenuBtn.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, ListAllMenuFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        viewModel.menus.observe(viewLifecycleOwner) { menus ->
            if (menus.isEmpty()) {
                Log.d("RestoFragment", "Menus is empty, waiting for data...")
                // Optionally, you can show a loading state or handle the empty case
                return@observe
            }

            adapter.submitMenuList(menus)
            binding.rvMenu.let {
                it.adapter = adapter
                it.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }
}