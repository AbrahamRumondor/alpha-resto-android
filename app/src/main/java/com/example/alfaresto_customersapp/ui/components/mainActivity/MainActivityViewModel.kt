package com.example.alfaresto_customersapp.ui.components.mainActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.usecase.shipment.ShipmentUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val shipmentUseCase: ShipmentUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {
    fun observeMyShipments() {
        viewModelScope.launch {
            userUseCase.getCurrentUser().collectLatest {
                shipmentUseCase.observeShipmentChanges(it)
            }
        }
    }
}