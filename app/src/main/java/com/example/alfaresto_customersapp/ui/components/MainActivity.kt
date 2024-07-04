package com.example.alfaresto_customersapp.ui.components

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.ActivityMainBinding
import com.example.alfaresto_customersapp.domain.network.NetworkUtils
import com.example.alfaresto_customersapp.domain.network.NetworkUtils.warningAppear
import com.example.alfaresto_customersapp.domain.network.networkStatusObserver.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkConnectivityStatus()
        connectivityStatus()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_switch_screen) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bnvCustomerNavigation.setupWithNavController(navController)

        binding.bnvCustomerNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.resto_fragment -> {
                    navController.navigate(R.id.resto_fragment)
                    true
                }

                R.id.order_history_fragment -> {
                    navController.navigate(R.id.order_history_fragment)
                    true
                }

                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Timber.tag("abcd").d(destination.id.toString())
            when (destination.id) {
                R.id.address_list -> hideBottomNav()
                R.id.add_new_address_fragment -> hideBottomNav()
                R.id.thank_you_fragment -> hideBottomNav()
                R.id.track_order_fragment -> hideBottomNav()
                R.id.order_history_detail_fragment -> hideBottomNav()
                R.id.chat_fragment -> hideBottomNav()
                R.id.detail_food_fragment -> hideBottomNav()
                R.id.chat_fragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }

        setBlockActionListener()
    }

    private fun hideBottomNav() {
        binding.bnvCustomerNavigation.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bnvCustomerNavigation.visibility = View.VISIBLE
    }

    private fun checkConnectivityStatus() {
        lifecycleScope.launch {
            connectivityObserver.observe().collectLatest {
                if (it.toString() == getString(R.string.available) &&
                    NetworkUtils.isConnectedToNetwork.value != true
                ) {
                    binding.vBlockActions.visibility = View.GONE
                    NetworkUtils.setConnectionToTrue()
                } else if (it.toString() != getString(R.string.available) &&
                    NetworkUtils.isConnectedToNetwork.value != false
                ) {
                    binding.vBlockActions.visibility = View.VISIBLE
                    NetworkUtils.setConnectionToFalse()
                }
            }
        }
    }

    private fun setBlockActionListener() {
        binding.vBlockActions.setOnClickListener {

        }
    }

    private fun connectivityStatus() {
        lifecycleScope.launch {
            delay(1000)
            NetworkUtils.isConnectedToNetwork.distinctUntilChanged()
                .observe(this@MainActivity, Observer {
                    if (!it && !warningAppear) {
                        warningAppear = true
                        showAlertDialog().setPositiveButton(R.string.retry) { _, _ ->
                            warningAppear = false
                        }?.setIcon(android.R.drawable.ic_dialog_alert)?.setOnDismissListener {
                            warningAppear = false
                            connectivityStatus()
                        }?.show()
                    }
                })
        }

    }

    fun showAlertDialog(): AlertDialog.Builder {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Connection Lost")
            .setMessage("We currently cannot connect to the internet, please click to retry.")
        return builder
    }
}