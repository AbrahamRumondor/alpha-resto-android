package com.example.alfaresto_customersapp.domain.usecase.shipment

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.repository.ShipmentRepository
import javax.inject.Inject

class ShipmentUseCaseImpl @Inject constructor(
    private val shipmentRepository: ShipmentRepository
) : ShipmentUseCase {

    override suspend fun getShipments(): LiveData<List<Shipment>> {
        return shipmentRepository.getShipments()
    }

    override suspend fun getShipmentById(id: String): LiveData<Shipment?> {
        return shipmentRepository.getShipmentById(id)
    }

    override fun createShipment(shipment: Shipment) {
        shipmentRepository.createShipment(shipment)
    }
}