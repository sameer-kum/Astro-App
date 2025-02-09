package com.example.astros.utils

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.astros.R

object NavigationUtils {

    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left) // Ensures back navigation comes from left
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    fun navigateWithAnimation(navController: NavController, destinationId: Int) {
        navController.navigate(destinationId, null, navOptions)
    }
}
