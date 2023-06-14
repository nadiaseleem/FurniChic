package com.example.furnichic.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.furnichic.R
import com.example.furnichic.activities.ShoppingActivity
import com.example.furnichic.data.User
import com.example.furnichic.databinding.FragmentRegisterBinding
import com.example.furnichic.util.Resource
import com.example.furnichic.util.validateEmail
import com.example.furnichic.util.validateFistName
import com.example.furnichic.util.validateLastName
import com.example.furnichic.util.validatePassword
import com.example.furnichic.viewmodel.RegisterViewModel
import com.google.android.material.color.utilities.MaterialDynamicColors.background
import dagger.hilt.android.AndroidEntryPoint

private val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()
    private lateinit var firstName:String
    private lateinit var lastName:String
    private lateinit var email:String
    private lateinit var password:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstName = binding.FirstNameRegisterEdt.text.toString().trim()
         lastName = binding.LastNameRegisterEdt.text.toString().trim()
         email = binding.emailEdt.text.toString().trim()
         password = binding.passwordEdt.text.toString()
        onLoginClick()
        onNameFocusListener()
        onEmailFocusListener()
        onPasswordFocusListener()
        onRegisterButtonClick()


    }

    private fun onLoginClick() {
        binding.tvDoYouHaveAccount.setOnClickListener{
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }

    private fun onNameFocusListener() {
        binding.FirstNameRegisterEdt.setOnFocusChangeListener { _, focused ->
            firstName = binding.FirstNameRegisterEdt.text.toString().trim()

            if (!focused) {
                binding.FirstNameRegisterTil.helperText = validateFistName(firstName)
            }
        }

        binding.LastNameRegisterEdt.setOnFocusChangeListener { _, focused ->
            lastName = binding.LastNameRegisterEdt.text.toString().trim()

            if (!focused) {
                binding.LastNameRegisterTil.helperText = validateLastName(lastName)
            }
        }
    }



    private fun onEmailFocusListener() {
        binding.emailEdt.setOnFocusChangeListener { _, focused ->
            email = binding.emailEdt.text.toString().trim()

            if (!focused) {
                binding.emailTil.helperText = validateEmail(email)
            }
        }
    }

    private fun onPasswordFocusListener() {
        binding.passwordEdt.setOnFocusChangeListener { _, focused ->
            password = binding.passwordEdt.text.toString()

            if (!focused) {
                binding.passwordTil.helperText = validatePassword(password)
            }
        }
    }


    private fun submitForm() {

        clearTextFieldsFocus()
        validateTextFields()

        val validFirstName = binding.FirstNameRegisterTil.helperText == null
        val validLastName = binding.LastNameRegisterTil.helperText == null
        val validEmail = binding.emailTil.helperText == null
        val validPassword = binding.passwordTil.helperText == null

        if (validFirstName&&validLastName&& validEmail && validPassword)
            registerViewModel.createAccountWithEmailAndPassword(
                User(firstName, lastName, email),
                password
            )
        else
            Toast.makeText(context,"Please Check The Required Fields and Try Again!",Toast.LENGTH_LONG).show()
    }

    private fun validateTextFields() {

        firstName = binding.FirstNameRegisterEdt.text.toString().trim()
        lastName = binding.LastNameRegisterEdt.text.toString().trim()
        email = binding.emailEdt.text.toString().trim()
        password = binding.passwordEdt.text.toString()

        binding.FirstNameRegisterTil.helperText = validateFistName(firstName)
        binding.LastNameRegisterTil.helperText = validateLastName(lastName)
        binding.emailTil.helperText = validateEmail(email)
        binding.passwordTil.helperText = validatePassword(password)
    }

    private fun clearTextFieldsFocus() {
        binding.FirstNameRegisterEdt.clearFocus()
        binding.LastNameRegisterEdt.clearFocus()
        binding.emailEdt.clearFocus()
        binding.passwordEdt.clearFocus()
    }


    private fun onRegisterButtonClick() {
        binding.buttonRegisterRegister.setOnClickListener {

            submitForm()

        }

        lifecycleScope.launchWhenStarted {
            registerViewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonRegisterRegister.startAnimation()
                    }

                    is Resource.Success -> {
                        Log.d(TAG, it.data.toString())
                        binding.buttonRegisterRegister.revertAnimation()
                        Toast.makeText(requireContext(),"You have created a new account,\nLogin now!",Toast.LENGTH_SHORT).show()
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                    }

                    is Resource.Error -> {
                        Log.d(TAG, it.message.toString())
                        binding.buttonRegisterRegister.revertAnimation()
                        Toast.makeText(context,it.message.toString(), Toast.LENGTH_LONG).show()

                    }

                   else -> Unit

                }
            }
        }


    }


}