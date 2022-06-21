package com.example.app.presentation.screens.locations

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.domain.model.Location
import com.example.app.domain.model.MessageBarState
import com.example.app.domain.model.Project
import com.example.app.domain.model.Response
import com.example.app.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _locations = mutableStateOf<Response<List<Location>>>(Response.Loading)
    val location: State<Response<List<Location>>> = _locations

    private val _progressBarState: MutableState<Boolean> = mutableStateOf(false)
    val progressBarState: State<Boolean> = _progressBarState

    private val _messageBarState: MutableState<MessageBarState> = mutableStateOf(MessageBarState())
    val messageBarState: State<MessageBarState> = _messageBarState

    fun getLocations(project: Project) {
        val projectName = project.id
        viewModelScope.launch {
            repository.getLocations(projectName).collect() { response ->
                _locations.value = response
            }
        }
    }



    fun addLocation(project: Project, name: String) {
        viewModelScope.launch {
            repository.addLocation(project = project.id, name = name).collect { response ->
                when (response){
                    is Response.Loading -> _progressBarState.value = true
                    is Response.Success -> {
                        _progressBarState.value = false
                        _messageBarState.value = response.data
                    }
                    is Response.ErrorMessageBar -> {
                        _progressBarState.value = false
                        _messageBarState.value = response.data
                    }
                    else -> {}
                }
            }
        }
    }

    fun deleteLocation(location: Location, project: Project) {
        viewModelScope.launch {
            repository.deleteLocation(location = location, projectId = project.id).collect { response ->
                when (response){
                    is Response.Loading -> _progressBarState.value = true
                    is Response.Success -> {
                        _progressBarState.value = false
                        _messageBarState.value = response.data
                    }
                    is Response.ErrorMessageBar -> {
                        _progressBarState.value = false
                        _messageBarState.value = response.data
                        Log.d("Delete", response.data.error!!.message!!)
                    }
                    else -> {}
                }
            }
        }
    }
}
