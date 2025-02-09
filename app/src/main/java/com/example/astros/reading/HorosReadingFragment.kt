package com.example.astros.reading

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.databinding.FragmentHorosReadingBinding
import com.example.astros.utils.NavigationUtils


class HorosReadingFragment : Fragment() {

    private lateinit var binding: FragmentHorosReadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHorosReadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_horosFragment_to_tarotFragment)
        }

    }
}