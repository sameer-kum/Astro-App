package com.example.astros.otp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.databinding.FragmentOtpBinding


class OtpFragment : Fragment() {

    private lateinit var binding: FragmentOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVerify.setOnClickListener {
            // Navigate to ResetPasswordFragment
            findNavController().navigate(R.id.action_otpFragment_to_resetPasswordFragment)
        }

    }

}