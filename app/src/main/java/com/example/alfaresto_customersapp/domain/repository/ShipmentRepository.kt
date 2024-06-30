package com.example.alfaresto_customersapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Shipment

interface ShipmentRepository {
    suspend fun getShipments(): LiveData<List<Shipment>>
    suspend fun getShipmentById(id: String): LiveData<Shipment?>
    fun createShipment(shipment: Shipment)
}