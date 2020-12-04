package com.example.restaurantreviewer.ui.restaurants

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.Spinner
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
    private var mButtonConfirm: ImageButton? = null
    private var mainView: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.confirm_add_menu, menu)

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
        mainView = view;
        val converter = EnumConverters(requireContext());
        fillSpinner(view.findViewById(R.id.spinner_type), converter)
        mainView = view;
        /*mButtonConfirm = view.findViewById(R.id.button_confirm_restaurant)
        mName = view.findViewById(R.id.text_name)
        mLocation = view.findViewById(R.id.text_location)
        mType = view.findViewById(R.id.spinner_type)
        mNote = view.findViewById(R.id.text_note)
        mFoodRating = view.findViewById(R.id.rate_food)
        mPersonellRating = view.findViewById(R.id.rate_personal)
        mLocationRating = view.findViewById(R.id.rate_location)
        mAtmosphereRating = view.findViewById(R.id.rate_atmosphere)

        mButtonConfirm?.setOnClickListener {
            val newRest: Restaurant = Restaurant()
            newRest.name = mName?.editableText.toString()
            newRest.location = mLocation?.editableText.toString()
            newRest.type = converter.ConvertRestaurantTypeString(mType?.selectedItem.toString())
            newRest.note = mNote?.editableText.toString()
            newRest.ratingFood = mFoodRating?.rating!!
            newRest.ratingLocation = mLocationRating?.rating!!
            newRest.ratingPersonnel = mPersonellRating?.rating!!
            newRest.ratingAtmosphere = mAtmosphereRating?.rating!!
            newRest.ratingFinal =
                ((newRest.ratingFood + newRest.ratingLocation
                        + newRest.ratingPersonnel + newRest.ratingAtmosphere) / 4)

            restaurantViewModel.insert(newRest)
            view.findNavController().navigate(R.id.navigation_restaurant)
        }*/
    }

    private fun fillSpinner(view: Spinner?, converter: EnumConverters) {
        val values = enumValues<RestaurantTypeEnum>()
        val items: MutableList<String> = mutableListOf()
        values.forEach {
            items.add(converter.ConvertRestaurantTypeEnum(it))
        }
    }

    private fun insertAction(view: View?) {
        val converter = EnumConverters(requireContext());
        view?.let {
            mButtonConfirm = it.findViewById(R.id.button_confirm_restaurant)
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
            newRest.type = converter.ConvertRestaurantTypeString(mType?.selectedItem.toString())
            newRest.note = mNote?.editableText.toString()
            newRest.ratingFood = mFoodRating?.rating!!
            newRest.ratingLocation = mLocationRating?.rating!!
            newRest.ratingPersonnel = mPersonellRating?.rating!!
            newRest.ratingAtmosphere = mAtmosphereRating?.rating!!
            newRest.ratingFinal =
                    ((newRest.ratingFood + newRest.ratingLocation
                            + newRest.ratingPersonnel + newRest.ratingAtmosphere) / 4)

            restaurantViewModel.insert(newRest)
            it.findNavController().navigate(R.id.navigation_restaurant)
        }
    }

    companion object {
        fun newInstance(): RestaurantAddFragment = RestaurantAddFragment()
    }
}