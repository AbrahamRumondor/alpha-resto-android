package com.example.alfaresto_customersapp.domain.usecase.shipment

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface ShipmentUseCase {
    suspend fun getShipments(): StateFlow<List<Shipment>>
    suspend fun getShipmentById(id: String): LiveData<Shipment?>
    fun observeShipmentChanges(user: User)
    suspend fun createShipment(shipment: Shipment)
}