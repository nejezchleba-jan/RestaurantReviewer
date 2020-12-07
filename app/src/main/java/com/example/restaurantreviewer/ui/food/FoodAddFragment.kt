package com.example.restaurantreviewer.ui.food

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.enums.FoodCurrencyEnum
import com.example.restaurantreviewer.model.Food
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.EnumConverters
import com.example.restaurantreviewer.utils.JsonConverters
import com.example.restaurantreviewer.utils.RatingInputFilter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_food.view.*
import kotlinx.android.synthetic.main.fragment_edit_food.*
import java.time.LocalDate


class FoodAddFragment : Fragment() {
    private lateinit var restaurantsList: MutableList<Restaurant>
    private val REQUEST_READ_STORAGE_REQUEST_CODE = 0
    private val LOAD_IMAGE_RESULTS = 1
    private lateinit var foodViewModel: FoodViewModel
    private var mName: EditText? = null
    private var mImageView: ImageView? = null
    private var mPrice: EditText? = null
    private var mOrderDate: DatePicker? = null
    private var mNote: EditText? = null
    private var mFoodRating: EditText? = null
    private var mainView: View? = null
    private var mButtonDeleteImage: ImageButton? = null
    private var mImageUri: Uri =  Uri.EMPTY


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
        restaurantsList = JsonConverters().getListRestaurantFromJson(arguments?.getString("restaurants")!!)
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_food, container, false)
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val converter = EnumConverters(requireContext())
        fillSpinnerRestaurants(view.findViewById(R.id.spinner_food_restaurant))
        fillSpinnerCurrency(view.findViewById(R.id.spinner_currency), converter)
        mFoodRating = view.findViewById(R.id.rate_food)
        mImageView  = view.findViewById(R.id.image_food)
        RatingInputFilter().setTextWatcher(mFoodRating!!)
        mButtonDeleteImage = view.findViewById(R.id.button_delete_image)
        mainView = view;
        button_rate_up.setOnClickListener{
            val curVal = mFoodRating?.text.toString().toInt()
            mFoodRating?.setText((curVal+1).toString())
        }

        button_rate_down.setOnClickListener{
            val curVal = mFoodRating?.text.toString().toInt()
            mFoodRating?.setText((curVal-1).toString())
        }

        mImageView?.setOnClickListener{
            if(hasReadPermissions()) {
                val gallery = Intent(
                    Intent.ACTION_OPEN_DOCUMENT,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                gallery.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                startActivityForResult(gallery, LOAD_IMAGE_RESULTS)
            } else {
                requestAppPermissions()
            }

        }

        mButtonDeleteImage?.setOnClickListener{
            mImageUri = Uri.EMPTY
            mImageView?.setImageURI(mImageUri)
            mImageView?.setImageResource(R.drawable.ic_image)
            mButtonDeleteImage?.visibility = View.GONE
        }
    }

    private fun fillSpinnerRestaurants(spinner: Spinner) {
        val items: MutableList<String> = mutableListOf()
        restaurantsList.forEach {
            items.add(it.name)
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
    }

    private fun fillSpinnerCurrency(spinner: Spinner, converters: EnumConverters) {
        val values = enumValues<FoodCurrencyEnum>()
        val items: MutableList<String> = mutableListOf()
        values.forEach {
            items.add(converters.convertFoodCurrencyEnum(it))
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
    }

    private fun insertAction(view: View?) {
        val converter = EnumConverters(requireContext());
        view?.let {
            mName = it.findViewById(R.id.text_name)
            mPrice = it.findViewById(R.id.text_price)
            mNote = it.findViewById(R.id.text_note)
            mOrderDate = it.findViewById(R.id.date_ordered)
            mFoodRating = it.findViewById(R.id.rate_food)

            val newFood: Food = Food()
            newFood.restaurantId = restaurantsList[it.spinner_food_restaurant.selectedItemPosition].id
            newFood.name = mName?.editableText.toString()
            newFood.image = mImageUri.toString()
            newFood.price = if(mPrice?.editableText.toString().isEmpty()) 0.0 else mPrice?.editableText.toString().toDouble()
            newFood.currency = converter.convertFoodCurrencyString(it.spinner_currency.selectedItem.toString())
            newFood.note = mNote?.editableText.toString()
            newFood.orderDate = LocalDate.of(mOrderDate?.year!!, mOrderDate?.month!!, mOrderDate?.dayOfMonth!!, )
            newFood.rating = mFoodRating?.editableText.toString().toInt()

            if(newFood.name.isEmpty() || newFood.name.isBlank()) {
                Toast.makeText(activity?.applicationContext, R.string.toast_empty_name, Toast.LENGTH_SHORT).show()
            } else {
                foodViewModel.insert(newFood)
                it.findNavController().navigate(R.id.navigation_food)
            }
        }
    }

    companion object {
        fun newInstance(): FoodAddFragment = FoodAddFragment()
    }

    private fun requestAppPermissions() {
        if (hasReadPermissions()) {
            return
        }
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), REQUEST_READ_STORAGE_REQUEST_CODE
        ) // your request code
    }

    private fun hasReadPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity().applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == LOAD_IMAGE_RESULTS) {
            mImageUri = data?.data!!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                requireContext().contentResolver.takePersistableUriPermission(
                    mImageUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                mImageView?.background = null
                Picasso.with(context).load(mImageUri)
                    .fit()
                    .centerCrop()
                    .into(mImageView)
                mButtonDeleteImage?.visibility = View.VISIBLE
            }
        }
    }
}