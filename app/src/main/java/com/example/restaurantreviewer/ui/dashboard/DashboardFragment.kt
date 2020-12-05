package com.example.restaurantreviewer.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.adapters.FoodAdapter
import com.example.restaurantreviewer.adapters.RestaurantAdapter
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.ui.food.FoodViewModel
import com.example.restaurantreviewer.ui.restaurants.RestaurantViewModel
import com.example.restaurantreviewer.utils.JsonConverters
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_food.*
import kotlinx.android.synthetic.main.fragment_restaurants.*
import java.time.Instant
import java.time.LocalDate

class DashboardFragment : Fragment() {

    private lateinit var foodViewModel: FoodViewModel
    private lateinit var foodAdapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        foodAdapter = FoodAdapter(mutableListOf(), mutableListOf())
        foodViewModel.mListFood.observe(viewLifecycleOwner, Observer { food ->
            foodViewModel.mListRestaurant.observe(viewLifecycleOwner, Observer { rest ->
                foodAdapter.setData(food, rest)
                recycler_food.adapter = foodAdapter
            })
        })

        return inflater.inflate(R.layout.fragment_food, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mButtonAdd: FloatingActionButton = view.findViewById(R.id.button_add_food)
        val mSearchView: androidx.appcompat.widget.SearchView = view.findViewById(R.id.search_food)

        mSearchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                foodAdapter.searchFilter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                foodAdapter.searchFilter(newText)
                return true
            }
        })

        mButtonAdd.setOnClickListener {
            val bundle = Bundle()
            if(foodAdapter.restaurantList.size == 0) {
                Snackbar.make(view, "Please create a restaurant first.", Snackbar.LENGTH_SHORT).show()
            }
            else {
                bundle.putString("restaurants", JsonConverters().convertListRestaurantToJson(foodAdapter.restaurantList))
                it.findNavController().navigate(R.id.foodAddFragment, bundle)
            }
        }

        recycler_food.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = foodAdapter
        }
    }

    companion object {
        fun newInstance(): DashboardFragment = DashboardFragment()
    }
}