package com.example.alfaresto_customersapp.data.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alfaresto_customersapp.data.model.ShipmentResponse
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.repository.ShipmentRepository
import com.example.alfaresto_customersapp.ui.service.NotificationForegroundService
import com.example.alfaresto_customersapp.utils.singleton.UserInfo
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
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
                    UserInfo.SHIPMENT.postValue(Shipment().copy(statusDelivery = "On Process"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val shipmentResponse = snapshot.toObject(ShipmentResponse::class.java)
                    shipmentResponse?.let {
                        val shipment = ShipmentResponse.transform(it)
                        _shipment.value = shipment
                        UserInfo.SHIPMENT.postValue(shipment)
//                        startForegroundService(shipmentId = shipment.id, orderId = shipment.orderID, orderStatus = shipment.statusDelivery)
                    }
                } else {
                    _shipment.value = Shipment().copy(statusDelivery = "On Process")
                    UserInfo.SHIPMENT.postValue(Shipment().copy(statusDelivery = "On Process"))
                }
            }

        return shipment
    }

    override fun observeShipmentChanges(user: User) {
        listenerRegistration = shipmentsRef.addSnapshotListener { snapshots, e ->
            if (e != null) {
                // Handle the error
                return@addSnapshotListener
            }

            for (docChange in snapshots!!.documentChanges) {
                when (docChange.type) {
                    DocumentChange.Type.ADDED -> {
                        // A new document was added
                        val newDoc = docChange.document
                    }

                    DocumentChange.Type.MODIFIED -> {
                        // An existing document was modified
                        val modifiedDoc = docChange.document
                        val shipmentResponse = modifiedDoc.toObject(ShipmentResponse::class.java)
                        val shipment = ShipmentResponse.transform(shipmentResponse)
                        UserInfo.SHIPMENT.postValue(shipment)

                        notifyUser(shipment, user)
                    }

                    DocumentChange.Type.REMOVED -> {
                        // An existing document was removed
                        val removedDoc = docChange.document
                    }
                }
            }
        }
    }

    private fun notifyUser(shipment: Shipment, user: User) {
        Timber.tag("notiv").d("$shipment dan $user")
        if (shipment.userId == user.id) {
            startForegroundService(
                shipmentId = shipment.id,
                orderId = shipment.orderID,
                orderStatus = shipment.statusDelivery
            )
        }
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
        startForegroundService(
            shipmentId = shipment.id,
            orderId = shipment.orderID,
            orderStatus = shipment.statusDelivery
        )
    }

    private fun generateShipmentId(): String {
        return shipmentsRef.document().id
    }

    private fun startForegroundService(orderId: String, shipmentId: String, orderStatus: String) {
        val serviceIntent = Intent(context, NotificationForegroundService::class.java)
        serviceIntent.putExtra("orderId", orderId)
        serviceIntent.putExtra("shipmentId", shipmentId)
        serviceIntent.putExtra("orderStatus", orderStatus)

        ContextCompat.startForegroundService(context, serviceIntent)
    }

    override suspend fun getShipmentByOrderId(orderId: String): Shipment? {

        return getShipments().value.find { it.orderID == orderId }
    }
}