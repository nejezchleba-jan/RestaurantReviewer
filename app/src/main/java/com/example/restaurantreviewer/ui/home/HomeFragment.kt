package com.example.restaurantreviewer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.adapters.RestaurantAdapter
import com.example.restaurantreviewer.enums.RestaurantTypeEnum
import com.example.restaurantreviewer.model.Restaurant
import kotlinx.android.synthetic.main.fragment_restaurants.*
import java.time.Instant

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

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
    /*override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_restaurants, container, false)
        *//*val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*//*
        return root
    }*/

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurants, container, false)
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView node initialized here
        recycler_restaurant.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = RestaurantAdapter(testRestaurants())
        }
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}