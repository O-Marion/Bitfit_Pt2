package com.bitfit.bitfitpart2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var addFood : Button
    private lateinit var bottomNavigationView: BottomNavigationView

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if ( result.data?.action == "food_recorded"){
            // Set dashboard selection
            bottomNavigationView.selectedItemId = R.id.nav_dashboard
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // define your fragments here
        val foodLogFragment: Fragment = FoodLogFragment()
        val dashboardFragment: Fragment = DashboardFragment()

         addFood  = findViewById(R.id.btnAdd)
         bottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_log -> fragment = foodLogFragment
                R.id.nav_dashboard -> fragment = dashboardFragment
            }
            replaceFragment(fragment)
            true
        }

        addFood.setOnClickListener {
            val intent = Intent(this, AddFoodActivity::class.java)
            startForResult.launch(intent)
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.nav_log

        replaceFragment(FoodLogFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container_frame_layout, fragment)
        fragmentTransaction.commit()
    }

}