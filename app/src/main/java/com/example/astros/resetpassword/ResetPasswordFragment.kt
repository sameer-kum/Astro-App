package com.example.astros.resetpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.databinding.FragmentResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentResetPasswordBinding
    private var newPassword: String = ""
    private var confirmPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etNewPassword.addTextChangedListener(
            afterTextChanged = {
                // Check if the password is valid
                newPassword = binding.etNewPassword.text.toString()
                if (newPassword.length < 6) {
                    binding.etNewPassword.error = "Password must be at least 6 characters long"
                }
            }
        )

        binding.etConfimPassword.addTextChangedListener(
            afterTextChanged = {
                // Check if the password is valid
                confirmPassword = binding.etConfimPassword.text.toString()
                if (confirmPassword.length < 6) {
                    binding.etConfimPassword.error = "Password must be at least 6 characters long"
                }
            }
        )


        binding.btnSubmit.setOnClickListener {
            // Navigate to LoginFragment
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            resetPassword(newPassword)

//            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }

    }

    private fun resetPassword(newPassword: String) {
        val user = FirebaseAuth.getInstance().currentUser

        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset successful, navigate to login
                    findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                } else {
                    Toast.makeText(requireContext(), "Error resetting password", Toast.LENGTH_SHORT).show()
                }
            }
    }

}