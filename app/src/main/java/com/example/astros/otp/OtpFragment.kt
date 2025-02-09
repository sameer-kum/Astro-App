package com.example.astros.otp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.databinding.FragmentOtpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider


class OtpFragment : Fragment() {

    private lateinit var binding: FragmentOtpBinding
    private lateinit var verificationId: String


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
        verificationId = arguments?.getString("verificationId").toString()


        binding.btnVerify.setOnClickListener {
            // Navigate to ResetPasswordFragment
            val otp = getOtpFromFields()

            if (otp.length == 6) {
                verifyOtp(otp)
            } else {
                Toast.makeText(requireContext(), "Please enter a valid OTP", Toast.LENGTH_SHORT).show()
            }
            findNavController().navigate(R.id.action_otpFragment_to_resetPasswordFragment)
        }

    }

    private fun getOtpFromFields(): String {
        return binding.otp1.text.toString() + binding.otp2.text.toString() +
                binding.otp3.text.toString() + binding.otp4.text.toString() +
                binding.otp5.text.toString() + binding.otp6.text.toString()
    }

    private fun verifyOtp(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)

        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // OTP is correct, navigate to ResetPasswordFragment
                    findNavController().navigate(R.id.action_otpFragment_to_resetPasswordFragment)
                } else {
                    Toast.makeText(requireContext(), "Invalid OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }
}