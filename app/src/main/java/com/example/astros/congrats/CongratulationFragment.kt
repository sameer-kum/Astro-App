package com.example.astros.congrats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.base.BaseFragment
import com.example.astros.databinding.FragmentCongratulationBinding
import com.example.astros.utils.NavigationUtils

class CongratulationFragment : BaseFragment() {

    private lateinit var binding: FragmentCongratulationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCongratulationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinue.setOnClickListener {
            NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_congratsFragment_to_dashboardFragment)
        }
    }

}