package com.example.restaurantreviewer.database

import android.content.Context
import androidx.room.Room

class DatabaseClient private constructor(mCtx: Context) {
    private val mCtx: Context = mCtx
    private val appDatabase: RestaurantDB = Room.databaseBuilder(mCtx, RestaurantDB::class.java, "RestaurantDB").build()

    fun getAppDatabase(): RestaurantDB {
        return appDatabase
    }

    companion object {
        private var mInstance: DatabaseClient? = null
        @Synchronized
        fun getInstance(mCtx: Context): DatabaseClient? {
            if (mInstance == null) {
                mInstance = DatabaseClient(mCtx)
            }
            return mInstance
        }
    }
}
