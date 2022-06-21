package com.example.app.presentation.screens.defects_list

import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.app.R
import com.example.app.core.Constants
import com.example.app.core.LockScreenOrientation
import com.example.app.core.MyAlertDialog
import com.example.app.domain.model.*
import com.example.app.presentation.components.DefectsTopBar
import com.example.app.presentation.components.SelectableButton
import com.example.app.presentation.navigation.Screen
import com.example.app.presentation.screens.defect.components.DropDownSearchContractors
import com.example.app.presentation.screens.defect.components.DropDownSearchDefectsTypes
import com.example.app.presentation.screens.defect.components.DropDownSearchLocations
import com.example.app.presentation.screens.defects_list.components.DefectList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun DefectsListScreen(
    navController: NavHostController,
    viewModel: DefectsListViewModel = hiltViewModel(),
    location: Location?,
    project: Project?
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val selectedItem by viewModel.checkedType
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var shouldShowFilters by remember { mutableStateOf(false) }

    var selectedLocation by remember { mutableStateOf(Location()) }
    var searchLocation by remember { mutableStateOf("") }
    var selectedContractor by remember { mutableStateOf(Contractor()) }
    var searchContractor by remember { mutableStateOf("") }
    var selectedDefectType by remember { mutableStateOf(DefectType()) }
    var searchDefectType by remember { mutableStateOf("") }

    var shouldShowProgressBar by remember { mutableStateOf(false)}
    var openDialog by remember { mutableStateOf(false)}
    var defectToDelete by remember {mutableStateOf(Defect())}

    LaunchedEffect(key1 = true)
    {
        shouldShowProgressBar = true
        viewModel.getLocations(project!!.id)
        if (location != null && location.name != "") {
            selectedLocation = location
            searchLocation = location.name
        } else {
            viewModel.getDefects(project = project)
            delay(500)
        }
        shouldShowProgressBar = false
    }

    LaunchedEffect(key1 = selectedLocation) {
        withContext(Dispatchers.IO) {
            shouldShowProgressBar = true
            if(selectedContractor.name != "" || selectedDefectType.name != ""){
                viewModel.resetFilters(project = project!!)
                delay(500)
                if (selectedContractor.name != "")
                    viewModel.filterDefectsByContractor(contractor = selectedContractor)
                if (selectedDefectType.name != "")
                    viewModel.filterDefectsByDefectType(defectType = selectedDefectType)
                if (selectedLocation.name != "")
                    viewModel.filterDefectsByLocation(location = selectedLocation)
            } else {
                viewModel.getDefects(project = project!!)
                delay(500)
                if (selectedLocation.name != "")
                    viewModel.filterDefectsByLocation(location = selectedLocation)
            }
            shouldShowProgressBar = false
        }
    }

    LaunchedEffect(key1 = selectedContractor) {
        withContext(Dispatchers.IO) {
            shouldShowProgressBar = true
            if(selectedLocation.name != "" || selectedDefectType.name != ""){
                viewModel.resetFilters(project = project!!)
                delay(500)
                if (selectedContractor.name != "")
                    viewModel.filterDefectsByContractor(contractor = selectedContractor)
                if (selectedDefectType.name != "")
                    viewModel.filterDefectsByDefectType(defectType = selectedDefectType)
                if (selectedLocation.name != "")
                    viewModel.filterDefectsByLocation(location = selectedLocation)
            } else {
                viewModel.getDefects(project = project!!)
                delay(500)
                if (selectedContractor.name != "")
                    viewModel.filterDefectsByContractor(contractor = selectedContractor)
            }
            shouldShowProgressBar = false
        }
    }

    LaunchedEffect(key1 = selectedDefectType) {
        withContext(Dispatchers.IO) {
            shouldShowProgressBar = true
            if(selectedContractor.name != "" || selectedLocation.name != ""){
                viewModel.resetFilters(project = project!!)
                delay(500)
                if (selectedContractor.name != "")
                    viewModel.filterDefectsByContractor(contractor = selectedContractor)
                if (selectedDefectType.name != "")
                    viewModel.filterDefectsByDefectType(defectType = selectedDefectType)
                if (selectedLocation.name != "")
                    viewModel.filterDefectsByLocation(location = selectedLocation)
            } else {
                viewModel.getDefects(project = project!!)
                delay(500)
                if (selectedDefectType.name != "")
                    viewModel.filterDefectsByDefectType(defectType = selectedDefectType)
            }
            shouldShowProgressBar = false
        }
    }

    MyAlertDialog(
        question = stringResource(R.string.notification),
        description = stringResource(R.string.delete_defect_confirm),
        onDismiss = { openDialog = false },
        onConfirm = {
            viewModel.deleteDefect(defect = defectToDelete)
                    },
        openDialog = openDialog
    )

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White,
        elevation = 10.dp
    ) {
        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.White)

                ) {
                    DefectsTopBar(
                        selectedItem = selectedItem,
                        onClick = { viewModel.typeChange(it) }
                    )
                    if (shouldShowFilters) {
                        val defectTypeList by viewModel.defectTypes
                        val contractorList by viewModel.contractors
                        val locationList by viewModel.locations

                        when (locationList) {
                            is Response.Success -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(horizontal = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    var expandedLocation by remember { mutableStateOf(false) }
                                    var searchLocationValue by remember { mutableStateOf(Constants.SEARCH) }
                                    DropDownSearchLocations(
                                        items = (locationList as Response.Success<List<Location>>).data,
                                        value = searchLocation,
                                        onValueChange = { searchLocation = it },
                                        onExpandedClicked = {
                                            expandedLocation = !expandedLocation
                                        },
                                        expanded = expandedLocation,
                                        onSelectedClicked = {
                                            selectedLocation = it
                                            searchLocation = it.name
                                            expandedLocation = false
                                        },
                                        searchValue = searchLocationValue,
                                        onClearClicked = {
                                            expandedLocation = false
                                            searchLocationValue = Constants.SEARCH
                                            focusManager.clearFocus()
                                        },
                                        onSearchClicked = {
                                            focusManager.clearFocus()
                                        },
                                        onSearchValueChange = {
                                            searchLocationValue = it
                                        },
                                        onSearchValueClicked = {
                                            searchLocationValue = Constants.SEARCH
                                        },
                                        label = stringResource(R.string.chooes_location),
                                        onAddClick = {

                                        },
                                        shouldShowAddButton = false,
                                        onValueErase = {
                                            searchLocationValue = ""
                                        }
                                    )
                                    if(contractorList is Response.Success) {
                                        var expandedContractor by remember { mutableStateOf(false) }
                                        var searchContractorValue by remember { mutableStateOf(Constants.SEARCH) }
                                        DropDownSearchContractors(
                                            items = (contractorList as Response.Success<List<Contractor>>).data,
                                            value = searchContractor,
                                            onValueChange = { searchContractor = it },
                                            onExpandedClicked = {
                                                expandedContractor = !expandedContractor
                                            },
                                            expanded = expandedContractor,
                                            onSelectedClicked = {
                                                selectedContractor = it
                                                searchContractor = it.name
                                                expandedContractor = false
                                            },
                                            searchValue = searchContractorValue,
                                            onClearClicked = {
                                                expandedContractor = false
                                                searchContractorValue = Constants.SEARCH
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

                                            },
                                            onValueErase = {
                                                searchContractor = ""
                                            },
                                            shouldShowAddButton = false
                                        )
                                    }
                                    if(defectTypeList is Response.Success) {
                                        var expandedDefectType by remember { mutableStateOf(false) }
                                        var searchDefectTypeValue by remember { mutableStateOf(Constants.SEARCH) }
                                        DropDownSearchDefectsTypes(
                                            items = (defectTypeList as Response.Success<List<DefectType>>).data,
                                            value = searchDefectType,
                                            onValueChange = { searchDefectType = it },
                                            onExpandedClicked = {
                                                expandedDefectType = !expandedDefectType
                                            },
                                            expanded = expandedDefectType,
                                            onSelectedClicked = {
                                                selectedDefectType = it
                                                searchDefectType = it.name
                                                expandedDefectType = false
                                            },
                                            searchValue = searchDefectTypeValue,
                                            onClearClicked = {
                                                expandedDefectType = false
                                                searchDefectTypeValue = Constants.SEARCH
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

                                            },
                                            onValueErase = {
                                                searchDefectTypeValue = ""
                                            }
                                        )
                                    }

                                }

                            }
                            else -> {
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth()
                                        .background(Color.White),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    SelectableButton(
                        selected = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(bottom = 12.dp, start = 12.dp, end = 12.dp),
                        title = stringResource(R.string.reset_filters),
                        onClick = {
                            searchLocation = ""
                            selectedLocation = Location()
                            searchContractor = ""
                            selectedContractor = Contractor()
                            searchDefectType = ""
                            selectedDefectType = DefectType()
                            viewModel.resetFilters(project = project!!)

                        },
                        icon = false
                    )
                }
            },
            content = {
                if(shouldShowProgressBar){
                    Column(
                        modifier = Modifier.fillMaxSize().background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(100.dp))
                    }
                } else {
                    when (val defectList = viewModel.defectList.value) {
                        is Response.Loading -> {
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.White),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(100.dp))
                            }

                        }
                        is Response.Success -> {
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.White)
                                    .scrollable(
                                        state = scrollState,
                                        orientation = Orientation.Vertical,
                                        enabled = true
                                    )
                            ) {
                                DefectList(
                                    defectList = defectList.data,
                                    onDeleteClicked = {
                                        defectToDelete = it
                                        openDialog = true
                                    },
                                    onEditClicked = { defect ->
                                        navController
                                            .currentBackStackEntry?.savedStateHandle?.set(
                                                key = "project",
                                                value = project
                                            )
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            key = "defect",
                                            value = defect
                                        )
                                        navController.navigate(Screen.Defect.route)
                                    },
                                    onStatusChanged = { defect ->
                                        if (defect.id.isNotEmpty()) {
                                            viewModel.updateDefectStatus(defect)
                                        }
                                    },
                                    typeCheck = selectedItem.type,
                                    onShowFiltersClicked = {
                                        shouldShowFilters = !shouldShowFilters
                                    }
                                )


                            }
                        }
                        is Response.ErrorMessageBar -> {

                        }
                        else -> {}
                    }
                }
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    SelectableButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(12.dp),
                        title = stringResource(id = R.string.add).uppercase(),
                        onClick = {
                            navController.currentBackStackEntry!!.savedStateHandle.set(
                                key = "location",
                                value = location
                            )
                            navController.currentBackStackEntry!!.savedStateHandle.set(
                                key = "project",
                                value = project
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "defect",
                                value = null
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "typeCheck",
                                value = selectedItem
                            )
                            Log.d("Type Check", selectedItem.type)
                            navController.navigate(Screen.Defect.route)
                        },
                        icon = true
                    )
                    SelectableButton(
                        selected = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(bottom = 12.dp, start = 12.dp, end = 12.dp),
                        title = stringResource(R.string.report),
                        onClick = {
                            /*TODO*/
                        },
                        icon = false,
                    )
                }
            }
        )
    }
}


