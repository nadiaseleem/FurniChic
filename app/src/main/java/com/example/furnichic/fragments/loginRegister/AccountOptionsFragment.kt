package com.example.furnichic.fragments.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.furnichic.R
import com.example.furnichic.databinding.FragmentAccountOptionsBinding
import com.example.furnichic.databinding.FragmentIntroductionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountOptionsFragment:Fragment(R.layout.fragment_introduction) {
    private lateinit var binding:FragmentAccountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountOptionsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRegisterClick()
        onLoginClick()
    }

    private fun onLoginClick() {
        binding.buttonLoginAccountOptions.setOnClickListener {
            val action = AccountOptionsFragmentDirections.actionAccountOptionsFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }

    private fun onRegisterClick() {
        binding.buttonRegisterAccountOptions.setOnClickListener {
            val action = AccountOptionsFragmentDirections.actionAccountOptionsFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }
}