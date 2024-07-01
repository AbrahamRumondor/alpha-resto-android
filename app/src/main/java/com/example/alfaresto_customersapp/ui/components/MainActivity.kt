package com.example.alfaresto_customersapp.ui.components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.alfaresto_customersapp.databinding.ActivityMainBinding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.alfaresto_customersapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bnvCustomerNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.address_list -> hideBottomNav()
                R.id.add_new_address_fragment -> hideBottomNav()
                R.id.thank_you_fragment -> hideBottomNav()
                R.id.track_order_fragment -> hideBottomNav()
                R.id.detail_fragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun hideBottomNav() {
        binding.bnvCustomerNavigation.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bnvCustomerNavigation.visibility = View.VISIBLE
    }
}