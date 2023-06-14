package com.example.furnichic.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furnichic.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
    ) : ViewModel() {

    private val _login = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val login: Flow<Resource<FirebaseUser>> = _login

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
     val resetPassword = _resetPassword.asSharedFlow()

    fun loginWithEmailAndPassword(email:String, password: String) {
        _login.value = Resource.Loading()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                it.user?.let {
                    _login.value = Resource.Success(it)
                }

            }.addOnFailureListener {
                _login.value = Resource.Error(it.message.toString())
            }

    }

    fun resetPassword(email:String){
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _resetPassword.emit(Resource.Success(email))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _resetPassword.emit(Resource.Error(it.message.toString()))
                    }
                }

    }

    fun isUserSignedIn() : Boolean {
        if (firebaseAuth.currentUser?.uid != null)
            return true
        return false

    }

}