package com.example.restaurantreviewer.utils

import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonConverters {
        private val gson: Gson = Gson()

    //TO JSON
        fun convertFoodToJson(food: Food): String {
            return gson.toJson(food, Food::class.java)
        }

        fun convertRestaurantToJson(restaurant: Restaurant): String {
            return gson.toJson(restaurant, Restaurant::class.java)
        }

        fun convertListRestaurantToJson(listRestaurant: MutableList<Restaurant>) : String {
            return gson.toJson(listRestaurant)
        }

        fun convertListFoodToJson(listFood: MutableList<Food>) : String {
            return gson.toJson(listFood)
        }

    //FROM JSON
        fun getFoodFromJson(json: String): Food {
            return gson.fromJson(json, Food::class.java)
        }

        fun getRestaurantFromJson(json: String): Restaurant {
            return gson.fromJson(json, Restaurant::class.java)
        }

        fun getListFoodFromJson(json: String): MutableList<Food> {
            val type = object : TypeToken<MutableList<Food>>() {}.type
            return gson.fromJson(json, type)
        }

        fun getListRestaurantFromJson(json: String): MutableList<Restaurant> {
            val type = object : TypeToken<MutableList<Restaurant>>() {}.type
            return gson.fromJson(json, type)
        }


}