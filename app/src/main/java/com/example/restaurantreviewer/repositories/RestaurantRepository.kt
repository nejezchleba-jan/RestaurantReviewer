package com.example.restaurantreviewer.repositories

import android.app.Application
import com.example.restaurantreviewer.dao.RestaurantItemDao
import com.example.restaurantreviewer.database.DatabaseClient
import com.example.restaurantreviewer.database.RestaurantDB
import com.example.restaurantreviewer.model.Restaurant
import kotlinx.coroutines.*


class RestaurantRepository(
    private val app: Application
    ) {
    private val db: RestaurantDB = DatabaseClient.getInstance(app.applicationContext)?.getAppDatabase()!!
    private val mRestaurantDao: RestaurantItemDao
    lateinit var mRestaurantList: List<Restaurant>

    init {
        mRestaurantDao = db.restaurantItem
        getAllRestaurants()
    }

    fun getAllRestaurants() {
        GlobalScope.launch(Dispatchers.Main) {
            mRestaurantList = mRestaurantDao.getAllItems()
        }
    }


    fun getItem(id: Int): Restaurant? {
        var restaurant: Restaurant? = null
        runBlocking {
            GlobalScope.async {
                restaurant = mRestaurantDao.getItemById(id)
            }
        }
        return restaurant;
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