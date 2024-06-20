package com.example.alfaresto_customersapp.ui.components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.alfaresto_customersapp.databinding.ActivityMainBinding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.alfaresto_customersapp.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bnvCustomerNavigation.setupWithNavController(navController)

        showUserInfo()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addressList -> hideBottomNav()
                R.id.addNewAddressFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun showUserInfo() {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        binding.email.text = user?.email
    }

    private fun hideBottomNav() {
        binding.bnvCustomerNavigation.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bnvCustomerNavigation.visibility = View.VISIBLE
    }
}