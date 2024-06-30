package com.example.alfaresto_customersapp.domain.usecase.shipment

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Shipment

interface ShipmentUseCase {
    suspend fun getShipments(): LiveData<List<Shipment>>
    suspend fun getShipmentById(id: String): LiveData<Shipment?>
    fun createShipment(shipment: Shipment)
}