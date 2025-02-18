package com.example.astros.language

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.base.BaseFragment
import com.example.astros.databinding.FragmentChooseLanguageBinding
import com.example.astros.service.SharedPreference
import com.example.astros.utils.GeneralUtil
import com.example.astros.utils.NavigationUtils

class ChooseLanguageFragment : BaseFragment() {

    private lateinit var binding: FragmentChooseLanguageBinding
    private lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnNext.setOnClickListener {
            var selectedLanguage: String? = null

            // Check which radio button is selected and assign the corresponding language
            when {
                binding.rbEnglish.isChecked -> selectedLanguage = "English"
                binding.rbHindi.isChecked -> selectedLanguage = "Hindi"
                binding.rbFrench.isChecked -> selectedLanguage = "French"
                binding.rbSpanish.isChecked -> selectedLanguage = "Spanish"
            }

            if (selectedLanguage != null) {
                Log.d("ChoosedLanguage", "Selected Language: $selectedLanguage")
                sharedPreference.saveSelectedLanguage(selectedLanguage)
                NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_chooseLanguageFragment_to_loginFragment)
             } else {
                GeneralUtil.showToast(requireContext(), "Please choose a language first!")
            }
        }
    }
}