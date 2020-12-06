package com.example.restaurantreviewer.ui.dashboard

import android.os.Bundle
import android.view.*
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
    private var mainView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_menu, menu)
        val menuItem = menu.findItem(R.id.action_add)
        val view = menuItem.actionView
        view.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_add -> add()
        }
        return super.onOptionsItemSelected(item)
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
        mainView = view
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

        recycler_food.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = foodAdapter
        }
    }

    private fun add() {
        val bundle = Bundle()
        if(foodAdapter.restaurantList.size == 0) {
            Toast.makeText(requireContext(), "Please create a restaurant first.", Toast.LENGTH_SHORT).show()
        }
        else {
            bundle.putString("restaurants", JsonConverters().convertListRestaurantToJson(foodAdapter.restaurantList))
            mainView?.findNavController()?.navigate(R.id.foodAddFragment, bundle)
        }
    }

    companion object {
        fun newInstance(): DashboardFragment = DashboardFragment()
    }
}