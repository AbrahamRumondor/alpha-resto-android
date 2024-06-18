package com.example.alfaresto_customersapp.domain.dummy

import com.example.alfaresto_customersapp.domain.model.Menu

object MenuDummy {

    val menuList = listOf(
        Menu(
            menuId = "1",
            restoId = "101",
            menuName = "Cheeseburger",
            menuDesc = "A juicy beef patty with cheese, lettuce, tomato, and special sauce.",
            menuPrice = 799,
            menuStock = 50,
            menuImage = "cheeseburger.jpg",
            isSelected = false,
            orderCartQuantity = 0
        ),
        Menu(
            menuId = "2",
            restoId = "101",
            menuName = "Vegan Burger",
            menuDesc = "A delicious plant-based burger with all the fixings.",
            menuPrice = 899,
            menuStock = 30,
            menuImage = "vegan_burger.jpg",
            isSelected = false,
            orderCartQuantity = 0
        ),
        Menu(
            menuId = "3",
            restoId = "102",
            menuName = "Margherita Pizza",
            menuDesc = "Classic pizza with fresh tomatoes, mozzarella, and basil.",
            menuPrice = 1299,
            menuStock = 40,
            menuImage = "margherita_pizza.jpg",
            isSelected = false,
            orderCartQuantity = 0
        ),
        Menu(
            menuId = "4",
            restoId = "102",
            menuName = "Pepperoni Pizza",
            menuDesc = "Spicy pepperoni slices on a bed of mozzarella and tomato sauce.",
            menuPrice = 1399,
            menuStock = 35,
            menuImage = "pepperoni_pizza.jpg",
            isSelected = false,
            orderCartQuantity = 0
        ),
        Menu(
            menuId = "5",
            restoId = "103",
            menuName = "Caesar Salad",
            menuDesc = "Crisp romaine lettuce with Caesar dressing, croutons, and Parmesan cheese.",
            menuPrice = 699,
            menuStock = 25,
            menuImage = "caesar_salad.jpg",
            isSelected = false,
            orderCartQuantity = 0
        ),
        Menu(
            menuId = "6",
            restoId = "103",
            menuName = "Greek Salad",
            menuDesc = "Fresh veggies with feta cheese, olives, and a tangy vinaigrette.",
            menuPrice = 799,
            menuStock = 20,
            menuImage = "greek_salad.jpg",
            isSelected = false,
            orderCartQuantity = 0
        ),
        Menu(
            menuId = "7",
            restoId = "104",
            menuName = "Spaghetti Bolognese",
            menuDesc = "Traditional Italian pasta with a rich meat sauce.",
            menuPrice = 1099,
            menuStock = 45,
            menuImage = "spaghetti_bolognese.jpg",
            isSelected = false,
            orderCartQuantity = 0
        ),
        Menu(
            menuId = "8",
            restoId = "104",
            menuName = "Lasagna",
            menuDesc = "Layers of pasta, meat, cheese, and sauce baked to perfection.",
            menuPrice = 1199,
            menuStock = 30,
            menuImage = "lasagna.jpg",
            isSelected = false,
            orderCartQuantity = 0
        ),
        Menu(
            menuId = "9",
            restoId = "105",
            menuName = "Sushi Platter",
            menuDesc = "An assortment of fresh sushi rolls and sashimi.",
            menuPrice = 1999,
            menuStock = 20,
            menuImage = "sushi_platter.jpg",
            isSelected = false,
            orderCartQuantity = 0
        ),
        Menu(
            menuId = "10",
            restoId = "105",
            menuName = "Tempura",
            menuDesc = "Lightly battered and fried shrimp and vegetables.",
            menuPrice = 899,
            menuStock = 25,
            menuImage = "tempura.jpg",
            isSelected = false,
            orderCartQuantity = 0
        )
    )
}