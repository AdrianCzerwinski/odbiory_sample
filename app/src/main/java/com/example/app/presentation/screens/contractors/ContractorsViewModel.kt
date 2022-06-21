package com.example.app.presentation.screens.contractors

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.domain.model.Contractor
import com.example.app.domain.model.MessageBarState
import com.example.app.domain.model.Response
import com.example.app.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractorsViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    init {
        getContractors()
    }

    private val _messageBarState: MutableState<MessageBarState> = mutableStateOf(MessageBarState())
    val messageBarState: State<MessageBarState> = _messageBarState

    private val _progressBarState: MutableState<Boolean> = mutableStateOf(false)
    val progressBarState: State<Boolean> = _progressBarState

    private val _contractors = mutableStateOf<Response<List<Contractor>>>(Response.Loading)
    val contractors: State<Response<List<Contractor>>> = _contractors

    private fun getContractors(){
        viewModelScope.launch {
            repository.getContractors().collect { response ->
                _contractors.value = response
            }
        }
    }

    fun addContractor(name: String, phone: String, contactPerson: String, email: String) {
        viewModelScope.launch {
            repository.addContractor(
                name = name,
                contactPerson = contactPerson,
                email = email,
                phone = phone
            ).collect { response ->
                when (response){
                    is Response.Loading -> _progressBarState.value = true
                    is Response.Success ->
                        _messageBarState.value = MessageBarState(message = "Dodano podwykonawcę: $name")
                   is Response.ErrorMessageBar ->
                       _messageBarState.value = response.data
                    else -> {}
                }
            }
        }
    }

    fun deleteContractor(contractor: Contractor) {
        val name = contractor.name
        viewModelScope.launch {
            repository.deleteContractor(contractor = contractor).collect() { response ->
                when (response){
                    is Response.Loading -> _progressBarState.value = true
                    is Response.Success ->
                        _messageBarState.value = MessageBarState(message = "Usunięto podwykonawcę: $name")
                    is Response.ErrorMessageBar ->
                        _messageBarState.value = response.data
                    else -> {}
                }
            }
        }
    }
}