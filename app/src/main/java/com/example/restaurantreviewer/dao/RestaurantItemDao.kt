package com.example.restaurantreviewer.dao

import androidx.room.*
import com.example.restaurantreviewer.model.Restaurant

@Dao
interface RestaurantItemDao  {
    @Query("SELECT * FROM Restaurants ORDER BY Created Desc")
    fun getAllItems(): MutableList<Restaurant>
    @Query("SELECT * FROM Restaurants WHERE id == (:itemId)")
    fun getItemById(itemId: Int): Restaurant
    @Insert()
    fun insertItem(item: Restaurant)
    @Update()
    fun updateItem(item: Restaurant)
    @Delete()
    fun deleteItem(item: Restaurant)
}