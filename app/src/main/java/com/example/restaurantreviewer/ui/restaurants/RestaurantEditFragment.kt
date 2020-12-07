package com.example.restaurantreviewer.ui.restaurants

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
import com.example.restaurantreviewer.enums.RestaurantTypeEnum
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.EnumConverters
import com.squareup.picasso.Picasso


class RestaurantEditFragment : Fragment() {
    private lateinit var restaurantViewModel: RestaurantViewModel
    private val REQUEST_READ_STORAGE_REQUEST_CODE = 0
    private val LOAD_IMAGE_RESULTS = 1
    private var mName: EditText? = null
    private var mLocation: EditText? = null
    private var mNote: EditText? = null
    private var mFoodRating: RatingBar? = null
    private var mPersonellRating: RatingBar? = null
    private var mLocationRating: RatingBar? = null
    private var mAtmosphereRating: RatingBar? = null
    private var mType: Spinner? = null
    private var mainView: View? = null
    private var mImageView: ImageView? = null
    private var mImageUri: Uri = Uri.EMPTY
    private var mButtonDeleteImage: ImageButton? = null
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
        mImageView = view.findViewById(R.id.image_restaurant)
        mButtonDeleteImage = view.findViewById(R.id.button_delete_image)

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
                if(item.image != null) {
                    Picasso.with(requireContext()).load(Uri.parse(item.image))
                        .fit()
                        .centerCrop()
                        .into(mImageView)
                    mImageUri = Uri.parse(item.image)
                    mButtonDeleteImage?.visibility = View.VISIBLE
                }
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
            newRest.image = if(mImageUri != Uri.EMPTY) mImageUri.toString() else null
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
}