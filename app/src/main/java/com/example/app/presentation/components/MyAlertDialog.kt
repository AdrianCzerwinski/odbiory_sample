package com.example.app.core

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.app.R
import com.example.app.ui.theme.DarkBlue
import com.example.app.ui.theme.LightBlue

@Composable
fun MyAlertDialog(
    question: String,
    description: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    openDialog: Boolean
) {
    if (openDialog) {
        AlertDialog(
            title = {
                Text(
                    text = question,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )
            },
            text = {
                Text(
                    color = LightBlue,
                    text = description,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                    onDismiss()
                }) {
                    Text(text = stringResource(com.example.app.R.string.yes))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    onDismiss()
                }) {
                    Text(text = stringResource(R.string.no))
                }

            },
            onDismissRequest = { onDismiss() }
        )
    }

}