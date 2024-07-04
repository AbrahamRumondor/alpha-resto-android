package com.example.alfaresto_customersapp.ui.components.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.auth.FirebaseAuth

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
    }

    private fun showUserInfo() {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        binding.tvEmail.text = user?.email
    }
}