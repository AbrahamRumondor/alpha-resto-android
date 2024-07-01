package com.example.alfaresto_customersapp.domain.usecase.shipment

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Shipment
import kotlinx.coroutines.flow.StateFlow

interface ShipmentUseCase {
    suspend fun getShipments(): LiveData<List<Shipment>>
    suspend fun getShipmentById(id: String): LiveData<Shipment?>
    suspend fun createShipment(shipment: Shipment)
}