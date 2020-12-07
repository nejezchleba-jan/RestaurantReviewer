package com.example.restaurantreviewer.ui.food

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.adapters.FoodAdapter
import com.example.restaurantreviewer.adapters.RestaurantAdapter
import com.example.restaurantreviewer.enums.*
import com.example.restaurantreviewer.utils.EnumConverters
import com.example.restaurantreviewer.utils.RatingInputFilter
import kotlinx.android.synthetic.main.fragment_edit_food.*

class ListOptionsFoodDialog(adapter: FoodAdapter) : DialogFragment() {
    private val foodAdapter: FoodAdapter = adapter
    private var filterVal: String = adapter.filterVal
    private var mainView: View? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_Light_DarkActionBar);
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_options_layout_food, container, false)
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        val mSpinnerGroupBy: Spinner = view.findViewById(R.id.spinner_group_by)
        //val mSpinnerOrderBy: Spinner = view.findViewById(R.id.spinner_order)
        val mSpinnerFilterBy: Spinner = view.findViewById(R.id.spinner_filter)
        val mFoodRating: EditText = view.findViewById(R.id.filter_value_rating)
        val mButtonBack: Button = view.findViewById(R.id.button_back)
        val mButtonApply: Button = view.findViewById(R.id.button_apply)
        val converter = EnumConverters(requireContext())
        RatingInputFilter().setTextWatcher(mFoodRating)
        sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)!!

        val groupBy = converter.convertFoodGroupingString(sharedPreferences.getString("groupBy_food",
                        converter.convertRestaurantGroupingEnum(RestaurantGroupingEnum.DATE))!!)
        /*val orderBy = converter.convertRestaurantOrderString(sharedPreferences.getString("orderBy_rest",
                converter.convertRestaurantOrderEnum(RestaurantOrderEnum.NAME))!!)*/
        val filterBy = converter.convertFoodFilterString(sharedPreferences.getString("filter_food",
                converter.convertFoodFilterEnum(FoodFilterEnum.NONE))!!)
        val filterVal = sharedPreferences.getString("filterVal_food", "")!!

        fillSpinnerGroupBy(mSpinnerGroupBy, converter, groupBy)
        //fillSpinnerOrderBy(mSpinnerOrderBy, converter, orderBy)
        fillSpinnerFilterBy(mSpinnerFilterBy, converter, filterBy)
        switchFilterValue(mainView!!, filterBy, filterVal)
        mSpinnerFilterBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                switchFilterValue(mainView!!, converter.convertFoodFilterString(mSpinnerFilterBy.selectedItem.toString()), filterVal)
            }

        }

        button_rate_up.setOnClickListener{
            val curVal = mFoodRating?.text.toString().toInt()
            mFoodRating?.setText((curVal+1).toString())
        }

        button_rate_down.setOnClickListener{
            val curVal = mFoodRating?.text.toString().toInt()
            mFoodRating?.setText((curVal-1).toString())
        }

        mButtonBack.setOnClickListener{
            dialog?.dismiss()
        }

        mButtonApply.setOnClickListener {
            applyListOptions(view)
            dialog?.dismiss()
        }


    }

    private fun fillSpinnerGroupBy(spinner: Spinner, converter: EnumConverters, curValue: FoodGroupingEnum) {
        val values = enumValues<FoodGroupingEnum>()
        val items: MutableList<String> = mutableListOf()
        var curPosition: Int = 0
        values.forEach {
            items.add(converter.convertFoodGroupingEnum(it))
            if(it == curValue) curPosition = items.lastIndex
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
        spinner.setSelection(curPosition)
    }

    private fun fillSpinnerOrderBy(spinner: Spinner, converter: EnumConverters, curValue: FoodOrderEnum) {
        val values = enumValues<FoodOrderEnum>()
        val items: MutableList<String> = mutableListOf()
        var curPosition: Int = 0
        values.forEach {
            items.add(converter.convertFoodOrderEnum(it))
            if(it == curValue) curPosition = items.lastIndex
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
        spinner.setSelection(curPosition)
    }

    private fun fillSpinnerFilterBy(spinner: Spinner, converter: EnumConverters, curValue: FoodFilterEnum) {
        val values = enumValues<FoodFilterEnum>()
        val items: MutableList<String> = mutableListOf()
        var curPosition: Int = 0
        values.forEach {
            items.add(converter.convertFoodFilterEnum(it))
            if(it == curValue) curPosition = items.lastIndex
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
        spinner.setSelection(curPosition)
    }


    private fun switchFilterValue(view: View, filter: FoodFilterEnum, curFilterVal: String) {
        val mValueLayout: LinearLayout = view.findViewById(R.id.layout_filter_value)
        val mEditTextFilter: EditText = view.findViewById(R.id.filter_value_text)
        val mRatingLayout: ConstraintLayout = view.findViewById(R.id.layout_filter_rate_food)
        val mRatingFilter: EditText = view.findViewById(R.id.filter_value_rating)
        filterVal = curFilterVal
        if(foodAdapter.filter != filter) filterVal = ""

        when(filter) {
            FoodFilterEnum.NONE -> {
                mValueLayout.visibility = View.GONE
            }
            FoodFilterEnum.RATING_LESS -> {
                mValueLayout.visibility = View.VISIBLE
                mEditTextFilter.visibility = View.GONE
                mRatingLayout.visibility = View.VISIBLE
                mRatingFilter.setText(if(filterVal.isEmpty()) "0" else filterVal)
            }
            FoodFilterEnum.RATING_MORE -> {
                mValueLayout.visibility = View.VISIBLE
                mEditTextFilter.visibility = View.GONE
                mRatingLayout.visibility = View.VISIBLE
                mRatingFilter.setText(if(filterVal.isEmpty()) "0" else filterVal)
            } else -> {
                mValueLayout.visibility = View.VISIBLE
                mEditTextFilter.visibility = View.VISIBLE
                mRatingLayout.visibility = View.GONE
                mEditTextFilter.setText(if(filterVal.isEmpty()) "" else filterVal)
                }
            }
    }

    fun applyListOptions(view: View) {
        val converter = EnumConverters(requireContext())
        val mSpinnerGroupBy: Spinner = view.findViewById(R.id.spinner_group_by)
        //val mSpinnerOrderBy: Spinner = view.findViewById(R.id.spinner_order)
        val mSpinnerFilterBy: Spinner = view.findViewById(R.id.spinner_filter)
        setFilterVal(view)

        with(sharedPreferences.edit()) {
            putString("filterVal_food", filterVal)
            putString("filter_food", mSpinnerFilterBy.selectedItem.toString())
            putString("groupBy_food", mSpinnerGroupBy.selectedItem.toString())
            //putString("orderBy_food", mSpinnerOrderBy.selectedItem.toString())
            apply()
        }

        foodAdapter.changeGrouping(converter.convertFoodGroupingString(mSpinnerGroupBy.selectedItem.toString()))
        //restaurantAdapter.changeOrder(converter.convertRestaurantOrderString(mSpinnerOrderBy.selectedItem.toString()))
        foodAdapter.changeFilter(converter.convertFoodFilterString(mSpinnerFilterBy.selectedItem.toString()), filterVal)
        foodAdapter.applyChangesToList()
    }

    private fun setFilterVal(view: View) {
        val mValueLayout: LinearLayout = view.findViewById(R.id.layout_filter_value)
        val mRateLayout: ConstraintLayout = view.findViewById(R.id.layout_filter_rate_food)
        val mEditTextFilter: EditText = view.findViewById(R.id.filter_value_text)
        val mRatingFilter: EditText = view.findViewById(R.id.filter_value_rating)

        when {
            mValueLayout.visibility == View.GONE -> filterVal = ""
            mRateLayout.visibility == View.VISIBLE -> filterVal = mRatingFilter.editableText.toString()
            mEditTextFilter.visibility == View.VISIBLE -> filterVal = mEditTextFilter.editableText.toString()
            else -> filterVal = ""
        }
    }

    companion object {
        fun newInstance(): FoodAddFragment = FoodAddFragment()
    }
}