package com.example.alfaresto_customersapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Shipment
import kotlinx.coroutines.flow.StateFlow

interface ShipmentRepository {
    suspend fun getShipments(): StateFlow<List<Shipment>>
    suspend fun getShipmentById(id: String): LiveData<Shipment?>
    suspend fun createShipment(shipment: Shipment)
    suspend fun getShipmentByOrderId(orderId: String): Shipment?
}