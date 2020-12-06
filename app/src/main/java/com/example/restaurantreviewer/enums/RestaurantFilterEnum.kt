package com.example.restaurantreviewer.enums

enum class RestaurantFilterEnum(val code: String) {
    NONE("None"),
    NAME("Name"),
    RATING_LESS("Rating_less"),
    RATING_MORE("Rating_more"),
    TYPE("Type"),
    LOCATION("Location")
}