package com.example.restaurantreviewer.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.repositories.FoodRepository
import com.example.restaurantreviewer.repositories.RestaurantRepository

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepositoryFood: FoodRepository = FoodRepository(application)
    private val mRepositoryRestaurant: RestaurantRepository = RestaurantRepository(application)
    var mListFoodCount: Int = 0
    var mListRestaurantCount: Int = 0

    fun getFoodCount() {
        mListFoodCount = mRepositoryFood.getFoodCount()
    }

    fun getRestaurantCount() {
        mListRestaurantCount = mRepositoryRestaurant.getRestaurantCount()
    }




}