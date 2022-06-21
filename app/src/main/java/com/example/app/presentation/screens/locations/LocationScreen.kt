package com.example.app.presentation.screens.locations

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
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
import com.example.app.core.LockScreenOrientation
import com.example.app.core.MyAlertDialog
import com.example.app.domain.model.Location
import com.example.app.domain.model.Project
import com.example.app.domain.model.Response
import com.example.app.presentation.components.MessageBar
import com.example.app.presentation.components.ProgressBar
import com.example.app.presentation.navigation.Screen
import com.example.app.presentation.screens.contractors.components.TopSubScreenBar
import com.example.app.presentation.screens.locations.components.AddNewLocation
import com.example.app.presentation.screens.locations.components.LocationsList
import com.example.app.ui.theme.DarkGray
import com.example.app.ui.theme.Typography

@Composable
fun Locations(
    navController: NavHostController,
    viewModel: LocationViewModel = hiltViewModel(),
    project: Project?
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val scaffoldState = rememberScaffoldState()
    var locationName by remember { mutableStateOf("") }
    val localFocusManager = LocalFocusManager.current
    val messageBarState by viewModel.messageBarState
    val progressBarState by viewModel.progressBarState
    var openDeleteDialog by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf(Location()) }

    LaunchedEffect(key1 = true) {
        if (project != null) viewModel.getLocations(project)
    }

    MyAlertDialog(
        question = "USUWANIE LOKALU",
        description = "Czy na pewno usunąć? Wszystkie powiązane usterki zostaną utracone",
        onDismiss = { openDeleteDialog = false },
        onConfirm = {
            viewModel.deleteLocation(location = currentLocation, project = project!!)
            currentLocation = Location(
                id = "",
                name = ""
            )
            openDeleteDialog = false
        },
        openDialog = openDeleteDialog
    )

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {

                Column {
                    MessageBar(messageBarState = messageBarState)
                    TopSubScreenBar(
                        onBackClicked = { navController.navigateUp() },
                        onHomeClicked = {
                            navController.navigateUp()
                        }
                    )
                    if (progressBarState) ProgressBar()
                }
            },
            content = {
                Surface(color = Color.White) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        when (val locations = viewModel.location.value) {
                            is Response.Loading ->
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            is Response.Success -> {
                                if (locations.data.isEmpty()) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.List,
                                            contentDescription = "placeholder",
                                            tint = DarkGray.copy(alpha = 0.1f),
                                            modifier = Modifier.size(200.dp)
                                        )
                                        Text(
                                            text = stringResource(R.string.no_locations),
                                            style = Typography.subtitle1,
                                            color = DarkGray.copy(alpha = 0.3f),
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                } else {
                                    LocationsList(
                                        onLocationClicked = {
                                            currentLocation = it
                                            navController
                                                .currentBackStackEntry?.savedStateHandle?.set(
                                                    key = "location",
                                                    value = currentLocation
                                                )
                                            navController
                                                .currentBackStackEntry?.savedStateHandle?.set(
                                                    key = "project",
                                                    value = project
                                                )
                                            navController.navigate(Screen.DefectsList.route)
                                        },
                                        locations = locations.data,
                                        onDeleteClicked = {
                                            currentLocation = it
                                            openDeleteDialog = true
                                        }
                                    )
                                }
                            }
                            else -> {}
                        }
                    }
                }
            },
            bottomBar = {
                Surface(color = Color.White) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        AddNewLocation(
                            modifier = Modifier.padding(14.dp),
                            onNewLocationClicked = {
                                viewModel.addLocation(
                                    project = project!!,
                                    name = locationName
                                )
                                locationName = ""
                                localFocusManager.clearFocus()
                            },
                            onValueChange = { locationName = it },
                            value = locationName,
                            onClearClick = {
                                locationName = ""
                                localFocusManager.clearFocus()
                            }
                        )
                    }

                }

            }
        )
    }
}
