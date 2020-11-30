package com.example.restaurantreviewer.adapters
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.enums.FoodGroupingEnum
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant
import java.util.*


class FoodAdapter(private val list: List<Food>, private val grouping: FoodGroupingEnum = FoodGroupingEnum.DATE)
: RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

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
            //TODO Images load/save
            setRating(food, mRating)

        }

        private fun setRating(food: Food, view: TextView?) {
            view?.text = food.rating.toString()
            view?.text = food.rating.toString()
            when {
                food.rating >= 75 -> {
                    view?.setBackgroundResource(R.drawable.rounded_rating_high)
                    context?.getColor(R.color.high_rating)?.let { view?.setTextColor(it) }
                }
                food.rating >= 50 -> {
                    view?.setBackgroundResource(R.drawable.rounded_rating_medium)
                    context?.getColor(R.color.medium_rating)?.let { view?.setTextColor(it) }
                }
                food.rating >= 0 -> {
                    view?.setBackgroundResource(R.drawable.rounded_rating_low)
                    context?.getColor(R.color.low_rating)?.let { view?.setTextColor(it) }
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
        return FoodViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food: Food = list[position]
        val previousItem: Food? = if (position == 0) null else list[position - 1]
        holder.bind(food, previousItem)
        //TODO onClickListener
    }

    override fun getItemCount(): Int = list.size


}