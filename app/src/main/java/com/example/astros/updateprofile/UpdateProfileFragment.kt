package com.example.astros.updateprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.astros.app.data.dto.User
import com.example.astros.databinding.FragmentSignupBinding
import com.example.astros.service.SharedPreference
import com.example.astros.utils.GeneralUtil
import com.example.astros.utils.GeneralUtil.showToast

class UpdateProfileFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var updateProfileViewModel: UpdateProfileViewModel
    private var userName: String = ""
    private var phoneNo: String = ""
    private var gender: String = ""
    private var maritalStatus: String = ""
    private var dateOfBirth: String = ""
    private var timeOfBirth: String = ""
    private var placeOfBirth: String = ""
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        updateProfileViewModel = UpdateProfileViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        token = sharedPreference.getToken().toString()
        subscribeUI(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvhead.text = "Update Profile"
        binding.llName.visibility = View.GONE
        binding.llEmail.visibility = View.GONE
        binding.llPassword.visibility = View.GONE
        binding.tvjoinus.visibility = View.GONE
        binding.tvterms.visibility = View.GONE
        binding.llScan.visibility = View.GONE
        binding.btnContinue.text = "Update"

        binding.etUserName.addTextChangedListener(
            afterTextChanged = {
                userName = binding.etUserName.text.toString()
            }
        )

        binding.etPhoneNo.addTextChangedListener(
            afterTextChanged = {
                phoneNo = binding.etPhoneNo.text.toString()
            }
        )

        binding.llGender.setOnClickListener {
            GeneralUtil.showGenderPicker(requireContext(), binding.tvGender) { selectedGender ->
                gender = selectedGender
            }
        }

        binding.llMartitalStatus.setOnClickListener {
            GeneralUtil.showMaritalStatusMenu(requireContext(), binding.tvMaritalStatus) { selectedStatus ->
                maritalStatus = selectedStatus
            }
        }

        binding.llDOB.setOnClickListener {
            GeneralUtil.openDatePicker(requireContext(), binding.tvDOB){ selectedDate ->
                dateOfBirth = selectedDate
            }
        }

        binding.llTime.setOnClickListener {
            GeneralUtil.openTimePicker(requireContext(), binding.tvTime){ selectedTime ->
                timeOfBirth = selectedTime
            }
        }

        binding.etCity.addTextChangedListener {
            placeOfBirth = binding.etCity.text.toString()
        }

        binding.btnContinue.setOnClickListener {
            updateProfile()
        }

    }

    private fun updateProfile() {
        val user = User(
            userName = userName,
            phoneNo = phoneNo,
            gender = gender,
            maritalStatus = maritalStatus,
            dateOfBirth = dateOfBirth,
            timeOfBirth = timeOfBirth,
            placeOfBirth = placeOfBirth,
        )
        updateProfileViewModel.updateUserProfile(token, user)
    }

    private fun subscribeUI(binding: FragmentSignupBinding) {

        updateProfileViewModel.updateResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                showToast(requireContext(), "Profile updated successfully")
            }.onFailure {
                showToast(requireContext(), it.message ?: "Update failed")
            }
        }
    }

}