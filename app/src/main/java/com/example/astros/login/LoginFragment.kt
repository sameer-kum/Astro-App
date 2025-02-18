package com.example.astros.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.base.BaseFragment
import com.example.astros.databinding.FragmentLoginBinding
import com.example.astros.service.SharedPreference
import com.example.astros.utils.GeneralUtil
import com.google.firebase.auth.FirebaseAuth
import com.example.astros.utils.NavigationUtils


class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private var email: String = ""
    private var password: String = ""
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = LoginViewModel(SharedPreference(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        subscribeUI(binding)
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
                GeneralUtil.showToast(requireContext(),"Please enter Email ID and Password")
            } else {
                loginViewModel.loginUser(email, password)
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

    private fun subscribeUI(binding: FragmentLoginBinding) {

        // Observe login response from ViewModel
        loginViewModel.loginResponse.observe(viewLifecycleOwner) { authResponse ->
            authResponse?.let {
                NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_loginFragment_to_dashboardFragment)
                GeneralUtil.showToast(requireContext(), "Login Successful")
            }
        }

        // Observe login errors from ViewModel
        loginViewModel.loginError.observe(viewLifecycleOwner) { errorMessage ->
            GeneralUtil.showToast(requireContext(), errorMessage)
        }

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
                        GeneralUtil.showToast(requireContext(), "Press back again to exit")
                    }
                }
            }
        )
    }
}