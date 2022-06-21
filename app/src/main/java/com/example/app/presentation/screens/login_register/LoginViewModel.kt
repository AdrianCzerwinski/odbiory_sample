package com.example.app.presentation.screens.login_register

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.domain.model.MessageBarState
import com.example.app.domain.model.Response
import com.example.app.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _messageBarState =
        mutableStateOf<Response<MessageBarState>>(Response.Success(MessageBarState()))
    val messageBarState: State<Response<MessageBarState>> = _messageBarState

    private val _signInState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val signInState: State<Response<Boolean>> = _signInState

    fun signUpUser(email: String, password: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.signUpWithEmailPassword(email, password, name)

                delay(1000)
                repository.signInWithEmailPassword(email, password).collect { response ->
                    _signInState.value = response

                }

            } catch (e: Exception) {

            }
        }
    }

    fun signInUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.signInWithEmailPassword(email, password).collect { response ->
                    _signInState.value = response
                }
            } catch (e: Exception) {
                Log.d("Login: ", "${e.message}")
            }

        }
    }

    fun signOut() {
        repository.signOut()
        _signInState.value = Response.Success(false)
    }


}

