package com.example.restaurantreviewer.ui.food

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.adapters.FoodAdapter
import com.example.restaurantreviewer.adapters.RestaurantAdapter
import com.example.restaurantreviewer.enums.*
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.EnumConverters
import com.example.restaurantreviewer.utils.JsonConverters
import kotlinx.android.synthetic.main.fragment_food.*
import kotlinx.android.synthetic.main.fragment_restaurants.*

class FoodFragment : Fragment() {

    private lateinit var foodViewModel: FoodViewModel
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var converters: EnumConverters
    private var mainView: View? = null
    private var restaurantName: String? = null


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
            R.id.action_add -> {
                add()
                applyPreferences(foodAdapter)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)!!
        converters = EnumConverters(requireContext())
        setUpPreferences()
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        foodAdapter = FoodAdapter(mutableListOf(), mutableListOf())
        foodViewModel.mListFood.observe(viewLifecycleOwner, Observer { food ->
            foodViewModel.mListRestaurant.observe(viewLifecycleOwner, Observer { rest ->
                foodAdapter = FoodAdapter(food, rest)
                recycler_food.adapter = foodAdapter
                applyPreferences(recycler_food.adapter as FoodAdapter)
            })
        })

        return inflater.inflate(R.layout.fragment_food, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        val mButtonList: ImageButton = view.findViewById(R.id.button_list_options)
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

        mButtonList.setOnClickListener {
            val fm: FragmentManager = parentFragmentManager
            val dialogFragment = ListOptionsFoodDialog(foodAdapter)
            dialogFragment.setTargetFragment(this, 1)
            dialogFragment.show(fm,"Tag")
        }

        recycler_food.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = foodAdapter
            applyPreferences(recycler_food.adapter as FoodAdapter)
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


    private fun setUpPreferences() {
        with(sharedPreferences.edit()) {
            if(!sharedPreferences.contains("filterVal_food")) {
                putString("filterVal_food", "")
            }
            if(!sharedPreferences.contains("filter_food")) {
                putString("filter_food", converters.convertFoodFilterEnum(FoodFilterEnum.NONE))
            }
            if(!sharedPreferences.contains("groupBy_food")) {
                putString("groupBy_food", converters.convertFoodGroupingEnum(FoodGroupingEnum.DATE))
            }
            if(!sharedPreferences.contains("orderBy_food")) {
                putString("orderBy_food", converters.convertFoodOrderEnum(FoodOrderEnum.NAME))
            }

            //RESTAURANT FILTER OFF

            if(sharedPreferences.contains("filterVal_rest")) {
                putString("filterVal_rest", "")
            }
            if(sharedPreferences.contains("filter_rest")) {
                putString("filter_rest", converters.convertFoodFilterEnum(FoodFilterEnum.NONE))
            }
            apply()
        }
    }

    private fun applyPreferences(adapter: FoodAdapter) {
        adapter.changeGrouping(
                converters.convertFoodGroupingString(sharedPreferences.getString("groupBy_food",
                        converters.convertFoodGroupingEnum(FoodGroupingEnum.DATE))!!))

        adapter.changeOrder(
                converters.convertFoodOrderString(sharedPreferences.getString("orderBy_food",
                        converters.convertFoodOrderEnum(FoodOrderEnum.NAME))!!))

        adapter.changeFilter(
                converters.convertFoodFilterString(sharedPreferences.getString("filter_food",
                        converters.convertFoodFilterEnum(FoodFilterEnum.NONE))!!),
                sharedPreferences.getString("filterVal_food", "")!!)

        filterIfClickedRestaurant()
        adapter.applyChangesToList()
    }

    private fun filterIfClickedRestaurant() {
        if(arguments != null) {
            restaurantName = arguments?.getString("restaurantName")
            with(sharedPreferences.edit()) {
                putString("filterVal_food", restaurantName)
                putString("filter_food", "Restaurant")
                apply()
            }
        }
    }

    companion object {
        fun newInstance(): FoodFragment = FoodFragment()
    }
}