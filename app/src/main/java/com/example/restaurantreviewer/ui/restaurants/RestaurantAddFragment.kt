package com.example.restaurantreviewer.ui.restaurants

import android.Manifest
import android.app.Activity.RESULT_OK
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
import com.example.restaurantreviewer.enums.RestaurantTypeEnum
import com.example.restaurantreviewer.model.Restaurant
import com.example.restaurantreviewer.utils.EnumConverters
import com.example.restaurantreviewer.utils.TransformRoundedImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_restaurant.*


class RestaurantAddFragment : Fragment() {
    private lateinit var restaurantViewModel: RestaurantViewModel
    private val REQUEST_READ_STORAGE_REQUEST_CODE = 0
    private val LOAD_IMAGE_RESULTS = 1
    private var mName: EditText? = null
    private var mLocation: EditText? = null
    private var mType: Spinner? = null
    private var mNote: EditText? = null
    private var mFoodRating: RatingBar? = null
    private var mPersonellRating: RatingBar? = null
    private var mLocationRating: RatingBar? = null
    private var mAtmosphereRating: RatingBar? = null
    private var mImageView: ImageView? = null
    private var mImageUri: Uri = Uri.EMPTY
    private var mButtonDeleteImage: ImageButton? = null
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
        if (resultCode == RESULT_OK && requestCode == LOAD_IMAGE_RESULTS) {
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

    private fun fillSpinner(spinner: Spinner, converter: EnumConverters) {
        val values = enumValues<RestaurantTypeEnum>()
        val items: MutableList<String> = mutableListOf()
        values.forEach {
            items.add(converter.convertRestaurantTypeEnum(it))
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        )
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        spinner.adapter = spinnerArrayAdapter
    }

    private fun insertAction(view: View?) {
        val converter = EnumConverters(requireContext())
        view?.let {
            mName = it.findViewById(R.id.text_name)
            mLocation = it.findViewById(R.id.text_location)
            mType = it.findViewById(R.id.spinner_type)
            mNote = it.findViewById(R.id.text_note)
            mFoodRating = it.findViewById(R.id.rate_food)
            mPersonellRating = it.findViewById(R.id.rate_personal)
            mLocationRating = it.findViewById(R.id.rate_location)
            mAtmosphereRating = it.findViewById(R.id.rate_atmosphere)

            val newRest = Restaurant()
            newRest.image = mImageUri.toString()
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
                Toast.makeText(
                    activity?.applicationContext,
                    R.string.toast_empty_name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                restaurantViewModel.insert(newRest)
                it.findNavController().navigate(R.id.navigation_restaurant)
            }
        }
    }

    companion object {
        fun newInstance(): RestaurantAddFragment = RestaurantAddFragment()
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