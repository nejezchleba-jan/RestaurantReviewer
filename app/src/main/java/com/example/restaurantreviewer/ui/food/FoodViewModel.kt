package com.example.restaurantreviewer.ui.food

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.repositories.FoodRepository
import com.example.restaurantreviewer.repositories.RestaurantRepository
import kotlinx.coroutines.launch


class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: FoodRepository = FoodRepository(application)
    var mListFood: LiveData<MutableList<Food>>
    var mListRestaurant: LiveData<MutableList<Restaurant>>

    init {
        mListFood = mRepository.getAllFood()
        mListRestaurant = mRepository.getAllRestaurantsForFood()
    }

    fun insert(food: Food?) {
        mRepository.insert(food)
    }

    fun getItem(id: Int): LiveData<Food> {
        return mRepository.getItem(id)
    }

    fun getItemsByRestaurant(restaurantId: Int): LiveData<MutableList<Food>> {
        return mRepository.getItemsByRestaurant(restaurantId)
    }

    fun update(food: Food?) {
        mRepository.update(food)
    }

    fun delete(food: Food?) {
        mRepository.delete(food)
    }
}