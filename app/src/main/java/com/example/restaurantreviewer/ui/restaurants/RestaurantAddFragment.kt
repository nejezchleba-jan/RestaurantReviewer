package com.example.restaurantreviewer.ui.restaurants

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.enums.RestaurantTypeEnum
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.EnumConverters


class RestaurantAddFragment : Fragment() {
    private lateinit var restaurantViewModel: RestaurantViewModel
    private var mName: EditText? = null
    private var mLocation: EditText? = null
    private var mType: Spinner? = null
    private var mNote: EditText? = null
    private var mFoodRating: RatingBar? = null
    private var mPersonellRating: RatingBar? = null
    private var mLocationRating: RatingBar? = null
    private var mAtmosphereRating: RatingBar? = null
    private var mainView: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.confirm_add_menu, menu)
        val menuItem = menu.findItem(R.id.action_confirm)
        val view = menuItem.actionView
        view.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_confirm ->
                insertAction(mainView)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add_restaurant, container, false)
        return root
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val converter = EnumConverters(requireContext());
        fillSpinner(view.findViewById(R.id.spinner_type), converter)
        mainView = view;
    }

    private fun fillSpinner(spinner: Spinner, converter: EnumConverters) {
        val values = enumValues<RestaurantTypeEnum>()
        val items: MutableList<String> = mutableListOf()
        values.forEach {
            items.add(converter.convertRestaurantTypeEnum(it))
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_restaurant_type);
        spinner.adapter = spinnerArrayAdapter
    }

    private fun insertAction(view: View?) {
        val converter = EnumConverters(requireContext());
        view?.let {
            mName = it.findViewById(R.id.text_name)
            mLocation = it.findViewById(R.id.text_location)
            mType = it.findViewById(R.id.spinner_type)
            mNote = it.findViewById(R.id.text_note)
            mFoodRating = it.findViewById(R.id.rate_food)
            mPersonellRating = it.findViewById(R.id.rate_personal)
            mLocationRating = it.findViewById(R.id.rate_location)
            mAtmosphereRating = it.findViewById(R.id.rate_atmosphere)

            val newRest: Restaurant = Restaurant()
            newRest.name = mName?.editableText.toString()
            newRest.location = mLocation?.editableText.toString()
            newRest.type = converter.convertRestaurantTypeString(mType?.selectedItem.toString())
            newRest.note = mNote?.editableText.toString()
            newRest.ratingFood = mFoodRating?.rating!!
            newRest.ratingLocation = mLocationRating?.rating!!
            newRest.ratingPersonnel = mPersonellRating?.rating!!
            newRest.ratingAtmosphere = mAtmosphereRating?.rating!!
            newRest.ratingFinal =
                    ((newRest.ratingFood + newRest.ratingLocation
                            + newRest.ratingPersonnel + newRest.ratingAtmosphere) / 4)

            if(newRest.name.isEmpty() || newRest.name.isBlank()) {
                Toast.makeText(activity?.applicationContext, R.string.toast_empty_name, Toast.LENGTH_SHORT).show()
            } else {
                restaurantViewModel.insert(newRest)
                it.findNavController().navigate(R.id.navigation_restaurant)
            }
        }
    }

    companion object {
        fun newInstance(): RestaurantAddFragment = RestaurantAddFragment()
    }
}