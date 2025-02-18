package com.example.astros.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.base.BaseFragment
import com.example.astros.databinding.FragmentDashboardBinding
import com.example.astros.service.SharedPreference
import com.example.astros.utils.GeneralUtil
import com.example.astros.utils.GeneralUtil.showToast
import com.example.astros.utils.NavigationUtils
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : BaseFragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var drawerLayout: DrawerLayout
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        dashboardViewModel = DashboardViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        setupObserver(binding)
        drawerLayout = binding.drawerLayout
        sharedPreference.getToken()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = sharedPreference.getToken() // Get token from storage
        if (token != null) {
            dashboardViewModel.fetchUserProfile(token)
        } else {
            showToast(requireContext(), "User not logged in")
        }

        // Setup Drawer Toggle
        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigation(menuItem)
            true
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_notifications -> {
                    showToast(requireContext(), "Notifications clicked")
                    true
                }

                R.id.action_account -> {
                    showToast(requireContext(), "Account clicked")
                    NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_dashboardFragment_to_profileFragment)
                    true
                }

                else -> false
            }
        }

        binding.btnLogout.setOnClickListener {
            signOut()
        }

    }

    private fun setupObserver(binding: FragmentDashboardBinding) {

        dashboardViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                Log.d("UserProfile", "Name: ${profile}")

                binding.tvUserName.text = "Hello ${profile.userName}"
            }
        }

        dashboardViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let { showToast(requireContext(), "Error: $it") }
        }

    }

    private fun handleNavigation(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.nav_home -> {
                showToast(requireContext(), "Home Clicked")
            }

            R.id.nav_profile -> {
                showToast(requireContext(), "Profile Clicked")
                NavigationUtils.navigateWithAnimation(
                    findNavController(),
                    R.id.action_dashboardFragment_to_profileFragment
                )
            }

            R.id.nav_settings -> {
                showToast(requireContext(), "Settings Clicked")
            }

            R.id.nav_logout -> signOut()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun signOut() {
        sharedPreference.clearToken()
        sharedPreference.saveLoginStatus(false)
        showToast(requireContext(), "Logged out successfully")
        findNavController().navigate(R.id.loginFragment, null,
            androidx.navigation.NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true) // Clears entire back stack
                .build()
        )
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (System.currentTimeMillis() - backPressedTime < 2000) {
                        requireActivity().finishAffinity() // Exit app
                    } else {
                        backPressedTime = System.currentTimeMillis()
                        showToast(
                            requireContext(),
                            "Press back again to exit"
                        )
                    }
                }
            }
        )
    }
}