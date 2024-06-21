package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Menu
import com.google.firebase.firestore.PropertyName

data class MenuResponse(
    val id: String = "",

    @get:PropertyName("menu_description")
    @set:PropertyName("menu_description")
    var menuDescription: String = "",

    @get:PropertyName("menu_image")
    @set:PropertyName("menu_image")
    var menuImage: String = "",

    @get:PropertyName("menu_name")
    @set:PropertyName("menu_name")
    var menuName: String = "",

    @get:PropertyName("menu_price")
    @set:PropertyName("menu_price")
    var menuPrice: Int = 0,

    @get:PropertyName("menu_stock")
    @set:PropertyName("menu_stock")
    var menuStock: Int = 0,

    @get:PropertyName("resto_id")
    @set:PropertyName("resto_id")
    var restoId: String = ""
) {
    // Public no-argument constructor required by Firestore
    constructor() : this("", "", "", "", 0, 0, "")

    companion object {
        fun transform(itemResponse: MenuResponse): Menu {
            return Menu(
                id = itemResponse.id,
                description = itemResponse.menuDescription,
                image = itemResponse.menuImage,
                name = itemResponse.menuName,
                price = itemResponse.menuPrice,
                stock = itemResponse.menuStock,
                restoId = itemResponse.restoId
            )
        }
    }
}