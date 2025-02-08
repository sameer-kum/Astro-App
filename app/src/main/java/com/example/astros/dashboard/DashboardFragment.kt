package com.example.astros.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.astros.R
import com.example.astros.databinding.FragmentDashboardBinding
import com.example.astros.service.SharedPreference

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())

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
        Log.d("Username", "getPassword: ${sharedPreference.getPassword()}")
        Log.d("Username", "getGender: ${sharedPreference.getGender()}")
        Log.d("Username", "getMaritalStatus: ${sharedPreference.getMaritalStatus()}")
        Log.d("Username", "getDOB: ${sharedPreference.getDOB()}")
        Log.d("Username", "getBirthTime: ${sharedPreference.getBirthTime()}")
        Log.d("Username", "getBirthPlace: ${sharedPreference.getBirthPlace()}")
        Log.d("Username", "getSelectedLanguage: ${sharedPreference.getSelectedLanguage()}")

    }
 }