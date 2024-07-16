package com.example.alfaresto_customersapp.ui.components.mainActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.ActivityMainBinding
import com.example.alfaresto_customersapp.data.network.NetworkUtils
import com.example.alfaresto_customersapp.data.network.networkStatusObserver.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivityViewModel.observeMyShipments()

        checkConnectivityStatus()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_switch_screen) as NavHostFragment
        navController = navHostFragment.navController

        navController?.let { navController ->
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
                    R.id.order_summary_fragment -> hideBottomNav()
                    R.id.detail_food_fragment -> hideBottomNav()
                    R.id.chat_fragment -> hideBottomNav()
                    else -> showBottomNav()
                }
            }

            setBlockActionListener()
            handleIntent(intent)
            handleDeepLinkIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
        handleDeepLinkIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val orderId = intent.getStringExtra(intentOrderId)
        val shipmentId = intent.getStringExtra(intentShipmentId)

        when (intent.getStringExtra(navigateToFragment)) {
            ON_DELIVERY -> {
                val bundle = Bundle().apply {
                    putString(intentOrderId, orderId)
                    putString(intentShipmentId, shipmentId)
                }
                navController?.navigate(R.id.track_order_fragment, bundle)
            }

            ON_PROCESS -> {
                val bundle = Bundle().apply {
                    putString(intentOrderId, orderId)
                    putString(intentOrderStatus, ON_PROCESS)
                }
                navController?.navigate(R.id.order_history_detail_fragment, bundle)
            }

            DELIVERED -> {
                val bundle = Bundle().apply {
                    putString(intentOrderId, orderId)
                    putString(intentOrderStatus, DELIVERED)
                }
                navController?.navigate(R.id.order_history_detail_fragment, bundle)
            }

            CANCELLED -> {
                val bundle = Bundle().apply {
                    putString(intentOrderId, orderId)
                    putString(intentOrderStatus, CANCELLED)
                }
                navController?.navigate(R.id.order_history_detail_fragment, bundle)
            }
        }
    }

    private fun handleDeepLinkIntent(intent: Intent?) {
        val link = intent?.getStringExtra(intentLink)
        Log.d("notiv", "link: $link")
        if (link != null) {
            val newIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            navController?.handleDeepLink(newIntent)
        }
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
//                    binding.vBlockActions.visibility = View.GONE
                    NetworkUtils.setConnectionToTrue()
                } else if (it.toString() != getString(R.string.available) &&
                    NetworkUtils.isConnectedToNetwork.value != false
                ) {
//                    binding.vBlockActions.visibility = View.VISIBLE
                    NetworkUtils.setConnectionToFalse()
                }
            }
        }
    }

    private fun setBlockActionListener() {
        binding.vBlockActions.setOnClickListener {

        }
    }

    companion object {
        const val ON_DELIVERY = "On Delivery"
        const val ON_PROCESS = "On Process"
        const val DELIVERED = "Delivered"
        const val CANCELLED = "Cancelled"
        const val intentOrderId = "orderId"
        const val intentShipmentId = "shipmentId"
        const val intentOrderStatus = "orderStatus"
        const val navigateToFragment = "navigate_to_fragment"
        const val intentLink = "link"
    }
}