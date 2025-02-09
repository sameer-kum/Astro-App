package com.example.astros.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.databinding.FragmentDashboardBinding
import com.example.astros.service.SharedPreference
import com.example.astros.utils.NavigationUtils
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Username", "getName: ${sharedPreference.getName()}")
        Log.d("Username", "getEmail: ${sharedPreference.getEmail()}")
        Log.d("Username", "getPhoneNo: ${sharedPreference.getPhoneNo()}")
        Log.d("Username", "getPassword: ${sharedPreference.getPassword()}")
        Log.d("Username", "getGender: ${sharedPreference.getGender()}")
        Log.d("Username", "getMaritalStatus: ${sharedPreference.getMaritalStatus()}")
        Log.d("Username", "getDOB: ${sharedPreference.getDOB()}")
        Log.d("Username", "getBirthTime: ${sharedPreference.getBirthTime()}")
        Log.d("Username", "getBirthPlace: ${sharedPreference.getBirthPlace()}")
        Log.d("Username", "getSelectedLanguage: ${sharedPreference.getSelectedLanguage()}")

        binding.btnLogout.setOnClickListener {
            getUserDetailsBeforeSignOut()
            signOut()

        }

    }

    private fun getUserDetailsBeforeSignOut() {
        val user = auth.currentUser
        if (user != null) {
            // Get user details
            val userEmail = user.email
            val userDisplayName = user.displayName
            val userPhoneNumber = user.phoneNumber

            // You can log these details or store them for future use
            Log.d("SignOut", "User Email: $userEmail")
            Log.d("SignOut", "User Display Name: $userDisplayName")
            Log.d("SignOut", "User Phone Number: $userPhoneNumber")
        }
    }

    private fun signOut() {
        auth.signOut()
        Toast.makeText(requireContext(), "Signed out successfully", Toast.LENGTH_SHORT).show()
        // Navigate back to LoginFragment or any other screen
        NavigationUtils.navigateWithAnimation(
            findNavController(),
            R.id.action_dashboardFragment_to_loginFragment
        )
    }
}