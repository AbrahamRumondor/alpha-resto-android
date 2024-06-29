package com.example.alfaresto_customersapp.ui.components.restoTab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.FragmentRestoBinding
import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
import com.example.alfaresto_customersapp.ui.components.restoTab.adapter.RestoAdapter
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

        binding.rvMenu.let {
            it.adapter = adapter
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        viewModel.menus.observe(viewLifecycleOwner) { menus ->
            if (menus.isEmpty()) {
                Log.d("RestoFragment", "Menus is empty, waiting for data...")
                // Optionally, you can show a loading state or handle the empty case
                return@observe
            }

            adapter.submitMenuList(menus)
        }

        binding.btnAllMenu.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_resto_fragment_to_list_all_menu_fragment)
        }

        binding.toolbar.btnLogout.setOnClickListener {
            logoutValidation()
        }
    }

    private fun logoutValidation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", false)
            apply()
        }

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}