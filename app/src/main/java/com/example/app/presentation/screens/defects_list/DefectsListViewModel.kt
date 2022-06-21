package com.example.app.presentation.screens.defects_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.domain.model.*
import com.example.app.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DefectsListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    init {
        getDefectsTypes()
        getContractors()
    }

    private val _messageBarState: MutableState<MessageBarState> = mutableStateOf(MessageBarState())
    val messageBarState: State<MessageBarState> = _messageBarState

    private val _checkedType: MutableState<CheckType> = mutableStateOf(CheckType.PARTIAL)
    val checkedType: State<CheckType> = _checkedType

    private val _defectList: MutableState<Response<List<Defect>>> = mutableStateOf(Response.Loading)
    var defectList: State<Response<List<Defect>>> = _defectList

    private val _contractors = mutableStateOf<Response<List<Contractor>>>(Response.Loading)
    val contractors: State<Response<List<Contractor>>> = _contractors

    private val _locations = mutableStateOf<Response<List<Location>>>(Response.Loading)
    val locations: State<Response<List<Location>>> = _locations

    private val _defectsTypes = mutableStateOf<Response<List<DefectType>>>(Response.Loading)
    val defectTypes: State<Response<List<DefectType>>> = _defectsTypes

    private val _progressBarState: MutableState<Boolean> = mutableStateOf(false)
    val progressBarState: State<Boolean> = _progressBarState

    fun typeChange(checkType: CheckType){
        _checkedType.value = checkType
    }

    fun getDefects(project: Project) {
        viewModelScope.launch {
            repository.getDefects(project = project).collect { response ->
                _defectList.value = response
            }
        }
    }

    fun getLocations(projectName: String) {
        viewModelScope.launch {
            repository.getLocations(project = projectName).collect() { response ->
                _locations.value = response
            }
        }
    }

    private fun getDefectsTypes() {
        viewModelScope.launch {
            repository.getDefectsTypes().collect { response ->
                _defectsTypes.value = response
            }
        }
    }

    private fun getContractors(){
        viewModelScope.launch {
            repository.getContractors().collect { response ->
                _contractors.value = response
            }
        }
    }

    fun filterDefectsByLocation(
        location: Location
    ){
        viewModelScope.launch {
            if(_defectList.value is Response.Success) {
                val list = (_defectList.value as Response.Success<List<Defect>>).data
                _defectList.value = Response.Success(
                    list.filter {
                        it.location == location.name
                    }
                )
            }
        }
    }

    fun filterDefectsByContractor(
        contractor: Contractor
    ){
        viewModelScope.launch {
            if(_defectList.value is Response.Success) {
                val list = (_defectList.value as Response.Success<List<Defect>>).data
                _defectList.value = Response.Success(
                    list.filter {
                        it.contractor == contractor.name
                    }
                )
            }
        }
    }

    fun filterDefectsByDefectType(
        defectType: DefectType
    ){
        viewModelScope.launch {
            if(_defectList.value is Response.Success) {
                val list = (_defectList.value as Response.Success<List<Defect>>).data
                _defectList.value = Response.Success(
                    list.filter {
                        it.type == defectType.name
                    }
                )
            }
        }
    }

    fun resetFilters(project: Project) {
        getDefects(project = project)
    }

    fun updateDefectStatus(defect: Defect) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDefectStatus(defect = defect).collect { response ->
                when (response) {
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

    fun deleteDefect(defect: Defect) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDefect(defect = defect).collect { response ->
                when (response) {
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



}





