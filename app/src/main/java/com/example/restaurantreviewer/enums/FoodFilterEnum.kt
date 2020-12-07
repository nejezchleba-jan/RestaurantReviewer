package com.example.restaurantreviewer.enums

enum class FoodFilterEnum(val code: String)  {
    NONE("None"),
    NAME("Name"),
    //PRICE_LESS("Price_less"),
    //PRICE_MORE("Price_more"),
    RESTAURANT("Restaurant"),
    RATING_LESS("Rating_less"),
    RATING_MORE("Rating_more")
}