package com.example.astros.reading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.base.BaseFragment
import com.example.astros.databinding.FragmentNumerologyReadingBinding
import com.example.astros.utils.NavigationUtils


class NumerologyReadingFragment : BaseFragment() {

    private lateinit var binding: FragmentNumerologyReadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNumerologyReadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_numerologyFragment_to_chooseLanguageFragment)
        }

    }
}