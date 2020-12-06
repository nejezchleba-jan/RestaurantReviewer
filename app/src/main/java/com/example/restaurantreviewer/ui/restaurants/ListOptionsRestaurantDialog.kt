package com.example.restaurantreviewer.ui.restaurants

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.adapters.RestaurantAdapter
import com.example.restaurantreviewer.enums.RestaurantFilterEnum
import com.example.restaurantreviewer.enums.RestaurantGroupingEnum
import com.example.restaurantreviewer.enums.RestaurantOrderEnum
import com.example.restaurantreviewer.enums.RestaurantTypeEnum
import com.example.restaurantreviewer.utils.EnumConverters

class ListOptionsRestaurantDialog(adapter: RestaurantAdapter) : DialogFragment() {
    private val restaurantAdapter: RestaurantAdapter = adapter
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
        return inflater.inflate(R.layout.list_options_layout, container, false)
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        val mSpinnerGroupBy: Spinner = view.findViewById(R.id.spinner_group_by)
        //val mSpinnerOrderBy: Spinner = view.findViewById(R.id.spinner_order)
        val mSpinnerFilterBy: Spinner = view.findViewById(R.id.spinner_filter)
        val mTypeFilter: Spinner = view.findViewById(R.id.filter_value_type)
        val mButtonBack: Button = view.findViewById(R.id.button_back)
        val mButtonApply: Button = view.findViewById(R.id.button_apply)
        val converter = EnumConverters(requireContext())
        sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)!!

        val groupBy = converter.convertRestaurantGroupingString(sharedPreferences.getString("groupBy_rest",
                        converter.convertRestaurantGroupingEnum(RestaurantGroupingEnum.DATE))!!)
        val orderBy = converter.convertRestaurantOrderString(sharedPreferences.getString("orderBy_rest",
                converter.convertRestaurantOrderEnum(RestaurantOrderEnum.NAME))!!)
        val filterBy = converter.convertRestaurantFilterString(sharedPreferences.getString("filter_rest",
                converter.convertRestaurantFilterEnum(RestaurantFilterEnum.NONE))!!)
        val filterVal = sharedPreferences.getString("filterVal_rest", "")!!

        fillSpinnerGroupBy(mSpinnerGroupBy, converter, groupBy)
        //fillSpinnerOrderBy(mSpinnerOrderBy, converter, orderBy)
        fillSpinnerFilterBy(mSpinnerFilterBy, converter, filterBy)
        fillSpinnerType(mTypeFilter, converter)
        switchFilterValue(mainView!!, filterBy, filterVal)
        mSpinnerFilterBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                switchFilterValue(mainView!!, converter.convertRestaurantFilterString(mSpinnerFilterBy.selectedItem.toString()), filterVal)
            }

        }

        mButtonBack.setOnClickListener{
            dialog?.dismiss()
        }

        mButtonApply.setOnClickListener {
            applyListOptions(view)
            dialog?.dismiss()
        }


    }

    private fun fillSpinnerGroupBy(spinner: Spinner, converter: EnumConverters, curValue: RestaurantGroupingEnum) {
        val values = enumValues<RestaurantGroupingEnum>()
        val items: MutableList<String> = mutableListOf()
        var curPosition: Int = 0
        values.forEach {
            items.add(converter.convertRestaurantGroupingEnum(it))
            if(it == curValue) curPosition = items.lastIndex
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
        spinner.setSelection(curPosition)
    }

    private fun fillSpinnerOrderBy(spinner: Spinner, converter: EnumConverters, curValue: RestaurantOrderEnum) {
        val values = enumValues<RestaurantOrderEnum>()
        val items: MutableList<String> = mutableListOf()
        var curPosition: Int = 0
        values.forEach {
            items.add(converter.convertRestaurantOrderEnum(it))
            if(it == curValue) curPosition = items.lastIndex
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
        spinner.setSelection(curPosition)
    }

    private fun fillSpinnerFilterBy(spinner: Spinner, converter: EnumConverters, curValue: RestaurantFilterEnum) {
        val values = enumValues<RestaurantFilterEnum>()
        val items: MutableList<String> = mutableListOf()
        var curPosition: Int = 0
        values.forEach {
            items.add(converter.convertRestaurantFilterEnum(it))
            if(it == curValue) curPosition = items.lastIndex
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
        spinner.setSelection(curPosition)
    }

    private fun fillSpinnerType(spinner: Spinner, converter: EnumConverters) {
        val values = enumValues<RestaurantTypeEnum>()
        val items: MutableList<String> = mutableListOf()
        var curPosition: Int = 0
        values.forEach {
            items.add(converter.convertRestaurantTypeEnum(it))
            if(it == converter.convertRestaurantTypeString(filterVal)) curPosition = items.lastIndex
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
        spinner.setSelection(curPosition)
    }

    private fun switchFilterValue(view: View, filter: RestaurantFilterEnum, curFilterVal: String) {
        val mValueLayout: LinearLayout = view.findViewById(R.id.layout_filter_value)
        val mEditTextFilter: EditText = view.findViewById(R.id.filter_value_text)
        val mRatingFilter: RatingBar = view.findViewById(R.id.filter_value_rating)
        val mTypeFilter: Spinner = view.findViewById(R.id.filter_value_type)
        filterVal = curFilterVal
        if(restaurantAdapter.filter != filter) filterVal = ""

        when(filter) {
            RestaurantFilterEnum.NONE -> {
                mValueLayout.visibility = View.GONE
            }
            RestaurantFilterEnum.TYPE -> {
                mValueLayout.visibility = View.VISIBLE
                mEditTextFilter.visibility = View.GONE
                mRatingFilter.visibility = View.GONE
                mTypeFilter.visibility = View.VISIBLE
                mTypeFilter.setSelection(0)
            }
            RestaurantFilterEnum.RATING_LESS -> {
                mValueLayout.visibility = View.VISIBLE
                mEditTextFilter.visibility = View.GONE
                mRatingFilter.visibility = View.VISIBLE
                mTypeFilter.visibility = View.GONE
                mRatingFilter.rating = if(filterVal.isEmpty()) 0.0F else filterVal.toFloat()
            }
            RestaurantFilterEnum.RATING_MORE -> {
                mValueLayout.visibility = View.VISIBLE
                mEditTextFilter.visibility = View.GONE
                mRatingFilter.visibility = View.VISIBLE
                mTypeFilter.visibility = View.GONE
                mRatingFilter.rating = if(filterVal.isEmpty()) 0.0F else filterVal.toFloat()
            } else -> {
                mValueLayout.visibility = View.VISIBLE
                mEditTextFilter.visibility = View.VISIBLE
                mRatingFilter.visibility = View.GONE
                mTypeFilter.visibility = View.GONE
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
            putString("filterVal_rest", filterVal)
            putString("filter_rest", mSpinnerFilterBy.selectedItem.toString())
            putString("groupBy_rest", mSpinnerGroupBy.selectedItem.toString())
            //putString("orderBy_rest", mSpinnerOrderBy.selectedItem.toString())
            apply()
        }

        restaurantAdapter.changeGrouping(converter.convertRestaurantGroupingString(mSpinnerGroupBy.selectedItem.toString()))
        //restaurantAdapter.changeOrder(converter.convertRestaurantOrderString(mSpinnerOrderBy.selectedItem.toString()))
        restaurantAdapter.changeFilter(converter.convertRestaurantFilterString(mSpinnerFilterBy.selectedItem.toString()), filterVal)
        restaurantAdapter.applyChangesToList(converter)
    }

    private fun setFilterVal(view: View) {
        val mValueLayout: LinearLayout = view.findViewById(R.id.layout_filter_value)
        val mEditTextFilter: EditText = view.findViewById(R.id.filter_value_text)
        val mRatingFilter: RatingBar = view.findViewById(R.id.filter_value_rating)
        val mTypeFilter: Spinner = view.findViewById(R.id.filter_value_type)

        when {
            mValueLayout.visibility == View.GONE -> filterVal = ""
            mRatingFilter.visibility == View.VISIBLE -> filterVal = mRatingFilter.rating.toString()
            mTypeFilter.visibility == View.VISIBLE -> filterVal = mTypeFilter.selectedItem.toString()
            mEditTextFilter.visibility == View.VISIBLE -> filterVal = mEditTextFilter.editableText.toString()
            else -> filterVal = ""
        }
    }

    companion object {
        fun newInstance(): RestaurantFragment = RestaurantFragment()
    }
}