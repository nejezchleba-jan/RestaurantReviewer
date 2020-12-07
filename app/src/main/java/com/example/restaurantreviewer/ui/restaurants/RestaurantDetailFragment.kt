package com.example.restaurantreviewer.ui.restaurants

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.EnumConverters
import com.squareup.picasso.Picasso

class RestaurantDetailFragment : Fragment() {
    private lateinit var restaurantViewModel: RestaurantViewModel
    private var mName: TextView? = null
    private var mImage: ImageView? = null
    private var mLocation: TextView? = null
    private var mType: TextView? = null
    private var mNote: TextView? = null
    private var mFinalRating: RatingBar? = null
    private var mFoodRating: RatingBar? = null
    private var mPersonellRating: RatingBar? = null
    private var mLocationRating: RatingBar? = null
    private var mAtmosphereRating: RatingBar? = null
    private var selectedRestaurant: Restaurant? = null

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
        mImage = view.findViewById(R.id.image_restaurant)
        mLocation = view.findViewById(R.id.text_location)
        mType = view.findViewById(R.id.text_type)
        mNote = view.findViewById(R.id.text_note)
        mFinalRating = view.findViewById(R.id.rate_final)
        mFoodRating = view.findViewById(R.id.rate_food)
        mPersonellRating = view.findViewById(R.id.rate_personal)
        mLocationRating = view.findViewById(R.id.rate_location)
        mAtmosphereRating = view.findViewById(R.id.rate_atmosphere)
        val converter = EnumConverters(requireContext());

        arguments?.let {
            val tmp = restaurantViewModel.getItem(it.getInt("restaurantId"))
            tmp.observe(viewLifecycleOwner, Observer { item ->
                selectedRestaurant = item
                if(item.image?.isNotEmpty()!!) {
                    Picasso.with(context).load(Uri.parse(item.image))
                        .fit()
                        .centerCrop()
                        .into(mImage)
                } else {
                    val layout: ConstraintLayout = view.findViewById(R.id.layout_image_restaurant)
                    layout.visibility = View.GONE
                }
                if(item.location?.isNotEmpty()!!) {
                    mLocation?.text = item.location
                } else {
                    val layout: LinearLayout = view.findViewById(R.id.layout_location)
                    layout.visibility = View.GONE
                }
                if(item.note?.isNotEmpty()!!) {
                    mNote?.text = item.note
                } else {
                    val layout: LinearLayout = view.findViewById(R.id.layout_note)
                    layout.visibility = View.GONE
                }
                mName?.text = item.name
                mType?.text = converter.convertRestaurantTypeEnum(item.type);
                setRating(mFinalRating, item.ratingFinal)
                setRating(mFoodRating, item.ratingFood)
                setRating(mPersonellRating, item.ratingPersonnel)
                setRating(mLocationRating, item.ratingLocation)
                setRating(mAtmosphereRating, item.ratingAtmosphere)
            })
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