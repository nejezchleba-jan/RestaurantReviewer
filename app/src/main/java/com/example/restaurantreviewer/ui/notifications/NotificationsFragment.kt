package com.example.restaurantreviewer.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.ui.restaurants.RestaurantViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_about, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFoodCount: TextView = view.findViewById(R.id.num_food_reviewed)
        val mRestaurantCount: TextView = view.findViewById(R.id.num_restaurants_reviewed)
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        GlobalScope.launch {
            notificationsViewModel.getFoodCount()
            notificationsViewModel.getRestaurantCount()

            activity?.runOnUiThread{
                mFoodCount.text = notificationsViewModel.mListFoodCount.toString()
                mRestaurantCount.text = notificationsViewModel.mListRestaurantCount.toString()
            }
        }
    }
}