package com.example.restaurantreviewer.adapters
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.enums.RestaurantGroupingEnum
import com.example.restaurantreviewer.model.Restaurant


class RestaurantAdapter(
        list: MutableList<Restaurant>,
        private val grouping: RestaurantGroupingEnum = RestaurantGroupingEnum.DATE)
: RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    private val restaurantList: MutableList<Restaurant> = list
    private var copyList: MutableList<Restaurant> = list

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
        copyList = mutableListOf()
        copyList.addAll(restaurantList)

        return holder

    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant: Restaurant = restaurantList[position]
        val previousItem: Restaurant? = if (position == 0) null else restaurantList[position - 1]
        holder.bind(restaurant, previousItem)

        val mButtonMore: ImageButton = holder.itemView.findViewById(R.id.button_more)
        val mButtonFood: ImageButton = holder.itemView.findViewById(R.id.button_food)
        val mButtonEdit: ImageButton = holder.itemView.findViewById(R.id.button_edit_restaurant)

        mButtonMore.setOnClickListener {
            animateButtons(holder.itemView)
        }

        mButtonEdit.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("restaurantId", restaurantList[position].id)
            it.findNavController().navigate(R.id.restaurantEditFragment, bundle)
            animateButtons(holder.itemView)
        }

        mButtonFood.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("restaurantId", restaurantList[position].id)
            bundle.putString("restaurantName", restaurantList[position].name)
            //TODO Kontrola jestli mi přišel bundle a podle toho vyfiltrovat food
            it.findNavController().navigate(R.id.navigation_food, bundle)
            animateButtons(holder.itemView)
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("restaurantId", restaurantList[position].id)
            it.findNavController().navigate(R.id.restaurantDetailFragment, bundle)
        }
    }

    override fun getItemCount(): Int = restaurantList.size

    fun setData(newList: MutableList<Restaurant>) {
        restaurantList.clear()
        restaurantList.addAll(newList)
        this.notifyDataSetChanged()
    }

    private fun animateButtons(view: View) {
        val buttonMore: ImageButton = view.findViewById(R.id.button_more)
        val actionButtons: View = view.findViewById(R.id.action_buttons)

        if (buttonMore.visibility == View.VISIBLE) {
            hideButton(buttonMore)
            showButton(actionButtons)

        } else {
            showButton(buttonMore)
            hideButton(actionButtons)
        }
    }

    private fun hideButton(view: View) {
        view.visibility = View.GONE
    }

    private fun showButton(view: View) {
        view.visibility = View.VISIBLE
    }

    fun searchFilter(_text: String) {
        var text = _text
        restaurantList.clear()
        if (text.isEmpty()) {
            restaurantList.addAll(copyList)
        } else {
            text = text.toLowerCase()
            for (item in copyList) {
                if (item.name.toLowerCase().contains(text)) {
                    restaurantList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}