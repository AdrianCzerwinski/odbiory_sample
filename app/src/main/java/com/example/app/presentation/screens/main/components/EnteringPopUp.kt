package com.example.app.presentation.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app.R
import com.example.app.presentation.components.GoButton
import com.example.app.presentation.screens.main.MainViewModel
import com.example.app.ui.theme.*

@Composable
fun EnteringPopUp(
    viewModel: MainViewModel = hiltViewModel()
) {

    var userName by remember {
        mutableStateOf("")
    }
    val localFocusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier
            .padding(40.dp)
            .wrapContentSize()
            .shadow(8.dp, shape = RoundedCornerShape(8.dp))
            ,
        shape = RoundedCornerShape(8.dp),
        ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(R.string.welcome),
                style = Typography.h1,
                color = LightBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp)
                    .background(Color.White)
                    .fillMaxWidth(),
                shape = Shapes.medium,
                value = userName,
                onValueChange = { userName = it },
                label = {
                    Text(
                        text = stringResource(R.string.name),
                        color = DarkBlue,
                        style = Typography.subtitle1
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = LightBlue,
                    unfocusedBorderColor = DarkBlue,
                    textColor = DarkBlue
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

            Spacer(modifier = Modifier.height(24.dp))

            GoButton(
                isVerified = true,
                text = stringResource(R.string.zatwierdz),
                onClick = {
                    viewModel.updateName(userName)
                }
            )
        }
    }
}