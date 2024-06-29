package com.example.alfaresto_customersapp.data.di

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alfaresto_customersapp.data.model.ShipmentResponse
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.repository.ShipmentRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class ShipmentRepositoryImpl @Inject constructor(
    @Named("shipmentsRef") private val shipmentsRef: CollectionReference
) : ShipmentRepository {

    private val _shipments = MutableLiveData<List<Shipment>>(emptyList())
    private val shipments: LiveData<List<Shipment>> = _shipments

    private val _shipment = MutableLiveData<Shipment?>(null)
    private val shipment: LiveData<Shipment?> = _shipment

    override suspend fun getShipments(): LiveData<List<Shipment>> {
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
        // Add a snapshot listener to the document
        shipmentsRef.document("E5eUgeleoiZxJsH3WfYB")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching document: $error")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val shipmentResponse = snapshot.toObject(ShipmentResponse::class.java)
                    Log.d("Firestore", "Snapshot received: $shipmentResponse")
                    shipmentResponse?.let {
                        val shipment = ShipmentResponse.transform(it)
                        _shipment.value = shipment
                    }
                } else {
                    Log.d("Firestore", "Current data: null") }
            }

        return shipment
    }


}