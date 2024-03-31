package com.aquaguardian.projectaquaguard.ui.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ServerViewModel : ViewModel() {
    class AuthViewModel : ViewModel() {
        private val _isAuthenticated = MutableLiveData<Boolean>()
        val isAuthenticated: LiveData<Boolean> = _isAuthenticated
        init {
            // Initialize Firebase Authentication
            FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
                _isAuthenticated.value = firebaseAuth.currentUser != null
            }
        }
    }

}