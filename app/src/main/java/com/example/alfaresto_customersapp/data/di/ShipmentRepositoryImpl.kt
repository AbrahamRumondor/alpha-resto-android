package com.example.alfaresto_customersapp.data.di

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
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
import kotlinx.coroutines.tasks.await
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
        try {
            val snapshot = shipmentsRef.get().await()
            val shipmentList = snapshot.toObjects(ShipmentResponse::class.java)
                .map { ShipmentResponse.transform(it) }
            _shipments.value = shipmentList
        } catch (e: Exception) {
            _shipments.value = emptyList()
        }
        return shipments
    }

    override suspend fun getShipmentById(id: String): LiveData<Shipment?> {
        listenerRegistration = shipmentsRef.document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching document: $error")
                    _shipment.value = Shipment().copy(statusDelivery = "On Process")
                    UserConstants.SHIPMENT_STATUS = "On Process"
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val shipmentResponse = snapshot.toObject(ShipmentResponse::class.java)
                    Log.d("Firestore", "Snapshot received: $shipmentResponse")
                    shipmentResponse?.let {
                        val shipment = ShipmentResponse.transform(it)
                        _shipment.value = shipment
                        UserConstants.SHIPMENT_STATUS = shipment.statusDelivery
                        startForegroundService()
                    }
                } else {
                    Log.d("Firestore", "Current data: null")
                    _shipment.value = Shipment().copy(statusDelivery = "On Process")
                    UserConstants.SHIPMENT_STATUS = "On Process"

                }
            }

        return shipment
    }

    override fun createShipment(shipment: Shipment) {
        val newShipmentId = generateShipmentId()
        shipmentsRef.document(newShipmentId).set(
            ShipmentResponse.transform(shipment.copy(id = newShipmentId))
        ).addOnSuccessListener {
            Log.d(
                "TEST",
                "SUCCESS ON ORDER INSERTION"
            )
        }
            .addOnFailureListener { Log.d("TEST", "ERROR ON ORDER INSERTION") }
    }

    private fun generateShipmentId(): String {
        return shipmentsRef.document().id
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(context, NotificationForegroundService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
    }

    override suspend fun getShipmentByOrderId(orderId: String): Shipment? {
        val myShipment = getShipments().value.find { it.orderID == orderId }

        return myShipment
    }
}