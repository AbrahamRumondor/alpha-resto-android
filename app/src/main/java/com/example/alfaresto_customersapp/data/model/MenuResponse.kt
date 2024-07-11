package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Menu
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class MenuResponse(
    val id: String = "",

    @get:PropertyName("menu_description")
    @set:PropertyName("menu_description")
    var description: String = "",

    @get:PropertyName("menu_image")
    @set:PropertyName("menu_image")
    var image: String = "",

    @get:PropertyName("menu_name")
    @set:PropertyName("menu_name")
    var name: String = "",

    @get:PropertyName("menu_price")
    @set:PropertyName("menu_price")
    var price: Int = 0,

    @get:PropertyName("menu_stock")
    @set:PropertyName("menu_stock")
    var stock: Int = 0,

    @get:PropertyName("resto_id")
    @set:PropertyName("resto_id")
    var restoId: String = "",

    @get:PropertyName("date_created")
    @set:PropertyName("date_created")
    var dateCreated: Timestamp = Timestamp.now()
) {
    // Public no-argument constructor required by Firestore
    constructor() : this("", "", "", "", 0, 0, "", Timestamp.now())

    companion object {
        fun transform(itemResponse: MenuResponse): Menu {
            return Menu(
                id = itemResponse.id,
                description = itemResponse.description,
                image = itemResponse.image,
                name = itemResponse.name,
                price = itemResponse.price,
                stock = itemResponse.stock,
                restoId = itemResponse.restoId,
                dateCreated = itemResponse.dateCreated
            )
        }
    }
}