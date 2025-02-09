package com.example.astros.login

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
import com.example.astros.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.astros.utils.NavigationUtils


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private var name: String? = ""
    private var email: String = ""
    private var password: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
         return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.addTextChangedListener(
            afterTextChanged = {
                email = binding.etEmail.text.toString()
            }
        )

        binding.etPassword.addTextChangedListener(
            afterTextChanged = {
                password = binding.etPassword.text.toString()
            }
        )

        binding.btnLogin.setOnClickListener {

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter Email ID and Password", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            // Navigate to SignupFragment
            NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_loginFragment_to_signupFragment)
         }

        binding.tvForgetPassword.setOnClickListener {
            // Navigate to ForgetPasswordFragment
            NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_loginFragment_to_forgetPasswordFragment)
         }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()
                    NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_loginFragment_to_dashboardFragment)
                 } else {
                    Toast.makeText(requireContext(), "Login credentials didn't match: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
 }