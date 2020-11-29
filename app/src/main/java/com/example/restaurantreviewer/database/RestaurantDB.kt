package com.example.restaurantreviewer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.restaurantreviewer.converters.Converters
import com.example.restaurantreviewer.dao.FoodItemDao
import com.example.restaurantreviewer.dao.RestaurantItemDao
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant

@Database(entities = [Restaurant::class, Food::class], exportSchema = false, version = 1)
@TypeConverters(Converters::class)
abstract class RestaurantDB : RoomDatabase(){
    abstract val restaurantItem : RestaurantItemDao
    abstract val foodItem: FoodItemDao
}
