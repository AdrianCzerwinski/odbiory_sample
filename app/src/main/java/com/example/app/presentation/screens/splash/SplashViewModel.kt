package com.example.app.presentation.screens.splash

import androidx.lifecycle.ViewModel
import com.example.app.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun checkUserState(): Boolean {
        return repository.isUserAuthenticatedInFirebase()
    }

}

