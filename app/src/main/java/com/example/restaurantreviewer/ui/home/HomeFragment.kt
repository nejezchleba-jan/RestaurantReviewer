package com.example.restaurantreviewer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.adapters.RestaurantAdapter
import com.example.restaurantreviewer.ui.restaurants.RestaurantViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_restaurants.*


class HomeFragment : Fragment() {

    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        restaurantAdapter = RestaurantAdapter(mutableListOf())
        restaurantViewModel.mListRestaurant.observe(viewLifecycleOwner, Observer {
            restaurantAdapter.setData(it)
            recycler_restaurant.adapter = restaurantAdapter
        })
        return inflater.inflate(R.layout.fragment_restaurants, container, false)
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mButtonAdd: FloatingActionButton = view.findViewById(R.id.button_add_restaurant)
        val mSearchView: androidx.appcompat.widget.SearchView = view.findViewById(R.id.search_restaurant)

        mSearchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                restaurantAdapter.searchFilter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                restaurantAdapter.searchFilter(newText)
                return true
            }
        })

        mButtonAdd.setOnClickListener {
            it.findNavController().navigate(R.id.restaurantAddFragment)
        }

        recycler_restaurant.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = restaurantAdapter
        }
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}