package com.example.app.presentation.screens.locations.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.DarkBlue
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.SuperLightBlue

@Composable
fun AddNewLocation(
    modifier: Modifier = Modifier,
    onNewLocationClicked: () -> Unit,
    value: String = "",
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit
) {
    var dismissIconState by remember {
        mutableStateOf(false)
    }
    val localFocusManager = LocalFocusManager.current
    Divider(color = Color.Transparent, thickness = 4.dp)
    Text(
        text = "Dodaj lokalizację",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 20.dp),
        textAlign = TextAlign.Start,
        color = LightBackground
    )
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            .onFocusChanged {
                dismissIconState = it.isFocused
            },

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
                IconButton(onClick = { onNewLocationClicked() }) {
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
                IconButton(onClick = { onClearClick() }) {
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
                onNewLocationClicked()
                onClearClick()
            }
        )

    )
    Text(
        text = "${value.length} / 30",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        textAlign = TextAlign.End,
        color = LightBackground
    )

}