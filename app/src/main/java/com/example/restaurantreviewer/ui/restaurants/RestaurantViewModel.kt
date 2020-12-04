package com.example.restaurantreviewer.ui.restaurants

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.repositories.RestaurantRepository
import kotlinx.coroutines.launch


class RestaurantViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: RestaurantRepository
    var mListRestaurant: LiveData<List<Restaurant>>


    init {
        mRepository = RestaurantRepository(application)
        mListRestaurant = mRepository.getAllRestaurants()
    }

    fun insert(restaurant: Restaurant?) {
        mRepository.insert(restaurant)
    }

    fun getItem(id: Int): LiveData<Restaurant> {
        return mRepository.getItem(id)
    }

    fun update(restaurant: Restaurant?) {
        mRepository.update(restaurant)
    }

    fun delete(restaurant: Restaurant?) {
        mRepository.delete(restaurant)
    }
}