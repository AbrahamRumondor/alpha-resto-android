package com.example.alfaresto_customersapp.ui.base

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import androidx.fragment.app.Fragment
import com.example.alfaresto_customersapp.domain.network.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {
    fun showAlertDialog(): Builder {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Connection Lost")
            .setMessage("We currently cannot connect to the internet, please click to retry.")
            .setCancelable(false)
        return builder
    }
}