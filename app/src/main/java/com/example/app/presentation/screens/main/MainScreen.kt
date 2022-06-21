package com.example.app.presentation.screens.main

import android.content.pm.ActivityInfo
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.app.core.LockScreenOrientation
import com.example.app.core.MyAlertDialog
import com.example.app.domain.model.Project
import com.example.app.domain.model.Response
import com.example.app.presentation.components.MessageBar
import com.example.app.presentation.components.ProgressBar
import com.example.app.presentation.navigation.Screen
import com.example.app.presentation.screens.main.components.*
import com.example.app.ui.theme.LightBackground

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val user = viewModel.user
    var shareProjectShow by remember { mutableStateOf(false) }
    var projectToShare by remember { mutableStateOf(Project()) }
    var shareUserEmail by remember { mutableStateOf("") }

    var newProjectName by remember {
        mutableStateOf("")
    }
    var newUserName by remember {
        mutableStateOf("")
    }
    var newEmailAddress by remember {
        mutableStateOf("")
    }
    var nameExpandedState by remember {
        mutableStateOf(false)
    }
    var emailExpandedState by remember {
        mutableStateOf(false)
    }
    var bottomSheetHeight by remember {
        mutableStateOf(56.dp)
    }
    val scrollState = rememberScrollState()
    val greetings = viewModel.greetings

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val currentSheetFraction = scaffoldState.currentSheetFraction
    val radiusAnim by animateDpAsState(
        targetValue = if (currentSheetFraction == 1f) 40.dp else 0.dp
    )
    val messageBarState by viewModel.messageBarState

    var openDialog by remember {
        mutableStateOf(false)
    }

    var currentProject by remember {
        mutableStateOf(Project())
    }

    MyAlertDialog(
        question = "USUWANIE PROJEKTU",
        description = "Czy na pewno chcesz usunąć ten projekt? Nie ma możliwości powrotu",
        onDismiss = { openDialog = false },
        onConfirm = {
            viewModel.deleteProject(currentProject)
            currentProject = Project(id = "empty", name = "empty")
        },
        openDialog = openDialog
    )
    ShareProject(
        onDismiss = {
            shareProjectShow = false
            shareUserEmail = ""
        },
        onConfirm = {
            viewModel.shareProject(email = shareUserEmail, project = projectToShare)
        },
        userEmail = shareUserEmail,
        onEmailChange = { shareUserEmail = it },
        openDialog = shareProjectShow
    )

    Surface(modifier = Modifier.fillMaxSize(), color = LightBackground) {

        BottomSheetScaffold(
            sheetShape = RoundedCornerShape(
                topStart = radiusAnim,
                topEnd = radiusAnim
            ),
            scaffoldState = scaffoldState,
            sheetPeekHeight = bottomSheetHeight,
            sheetContent = {
                MainBottomDrawer(
                    user = user!!,
                    newEmailAddress = newEmailAddress,
                    newUserName = newUserName,
                    onUserNameValueChange = {
                        newUserName = it
                    },
                    onEmailAddressValueChange = {
                        newEmailAddress = it
                    },
                    onClearClick = {
                        newEmailAddress = ""
                        newUserName = ""
                    },
                    onUserNameChange = {
                        viewModel.updateName(newUserName)
                        newUserName = ""
                        nameExpandedState = !nameExpandedState
                    },
                    onEmailChange = {
                        viewModel.updateEmail(newEmailAddress)
                        newEmailAddress = ""
                        emailExpandedState = !emailExpandedState
                    },
                    nameExpandedState = nameExpandedState,
                    onExpandedEmailClicked = {
                        emailExpandedState = !emailExpandedState
                    },
                    onExpandedNameClicked = {
                        nameExpandedState = !nameExpandedState
                    },
                    emailExpandedState = emailExpandedState,
                    onSignOutClicked = {
                        viewModel.signOut()
                        navController.navigate(Screen.Login.route)
                    },
                    onContractorsClicked = {
                        navController.navigate(Screen.Contractors.route)
                    },
                    onAddNewDefectClick = {
                        navController.navigate(Screen.Defect.route)
                    }
                )
            },
            content = {
                Scaffold(
                    topBar = {
                        Column {
                            MessageBar(messageBarState = messageBarState)
                            GreetingsBar(
                                greetings = greetings,
                                user = user?.displayName ?: "Użytkowniku"
                            )
                        }
                    },
                    content = {
                        when (val projectResponse = viewModel.projects.value) {
                            is Response.Loading -> ProgressBar()
                            is Response.Success ->
                                if (projectResponse.data.isEmpty()) {
                                    EmptyContent(

                                        onNewProjectClick = {
                                            viewModel.addNewProject(
                                                projectName = newProjectName
                                            )
                                        },
                                        value = newProjectName,
                                        onValueChange = {
                                            if (it.length <= 30) newProjectName = it
                                        },
                                        onClearClick = {
                                            newProjectName = ""
                                        },
                                        bottomSheetHide = { bottomSheetHeight = 0.dp },
                                        bottomSheetShow = { bottomSheetHeight = 56.dp }
                                    )
                                } else {

                                    MainContent(
                                        modifier = Modifier
                                            .fillMaxHeight(0.8f)
                                            .scrollable(
                                                state = scrollState,
                                                enabled = true,
                                                orientation = Orientation.Vertical
                                            ),
                                        projects = projectResponse.data,
                                        onNewProjectClick = {
                                            viewModel.addNewProject(
                                                newProjectName.trim()
                                            )
                                        },
                                        value = newProjectName,
                                        onValueChange = {
                                            if (it.length <= 30) newProjectName = it
                                        },
                                        onClearClick = {
                                            newProjectName = ""
                                        },
                                        bottomSheetHide = { bottomSheetHeight = 0.dp },
                                        bottomSheetShow = { bottomSheetHeight = 56.dp },
                                        onDeleteClicked = {
                                            openDialog = true
                                            currentProject = it
                                        },
                                        onNavigateToLocations = { project ->

                                            navController
                                                .currentBackStackEntry?.savedStateHandle?.set(
                                                    key = "project",
                                                    value = project
                                                )
                                            navController.navigate(Screen.Location.route)

                                        },
                                        onNavigateToDefects = { project ->

                                            navController
                                                .currentBackStackEntry?.savedStateHandle?.set(
                                                    key = "project",
                                                    value = project
                                                )
                                            navController.navigate(Screen.DefectsList.route)

                                        },
                                        onShareClicked = {
                                            shareProjectShow = true
                                            projectToShare = it
                                        }
                                    )
                                }
                            is Response.Error -> {}
                            else -> {}
                        }

                    }
                )
            },
            sheetGesturesEnabled = true
        )
    }

}


@ExperimentalMaterialApi
val BottomSheetScaffoldState.currentSheetFraction: Float
    get() {
        val fraction = bottomSheetState.progress.fraction
        val targetValue = bottomSheetState.targetValue
        val currentValue = bottomSheetState.currentValue
        return when {
            currentValue == BottomSheetValue.Collapsed && targetValue == BottomSheetValue.Collapsed -> 1f
            currentValue == BottomSheetValue.Expanded && targetValue == BottomSheetValue.Expanded -> 0.5f
            currentValue == BottomSheetValue.Collapsed && targetValue == BottomSheetValue.Expanded -> 1f - fraction
            currentValue == BottomSheetValue.Expanded && targetValue == BottomSheetValue.Collapsed -> 0.5f + fraction
            else -> fraction
        }

    }

