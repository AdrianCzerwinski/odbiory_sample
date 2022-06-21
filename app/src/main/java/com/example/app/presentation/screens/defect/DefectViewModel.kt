package com.example.app.presentation.screens.defect

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.BuildConfig
import com.example.app.domain.model.*
import com.example.app.domain.repository.Repository
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class DefectViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var photoUri: Uri? = null

    private val storageRef = FirebaseStorage.getInstance().reference.storage.reference
    val imageRef = storageRef.child("images")

    init {
        getProjects()
        getContractors()
        getDefectsTypes()
    }

    private val _messageBarState: MutableState<MessageBarState> = mutableStateOf(MessageBarState())
    val messageBarState: State<MessageBarState> = _messageBarState

    private val _defectAdded: MutableState<Boolean> = mutableStateOf(false)
    val defectAdded: State<Boolean> = _defectAdded

    private val _checkedType: MutableState<CheckType> = mutableStateOf(CheckType.PARTIAL)
    val checkedType: State<CheckType> = _checkedType

    private val _projects = mutableStateOf<Response<List<Project>>>(Response.Loading)
    val projects: State<Response<List<Project>>> = _projects

    private val _contractors = mutableStateOf<Response<List<Contractor>>>(Response.Loading)
    val contractors: State<Response<List<Contractor>>> = _contractors

    private val _locations = mutableStateOf<Response<List<Location>>>(Response.Loading)
    val locations: State<Response<List<Location>>> = _locations

    private val _defectsTypes = mutableStateOf<Response<List<DefectType>>>(Response.Loading)
    val defectTypes: State<Response<List<DefectType>>> = _defectsTypes

    private val _progressBarState: MutableState<Boolean> = mutableStateOf(false)
    val progressBarState: State<Boolean> = _progressBarState

    private val _photosToUploadList: MutableState<List<Pair<String, Boolean>>> =
        mutableStateOf(emptyList())
    val photosToUpdateList: State<List<Pair<String, Boolean>>> = _photosToUploadList

    private val _defect: MutableState<Defect> = mutableStateOf(Defect())
    val defect: State<Defect> = _defect

    private val _intent: MutableState<Intent?> = mutableStateOf(null)
    val intent: State<Intent?> = _intent

    fun typeChange(checkType: CheckType) {
        _checkedType.value = checkType
    }

    private fun getContractors() {
        viewModelScope.launch {
            repository.getContractors().collect { response ->
                _contractors.value = response
            }
        }
    }

    private fun getProjects() {
        viewModelScope.launch {
            repository.getProjects().collect { response ->
                _projects.value = response
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

    fun addDefectType(defectType: DefectType) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addDefectType(defectType = defectType).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        _progressBarState.value = true
                    }
                    is Response.Success -> {
                        _progressBarState.value = false
                        _messageBarState.value =
                            MessageBarState(message = response.data.message)
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

    fun addPhotoToUploadList(photo: Pair<String, Boolean>) {
        viewModelScope.launch {
            Log.d("Image Uri AddPhoUpload:", photo.first)
            _photosToUploadList.value = _photosToUploadList.value.plus(photo)
        }
    }

    fun deletePhotoFromUploadList(photo: Pair<String, Boolean>) {
        viewModelScope.launch {
            val photoMutableList: MutableList<Pair<String, Boolean>> =
                photosToUpdateList.value.toMutableList()
            photoMutableList.remove(photo)
            _photosToUploadList.value = photoMutableList

        }
    }

    fun changePhotoDoneStatus(oldPhoto: Pair<String, Boolean>, newPhoto: Pair<String, Boolean>) {
        viewModelScope.launch {
            val photoMutableList: MutableList<Pair<String, Boolean>> =
                photosToUpdateList.value.toMutableList()
            val index = photoMutableList.indexOf(oldPhoto)
            photoMutableList.set(index, newPhoto)
            _photosToUploadList.value = photoMutableList

        }
    }

    fun handleImageCapture(uri: Uri) {
        photoUri = uri
    }

    fun addDefect(defect: Defect, photosList: List<Pair<String, Boolean>>) {
        val newPhotosList = mutableListOf<Pair<String, Boolean>>()
        val uploadPhotosJob =
            viewModelScope.launch(Dispatchers.IO) {
                _progressBarState.value = true
                photosList.forEach { photo ->
                    val url = repository.uploadPhotos(photo = photo.first)
                    newPhotosList.add(Pair(url, photo.second))
                }

            }

        val uploadDefectJob = viewModelScope.launch(Dispatchers.IO) {
            uploadPhotosJob.join()
            defect.photos = newPhotosList.toMap()
            repository.addDefect(defect = defect).collect() { response ->
                when (response) {
                    is Response.Loading -> {
                        _progressBarState.value = true
                    }
                    is Response.Success -> {
                        _progressBarState.value = false
                        _messageBarState.value =
                            MessageBarState(message = "Dodano Usterkę")
                    }
                    is Response.ErrorMessageBar -> {
                        _progressBarState.value = false
                        _messageBarState.value = response.data
                    }
                    else -> {}
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            uploadDefectJob.join()
            delay(1000)
            _defectAdded.value = true
        }

    }

    fun updateDefect(defect: Defect) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDefect(defect = defect).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        _progressBarState.value = true
                    }
                    is Response.Success -> {
                        _progressBarState.value = false
                        _messageBarState.value =
                            response.data
                    }
                    is Response.ErrorMessageBar -> {
                        _progressBarState.value = false
                        _messageBarState.value = response.data
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun pdf(context: Context, defect: Defect) {
        val file = getFile(context = context)
        viewModelScope.launch(Dispatchers.Default) {
            repository.generatePDF(context = context, defect = defect, file = file)
            delay(1000)
            val data = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider", file)
            _intent.value = Intent(Intent.ACTION_VIEW)
                .setDataAndType(data,"application/pdf")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

    }

    private fun getFile(context: Context): File {
        return context.getOutputDirectory()
    }

    private fun Context.getOutputDirectory(): File {
        return File(
            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss-SSS",
                Locale.GERMANY
            ).format(System.currentTimeMillis()) + ".pdf"
        )
    }

}