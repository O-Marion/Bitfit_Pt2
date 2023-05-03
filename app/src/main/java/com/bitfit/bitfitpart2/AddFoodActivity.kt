package com.bitfit.bitfitpart2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddFoodActivity : AppCompatActivity() {

    private lateinit var foodDao: FoodDao
    private lateinit var btnRecord: Button
    private lateinit var foodEditText: EditText
    private lateinit var caloriesEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        foodDao = (application as FoodApplication).db.foodDao()
        btnRecord = findViewById(R.id.btnRecord)
        foodEditText = findViewById(R.id.etFood)
        caloriesEditText = findViewById(R.id.etCalories)

        btnRecord.setOnClickListener {

            if (foodEditText.text.isEmpty() || caloriesEditText.text.isEmpty()){
                Toast.makeText(this,"Please enter values for Food and Calories", Toast.LENGTH_SHORT).show()
            }else{
                val food = FoodEntity(
                    name = foodEditText.text.toString(),
                    calories = caloriesEditText.text.toString()
                )
                insertFood(food)
            }
        }
    }

    private fun insertFood(foodEntity: FoodEntity){
        lifecycleScope.launch (Dispatchers.IO){
            foodDao.insert(foodEntity)
        }
        setResult(100, Intent("food_recorded"))
        this.finish()
    }
}