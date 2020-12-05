package com.example.restaurantreviewer.utils

import android.content.Context
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.enums.FoodCurrencyEnum
import com.example.restaurantreviewer.enums.FoodGroupingEnum
import com.example.restaurantreviewer.enums.RestaurantGroupingEnum
import com.example.restaurantreviewer.enums.RestaurantTypeEnum

class EnumConverters(private val context: Context) {

     fun convertRestaurantTypeEnum(enumType: RestaurantTypeEnum): String {
        return when(enumType) {
            RestaurantTypeEnum.UNKNOWN -> context.getString(R.string.unknown)
            RestaurantTypeEnum.RESTAURANT -> context.getString(R.string.restaurant)
            RestaurantTypeEnum.CAFE -> context.getString(R.string.cafe)
            RestaurantTypeEnum.FAST_FOOD -> context.getString(R.string.fast_food)
            RestaurantTypeEnum.PUB -> context.getString(R.string.pub)
            RestaurantTypeEnum.SWEET_SHOP -> context.getString(R.string.sweet_shop)
            RestaurantTypeEnum.BISTRO -> context.getString(R.string.bistro)
        }
    }

    fun convertRestaurantTypeString(str: String): RestaurantTypeEnum {
        return when(str) {
            context.getString(R.string.unknown) -> RestaurantTypeEnum.UNKNOWN
            context.getString(R.string.restaurant) -> RestaurantTypeEnum.RESTAURANT
            context.getString(R.string.cafe) -> RestaurantTypeEnum.CAFE
            context.getString(R.string.fast_food) -> RestaurantTypeEnum.FAST_FOOD
            context.getString(R.string.pub) -> RestaurantTypeEnum.PUB
            context.getString(R.string.sweet_shop) -> RestaurantTypeEnum.SWEET_SHOP
            context.getString(R.string.bistro) -> RestaurantTypeEnum.BISTRO
            else -> RestaurantTypeEnum.UNKNOWN
        }
    }

    fun convertFoodCurrencyString(str: String): FoodCurrencyEnum {
        return when(str) {
            context.getString(R.string.euro) -> FoodCurrencyEnum.EURO
            context.getString(R.string.dollar) -> FoodCurrencyEnum.DOLLAR
            context.getString(R.string.czech_crown) -> FoodCurrencyEnum.CZECH_CROWN
            else -> FoodCurrencyEnum.CZECH_CROWN
        }
    }

    fun convertFoodCurrencyEnum(enumType: FoodCurrencyEnum): String {
        return when(enumType) {
            FoodCurrencyEnum.EURO -> context.getString(R.string.euro)
            FoodCurrencyEnum.DOLLAR -> context.getString(R.string.dollar)
            FoodCurrencyEnum.CZECH_CROWN -> context.getString(R.string.czech_crown)
        }
    }

    fun convertFoodGroupingEnum(enumType: FoodGroupingEnum): String {
        return when(enumType) {
            FoodGroupingEnum.DATE -> context.getString(R.string.date)
            FoodGroupingEnum.RESTAURANT -> context.getString(R.string.restaurant)
        }
    }

    fun convertRestaurantGroupingEnum(enumType: RestaurantGroupingEnum): String {
        return when(enumType) {
            RestaurantGroupingEnum.DATE -> context.getString(R.string.date)
            RestaurantGroupingEnum.TYPE -> context.getString(R.string.type)
        }
    }
}