package com.example.app.presentation.screens.contractors

import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.app.R
import com.example.app.core.LockScreenOrientation
import com.example.app.core.MyAlertDialog
import com.example.app.domain.model.Contractor
import com.example.app.domain.model.Response
import com.example.app.presentation.components.MessageBar
import com.example.app.presentation.navigation.Screen
import com.example.app.presentation.screens.contractors.components.AddNewContractor
import com.example.app.presentation.screens.contractors.components.ContractorsList
import com.example.app.presentation.screens.contractors.components.TopSubScreenBar
import com.example.app.ui.theme.DarkGray
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.Typography

@Composable
fun Contractors(
    navController: NavHostController,
    viewModel: ContractorsViewModel = hiltViewModel(),
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    BackHandler(
        enabled = true,
        onBack = { navController.navigate(Screen.Main.route) }
    )

    val scaffoldState = rememberScaffoldState()
    var addPopUpState by remember {
        mutableStateOf(false)
    }

    val messageBarState by viewModel.messageBarState
    var openDeleteDialog by remember { mutableStateOf(false) }
    var contractorName by remember { mutableStateOf("") }
    var contactPerson by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var currentContractor by remember { mutableStateOf(Contractor()) }

    AddNewContractor(
        onDismiss = { addPopUpState = !addPopUpState },
        onConfirm = {
            viewModel.addContractor(
                name = contractorName.trim(),
                contactPerson = contactPerson.trim(),
                phone = phone.trim(),
                email = email.trim()
            )
            contractorName = ""
            phone = ""
            email = ""
            contactPerson = ""
        },
        contractorName = contractorName,
        contactPerson = contactPerson,
        email = email,
        phone = phone,
        openDialog = addPopUpState,
        onContactPersonChange = { contactPerson = it },
        onEmailChange = { email = it },
        onPhoneChange = { phone = it },
        onContractorNameChange = { contractorName = it }
    )

    MyAlertDialog(
        question = "USUWANIE PODWYKONAWCY",
        description = "Czy na pewno usunąć? Wszystkie powiązane usterki zostaną utracone",
        onDismiss = { openDeleteDialog = false },
        onConfirm = {
            viewModel.deleteContractor(currentContractor)
            currentContractor = Contractor(
                id = "",
                name = "",
                contactPerson = "",
                phone = "",
                email = ""
            )
            openDeleteDialog = false
        },
        openDialog = openDeleteDialog
    )

    Surface(modifier = Modifier.fillMaxSize(), color = LightBackground) {
        Scaffold(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            scaffoldState = scaffoldState,
            topBar = {
                Column() {
                    MessageBar(messageBarState = messageBarState)
                    TopSubScreenBar(
                        modifier = Modifier.fillMaxWidth(),
                        onHomeClicked = {
                            navController.popBackStack(route = Screen.Main.route, inclusive = false)
                        },
                        onBackClicked = {
                            navController.navigate(Screen.Main.route)
                        }
                    )
                }
            },
            content = {
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Column(
                            modifier = Modifier.weight(0.9f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            when (val response = viewModel.contractors.value) {
                                is Response.Loading -> CircularProgressIndicator()
                                is Response.Success -> {
                                    if (response.data.isEmpty()) {
                                        Icon(
                                            imageVector = Icons.Default.List,
                                            contentDescription = "placeholder",
                                            tint = DarkGray.copy(alpha = 0.1f),
                                            modifier = Modifier.size(100.dp)
                                        )
                                        Text(
                                            text = stringResource(R.string.no_contractors),
                                            style = Typography.subtitle1,
                                            color = DarkGray.copy(alpha = 0.3f),
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    } else {
                                        ContractorsList(
                                            onDeleteClicked = {
                                                currentContractor = it
                                                openDeleteDialog = true
                                            },
                                            onEditClicked = {},
                                            contractors = response.data
                                        )
                                    }
                                }
                                else -> {}
                            }
                        }
                        Divider(
                            color = LightBackground,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Column(
                            modifier = Modifier
                                .weight(0.1f)
                                .fillMaxWidth()
                                .padding(18.dp)
                                .clickable {
                                    addPopUpState = true
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "add",
                                    tint = LightBackground.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = stringResource(R.string.add_subcontractor),
                                    style = Typography.subtitle1,
                                    color = LightBackground.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
