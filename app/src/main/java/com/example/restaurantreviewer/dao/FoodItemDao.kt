package com.example.restaurantreviewer.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant

@Dao
interface FoodItemDao  {
    @Query("SELECT * FROM Food ORDER BY Created Desc")
    fun getAllItems(): LiveData<MutableList<Food>>
    @Query("SELECT * FROM Food WHERE id == (:itemId)")
    fun getItemById(itemId: Int): LiveData<Food>
    @Query("SELECT * FROM Food WHERE RestaurantId = (:restaurantId)")
    fun getItemsByRestaurant(restaurantId: Int): LiveData<MutableList<Food>>
    @Insert()
    fun insertItem(item: Food)
    @Update()
    fun updateItem(item: Food)
    @Delete()
    fun deleteItem(item: Food)

    @Query("SELECT count(*) FROM Food")
    fun getFoodCount(): Int
}