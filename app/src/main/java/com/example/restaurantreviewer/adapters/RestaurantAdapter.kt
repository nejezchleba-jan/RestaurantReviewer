package com.example.restaurantreviewer.adapters
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.enums.RestaurantGroupingEnum
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.ui.restaurants.RestaurantDetailFragment


class RestaurantAdapter(
        list: List<Restaurant>,
        private val grouping: RestaurantGroupingEnum = RestaurantGroupingEnum.DATE)
: RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    private var restaurantList: List<Restaurant> = list

    inner class RestaurantViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_restaurant_item, parent, false)) {
        private var mItem: ConstraintLayout? = null
        private var mNameView: TextView? = null
        private var mLocationView: TextView? = null
        private var mHeader: TextView? = null
        private var mImageView: ImageView? = null
        private var mRatingBar: RatingBar? = null
        private var mButtonMore: AppCompatImageButton? = null
        private var mEditButton: AppCompatImageButton? = null
        private var mFoodButton: AppCompatImageButton? = null


        init {
            mItem = itemView.findViewById(R.id.restaurant_item)
            mNameView = itemView.findViewById(R.id.name_restaurant)
            mLocationView = itemView.findViewById(R.id.location_restaurant)
            mHeader = itemView.findViewById(R.id.header_restaurant)
            mImageView = itemView.findViewById(R.id.image_restaurant)
            mRatingBar = itemView.findViewById(R.id.rating_restaurant)
            mButtonMore = itemView.findViewById(R.id.button_more)
            mEditButton = itemView.findViewById(R.id.button_edit_restaurant)
            mFoodButton = itemView.findViewById(R.id.button_food)
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
                    if (previous == null || restaurant.created != previous.created) view?.visibility = View.VISIBLE
                    view?.text = restaurant.created.toString()
                }
                RestaurantGroupingEnum.TYPE -> {
                    if (previous == null || restaurant.type != previous.type) view?.visibility = View.VISIBLE
                    view?.text = restaurant.type.toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder = RestaurantViewHolder(inflater, parent)

        return holder

    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant: Restaurant = restaurantList[position]
        val previousItem: Restaurant? = if (position == 0) null else restaurantList[position - 1]
        holder.bind(restaurant, previousItem)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("restaurantId", restaurantList[position].id)
            it.findNavController().navigate(R.id.restaurantDetailFragment, bundle)
            /*val detailFragment: Fragment = RestaurantDetailFragment()
            val bundle = Bundle()
            val transaction: FragmentTransaction = fragmentManager?.beginTransaction()!!
            bundle.putInt("restaurantId", list[position].id)
            detailFragment.arguments = bundle;
            transaction.replace(R.id.nav_host_fragment, detailFragment) // give your fragment container id in first parameter
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()*/
        }
    }

    override fun getItemCount(): Int = restaurantList.size

    fun setData(newList: List<Restaurant>) {
        restaurantList = newList
        this.notifyDataSetChanged()
    }
}