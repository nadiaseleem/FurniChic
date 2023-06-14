package com.example.furnichic.viewmodel

import androidx.lifecycle.ViewModel
import com.example.furnichic.data.User
import com.example.furnichic.util.Constants
import com.example.furnichic.util.Constants.USERS_COLLECTION
import com.example.furnichic.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    fun createAccountWithEmailAndPassword(user: User, password: String) {
        _register.value = Resource.Loading()
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener {
                it.user?.let {
                    saveUserInfo(it.uid, user)
                }

            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }

    }

    private fun saveUserInfo(userUid: String, user: User) {
        db.collection(USERS_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)

            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }
}