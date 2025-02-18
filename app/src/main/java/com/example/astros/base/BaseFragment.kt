package com.example.astros.base

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.utils.GeneralUtil
import com.google.firebase.auth.FirebaseAuth

open class BaseFragment : Fragment() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        })
    }

    private fun handleBackPressed() {
        val currentFragmentId = findNavController().currentDestination?.id

        // Check if user is logged in and on DashboardFragment, prevent back navigation
        if (auth.currentUser != null && currentFragmentId == R.id.dashboardFragment) {
            GeneralUtil.showToast(requireContext(), "You are already on the dashboard")
            return
        }

        // Default back navigation
        findNavController().popBackStack()
    }
}
