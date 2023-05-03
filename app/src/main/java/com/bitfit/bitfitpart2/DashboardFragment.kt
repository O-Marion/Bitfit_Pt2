package com.bitfit.bitfitpart2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {

    private lateinit var foodDao : FoodDao
    private lateinit var tvAvgCalories: TextView
    private lateinit var tvMinCalories: TextView
    private lateinit var tvMaxCalories: TextView
    private var totalCalories = 0
    private var averageCalories = 0
    private var minimumCalories = Int.MAX_VALUE
    private var maximumCalories = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodDao = (activity?.application as FoodApplication).db.foodDao()
        tvAvgCalories = view.findViewById(R.id.tvAvgCalories)
        tvMinCalories = view.findViewById(R.id.tvMinCalories)
        tvMaxCalories = view.findViewById(R.id.tvMaxCalories)

        lifecycleScope.launch (Dispatchers.IO){
            foodDao.getAll().collect{ foodList ->

                foodList.forEach {foodEntity ->
                    totalCalories += foodEntity.calories?.toInt() ?: 0
                    averageCalories = totalCalories / foodList.size

                    if ((foodEntity.calories?.toInt() ?: 0) < minimumCalories){
                        minimumCalories = foodEntity.calories?.toInt() ?: 0
                    }

                    if((foodEntity.calories?.toInt() ?: 0) > maximumCalories){
                        maximumCalories = foodEntity.calories?.toInt() ?: 0
                    }
                }

                withContext(Dispatchers.Main){
                    tvAvgCalories.text = averageCalories.toString()
                    tvMinCalories.text = minimumCalories.toString()
                    tvMaxCalories.text = maximumCalories.toString()
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() : DashboardFragment = DashboardFragment()
    }
}