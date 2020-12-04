package com.example.restaurantreviewer.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.restaurantreviewer.enums.RestaurantTypeEnum
import java.time.Instant
import java.time.LocalDate
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
        @ColumnInfo(name = "RatingFood") var ratingFood: Float = -1.0F
        @ColumnInfo(name = "RatingLocation") var ratingLocation: Float = -1.0F
        @ColumnInfo(name = "RatingPersonnel") var ratingPersonnel: Float = -1.0F
        @ColumnInfo(name = "RatingAtmosphere") var ratingAtmosphere: Float = -1.0F
        @ColumnInfo(name = "RatingFinal") var ratingFinal: Float = 0.0F
        @ColumnInfo(name = "Created") var created: LocalDate = LocalDate.now()

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
