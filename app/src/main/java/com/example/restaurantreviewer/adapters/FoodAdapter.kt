package com.example.restaurantreviewer.adapters
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.icu.text.SimpleDateFormat
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
import com.example.restaurantreviewer.enums.FoodGroupingEnum
import com.example.restaurantreviewer.enums.RestaurantGroupingEnum
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.JsonConverters
import java.util.*


class FoodAdapter(
    list: MutableList<Food>,
    restaurants: MutableList<Restaurant>,
    private val grouping: FoodGroupingEnum = FoodGroupingEnum.DATE)
: RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    val restaurantList: MutableList<Restaurant> = restaurants
    private val foodList: MutableList<Food> = list
    private var copyList: MutableList<Food> = list

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

        fun bind(food: Food, previous: Food?) {
            setHeader(food, previous, mHeaderView)
            mNameView?.text = food.name
            mRestaurant?.text = restaurantList.find{ it.id == food.restaurantId }?.name
            //TODO Images load/save
            setRating(food, mRating)

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
                    if(previous == null || food.created != previous.created) view?.visibility = View.VISIBLE
                    view?.text = food.created.toString()
                }
                FoodGroupingEnum.RESTAURANT -> {
                    if(previous == null || food.restaurantId != previous.restaurantId) view?.visibility = View.VISIBLE
                    view?.text = food.created.toString();
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder = FoodViewHolder(inflater, parent)
        copyList = mutableListOf()
        copyList.addAll(foodList)
        return holder
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food: Food = foodList[position]
        val previousItem: Food? = if (position == 0) null else foodList[position - 1]
        val mButtonEdit: ImageButton = holder.itemView.findViewById(R.id.button_edit_food)
        holder.bind(food, previousItem)
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
        restaurantList.addAll(restaurants)
        this.notifyDataSetChanged()
    }

    fun searchFilter(_text: String) {
        var text = _text
        foodList.clear()
        if (text.isEmpty()) {
            foodList.addAll(copyList)
        } else {
            text = text.toLowerCase()
            for (item in copyList) {
                if (item.name.toLowerCase().contains(text)) {
                    foodList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}