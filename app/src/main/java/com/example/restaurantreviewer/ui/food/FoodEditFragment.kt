package com.example.restaurantreviewer.ui.food

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
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
import androidx.lifecycle.Observer
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


class FoodEditFragment : Fragment() {

    private lateinit var foodViewModel: FoodViewModel
    private val REQUEST_READ_STORAGE_REQUEST_CODE = 0
    private val LOAD_IMAGE_RESULTS = 1
    private var mName: EditText? = null
    private var mPrice: EditText? = null
    private var mOrderDate: DatePicker? = null
    private var mNote: EditText? = null
    private var mFoodRating: EditText? = null
    private var mainView: View? = null
    private var mRestaurants: Spinner? = null
    private var mCurrency: Spinner? = null
    private var selectedFood: Food? = null
    private var mImageView: ImageView? = null
    private var mImageUri: Uri = Uri.EMPTY
    private var mButtonDeleteImage: ImageButton? = null
    private var restaurantsList: MutableList<Restaurant> = mutableListOf()


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
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_edit_food, container, false)
        return root
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonDelete: ImageButton = view.findViewById(R.id.button_delete_restaurant)
        mainView = view
        mImageView = view.findViewById(R.id.image_food)
        mButtonDeleteImage = view.findViewById(R.id.button_delete_image)
        loadFood(view)

        buttonDelete.setOnClickListener{
            deleteDialog(it)
        }

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


    private fun loadFood(view: View)  {
        mName = view.findViewById(R.id.text_name)
        mPrice = view.findViewById(R.id.text_price)
        mNote = view.findViewById(R.id.text_note)
        mRestaurants = view.findViewById(R.id.spinner_food_restaurant)
        mOrderDate = view.findViewById(R.id.date_ordered)
        mFoodRating = view.findViewById(R.id.rate_food)
        RatingInputFilter().setTextWatcher(mFoodRating!!)
        mRestaurants = view.findViewById(R.id.spinner_food_restaurant)
        mCurrency = view.findViewById(R.id.spinner_currency)

        arguments?.let {
            val food = foodViewModel.getItem(it.getInt("foodId"))
            restaurantsList = JsonConverters().getListRestaurantFromJson(it.getString("restaurants")!!)
            food.observe(viewLifecycleOwner, Observer { item ->
                selectedFood = item
                mName?.setText(item.name)
                if(item.image != null) {
                    Picasso.with(context).load(Uri.parse(item.image))
                        .fit()
                        .centerCrop()
                        .into(mImageView)
                    mImageUri = Uri.parse(item.image)
                    mButtonDeleteImage?.visibility = View.VISIBLE
                }
                mPrice?.setText(item.price.toString())
                mOrderDate?.updateDate(item.orderDate?.year!!, item.orderDate?.monthValue!!, item.orderDate?.dayOfMonth!!)
                fillSpinnerRestaurants(mRestaurants!!, item.restaurantId!!)
                fillSpinnerCurrency(mCurrency!!, EnumConverters(requireContext()), item.currency!!)
                mNote?.setText(item.name)
                mFoodRating?.setText(item.rating.toString())
            })
        }
    }

    private fun updateAction(view: View?) {
        val converter = EnumConverters(requireContext());
        view?.let {
            val newFood: Food = Food()
            newFood.id = selectedFood?.id!!
            newFood.image = if(mImageUri != Uri.EMPTY) mImageUri.toString() else null
            newFood.restaurantId = restaurantsList[it.spinner_food_restaurant.selectedItemPosition].id
            newFood.name = mName?.editableText.toString()
            newFood.price = mPrice?.editableText.toString().toDouble()
            newFood.currency = converter.convertFoodCurrencyString(it.spinner_currency.selectedItem.toString())
            newFood.note = mNote?.editableText.toString()
            newFood.orderDate = LocalDate.of(mOrderDate?.year!!, mOrderDate?.month!!, mOrderDate?.dayOfMonth!!)
            newFood.rating = mFoodRating?.editableText.toString().toInt()
            if(newFood.name.isEmpty() || newFood.name.isBlank()) {
                Toast.makeText(activity?.applicationContext, R.string.toast_empty_name, Toast.LENGTH_SHORT).show()
            } else {
                foodViewModel.update(newFood)
                it.findNavController().navigate(R.id.navigation_food)
            }
        }
    }

    private fun deleteAction(view: View) {
        view.let {
            foodViewModel.delete(selectedFood)
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
            builder.setMessage("").setTitle(R.string.food_delete)
            builder.create()
        }
        alertDialog?.show()
    }

    private fun fillSpinnerRestaurants(spinner: Spinner, curRestId: Int) {
        val items: MutableList<String> = mutableListOf()
        var curPosition: Int = 0
        restaurantsList.forEach {
            items.add(it.name)
            if(it.id == curRestId) curPosition = items.lastIndex
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
        spinner.setSelection(curPosition)
    }

    private fun fillSpinnerCurrency(spinner: Spinner, converters: EnumConverters, curCurrencyEnum: FoodCurrencyEnum) {
        val values = enumValues<FoodCurrencyEnum>()
        val items: MutableList<String> = mutableListOf()
        var curPosition: Int = 0
        values.forEach {
            items.add(converters.convertFoodCurrencyEnum(it))
            if(it == curCurrencyEnum) curPosition = items.lastIndex
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.adapter = spinnerArrayAdapter
        spinner.setSelection(curPosition)
    }

    companion object {
        fun newInstance(): FoodEditFragment = FoodEditFragment()
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