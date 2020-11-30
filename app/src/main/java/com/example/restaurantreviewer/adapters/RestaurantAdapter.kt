package com.example.restaurantreviewer.adapters
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.enums.FoodGroupingEnum
import com.example.restaurantreviewer.enums.RestaurantGroupingEnum
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant
import org.w3c.dom.Text


class RestaurantAdapter(private val list: List<Restaurant>, private val grouping: RestaurantGroupingEnum = RestaurantGroupingEnum.DATE)
: RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    inner class RestaurantViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_restaurant_item, parent, false)) {
        private var mNameView: TextView? = null
        private var mLocationView: TextView? = null
        private var mHeader: TextView? = null
        private var mImageView: ImageView? = null
        private var mRatingBar: RatingBar? = null


        init {
            mNameView = itemView.findViewById(R.id.name_restaurant)
            mLocationView = itemView.findViewById(R.id.location_restaurant)
            mHeader = itemView.findViewById(R.id.header_restaurant)
            mImageView = itemView.findViewById(R.id.image_restaurant)
            mRatingBar = itemView.findViewById(R.id.rating_restaurant)
        }

        fun bind(restaurant: Restaurant, previous: Restaurant?) {
            setHeader(restaurant, previous, mHeader)
            mNameView?.text = restaurant.name
            mLocationView?.text = restaurant.location
            //TODO Images load/save
            mRatingBar?.rating = restaurant.ratingFinal
        }

        private fun setHeader(restaurant: Restaurant, previous: Restaurant?, view: TextView?) {
            when(grouping) {
                RestaurantGroupingEnum.DATE -> {
                    if(previous == null || restaurant.created != previous.created) view?.visibility = View.VISIBLE
                    view?.text = restaurant.created.toString()
                }
                RestaurantGroupingEnum.TYPE -> {
                    if(previous == null || restaurant.type != previous.type) view?.visibility = View.VISIBLE
                    view?.text = restaurant.type.toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RestaurantViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant: Restaurant = list[position]
        val previousItem: Restaurant? = if (position == 0) null else list[position - 1]
        holder.bind(restaurant, previousItem)
        //TODO onClickListener
    }

    override fun getItemCount(): Int = list.size
}