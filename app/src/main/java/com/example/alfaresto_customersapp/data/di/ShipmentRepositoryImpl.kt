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

    override suspend fun getShipments(): LiveData<List<Shipment>> {
        try {
            val snapshot = shipmentsRef.get().await()
            val shipmentList = snapshot.toObjects(ShipmentResponse::class.java)
                .map { ShipmentResponse.transform(it) }
            _shipments.value = shipmentList

            Log.d("OrderHistory shipmentrepoimpl", "Shipments: $shipments")
        } catch (e: Exception) {
            _shipments.value = emptyList()

            Log.e("OrderHistory shipmentrepoimpl", "Error fetching shipments", e)
        }
        return shipments
    }
}