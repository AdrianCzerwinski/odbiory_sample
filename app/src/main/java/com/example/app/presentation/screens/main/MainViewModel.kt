package com.example.app.presentation.screens.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.core.Constants.GREETINGS
import com.example.app.domain.model.MessageBarState
import com.example.app.domain.model.Project
import com.example.app.domain.model.Response
import com.example.app.domain.repository.Repository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    var user: FirebaseUser? = null

    init {
        viewModelScope.launch {
            user = repository.getCurrentUser()
            delay(500)
            getProjects()
        }
    }

    private val _progressBarState: MutableState<Boolean> = mutableStateOf(false)
    val progressBarState: State<Boolean> = _progressBarState

    private val _messageBarState: MutableState<MessageBarState> = mutableStateOf(MessageBarState())
    val messageBarState: State<MessageBarState> = _messageBarState

    private val _signOutState = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val signOutState: State<Response<Boolean>> = _signOutState

    private val _projects = mutableStateOf<Response<List<Project>>>(Response.Loading)
    val projects: State<Response<List<Project>>> = _projects

    val greetings = GREETINGS.random()

    //REMOTE:

    private fun getProjects() {
        viewModelScope.launch {
            repository.getProjects().collect { response ->
                _projects.value = response
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut().collect { response ->
                _signOutState.value = response

            }
        }
    }

    fun updateName(userName: String) {
        viewModelScope.launch {
            repository.updateUserName(userName)
        }
    }

    fun updateEmail(userName: String) {
        viewModelScope.launch {
            repository.updateUserEmail(userName)
        }
    }

    fun shareProject(email: String, project: Project) {
        viewModelScope.launch {
            repository.updateProject(email = email, project = project).collect { response ->
                Log.d("User", "started adding in ViewModel")
                when (response){
                    is Response.Loading -> _progressBarState.value = true
                    is Response.Success ->
                        _messageBarState.value =
                            MessageBarState(message = "Dodano nowego użytkownika")
                    is Response.ErrorMessageBar ->
                        _messageBarState.value = response.data
                    is Response.Error -> Log.d("User", "Can't Update")
                }
            }
        }
    }
    fun addNewProject(projectName: String) {
        viewModelScope.launch {
            Log.d("Project:", " invoked adding new project")
            if (_projects.value is Response.Success){
                Log.d("Project","started")
                if((_projects.value as Response.Success<List<Project>>).data
                        .any { project -> project.name == projectName}) {
                    Log.d("Project","istnieje taki")
                    _messageBarState.value =
                        MessageBarState(error =
                        Exception("Taki projekt już istnieje. Podaj inną nazwę lub poproś o jego udostępnienie"))
                } else{
                    Log.d("Project","Nie istnieje taki")
                    repository.addProject(projectName = projectName).collect { response ->
                        when (response){
                            is Response.Loading -> _progressBarState.value = true
                            is Response.Success ->
                                _messageBarState.value =
                                    MessageBarState(message = "Dodano projekt: $projectName")
                            is Response.ErrorMessageBar ->
                                _messageBarState.value = response.data
                            else -> {}
                        }
                    }
                }

            }
        }
    }

    fun deleteProject(project: Project) {
        val name = project.name
        viewModelScope.launch {
            repository.deleteProject(project = project).collect { response ->
                when (response){
                    is Response.Loading -> _progressBarState.value = true
                    is Response.Success ->
                        _messageBarState.value = MessageBarState(message = "Usunięto projekt: $name")
                    is Response.ErrorMessageBar ->
                        _messageBarState.value = response.data
                    else -> {}
                }
            }
        }
    }




}





