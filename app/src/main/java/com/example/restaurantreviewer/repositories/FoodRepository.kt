package com.example.restaurantreviewer.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.restaurantreviewer.dao.FoodItemDao
import com.example.restaurantreviewer.dao.RestaurantItemDao
import com.example.restaurantreviewer.database.DatabaseClient
import com.example.restaurantreviewer.database.RestaurantDB
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant
import kotlinx.coroutines.*


class FoodRepository(app: Application) {
    private val db: RestaurantDB = DatabaseClient.getInstance(app.applicationContext)?.getAppDatabase()!!
    private val mFoodDao: FoodItemDao
    private val mRestaurantDao: RestaurantItemDao
    private val mLiveData: LiveData<MutableList<Food>>
    private val mLiveDataRestaurant: LiveData<MutableList<Restaurant>>

    init {
        mFoodDao = db.foodItem
        mRestaurantDao = db.restaurantItem
        mLiveData = mFoodDao.getAllItems()
        mLiveDataRestaurant = mRestaurantDao.getAllItemsOrdered()
    }

    fun getAllFood(): LiveData<MutableList<Food>> {
        return mLiveData
    }

    fun getFoodCount(): Int {
        return mFoodDao.getFoodCount()
    }

    fun getAllRestaurantsForFood(): LiveData<MutableList<Restaurant>> {
        return mLiveDataRestaurant
    }

    fun getItemsByRestaurant(restaurantId: Int): LiveData<MutableList<Food>> {
        return mFoodDao.getItemsByRestaurant(restaurantId)
    }

    fun getItem(id: Int): LiveData<Food> {
        return mFoodDao.getItemById(id)
    }

    fun insert(food: Food?) {
        GlobalScope.launch {
            if(food != null) {
                mFoodDao.insertItem(food)
            }
        }
    }

    fun update(food: Food?) {
        GlobalScope.launch {
            if(food != null) {
                mFoodDao.updateItem(food)
            }
        }
    }

    fun delete(food: Food?) {
        GlobalScope.launch {
            if(food != null) {
                mFoodDao.deleteItem(food)
            }
        }
    }
}