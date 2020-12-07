package com.example.restaurantreviewer.adapters
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.enums.*
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.EnumConverters
import com.example.restaurantreviewer.utils.JsonConverters
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class FoodAdapter(
    list: MutableList<Food>,
    restaurants: MutableList<Restaurant>)
: RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    val restaurantList: MutableList<Restaurant> = restaurants
    private val foodList: MutableList<Food> = list
    private var copyList: MutableList<Food> = mutableListOf()
    var grouping: FoodGroupingEnum = FoodGroupingEnum.DATE
    var order: FoodOrderEnum = FoodOrderEnum.NAME
    var filter: FoodFilterEnum = FoodFilterEnum.NONE
    var filterVal: String = ""

    init {
        copyList.addAll(list)
    }

    inner class FoodViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_food_item, parent, false)) {
        private var mNameView: TextView? = null
        private var mRestaurant: TextView? = null
        private var mImageView: ImageView? = null
        private var mHeaderView: TextView? = null
        private var mRating: TextView? = null
        private var context: Context? = null


        init {
            mHeaderView = itemView.findViewById(R.id.header_food)
            mNameView = itemView.findViewById(R.id.name_food)
            mRestaurant = itemView.findViewById(R.id.name_restaurant)
            mImageView = itemView.findViewById(R.id.image_food)
            mRating = itemView.findViewById(R.id.rating_food)
            context = parent.context
        }

        fun bind(food: Food, previous: Food?, context: Context) {
            mHeaderView?.visibility = View.GONE
            var restaurantName = restaurantList.find{ it.id == food.restaurantId }?.name
            setHeader(food, previous, mHeaderView)
            mNameView?.text = if(food.name.length > 20) food.name.substring(0,20) + "..." else food.name
            mRestaurant?.text = if(restaurantName?.length!! >= 20) restaurantName.substring(0,20) + "..." else restaurantName
            if(food.image != null) Picasso
                .with(context)
                .load(Uri.parse(food.image))
                .fit()
                .into(mImageView)
            setRating(food, mRating)

        }

        fun formatDate(date: LocalDate): String {
            val formatters: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val text: String = date.format(formatters)
            return LocalDate.parse(text, formatters).format(formatters).toString()
        }

        private fun setRating(food: Food, view: TextView?) {
            view?.text = food.rating.toString()
            when {
                food.rating >= 75 -> {
                    view?.setBackgroundResource(R.drawable.rounded_rating_high)
                    view?.setTextColor(ContextCompat.getColor(context!!, R.color.high_rating))
                }
                food.rating >= 50 -> {
                    view?.setBackgroundResource(R.drawable.rounded_rating_medium)
                    view?.setTextColor(ContextCompat.getColor(context!!, R.color.medium_rating))
                }
                food.rating >= 25 -> {
                    view?.setBackgroundResource(R.drawable.rounded_rating_low)
                    view?.setTextColor(ContextCompat.getColor(context!!, R.color.low_rating))
                }
                food.rating >= 0 -> {
                    view?.setBackgroundResource(R.drawable.rounded_rating_worst)
                    view?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                }
                }
            }

        private fun setHeader(food: Food, previous: Food?, view: TextView?) {
            when(grouping) {
                FoodGroupingEnum.DATE -> {
                    if(previous == null || food.created != previous.created)
                    {
                        view?.visibility = View.VISIBLE
                        view?.text = formatDate(food.created)
                    }

                }
                FoodGroupingEnum.RESTAURANT -> {
                    if(previous == null || food.restaurantId != previous.restaurantId) {
                        view?.visibility = View.VISIBLE
                        view?.text = restaurantList.find { it.id == food.restaurantId }?.name
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder = FoodViewHolder(inflater, parent)
        return holder
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food: Food = foodList[position]
        val previousItem: Food? = if (position == 0) null else foodList[position - 1]
        val mButtonEdit: ImageButton = holder.itemView.findViewById(R.id.button_edit_food)
        holder.bind(food, previousItem, holder.itemView.context)

        mButtonEdit.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("foodId", foodList[position].id)
            bundle.putString("restaurants", JsonConverters().convertListRestaurantToJson(restaurantList))
            it.findNavController().navigate(R.id.foodEditFragment, bundle)
        }

        holder.itemView.setOnClickListener { it ->
            val bundle = Bundle()
            val foodRestaurant: Restaurant = restaurantList.find{rest -> foodList[position].restaurantId == rest.id}!!
            bundle.putInt("foodId", foodList[position].id)
            bundle.putString("restaurant", JsonConverters().convertRestaurantToJson(foodRestaurant))
            it.findNavController().navigate(R.id.foodDetailFragment, bundle)
        }
    }


    override fun getItemCount(): Int = foodList.size

    fun setData(newList: MutableList<Food>, restaurants: MutableList<Restaurant>) {
        foodList.clear()
        foodList.addAll(newList)
        restaurantList.clear()
        restaurantList.addAll(restaurants.asReversed())
        copyList.clear()
        copyList.addAll(newList.asReversed())
        applyChangesToList()
        this.notifyDataSetChanged()
    }

    fun searchFilter(_text: String) {
        var text = _text
        applyChangesToList()
        if (text.isNotEmpty()) {
            text = text.toLowerCase(Locale.ROOT)
            val filteredList: MutableList<Food> = mutableListOf()
            for (item in foodList) {
                if (item.name.toLowerCase(Locale.ROOT).contains(text)) {
                    filteredList.add(item)
                }
            }
            foodList.clear()
            foodList.addAll(filteredList)
        }
        notifyDataSetChanged()
    }

    fun changeGrouping(newGroup: FoodGroupingEnum) {
        grouping = newGroup
    }

    fun changeOrder(newOrder: FoodOrderEnum) {
        order = newOrder
    }

    fun applyChangesToList() {
        applyFilter()
        if(grouping == FoodGroupingEnum.RESTAURANT) {
            foodList.sortedWith(compareBy({ it.restaurantId }, { it.name }))

        } else {
            foodList.sortedWith(compareBy({ it.created }, { it.name }))
        }
        notifyDataSetChanged()
    }

    fun changeFilter(filterEnum: FoodFilterEnum, value: String) {
        filter = filterEnum
        filterVal = value
    }

    fun applyFilter() {
        foodList.clear()
        when(filter) {
            FoodFilterEnum.NONE -> {
                foodList.addAll(copyList)
            }
            FoodFilterEnum.NAME -> {
                foodList.addAll(copyList.filter { it.name.contains(filterVal) })
            }
            FoodFilterEnum.RESTAURANT -> {
                val restaurant: Restaurant? = restaurantList.firstOrNull{ it.name.equals(filterVal, ignoreCase = true) }
                foodList.addAll(copyList.filter { food -> restaurant?.id == food.restaurantId })
            }
            FoodFilterEnum.RATING_LESS -> {
                foodList.addAll(copyList.filter { it.rating <= filterVal.toInt() })
            }
            FoodFilterEnum.RATING_MORE -> {
                foodList.addAll(copyList.filter { it.rating >= filterVal.toInt() })
            }
        }
        notifyDataSetChanged()
    }
}