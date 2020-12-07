package com.example.restaurantreviewer.ui.restaurants

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.adapters.RestaurantAdapter
import com.example.restaurantreviewer.enums.RestaurantFilterEnum
import com.example.restaurantreviewer.enums.RestaurantGroupingEnum
import com.example.restaurantreviewer.enums.RestaurantOrderEnum
import com.example.restaurantreviewer.utils.EnumConverters
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_restaurants.*
import java.util.prefs.Preferences


class RestaurantFragment : Fragment() {

    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter
    private var mainView: View? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var converters: EnumConverters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    private fun setUpPreferences() {
        with(sharedPreferences.edit()) {
            if(!sharedPreferences.contains("filterVal_rest")) {
                putString("filterVal_rest", "")
            } else if(!sharedPreferences.contains("filter_rest")) {
                putString("filter_rest", converters.convertRestaurantFilterEnum(RestaurantFilterEnum.NONE))
            } else if(!sharedPreferences.contains("groupBy_rest")) {
                putString("groupBy_rest", converters.convertRestaurantGroupingEnum(RestaurantGroupingEnum.DATE))
            } else if(!sharedPreferences.contains("orderBy_rest")) {
                putString("orderBy_rest", converters.convertRestaurantOrderEnum(RestaurantOrderEnum.NAME))
            }
            apply()
        }
    }

    private fun applyPreferences(adapter: RestaurantAdapter) {
        adapter.changeGrouping(
                converters.convertRestaurantGroupingString(sharedPreferences.getString("groupBy_rest",
                        converters.convertRestaurantGroupingEnum(RestaurantGroupingEnum.DATE))!!))

        adapter.changeOrder(
                converters.convertRestaurantOrderString(sharedPreferences.getString("orderBy_rest",
                    converters.convertRestaurantOrderEnum(RestaurantOrderEnum.NAME))!!))

        adapter.changeFilter(
                converters.convertRestaurantFilterString(sharedPreferences.getString("filter_rest",
                    converters.convertRestaurantFilterEnum(RestaurantFilterEnum.NONE))!!),
                sharedPreferences.getString("filterVal_rest", "")!!)

        adapter.applyChangesToList(converters)
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
                mainView?.findNavController()?.navigate(R.id.restaurantAddFragment)
                applyPreferences(restaurantAdapter)
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
        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        setUpPreferences()
        restaurantAdapter = RestaurantAdapter(mutableListOf())
        restaurantViewModel.mListRestaurant.observe(viewLifecycleOwner, Observer {
            restaurantAdapter.setData(it, converters)
            applyPreferences(restaurantAdapter)
            recycler_restaurant.adapter = restaurantAdapter
        })
        return inflater.inflate(R.layout.fragment_restaurants, container, false)
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mButtonList: ImageButton = view.findViewById(R.id.button_list_options)
        val mSearchView: androidx.appcompat.widget.SearchView = view.findViewById(R.id.search_restaurant)
        mainView = view


        mSearchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                restaurantAdapter.searchFilter(query, converters)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                restaurantAdapter.searchFilter(newText, converters)
                return true
            }
        })

        mButtonList.setOnClickListener {
            val fm: FragmentManager = parentFragmentManager
            val dialogFragment = ListOptionsRestaurantDialog(restaurantAdapter)
            dialogFragment.setTargetFragment(this, 1)
            dialogFragment.show(fm,"Tag")
        }

        recycler_restaurant.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = restaurantAdapter
        }
    }

    companion object {
        fun newInstance(): RestaurantFragment = RestaurantFragment()
    }
}