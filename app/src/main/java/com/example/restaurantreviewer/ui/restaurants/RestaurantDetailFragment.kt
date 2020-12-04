package com.example.restaurantreviewer.ui.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.EnumConverters

class RestaurantDetailFragment : Fragment() {
    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var selectedRestaurant: Restaurant
    private val bundle = this.arguments
    private var mName: TextView? = null
    private var mLocation: TextView? = null
    private var mType: TextView? = null
    private var mNote: TextView? = null
    private var mFinalRating: RatingBar? = null
    private var mFoodRating: RatingBar? = null
    private var mPersonellRating: RatingBar? = null
    private var mLocationRating: RatingBar? = null
    private var mAtmosphereRating: RatingBar? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_detail_restaurant, container, false)
        return root
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mName = view.findViewById(R.id.name_restaurant)
        mLocation = view.findViewById(R.id.text_location)
        mType = view.findViewById(R.id.text_type)
        mNote = view.findViewById(R.id.text_note)
        mFinalRating = view.findViewById(R.id.rate_final)
        mFoodRating = view.findViewById(R.id.rate_food)
        mPersonellRating = view.findViewById(R.id.rate_personal)
        mLocationRating = view.findViewById(R.id.rate_location)
        mAtmosphereRating = view.findViewById(R.id.rate_atmosphere)
        val converter = EnumConverters(requireContext());

        if (bundle != null) {
            selectedRestaurant = restaurantViewModel.getItem(bundle.getInt("restaurantId"))!!
            mName?.text = selectedRestaurant.name
            mLocation?.text = selectedRestaurant.location
            mType?.text = converter.ConvertRestaurantTypeEnum(selectedRestaurant.type);
            mNote?.text = selectedRestaurant.name
            setRating(mFinalRating, selectedRestaurant.ratingFinal)
            setRating(mFoodRating, selectedRestaurant.ratingFood)
            setRating(mPersonellRating, selectedRestaurant.ratingPersonnel)
            setRating(mLocationRating, selectedRestaurant.ratingLocation)
            setRating(mAtmosphereRating, selectedRestaurant.ratingAtmosphere)
        }
    }

    private fun setRating(view: RatingBar?, value: Float) {
        if(value == -1.0F)  view?.visibility = View.GONE
        else view?.rating = value
    }

    companion object {
        fun newInstance(): RestaurantDetailFragment = RestaurantDetailFragment()
    }
}