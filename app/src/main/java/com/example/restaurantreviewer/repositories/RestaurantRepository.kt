package com.example.restaurantreviewer.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.restaurantreviewer.dao.RestaurantItemDao
import com.example.restaurantreviewer.database.DatabaseClient
import com.example.restaurantreviewer.database.RestaurantDB
import com.example.restaurantreviewer.model.Restaurant
import kotlinx.coroutines.*


class RestaurantRepository(app: Application) {
    private val db: RestaurantDB = DatabaseClient.getInstance(app.applicationContext)?.getAppDatabase()!!
    private val mRestaurantDao: RestaurantItemDao
    private val mLiveData: LiveData<MutableList<Restaurant>>

    init {
        mRestaurantDao = db.restaurantItem
        mLiveData = mRestaurantDao.getAllItems()
    }

    fun getAllRestaurants(): LiveData<MutableList<Restaurant>> {
        return mLiveData
    }

    fun getItem(id: Int): LiveData<Restaurant> {
        return mRestaurantDao.getItemById(id)
        /*var restaurant: Restaurant? = null
        GlobalScope.launch {
            restaurant = mRestaurantDao.getItemById(id)
        }
        return restaurant*/
    }

    fun insert(restaurant: Restaurant?) {
        GlobalScope.launch {
            if(restaurant != null) {
                mRestaurantDao.insertItem(restaurant)
            }
        }
    }

    fun update(restaurant: Restaurant?) {
        GlobalScope.launch {
            if(restaurant != null) {
                mRestaurantDao.updateItem(restaurant)
            }
        }
    }

    fun delete(restaurant: Restaurant?) {
        GlobalScope.launch {
            if(restaurant != null) {
                mRestaurantDao.deleteItem(restaurant)
            }
        }
    }
}