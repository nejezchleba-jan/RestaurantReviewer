package com.example.restaurantreviewer.ui.food

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.EnumConverters
import com.example.restaurantreviewer.utils.JsonConverters
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
                if(item.image?.isNotEmpty()!!) {
                    Picasso.with(context).load(Uri.parse(item.image))
                        .fit()
                        .centerCrop()
                        .into(mImage)
                } else {
                    val layout: ConstraintLayout = view.findViewById(R.id.layout_image_food)
                    layout.visibility = View.GONE
                }
                if(item.price != null) {
                    mPrice?.text = price
                } else {
                    val layout: LinearLayout = view.findViewById(R.id.layout_price)
                    layout.visibility = View.GONE
                }
                if(item.note?.isNotEmpty()!!) {
                    mNote?.text = item.note
                } else {
                    val layout: LinearLayout = view.findViewById(R.id.layout_note)
                    layout.visibility = View.GONE
                }
                mName?.text = item.name
                mRestaurant?.text = foodRestaurant.name
                mOrderDate?.text = formatDate(item.orderDate!!)
                mFoodRating?.text = item.rating.toString()
            })
        }
    }

    fun formatDate(date: LocalDate): String {
        val formatters: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val text: String = date.format(formatters)
        return LocalDate.parse(text, formatters).format(formatters).toString()
    }

    companion object {
        fun newInstance(): FoodDetailFragment = FoodDetailFragment()
    }
}