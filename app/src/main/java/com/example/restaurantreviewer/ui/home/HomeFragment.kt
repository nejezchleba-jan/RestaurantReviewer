package com.example.restaurantreviewer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.adapters.RestaurantAdapter
import com.example.restaurantreviewer.enums.RestaurantTypeEnum
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.ui.restaurants.RestaurantDetailFragment
import com.example.restaurantreviewer.ui.restaurants.RestaurantViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.ContextUtils
import kotlinx.android.synthetic.main.fragment_restaurants.*
import java.time.Instant

class HomeFragment : Fragment() {

    private lateinit var restaurantViewModel: RestaurantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    private fun testRestaurants(): List<Restaurant> {
        var i = Restaurant()
        i.id = 1;
        i.name = "Blah"
        i.location = "Olomouc"
        i.type = RestaurantTypeEnum.RESTAURANT
        i.ratingFinal = 2.0F

        var j = Restaurant()
        j.id = 2;
        j.name = "Restaurace B"
        j.location = "Praha"
        j.type = RestaurantTypeEnum.RESTAURANT
        j.ratingFinal = 2.5F

        return listOf(i, j)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        restaurantViewModel.mListRestaurant.observe(viewLifecycleOwner, Observer {
            recycler_restaurant.adapter = RestaurantAdapter(it)
            recycler_restaurant.layoutManager = LinearLayoutManager(activity)
        })
        return inflater.inflate(R.layout.fragment_restaurants, container, false)
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mButtonAdd: FloatingActionButton = view.findViewById(R.id.button_add_restaurant)
        mButtonAdd.setOnClickListener {
            it.findNavController().navigate(R.id.restaurantAddFragment)
        }
        // RecyclerView node initialized here
       /* recycler_restaurant.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            //adapter = RestaurantAdapter(restaurantViewModel.mListRestaurant)

        }*/
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}