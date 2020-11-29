package com.example.restaurantreviewer.dao

import androidx.room.*
import com.example.restaurantreviewer.model.Food

@Dao
interface FoodItemDao  {
    @Query("SELECT * FROM Food ORDER BY Created Desc")
    fun getAllItems(): MutableList<Food>
    @Query("SELECT * FROM Food WHERE id == (:itemId)")
    fun getItemById(itemId: Int): Food
    @Insert()
    fun insertItem(item: Food)
    @Update()
    fun updateItem(item: Food)
    @Delete()
    fun deleteItem(item: Food)
}