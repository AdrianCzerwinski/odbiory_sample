package com.example.app.presentation.screens.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.ui.theme.DarkBlue
import com.example.app.ui.theme.LightBlue
import com.example.app.ui.theme.Shapes
import com.example.app.ui.theme.Typography


@Composable
fun ShareProject(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    userEmail: String,
    onEmailChange: (String) -> Unit,
    openDialog: Boolean = false
) {
    val localFocusManager = LocalFocusManager.current
    if (openDialog) {
        Box(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxSize()
        ) {
            AlertDialog(
                modifier = Modifier.wrapContentHeight().fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(R.string.share_project),
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                },
                text = {

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 24.dp)) {

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                                .fillMaxWidth(),
                            shape = Shapes.medium,
                            value = userEmail,
                            onValueChange = onEmailChange,
                            label = { Text(
                                text = stringResource(R.string.email),
                                color = LightBlue,
                                style = Typography.subtitle2
                            ) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = LightBlue,
                                unfocusedBorderColor = DarkBlue,
                                textColor = DarkBlue
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Email
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { localFocusManager.clearFocus() }
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                },
                onDismissRequest = { onDismiss() },
                confirmButton = {
                    Button(
                        onClick = {
                            onConfirm()
                            onDismiss()
                        }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "confirm")
                        Text(stringResource(R.string.share))
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = {
                        onDismiss()
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "dismiss")
                        Text(text = stringResource(R.string.dismiss))
                    }
                }
            )
        }
    }

}

