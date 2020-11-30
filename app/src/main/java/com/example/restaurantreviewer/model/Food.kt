package com.example.restaurantreviewer.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.restaurantreviewer.enums.FoodCurrencyEnum
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Entity(tableName = "Food",
        foreignKeys = [ForeignKey(entity = Restaurant::class,
            parentColumns = arrayOf("Id"),
            childColumns = arrayOf("RestaurantId"),
            onDelete = ForeignKey.SET_NULL)]
)
class Food() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Int = -1

    @ColumnInfo(name = "Name") var name: String = "Unknown"
    @ColumnInfo(name = "RestaurantId") var restaurantId: Int? = null
    @ColumnInfo(name = "Image") var image: String? = null
    @ColumnInfo(name = "Price") var price: Double? = null
    @ColumnInfo(name = "Currency") var currency: FoodCurrencyEnum? = FoodCurrencyEnum.UNKNOWN
    @ColumnInfo(name = "OrderDate") var orderDate: Date? = null
    @ColumnInfo(name = "Note") var note: String? = null
    @ColumnInfo(name = "Rating") var rating: Int = 0
    @ColumnInfo(name = "Created") var created: LocalDate = LocalDate.now()

    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false // null check

        if (javaClass != other.javaClass)
            return false // type check

        val mOther = other as Food
        return id == mOther.id
                && created == mOther.created
                && name == mOther.name
                && restaurantId == mOther.restaurantId
                && image == mOther.image
                && price == mOther.price
                && note == mOther.note
                && currency == mOther.currency
                && orderDate == mOther.orderDate
                && rating == mOther.rating
                && created == mOther.created
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id, created, name, restaurantId, image,
            price, currency, note, orderDate, rating
        )
    }
}
