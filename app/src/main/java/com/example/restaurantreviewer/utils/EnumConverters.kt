package com.example.restaurantreviewer.utils

import android.content.Context
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.enums.*

class EnumConverters(private val context: Context) {

    //RESTAURANT TYPE

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

    //CURRENCY

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

    //ORDER

    fun convertRestaurantOrderEnum(enumType: RestaurantOrderEnum): String {
        return when(enumType) {
            RestaurantOrderEnum.NAME -> context.getString(R.string.name)
            RestaurantOrderEnum.RATING -> context.getString(R.string.rating)
        }
    }

    fun convertRestaurantOrderString(string: String): RestaurantOrderEnum {
        return when(string) {
            context.getString(R.string.name)-> RestaurantOrderEnum.NAME
            context.getString(R.string.rating) -> RestaurantOrderEnum.RATING
            else -> RestaurantOrderEnum.NAME
        }
    }

    fun convertFoodOrderEnum(enumType: FoodOrderEnum): String {
        return when(enumType) {
            FoodOrderEnum.NAME -> context.getString(R.string.name)
            FoodOrderEnum.RATING -> context.getString(R.string.rating)
        }
    }

    fun convertFoodOrderString(string: String): FoodOrderEnum {
        return when(string) {
            context.getString(R.string.name)-> FoodOrderEnum.NAME
            context.getString(R.string.rating) -> FoodOrderEnum.RATING
            else -> FoodOrderEnum.NAME
        }
    }

    //GROUPING

    fun convertFoodGroupingEnum(enumType: FoodGroupingEnum): String {
        return when(enumType) {
            FoodGroupingEnum.DATE -> context.getString(R.string.date)
            FoodGroupingEnum.RESTAURANT -> context.getString(R.string.restaurant)
        }
    }

    fun convertFoodGroupingString(string: String): FoodGroupingEnum {
        return when(string) {
            context.getString(R.string.date) -> FoodGroupingEnum.DATE
            context.getString(R.string.restaurant) -> FoodGroupingEnum.RESTAURANT
            else -> FoodGroupingEnum.DATE
        }
    }

    fun convertRestaurantGroupingEnum(enumType: RestaurantGroupingEnum): String {
        return when(enumType) {
            RestaurantGroupingEnum.DATE -> context.getString(R.string.date)
            RestaurantGroupingEnum.TYPE -> context.getString(R.string.type)
        }
    }

    fun convertRestaurantGroupingString(string: String): RestaurantGroupingEnum {
        return when(string) {
            context.getString(R.string.date) -> RestaurantGroupingEnum.DATE
            context.getString(R.string.type) -> RestaurantGroupingEnum.TYPE
            else -> RestaurantGroupingEnum.DATE
        }
    }

    //FILTERS

    fun convertRestaurantFilterEnum(enumType: RestaurantFilterEnum) : String {
        return when(enumType) {
            RestaurantFilterEnum.NONE -> context.getString(R.string.filter_none)
            RestaurantFilterEnum.NAME -> context.getString(R.string.name)
            RestaurantFilterEnum.TYPE -> context.getString(R.string.type)
            RestaurantFilterEnum.RATING_LESS -> context.getString(R.string.filter_rating_less)
            RestaurantFilterEnum.RATING_MORE -> context.getString(R.string.filter_rating_more)
            RestaurantFilterEnum.LOCATION -> context.getString(R.string.location)
        }
    }

    fun convertRestaurantFilterString(string: String) : RestaurantFilterEnum {
        return when(string) {
            context.getString(R.string.filter_none) -> RestaurantFilterEnum.NONE
            context.getString(R.string.name) -> RestaurantFilterEnum.NAME
            context.getString(R.string.type) -> RestaurantFilterEnum.TYPE
            context.getString(R.string.filter_rating_less) -> RestaurantFilterEnum.RATING_LESS
            context.getString(R.string.filter_rating_more) -> RestaurantFilterEnum.RATING_MORE
            context.getString(R.string.location) -> RestaurantFilterEnum.LOCATION
            else -> RestaurantFilterEnum.NONE
        }
    }
}