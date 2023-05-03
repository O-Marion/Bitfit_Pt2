package com.bitfit.bitfitpart2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodLogFragment : Fragment() {

    private val entries : MutableList<FoodEntity> = mutableListOf()
    private lateinit var foodDao : FoodDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodDao = (activity?.application as FoodApplication).db.foodDao()
        val rvFood = view.findViewById<RecyclerView>(R.id.rvFood)
        val foodAdapter = FoodAdapter(entries)

        rvFood?.adapter = foodAdapter
        rvFood?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).also {
            val dividerItemDecoration = DividerItemDecoration(requireContext(), it.orientation)
            rvFood.addItemDecoration(dividerItemDecoration)
        }

        lifecycleScope.launch (Dispatchers.IO){
            foodDao.getAll().collect{ foodList ->
                entries.clear()
                entries.addAll(foodList)
                withContext(Dispatchers.Main){
                    foodAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() : FoodLogFragment = FoodLogFragment()

    }
}