package com.example.restaurantreviewer.ui.restaurants

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.repositories.RestaurantRepository


class RestaurantViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: RestaurantRepository = RestaurantRepository(application)
    private val mListRestaurant: List<Restaurant>

    init {
        mListRestaurant = mRepository.mRestaurantList
    }

    fun getAllRestaurants(): List<Restaurant> {
        return mListRestaurant
    }

    fun insert(restaurant: Restaurant?) {
        mRepository.insert(restaurant)
    }

    fun getItem(id: Int): Restaurant? {
        return mRepository.getItem(id)
    }

    fun update(restaurant: Restaurant?) {
        mRepository.update(restaurant)
    }

    fun delete(restaurant: Restaurant?) {
        mRepository.delete(restaurant)
    }
}