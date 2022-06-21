package com.example.app.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.app.ui.theme.DarkBlue
import com.example.app.ui.theme.LightBackground

@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    onClearClick: () -> Unit,
    onSearchClick: () -> Unit,
    value: String = stringResource(R.string.search),
    onValueChange: (String) -> Unit,
    valueEraser: () -> Unit,
    trailingIcon: ImageVector = Icons.Default.Close
) {
    var dismissIconState by remember {
        mutableStateOf(false)
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    dismissIconState = it.isFocused
                    if (it.isFocused) valueEraser()
                },
            value = value,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.Red,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = LightBackground
            ),
            onValueChange = onValueChange,
            singleLine = true,
            leadingIcon = {

                    IconButton(onClick = {
                        onSearchClick()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                            tint = LightBackground
                        )
                    }

            },
            trailingIcon = {
                if (dismissIconState) {
                    IconButton(onClick = { onClearClick() }) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null,
                            tint = DarkBlue
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (trailingIcon == Icons.Default.Close) {
                        onSearchClick()
                        onClearClick()
                    } else {
                        onSearchClick()
                    }

                }
            )
        )
    }
}

@Preview
@Composable
fun PreviewSearch() {
    var value by remember { mutableStateOf("Search...")}
    SearchTopBar(
        value = value,
        onClearClick = { /*TODO*/ },
        onSearchClick = { /*TODO*/ },
        onValueChange = {value = it},
        valueEraser = { }
    )
}