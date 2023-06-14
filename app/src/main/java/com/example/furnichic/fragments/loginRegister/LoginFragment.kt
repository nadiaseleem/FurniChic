package com.example.furnichic.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.setBackground
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.furnichic.R
import com.example.furnichic.activities.ShoppingActivity
import com.example.furnichic.databinding.FragmentLoginBinding
import com.example.furnichic.dialog.setupBottomSheetDialog
import com.example.furnichic.util.Resource
import com.example.furnichic.util.validateEmail
import com.example.furnichic.util.validatePassword
import com.example.furnichic.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
private val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment:Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    private lateinit var email:String
    private lateinit var password:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = binding.emailEdt.text.toString().trim()
        password = binding.passwordEdt.text.toString()


        onEmailFocusListener()
        onPasswordFocusListener()
        onLoginButtonClick()
        onRegisterClick()
        onForgotPasswordClick()


    }

    private fun onForgotPasswordClick() {
        binding.tvForgotPasswordLogin.setOnClickListener{
            setupBottomSheetDialog {email->
               loginViewModel.resetPassword(email)

            }
        }

        lifecycleScope.launchWhenStarted {
            loginViewModel.resetPassword.collect{
                when(it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        Snackbar.make(
                            requireView(),
                            "Please check your mail inbox, we've sent a rest link to your email",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Error -> {
                        Snackbar.make(
                            requireView(),
                            "Error: ${it.message}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    else -> Unit

                }
            }
        }
    }

    private fun onRegisterClick() {
        binding.tvDontHaveAccount.setOnClickListener{
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }

    private fun onLoginButtonClick() {
        binding.buttonLoginLogin.setOnClickListener {

            submitForm()

        }

        lifecycleScope.launchWhenStarted {
            loginViewModel.login.collect{
                when(it) {
                    is Resource.Loading -> {
                        binding.buttonLoginLogin.startAnimation()
                    }

                    is Resource.Success -> {
                        Log.d(TAG, it.data.toString())
                        binding.buttonLoginLogin.revertAnimation()
                        val intent = Intent(requireActivity(),ShoppingActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }

                    is Resource.Error -> {
                        Log.d(TAG, it.message.toString())
                        binding.buttonLoginLogin.revertAnimation()
                        Toast.makeText(context,it.message.toString(), Toast.LENGTH_LONG).show()

                    }

                    else -> Unit

                }
            }
        }

    }

    private fun submitForm() {

        clearTextFieldsFocus()
        validateTextFields()

        val validEmail = binding.emailTil.helperText == null
        val validPassword = binding.passwordTil.helperText == null

        if (validEmail && validPassword)
            loginViewModel.loginWithEmailAndPassword(email, password)

        else
            Toast.makeText(context,"Please Check The Required Fields and Try Again!", Toast.LENGTH_LONG).show()
    }


    private fun clearTextFieldsFocus() {
        binding.emailEdt.clearFocus()
        binding.passwordEdt.clearFocus()
    }

    private fun validateTextFields() {
        email = binding.emailEdt.text.toString().trim()
        password = binding.passwordEdt.text.toString()

        binding.emailTil.helperText = validateEmail(email)
        binding.passwordTil.helperText = validatePassword(password)
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


}