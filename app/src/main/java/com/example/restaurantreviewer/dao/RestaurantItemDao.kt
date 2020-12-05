package com.example.restaurantreviewer.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurantreviewer.model.Restaurant

@Dao
interface RestaurantItemDao  {
    @Query("SELECT * FROM Restaurants ORDER BY Created Desc, Name Desc ")
    fun getAllItems(): LiveData<MutableList<Restaurant>>
    @Query("SELECT * FROM Restaurants WHERE Id = (:itemId)")
    fun getItemById(itemId: Int): LiveData<Restaurant>
    @Insert()
    fun insertItem(item: Restaurant)
    @Update()
    fun updateItem(item: Restaurant)
    @Delete()
    fun deleteItem(item: Restaurant)
}