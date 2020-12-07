package com.example.restaurantreviewer.ui.food

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.EnumConverters
import com.example.restaurantreviewer.utils.JsonConverters
import com.squareup.picasso.Picasso

class FoodDetailFragment : Fragment() {
    private lateinit var foodViewModel: FoodViewModel
    private var mRestaurant: TextView? = null
    private var mImage: ImageView? = null
    private var mName: TextView? = null
    private var mPrice: TextView? = null
    private var mOrderDate: TextView? = null
    private var mNote: TextView? = null
    private var mFoodRating: TextView? = null
    private var mainView: View? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_detail_food, container, false)
        return root
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO Databiding?
        mName = view.findViewById(R.id.name_food)
        mRestaurant = view.findViewById(R.id.text_restaurant)
        mPrice = view.findViewById(R.id.text_price)
        mOrderDate = view.findViewById(R.id.text_ordered)
        mNote = view.findViewById(R.id.text_note)
        mFoodRating = view.findViewById(R.id.rate_food)
        mImage = view.findViewById(R.id.image_food)

        arguments?.let {
            val food = foodViewModel.getItem(it.getInt("foodId"))
            val foodRestaurant: Restaurant = JsonConverters().getRestaurantFromJson(it.getString("restaurant")!!)
            food.observe(viewLifecycleOwner, Observer { item ->
                val price: String = item.price.toString() + " " + EnumConverters(requireContext()).convertFoodCurrencyEnum(item.currency!!)
                if(item.image != null) {
                    Picasso.with(context).load(Uri.parse(item.image))
                        .fit()
                        .centerCrop()
                        .into(mImage)
                }
                mName?.text = item.name
                mRestaurant?.text = foodRestaurant.name
                mPrice?.text = price
                mOrderDate?.text = item.orderDate.toString()
                mNote?.text = item.note
                mFoodRating?.text = item.rating.toString()
            })
        }
    }

    companion object {
        fun newInstance(): FoodDetailFragment = FoodDetailFragment()
    }
}