package com.example.restaurantreviewer.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantreviewer.R
import com.example.restaurantreviewer.adapters.FoodAdapter
import com.example.restaurantreviewer.model.Food
import kotlinx.android.synthetic.main.fragment_food.*
import java.time.Instant
import java.time.LocalDate

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    /*override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_food, container, false)
        *//*val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*//*
        return root
    }*/

    private fun testFood(): List<Food> {
        var i = Food()
        i.id = 1;
        i.name = "Mňamka"
        i.rating = 50
        i.created = LocalDate.of(2020,1,1)

        var j = Food()
        j.id = 2
        j.name = "Mňamka B"
        j.rating = 80
        return listOf(i, j)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food, container, false)
    }


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView node initialized here
        recycler_food.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = FoodAdapter(testFood())
        }
    }

    companion object {
        fun newInstance(): DashboardFragment = DashboardFragment()
    }
}