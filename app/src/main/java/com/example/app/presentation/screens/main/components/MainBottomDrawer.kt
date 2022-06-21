package com.example.app.presentation.screens.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.presentation.components.GoButton
import com.example.app.presentation.components.SelectableButton
import com.example.app.ui.theme.*
import com.google.firebase.auth.FirebaseUser

@Composable
fun MainBottomDrawer(
    modifier: Modifier = Modifier,
    onContractorsClicked:() -> Unit,
    user: FirebaseUser,
    newUserName: String,
    newEmailAddress: String,
    onClearClick: () -> Unit,
    onUserNameValueChange: (String) -> Unit,
    onEmailAddressValueChange: (String) -> Unit,
    onUserNameChange: () -> Unit,
    onEmailChange: () -> Unit,
    nameExpandedState: Boolean = false,
    emailExpandedState: Boolean = false,
    onExpandedNameClicked: () -> Unit,
    onExpandedEmailClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onAddNewDefectClick: () -> Unit

) {
    Column(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .padding(vertical = 12.dp),
            painter = painterResource(id = R.drawable.ic_logo_helmet),
            contentDescription = null
        )
        SelectableButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp),
            title = stringResource(R.string.add_defect).uppercase(),
            onClick = {
                onAddNewDefectClick()
            },
            icon = true
        )
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    onContractorsClicked()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.subcontractors),
                style = Typography.subtitle1,
                color = LightBackground,

                )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = LightBackground
            )
        }
        Divider(color = LightBackground.copy(alpha = 0.2f), thickness = 1.dp)
        PersonalData(
            user = user.displayName,
            field = stringResource(R.string.your_name),
            value = newUserName,
            onClearClick = onClearClick,
            onValueChange = onUserNameValueChange,
            onChange = onUserNameChange,
            expandedState = nameExpandedState,
            onExpandedClicked = onExpandedNameClicked
        )
        PersonalData(
            email = user.email!!,
            field = stringResource(R.string.your_email),
            value = newEmailAddress,
            onClearClick = onClearClick,
            onValueChange = onEmailAddressValueChange,
            onChange = onEmailChange,
            expandedState = emailExpandedState,
            onExpandedClicked = onExpandedEmailClicked
        )
        Spacer(modifier = Modifier.height(20.dp))
        GoButton(isVerified = true, text = stringResource(R.string.signout)) {
            onSignOutClicked()
        }


    }

}

@Composable
fun PersonalData(
    user: String? = "",
    email: String = "",
    field: String,
    onClearClick: () -> Unit,
    value: String = "",
    onValueChange: (String) -> Unit,
    onChange: () -> Unit,
    expandedState: Boolean = false,
    onExpandedClicked: () -> Unit
) {
    val dismissIconState = value.isNotBlank()
    val localFocusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(5f)) {
            Text(
                text = field,
                style = Typography.subtitle1,
                color = DarkGray.copy(alpha = 0.4f)
            )

            Text(
                text = user+email,
                style = Typography.subtitle2,
                color = LightBackground
            )

        }
        Column(modifier = Modifier.weight(2f), horizontalAlignment = Alignment.End) {
            Text(
                text = if (!expandedState) stringResource(R.string.change)
                else stringResource(R.string.close),
                style = Typography.subtitle1,
                color = DarkGray.copy(alpha = 0.4f),
                modifier = Modifier.clickable {
                    onExpandedClicked()
                })
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        }

    }
    if (expandedState) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                value = value,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = SuperLightBlue,
                    cursorColor = Color.Black,
                    disabledLabelColor = SuperLightBlue,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = DarkBlue
                ),
                onValueChange = onValueChange,
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                leadingIcon = {
                    if (value.isNotEmpty()) {
                        IconButton(onClick = { onChange() }) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = DarkBlue
                            )
                        }
                    }
                },
                trailingIcon = {
                    if (dismissIconState) {
                        IconButton(onClick = {
                            onClearClick()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null,
                                tint = DarkBlue
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        localFocusManager.clearFocus(true)
                        onChange()
                        onClearClick()
                    }
                )

            )
        }
    }
    Divider(color = LightBackground.copy(alpha = 0.2f), thickness = 1.dp)


}

