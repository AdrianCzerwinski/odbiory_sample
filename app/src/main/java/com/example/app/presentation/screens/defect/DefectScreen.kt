package com.example.app.presentation.screens.defect

import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.app.R
import com.example.app.core.Constants.SEARCH
import com.example.app.core.LockScreenOrientation
import com.example.app.core.MyAlertDialog
import com.example.app.domain.model.*
import com.example.app.presentation.components.DefectsTopBar
import com.example.app.presentation.components.MessageBar
import com.example.app.presentation.components.SelectableButton
import com.example.app.presentation.navigation.Screen
import com.example.app.presentation.screens.contractors.components.TopSubScreenBar
import com.example.app.presentation.screens.defect.components.*
import com.example.app.ui.theme.DarkBlue
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.Typography
import java.net.URLDecoder
import java.time.LocalDateTime


@Composable
fun DefectScreen(
    viewModel: DefectViewModel = hiltViewModel(),
    project: Project?,
    location: Location?,
    navController: NavHostController,
    defect: Defect?,
    typeCheck: CheckType?
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val selectedType by viewModel.checkedType
    val scrollState = rememberScrollState()
    val messageBarState by viewModel.messageBarState
    val photoListState by viewModel.photosToUpdateList
    var editDefect by remember { mutableStateOf(Defect()) }
    var openDialog by remember { mutableStateOf(false)}
    val intent by viewModel.intent

    BackHandler {
        if(photoListState.isNotEmpty()){
            openDialog = true
        } else {
            navController.navigateUp()
        }
    }

    MyAlertDialog(
        question = "UWAGA",
        description = "Czy na pewno chcesz wrócić? Wszystkie zdjęcia z aparatu zostaną utracone.",
        onDismiss = { openDialog = false },
        onConfirm = { navController.navigateUp() },
        openDialog = openDialog
    )

    var expandedProject by remember { mutableStateOf(false) }
    var searchProjectValue by remember { mutableStateOf("Szukaj...") }
    var selectedProject by remember { mutableStateOf(Project()) }
    var projectName by remember { mutableStateOf("") }

    var expandedLocation by remember { mutableStateOf(false) }
    var searchLocationValue by remember { mutableStateOf("Szukaj...") }
    var selectedLocation by remember { mutableStateOf(Location()) }
    var locationName by remember { mutableStateOf("") }
    var showLocation by remember { mutableStateOf(false) }

    var expandedContractor by remember { mutableStateOf(false) }
    var searchContractorValue by remember { mutableStateOf("Szukaj...") }
    var contractorName by remember { mutableStateOf("") }
    var selectedContractor by remember { mutableStateOf(Contractor()) }

    var expandedDefectType by remember { mutableStateOf(false) }
    var searchDefectTypeValue by remember { mutableStateOf("Szukaj...") }
    var defectTypeName by remember { mutableStateOf("") }
    var selectedDefectType by remember { mutableStateOf(DefectType()) }

    val shouldCloseScreen by viewModel.defectAdded
    val progressBarState by viewModel.progressBarState

    var commentValue by remember { mutableStateOf("") }

    val storageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                viewModel.handleImageCapture(uri)
                val photo = Pair(uri.toString(), false)
                viewModel.addPhotoToUploadList(photo = photo)
            }
        }

    var shouldShowCamera by remember { mutableStateOf(false) }
    var shouldShowFullSizedPhoto by remember { mutableStateOf(false) }
    var zoomImageState by remember { mutableStateOf("") }

    var saveDefectEnabled by remember { mutableStateOf(false) }

    saveDefectEnabled =
        selectedContractor.name.isNotEmpty() &&
                selectedProject.name.isNotEmpty() &&
                selectedLocation.name.isNotEmpty() &&
                selectedType.name.isNotEmpty()

    LaunchedEffect(key1 = intent) {
        if(intent != null) {
            context.startActivity(intent)
        }
    }

    LaunchedEffect(key1 = true) {

        if (project != null) {
            selectedProject = project
            projectName = selectedProject.name
        }
        if (location != null) {
            selectedLocation = location
            locationName = location.name
        }
        if (defect != null) {
            editDefect = defect
            locationName = defect.location
            contractorName = defect.contractor
            defectTypeName = defect.type
            commentValue = defect.comment
        }
        if (typeCheck != null) {
            Log.d("Type Check", typeCheck.type)
            viewModel.typeChange(typeCheck)
        }
    }

    LaunchedEffect(key1 = projectName) {
        if (projectName != "") {
            viewModel.getLocations(selectedProject.id)
            showLocation = true
        }
    }

    LaunchedEffect(key1 = shouldCloseScreen) {
        if (shouldCloseScreen) {
            navController.navigateUp()
        }
    }

    if (shouldShowCamera) {
        TakePhoto(onCaptureClicked = { uri ->
            viewModel.handleImageCapture(uri)
            val photo = Pair(uri.toString(), false)
            viewModel.addPhotoToUploadList(photo = photo)
            shouldShowCamera = false
        })
    } else {
        if (shouldShowFullSizedPhoto) {
            ZoomPhoto(
                uri = Uri.parse(zoomImageState),
                onBackClicked = { shouldShowFullSizedPhoto = false }
            )
        } else {
            Scaffold(
                topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(color = Color.White)
                    ) {
                        MessageBar(messageBarState = messageBarState)
                        TopSubScreenBar(
                            onBackClicked = { navController.navigateUp() },
                            onHomeClicked = {
                                navController.navigate(
                                    Screen.Main.route
                                )
                            },
                            iconSize = 30.dp,
                            iconTint = LightBackground,
                            textColor = LightBackground
                        )
                        if(progressBarState) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = DarkBlue
                            )
                        }
                        if (defect == null) {
                            DefectsTopBar(
                                selectedItem = selectedType,
                                onClick = { viewModel.typeChange(it) })
                        }

                    }
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(12.dp)
                            .scrollable(
                                state = scrollState,
                                enabled = true,
                                orientation = Orientation.Vertical
                            ),
                        verticalArrangement = Arrangement.Top
                    ) {

                        when (val projectList = viewModel.projects.value) {
                            is Response.Loading ->
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                }
                            is Response.Success -> {
                                if (projectList.data.isNotEmpty()) {
                                    DropDownSearchProjects(
                                        items = projectList.data,
                                        value = projectName,
                                        onValueChange = { projectName = it },
                                        onExpandedClicked = { expandedProject = !expandedProject },
                                        expanded = expandedProject,
                                        onSelectedClicked = {
                                            selectedProject = it
                                            projectName = it.name
                                            expandedProject = false
                                            editDefect.project = it.name
                                        },
                                        searchValue = searchProjectValue,
                                        onClearClicked = {
                                            expandedProject = false
                                            searchProjectValue = SEARCH
                                            focusManager.clearFocus()
                                        },
                                        onSearchClicked = {
                                            focusManager.clearFocus()
                                        },
                                        onSearchValueChange = {
                                            searchProjectValue = it
                                        },
                                        onSearchValueClicked = {
                                            searchProjectValue = ""
                                        },
                                        label = stringResource(R.string.choose_project),
                                        onAddClick = {
                                            navController.navigate(Screen.Main.route)
                                        }
                                    )
                                    if (showLocation) {
                                        when (val locationList = viewModel.locations.value) {
                                            is Response.Loading -> {
                                                CircularProgressIndicator()
                                            }
                                            is Response.Success -> {
                                                DropDownSearchLocations(
                                                    items = locationList.data,
                                                    value = locationName,
                                                    onValueChange = { locationName = it },
                                                    onExpandedClicked = {
                                                        expandedLocation = !expandedLocation
                                                    },
                                                    expanded = expandedLocation,
                                                    onSelectedClicked = {
                                                        selectedLocation = it
                                                        locationName = it.name
                                                        expandedLocation = false
                                                        editDefect.location = it.name
                                                    },
                                                    searchValue = searchLocationValue,
                                                    onClearClicked = {
                                                        expandedLocation = false
                                                        searchLocationValue = SEARCH
                                                        focusManager.clearFocus()
                                                    },
                                                    onSearchClicked = {
                                                        focusManager.clearFocus()
                                                    },
                                                    onSearchValueChange = {
                                                        searchLocationValue = it
                                                    },
                                                    onSearchValueClicked = {
                                                        searchLocationValue = ""
                                                    },
                                                    label = stringResource(R.string.chooes_location),
                                                    onAddClick = {
                                                        navController.currentBackStackEntry!!.savedStateHandle["project"] =
                                                            selectedProject
                                                        navController.navigate(Screen.Location.route)
                                                    },
                                                    onValueErase = {
                                                        searchLocationValue = ""
                                                    }
                                                )
                                            }
                                            else -> {}
                                        }
                                        when (val contractorList = viewModel.contractors.value) {
                                            is Response.Success -> {
                                                DropDownSearchContractors(
                                                    items = contractorList.data,
                                                    value = contractorName,
                                                    onValueChange = { contractorName = it },
                                                    onExpandedClicked = {
                                                        expandedContractor = !expandedContractor
                                                    },
                                                    expanded = expandedContractor,
                                                    onSelectedClicked = {
                                                        selectedContractor = it
                                                        contractorName = it.name
                                                        expandedContractor = false
                                                        editDefect.contractor = it.name
                                                    },
                                                    searchValue = searchContractorValue,
                                                    onClearClicked = {
                                                        expandedContractor = false
                                                        searchContractorValue = SEARCH
                                                        focusManager.clearFocus()
                                                    },
                                                    onSearchClicked = {
                                                        focusManager.clearFocus()
                                                    },
                                                    onSearchValueChange = {
                                                        searchContractorValue = it
                                                    },
                                                    onSearchValueClicked = {
                                                        searchContractorValue = ""
                                                    },
                                                    label = stringResource(R.string.choose_contractor),
                                                    onAddClick = {
                                                        navController.navigate(Screen.Contractors.route)
                                                    },
                                                    onValueErase = {
                                                        searchContractorValue = ""
                                                    }
                                                )
                                            }
                                            else -> {}
                                        }

                                        when (val defectTypesList = viewModel.defectTypes.value) {
                                            is Response.Success -> {
                                                DropDownSearchDefectsTypes(
                                                    items = defectTypesList.data,
                                                    value = defectTypeName,
                                                    onValueChange = { defectTypeName = it },
                                                    onExpandedClicked = {
                                                        expandedDefectType = !expandedDefectType
                                                    },
                                                    expanded = expandedDefectType,
                                                    onSelectedClicked = {
                                                        selectedDefectType = it
                                                        defectTypeName = it.name
                                                        expandedDefectType = false
                                                        editDefect.type = it.name
                                                    },
                                                    searchValue = searchDefectTypeValue,
                                                    onClearClicked = {
                                                        expandedDefectType = false
                                                        searchDefectTypeValue = SEARCH
                                                        focusManager.clearFocus()
                                                    },
                                                    onSearchClicked = {
                                                        focusManager.clearFocus()
                                                    },
                                                    onSearchValueChange = {
                                                        searchDefectTypeValue = it
                                                    },
                                                    onSearchValueClicked = {
                                                        searchDefectTypeValue = ""
                                                    },
                                                    label = stringResource(R.string.choose_defect_type),
                                                    onAddClick = {
                                                        viewModel.addDefectType(DefectType(name = searchDefectTypeValue))
                                                        focusManager.clearFocus()
                                                    },
                                                    onValueErase = {
                                                        searchDefectTypeValue = ""
                                                    }
                                                )
                                            }
                                            else -> {}
                                        }
                                    }
                                }
                            }
                            else -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .scrollable(
                                    state = scrollState,
                                    enabled = true,
                                    orientation = Orientation.Vertical
                                ),
                            verticalArrangement = Arrangement.Top
                        ) {
                            if (defect == null) {
                                PhotosList(
                                    photosList = photoListState.toMap(),
                                    onDeleteClicked = { photo ->
                                        viewModel.deletePhotoFromUploadList(photo)
                                    },
                                    onChangeDefectStatus = { photo ->
                                        val done = !photo.second
                                        val newPhoto = Pair(photo.first, done)
                                        viewModel.changePhotoDoneStatus(
                                            oldPhoto = photo,
                                            newPhoto = newPhoto
                                        )
                                    },
                                    onImageClicked = { uriString ->
                                        zoomImageState = uriString
                                        shouldShowFullSizedPhoto = true
                                    }
                                )
                            } else {
                                EditDefectPhotoList(
                                    photosList = editDefect.photos,
                                    onChangePhotoDoneStatus = { photo ->
                                        val map = editDefect.photos.toMutableMap()
                                        map[photo.first] = !photo.second
                                        editDefect.photos = map
                                    },
                                    onImageClicked = { uriString ->
                                        Log.d("Image Uri:", uriString)
                                        zoomImageState = uriString
                                        shouldShowFullSizedPhoto = true
                                    }
                                )
                            }
                        }
                    }
                },
                bottomBar = {
                    if (defect == null) {
                        Column(
                            modifier = Modifier
                                .background(color = Color.White)
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            OutlinedTextField(
                                value = commentValue,
                                onValueChange = { commentValue = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .background(color = Color.White)
                                    .padding(bottom = 8.dp),
                                label = {
                                    Text(
                                        stringResource(R.string.comment),
                                        color = DarkBlue,
                                        style = Typography.body2
                                    )
                                },
                                textStyle = Typography.body2,
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color.Black,
                                    focusedIndicatorColor = LightBackground,
                                    unfocusedIndicatorColor = LightBackground,
                                    textColor = DarkBlue
                                ),
                                maxLines = 3,
                                trailingIcon = {
                                    IconButton(onClick = { focusManager.clearFocus(force = true) }) {
                                        Icon(
                                            modifier = Modifier.padding(8.dp),
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = null,
                                            tint = DarkBlue
                                        )
                                    }
                                },
                            )
                            BottomBar(
                                onSaveClick = {
                                    val listOfPhotosNames = mutableListOf<String>()
                                    photoListState
                                        .toMap().keys.toMutableList().forEach { name ->
                                            val newName = URLDecoder.decode(name, "UTF-8")
                                        listOfPhotosNames.add(newName.substringAfterLast("/"))
                                    }
                                    val newDefect = Defect(
                                        type = selectedDefectType.name,
                                        checkType = selectedType.type,
                                        photos = photoListState.toMap(),
                                        contractor = selectedContractor.name,
                                        status = false,
                                        comment = commentValue,
                                        project = selectedProject.name,
                                        location = selectedLocation.name,
                                        date = LocalDateTime.now().toString(),
                                        photoNames = listOfPhotosNames
                                    )
                                    viewModel.addDefect(defect = newDefect,photosList = viewModel.photosToUpdateList.value)
                                },
                                onStorageClick = {
                                    storageLauncher.launch("image/*")

                                },
                                onCameraClick = {
                                    shouldShowCamera = true
                                },
                                saveEnabled =
                                saveDefectEnabled
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .background(color = Color.White)
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            OutlinedTextField(
                                value = commentValue,
                                onValueChange = {
                                    commentValue = it
                                    editDefect.comment = it
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .background(color = Color.White)
                                    .padding(bottom = 8.dp),
                                label = {
                                    Text(
                                        stringResource(R.string.comment),
                                        color = DarkBlue,
                                        style = Typography.body2
                                    )
                                },
                                textStyle = Typography.body2,
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color.Black,
                                    focusedIndicatorColor = LightBackground,
                                    unfocusedIndicatorColor = LightBackground,
                                    textColor = DarkBlue
                                ),
                                maxLines = 3,
                                trailingIcon = {
                                    IconButton(onClick = { focusManager.clearFocus(force = true) }) {
                                        Icon(
                                            modifier = Modifier.padding(8.dp),
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = null,
                                            tint = DarkBlue
                                        )
                                    }
                                },
                            )
                            SelectableButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(12.dp),
                                title = stringResource(id = R.string.save).uppercase(),
                                onClick = {
                                    if(!editDefect.photos.values.contains(false)){
                                        editDefect.status = true
                                    }
                                    viewModel.updateDefect(editDefect)
                                    navController.navigateUp()
                                },
                                icon = true,
                                selected = true
                            )
                            SelectableButton(
                                selected = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(bottom = 12.dp, start = 12.dp, end = 12.dp),
                                title = stringResource(R.string.report),
                                onClick = {
                                    viewModel.pdf(context = context, defect = defect )
                                },
                                icon = false,
                            )
                        }
                    }

                }
            )
        }
    }
}






