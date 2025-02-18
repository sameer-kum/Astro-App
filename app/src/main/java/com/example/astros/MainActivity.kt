package com.example.astros

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.astros.databinding.ActivityMainBinding
import com.example.astros.service.SharedPreference
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPreference: SharedPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPreference = SharedPreference(this)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        if (sharedPreference.isUserLoggedIn()) {
            navController.navigate(R.id.dashboardFragment)
            Log.d("MainActivity", "User is logged in")
        } else {
            navController.navigate(R.id.startFragment)
            Log.d("MainActivity", "User is not logged in")
        }
    }
}
