package com.example.astros.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.app.data.dto.User
import com.example.astros.base.BaseFragment
import com.example.astros.databinding.FragmentSignupBinding
import com.example.astros.service.SharedPreference
import com.example.astros.utils.GeneralUtil
import com.example.astros.utils.NavigationUtils

class SignupFragment : BaseFragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var signupViewModel: SignupViewModel
    private var name: String = ""
    private var userName: String = ""
    private var phoneNo: String = ""
    private var email: String = ""
    private var password: String = ""
    private var gender: String = ""
    private var maritalStatus: String = ""
    private var dateOfBirth: String = ""
    private var timeOfBirth: String = ""
    private var placeOfBirth: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupViewModel = SignupViewModel()
        sharedPreference = SharedPreference(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        setupObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etName.addTextChangedListener(
            afterTextChanged = {
                name = binding.etName.text.toString()
                sharedPreference.saveName(name)
            }
        )

        binding.etUserName.addTextChangedListener(
            afterTextChanged = {
                userName = binding.etUserName.text.toString()
                sharedPreference.saveUserName(userName)
            }
        )

        binding.etPhoneNo.addTextChangedListener(
            afterTextChanged = {
                phoneNo = binding.etPhoneNo.text.toString()
                sharedPreference.savePhoneNo(phoneNo)
            }
        )

        binding.etEmail.addTextChangedListener(
            afterTextChanged = {
                email = binding.etEmail.text.toString()
                sharedPreference.saveEmail(email)
            }
        )

        binding.etPassword.addTextChangedListener(
            afterTextChanged = {
                password = binding.etPassword.text.toString()
                sharedPreference.savePassword(password)
            }
        )

        binding.llGender.setOnClickListener {
            GeneralUtil.showGenderPicker(requireContext(), binding.tvGender) { selectedGender ->
                gender = selectedGender
                sharedPreference.saveGender(gender)
            }
        }

        binding.llMartitalStatus.setOnClickListener {
            GeneralUtil.showMaritalStatusMenu(requireContext(), binding.tvMaritalStatus) { selectedStatus ->
                maritalStatus = selectedStatus
                sharedPreference.saveMaritalStatus(maritalStatus)
            }
        }

        binding.llDOB.setOnClickListener {
            GeneralUtil.openDatePicker(requireContext(), binding.tvDOB) { selectedDate ->
                dateOfBirth = selectedDate
                sharedPreference.saveDOB(dateOfBirth)
            }
        }

        binding.llTime.setOnClickListener {
            GeneralUtil.openTimePicker(requireContext(), binding.tvTime) { selectedTime ->
                timeOfBirth = selectedTime
                sharedPreference.saveBirthTime(timeOfBirth)
            }
        }

        binding.etCity.addTextChangedListener {
            placeOfBirth = binding.etCity.text.toString()
            sharedPreference.saveBirthPlace(placeOfBirth)
        }

        binding.btnContinue.setOnClickListener {
            registerUser()
        }

        binding.tvjoinus.setOnClickListener {
            //Navigate to Login Fragment
            NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_signupFragment_to_loginFragment)
        }

    }

    private fun registerUser() {
        val user = User(
            name, userName, phoneNo, email, password, gender, maritalStatus, dateOfBirth, timeOfBirth, placeOfBirth
        )
        //Call registerUser function from ViewModel
        signupViewModel.registerUser(user)
    }

    private fun setupObservers() {
        signupViewModel.registrationResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { response ->
                    //Save token and login status in shared preference
                    GeneralUtil.showToast(requireContext(), "Registration Successful")
                    sharedPreference.apply {
                        clearToken()
                        saveToken(response.token)
                        saveLoginStatus(true)
                    }
                    //Navigate to Congrats Fragment
                    NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_signupFragment_to_congratsFragment)
                },
                onFailure = { error ->
                    GeneralUtil.showToast(requireContext(), error.message ?: "Registration failed")
                }
            )
        }
    }

}