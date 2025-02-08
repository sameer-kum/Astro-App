package com.example.astros.signup

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.compose.material3.DatePickerDialog
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.databinding.FragmentSignupBinding
import com.example.astros.service.SharedPreference
import com.example.astros.utils.GeneralUtil
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth  // Firebase Auth instance
    private lateinit var sharedPreference: SharedPreference
    private var name: String? = ""
    private var email: String = ""
    private var password: String = ""
    private var placeOfBirth: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        sharedPreference = SharedPreference(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etName.addTextChangedListener(
            afterTextChanged = {
                name = binding.etName.text.toString()
                sharedPreference.saveName(name!!)
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
            showGenderPicker()
        }

        binding.llMartitalStatus.setOnClickListener {
            showMaritalStatusMenu()
        }

        binding.llDOB.setOnClickListener {
            openDatePicker()
        }

        binding.llTime.setOnClickListener {
            openTimePicker()
        }

        binding.etCity.addTextChangedListener {
            placeOfBirth = binding.etCity.text.toString()
            sharedPreference.saveBirthPlace(placeOfBirth)
        }


        binding.btnContinue.setOnClickListener {
            email = binding.etEmail.text.toString().trim()
            password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            signUpUser(email, password)
        }

        binding.tvjoinus.setOnClickListener {
             findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

    }

    private fun showGenderPicker() {
        val popupMenu = PopupMenu(requireContext(), binding.chooseGender)
        popupMenu.menuInflater.inflate(R.menu.gender_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.tvGender.text = menuItem.title
            sharedPreference.saveGender(menuItem.title.toString())
            true
        }
        popupMenu.show()
    }

    private fun showMaritalStatusMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.chooseMaritalStatus)
        popupMenu.menuInflater.inflate(R.menu.marital_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            binding.tvMaritalStatus.text = item.title
            sharedPreference.saveMaritalStatus(item.title.toString())
            true
        }

        popupMenu.show()
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Format date as DD/MM/YYYY
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.tvDOB.text = dateFormat.format(selectedDate.time)
                sharedPreference.saveDOB(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }

    private fun openTimePicker() {
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                // Format time as HH:mm (24-hour format)
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                binding.tvTime.text = timeFormat.format(selectedTime.time)
                sharedPreference.saveBirthTime(timeFormat.format(selectedTime.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // Set to true for 24-hour format, false for AM/PM
        )

        timePicker.show()
    }

    private fun signUpUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Signup Successful!", Toast.LENGTH_SHORT)
                        .show()
                    // Navigate to next screen or home screen
                    findNavController().navigate(R.id.action_signupFragment_to_dashboardFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Signup Failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}