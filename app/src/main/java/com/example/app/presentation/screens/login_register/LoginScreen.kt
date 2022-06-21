package com.example.app.presentation.screens.login_register

import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.app.R
import com.example.app.core.LockScreenOrientation
import com.example.app.domain.model.MessageBarState
import com.example.app.domain.model.Response
import com.example.app.presentation.components.MessageBar
import com.example.app.presentation.components.ProgressBar
import com.example.app.presentation.navigation.Screen
import com.example.app.presentation.screens.login_register.components.Drawer
import com.example.app.ui.theme.*
import kotlinx.coroutines.delay


@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val localFocusManager = LocalFocusManager.current

    val userState by viewModel.signInState

    var messageBarState by remember {
        mutableStateOf(MessageBarState())
    }
    var startAnimation by remember {
        mutableStateOf(false)
    }
    var userEmail by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val isFormValid by derivedStateOf {
        userEmail.isNotBlank() && password.isNotBlank()
    }
    when (userState) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> if ((userState as Response.Success<Boolean>).data) {
            LaunchedEffect(key1 = (userState as Response.Success<Boolean>).data) {
                messageBarState = MessageBarState(message = "Zalogowano")
                delay(500)
                navController.navigate(Screen.Main.route)
            }
        }
        is Response.Error -> {
            val error = (userState as Response.Error).message
            messageBarState = MessageBarState(error = Exception(error))
        }
    }
    LaunchedEffect(key1 = true) {
        startAnimation = true

    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.weight(1f)) {
            MessageBar(messageBarState = messageBarState)
        }
        Column(modifier = Modifier.weight(9f)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = null,
                    modifier = Modifier.padding(30.dp)
                )
            }

            Drawer(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                content = {
                    Text(
                        text = stringResource(id = R.string.login_button),
                        style = Typography.h1,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp, top = 24.dp)
                            .background(Color.White)
                            .fillMaxWidth(),
                        shape = Shapes.medium,
                        value = userEmail,
                        onValueChange = { userEmail = it.trim() },
                        label = { Text(
                            text = stringResource(R.string.email),
                            color = DarkBlue,
                            style = Typography.subtitle1
                        ) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = LightBlue,
                            unfocusedBorderColor = DarkBlue
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp)
                            .background(Color.White)
                            .fillMaxWidth(),
                        shape = Shapes.medium,
                        value = password,
                        onValueChange = { password = it.trim() },
                        label = { Text(
                            text = stringResource(R.string.password),
                            color = DarkBlue,
                            style = Typography.subtitle1
                        ) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = LightBlue,
                            unfocusedBorderColor = DarkBlue
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { localFocusManager.clearFocus(true) }
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LoginButton(
                        isVerified = isFormValid,
                        onClick = {
                            viewModel.signInUser(email = userEmail, password = password)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.padding(8.dp)) {
                        Text(text = stringResource(R.string.new_user_part_1), style = Typography.body2, color = LightBlue)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.Register.route)
                            },
                            text = stringResource(R.string.new_user_part_2),
                            style = Typography.body1,
                            color = DarkBlue
                        )
                    }

                }, startAnimation = startAnimation
            )

        }
    }
}

