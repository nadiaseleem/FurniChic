package com.example.furnichic.fragments.loginRegister

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.furnichic.R
import com.example.furnichic.databinding.FragmentIntroductionBinding
import com.example.furnichic.util.Constants.SHOULD_SHOW
import com.example.furnichic.util.Constants.SPLASH_SHARED_PREF
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionFragment:Fragment(R.layout.fragment_introduction) {
    private lateinit var binding:FragmentIntroductionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shouldShowIntroductionFragment =
            requireActivity().getSharedPreferences(SPLASH_SHARED_PREF, Context.MODE_PRIVATE)
                .getBoolean(SHOULD_SHOW, true)


        if (!shouldShowIntroductionFragment)
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)

        else
            binding.buttonStart.setOnClickListener {

                findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)

                val sharedPref =
                    requireActivity().getSharedPreferences(SPLASH_SHARED_PREF, Context.MODE_PRIVATE)
                sharedPref.edit().putBoolean(SHOULD_SHOW, false).apply()
            }
    }


}