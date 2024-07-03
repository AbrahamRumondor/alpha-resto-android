package com.example.alfaresto_customersapp.data.di

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alfaresto_customersapp.data.model.ShipmentResponse
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.repository.ShipmentRepository
import com.example.alfaresto_customersapp.domain.service.NotificationForegroundService
import com.example.alfaresto_customersapp.utils.user.UserConstants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class ShipmentRepositoryImpl @Inject constructor(
    @Named("shipmentsRef") private val shipmentsRef: CollectionReference,
    private val context: Context
) : ShipmentRepository {

    private val _shipments = MutableStateFlow<List<Shipment>>(emptyList())
    private val shipments: StateFlow<List<Shipment>> = _shipments

    private val _shipment = MutableLiveData(Shipment().copy(statusDelivery = "On Process"))
    private val shipment: LiveData<Shipment?> = _shipment

    private var listenerRegistration: ListenerRegistration? = null

    override suspend fun getShipments(): StateFlow<List<Shipment>> {
        shipmentsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                _shipments.value = emptyList()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val shipmentList = snapshot.toObjects(ShipmentResponse::class.java)
                    .map { ShipmentResponse.transform(it) }
                _shipments.value = shipmentList
            } else {
                _shipments.value = emptyList()
            }
        }
        return shipments
    }

    override suspend fun getShipmentById(id: String): LiveData<Shipment?> {
        listenerRegistration = shipmentsRef.document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _shipment.value = Shipment().copy(statusDelivery = "On Process")
                    UserConstants.SHIPMENT_STATUS = "On Process"
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val shipmentResponse = snapshot.toObject(ShipmentResponse::class.java)
                    shipmentResponse?.let {
                        val shipment = ShipmentResponse.transform(it)
                        _shipment.value = shipment
                        UserConstants.SHIPMENT_STATUS = shipment.statusDelivery
                        startForegroundService()
                    }
                } else {
                    _shipment.value = Shipment().copy(statusDelivery = "On Process")
                    UserConstants.SHIPMENT_STATUS = "On Process"

                }
            }

        return shipment
    }

    override suspend fun createShipment(shipment: Shipment) {
        val newShipmentId = generateShipmentId()
        shipmentsRef.document(newShipmentId).set(
            ShipmentResponse.transform(shipment.copy(id = newShipmentId))
        ).addOnSuccessListener {
            Timber.tag("TEST").d("SUCCESS ON ORDER INSERTION")
        }
            .addOnFailureListener { Timber.tag("TEST").d("ERROR ON ORDER INSERTION") }

        getShipmentById(newShipmentId)
        startForegroundService()
    }

    private fun generateShipmentId(): String {
        return shipmentsRef.document().id
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(context, NotificationForegroundService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
    }

    override suspend fun getShipmentByOrderId(orderId: String): Shipment? {

        return getShipments().value.find { it.orderID == orderId }
    }
}