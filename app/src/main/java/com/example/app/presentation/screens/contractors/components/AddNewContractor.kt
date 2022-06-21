package com.example.app.presentation.screens.contractors.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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
fun AddNewContractor(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    contractorName: String,
    contactPerson: String,
    email: String,
    phone: String,
    openDialog: Boolean,
    onContactPersonChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onContractorNameChange: (String) -> Unit
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
                        text = stringResource(R.string.add_subcontractor),
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                },
                text = {

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()) {

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                                .fillMaxWidth(),
                            shape = Shapes.medium,
                            value = contractorName,
                            onValueChange = onContractorNameChange,
                            label = { Text(
                                text = stringResource(R.string.contractor_name),
                                color = LightBlue,
                                style = Typography.subtitle2
                            ) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = LightBlue,
                                unfocusedBorderColor = DarkBlue,
                                textColor = DarkBlue
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
                            ),
                            singleLine = true
                        )


                        OutlinedTextField(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                                .fillMaxWidth(),
                            shape = Shapes.medium,
                            value = contactPerson,
                            onValueChange = onContactPersonChange,
                            label = { Text(
                                text = stringResource(R.string.contact_person),
                                color = LightBlue,
                                style = Typography.subtitle2
                            ) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = LightBlue,
                                unfocusedBorderColor = DarkBlue,
                                textColor = DarkBlue
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                                .fillMaxWidth(),
                            shape = Shapes.medium,
                            value = email,
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
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Email
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                                .fillMaxWidth(),
                            shape = Shapes.medium,
                            value = phone,
                            onValueChange = onPhoneChange,
                            label = { Text(
                                text = stringResource(R.string.phone_number),
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
                                keyboardType = KeyboardType.Phone
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
                        Icon(imageVector = Icons.Default.Done, contentDescription = "confirm")
                        Text(stringResource(R.string.add))
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

