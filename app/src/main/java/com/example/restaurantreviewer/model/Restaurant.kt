package com.example.restaurantreviewer.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.restaurantreviewer.enums.RestaurantTypeEnum
import java.time.Instant
import java.util.*

@Entity(tableName = "Restaurants")
class Restaurant() {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "Id")
        var id: Int = -1

        @ColumnInfo(name = "Name") var name: String = "Unknown"
        @ColumnInfo(name = "Type") var type: RestaurantTypeEnum = RestaurantTypeEnum.UNKNOWN
        @ColumnInfo(name = "Image") var image: String? = null
        @ColumnInfo(name = "Location") var location: String? = null
        @ColumnInfo(name = "Note") var note: String? = null
        @ColumnInfo(name = "RatingFood") var ratingFood: Double? = null
        @ColumnInfo(name = "RatingLocation") var ratingLocation: Double? = null
        @ColumnInfo(name = "RatingPersonnel") var ratingPersonnel: Double? = null
        @ColumnInfo(name = "RatingAtmosphere") var ratingAtmosphere: Double? = null
        @ColumnInfo(name = "RatingFinal") var ratingFinal: Double? = null
        @ColumnInfo(name = "Created") var created: Instant = Instant.now()

        override fun equals(other: Any?): Boolean {
                if (other == null)
                        return false // null check

                if (javaClass != other.javaClass)
                        return false // type check

                val mOther = other as Restaurant
                return id == mOther.id
                        && created == mOther.created
                        && name == mOther.name
                        && type == mOther.type
                        && image == mOther.image
                        && location == mOther.location
                        && note == mOther.note
                        && ratingFood == mOther.ratingFood
                        && ratingLocation == mOther.ratingLocation
                        && ratingPersonnel == mOther.ratingPersonnel
                        && ratingAtmosphere == mOther.ratingAtmosphere
        }

        override fun hashCode(): Int {
                return Objects.hash(id, created, name, type, image, location, note,
                        ratingFood, ratingLocation, ratingPersonnel, ratingAtmosphere)
        }
}
