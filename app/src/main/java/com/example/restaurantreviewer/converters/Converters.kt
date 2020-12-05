package com.example.restaurantreviewer.converters

import androidx.room.TypeConverter
import com.example.restaurantreviewer.enums.FoodCurrencyEnum
import com.example.restaurantreviewer.enums.RestaurantTypeEnum
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Converters {
    companion object {
        //DATE CONVERTER
        @TypeConverter
        @JvmStatic
        fun datefromString(stringDate: String): LocalDate {
            return LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE)
        }

        @TypeConverter
        @JvmStatic
        fun dateToString(date: LocalDate): String {
            return date.toString()
        }

        //DATE CONVERTER
        @TypeConverter
        @JvmStatic
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }

        @TypeConverter
        @JvmStatic
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time
        }

        //RESTAURANT ENUM CONVERTER
        @TypeConverter
        @JvmStatic
        fun fromInteger(value: Int): RestaurantTypeEnum {
            return when (value) {
                0 -> RestaurantTypeEnum.RESTAURANT
                1 -> RestaurantTypeEnum.CAFE
                2 -> RestaurantTypeEnum.FAST_FOOD
                3 -> RestaurantTypeEnum.PUB
                4 -> RestaurantTypeEnum.SWEET_SHOP
                5 -> RestaurantTypeEnum.BISTRO
                else ->  RestaurantTypeEnum.UNKNOWN
            }
        }

        @TypeConverter
        @JvmStatic
        fun typeToInteger(type: RestaurantTypeEnum): Int {
            return type.code
        }

        //FOOD CURRENCY ENUM CONVERTER
        @TypeConverter
        @JvmStatic
        fun fromString(value: String): FoodCurrencyEnum {
            return when (value) {
                "$" -> FoodCurrencyEnum.DOLLAR
                "€" -> FoodCurrencyEnum.EURO
                "Kč" -> FoodCurrencyEnum.CZECH_CROWN
                else  -> FoodCurrencyEnum.CZECH_CROWN
            }
        }

        @TypeConverter
        @JvmStatic
        fun currencyToString(currency: FoodCurrencyEnum): String {
            return currency.code
        }
    }
}