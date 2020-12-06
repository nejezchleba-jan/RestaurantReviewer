package com.example.restaurantreviewer.ui.restaurants

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.enums.RestaurantTypeEnum
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.EnumConverters


class RestaurantEditFragment : Fragment() {
    private lateinit var restaurantViewModel: RestaurantViewModel
    private var mName: EditText? = null
    private var mLocation: EditText? = null
    private var mNote: EditText? = null
    private var mFoodRating: RatingBar? = null
    private var mPersonellRating: RatingBar? = null
    private var mLocationRating: RatingBar? = null
    private var mAtmosphereRating: RatingBar? = null
    private var mType: Spinner? = null
    private var mainView: View? = null
    private var selectedRestaurant: Restaurant? = null


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
                updateAction(mainView)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_edit_restaurant, container, false)
        return root
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonDelete: ImageButton = view.findViewById(R.id.button_delete_restaurant)
        loadRestaurant(view)
        buttonDelete.setOnClickListener{
            deleteDialog(it)
        }
        mainView = view;
    }


    private fun loadRestaurant(view: View)  {
        mName = view.findViewById(R.id.text_name)
        mLocation = view.findViewById(R.id.text_location)
        mType = view.findViewById(R.id.text_type)
        mNote = view.findViewById(R.id.text_note)
        mFoodRating = view.findViewById(R.id.rate_food)
        mPersonellRating = view.findViewById(R.id.rate_personal)
        mLocationRating = view.findViewById(R.id.rate_location)
        mAtmosphereRating = view.findViewById(R.id.rate_atmosphere)
        mType = view.findViewById(R.id.spinner_type)
        val converter = EnumConverters(requireContext())

        arguments?.let {
            val tmp = restaurantViewModel.getItem(it.getInt("restaurantId"))
            tmp.observe(viewLifecycleOwner, Observer { item ->
                selectedRestaurant = item
                mName?.setText(item.name)
                mLocation?.setText(item.location)
                fillSpinner(mType!!, converter, item.type)
                mNote?.setText(item.name)
                setRating(mFoodRating, item.ratingFood)
                setRating(mPersonellRating, item.ratingPersonnel)
                setRating(mLocationRating, item.ratingLocation)
                setRating(mAtmosphereRating, item.ratingAtmosphere)
            })
        }
    }

    private fun updateAction(view: View?) {
        val converter = EnumConverters(requireContext());
        view?.let {
            val newRest: Restaurant = Restaurant()
            newRest.id = selectedRestaurant?.id!!
            newRest.created = selectedRestaurant?.created!!
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
                restaurantViewModel.update(newRest)
                it.findNavController().navigate(R.id.navigation_restaurant)
            }
        }
    }

    private fun deleteAction(view: View) {
        view.let {
            restaurantViewModel.delete(selectedRestaurant)
            it.findNavController().navigate(R.id.navigation_restaurant)
        }
    }

    private fun deleteDialog(view: View) {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it, R.style.AlertDialogTheme)
            builder.apply {
                setPositiveButton(R.string.confirm,
                        DialogInterface.OnClickListener { dialog, _ ->
                            deleteAction(view)
                            dialog.dismiss()
                        })
                setNegativeButton(R.string.cancel,
                        DialogInterface.OnClickListener { dialog, _ ->
                            dialog.cancel()
                        })
            }
            builder.setMessage("").setTitle(R.string.restaurant_delete)

            builder.create()
        }
        alertDialog?.show()
    }

    private fun setRating(view: RatingBar?, value: Float) {
        if(value == -1.0F)  view?.visibility = View.GONE
        else view?.rating = value
    }

    private fun fillSpinner(spinner: Spinner, converter: EnumConverters, curValue: RestaurantTypeEnum) {
        val values = enumValues<RestaurantTypeEnum>()
        val items: MutableList<String> = mutableListOf()
        var curPosition: Int = 0
        values.forEach {
            items.add(converter.convertRestaurantTypeEnum(it))
            if(it == curValue) curPosition = items.lastIndex
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
        spinner.setSelection(curPosition)
    }

    companion object {
        fun newInstance(): RestaurantEditFragment = RestaurantEditFragment()
    }
}