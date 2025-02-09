package com.example.astros.forgetpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.databinding.FragmentForgetPasswordBinding
import com.example.astros.utils.NavigationUtils
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgetPasswordBinding
    private lateinit var auth: FirebaseAuth
    private var inputText: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
         return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.addTextChangedListener(afterTextChanged = {
            inputText = binding.etEmail.text.toString()
        })

        binding.btnSubmit.setOnClickListener {

            if (inputText.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter Email or Phone", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (isEmail(inputText)) {
                resetPassword(inputText)
            } else {
                Toast.makeText(
                    requireContext(), "Invalid email or phone number", Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    private fun isEmail(text: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Reset link sent to your email", Toast.LENGTH_LONG).show()
                    NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_forgetPasswordFragment_to_loginFragment)
                 } else {
                    Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

}