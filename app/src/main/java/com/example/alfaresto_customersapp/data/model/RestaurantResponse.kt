package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Restaurant
import com.google.firebase.firestore.PropertyName

data class RestaurantResponse(
    @get:PropertyName("resto_id")
    @set:PropertyName("resto_id")
    var id: String = "",

    @get:PropertyName("resto_email")
    @set:PropertyName("resto_email")
    var email: String = "",

    @get:PropertyName("resto_address")
    @set:PropertyName("resto_address")
    var address: String = "",

    @get:PropertyName("resto_no_telp")
    @set:PropertyName("resto_no_telp")
    var noTelp: String = "",

    @get:PropertyName("resto_description")
    @set:PropertyName("resto_description")
    var description: String = "",

    @get:PropertyName("opening_time")
    @set:PropertyName("opening_time")
    var openingTime: String = "",

    @get:PropertyName("closing_time")
    @set:PropertyName("closing_time")
    var closingTime: String = "",

    @get:PropertyName("resto_image")
    @set:PropertyName("resto_image")
    var image: String = "",

    @get:PropertyName("token")
    @set:PropertyName("token")
    var token: String = "",

    @get:PropertyName("latitude")
    @set:PropertyName("latitude")
    var latitude: Double = 0.0,

    @get:PropertyName("longitude")
    @set:PropertyName("longitude")
    var longitude: Double = 0.0
) {
    companion object {
        fun transform(resto: RestaurantResponse): Restaurant {
            val newResto = Restaurant()
            return newResto.copy(
                id = resto.id,
                email = resto.email,
                address = resto.address,
                noTelp = resto.noTelp,
                description = resto.description,
                openingTime = resto.openingTime,
                closingTime = resto.closingTime,
                image = resto.image,
                token = resto.token,
                latitude = resto.latitude,
                longitude = resto.longitude
            )
        }
    }
}
