package com.example.app.presentation.screens.defects_list.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.domain.model.Defect
import com.example.app.ui.theme.*

@Composable
fun DefectItem(
    defect: Defect,
    onDeleteClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onStatusChanged: () -> Unit,
    status: Boolean
) {
    var expandedState by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)

        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 4.dp)
                    .fillMaxWidth()
                    .clickable {
                        expandedState = !expandedState
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.weight(0.65f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(

                        modifier = Modifier
                            .size(25.dp)
                            .weight(0.1f),
                        onClick = { expandedState = !expandedState }
                    ) {
                        Icon(
                            imageVector =
                            if (!expandedState) {
                                Icons.Default.KeyboardArrowDown
                            } else {
                                Icons.Filled.KeyboardArrowUp
                            },
                            contentDescription = "DropDownArrow",
                            tint = DarkBlue
                        )
                    }
                    Text(
                        modifier = Modifier.weight(0.9f),
                        text = defect.date.dropLast(13) + " " + defect.type + " " + defect.contractor,
                        style = Typography.body2.copy(color = DarkBlue),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(0.35f).padding(end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = status,
                        onCheckedChange = {
                            onStatusChanged()
                        },
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 8.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = InfoGreen,
                            uncheckedThumbColor = Color.LightGray,
                            checkedTrackColor = InfoGreen.copy(alpha = 0.1f),
                            uncheckedTrackColor = Color.LightGray.copy(alpha = 0.1f),
                            checkedTrackAlpha = 1.0f,
                            uncheckedTrackAlpha = 0.3f
                        )
                    )
                }
            }

            if (expandedState) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            onEditClicked()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.edit).uppercase(),
                        style = Typography.subtitle2.copy(color = LightBlue)
                    )
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = LightBlue.copy(alpha = 0.5f)
                    )
                }
                Divider(color = LightBackground.copy(alpha = 0.2f), thickness = 1.dp)

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            onDeleteClicked()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.delete_uppercase),
                        style = Typography.subtitle2.copy(color = LightBlue)
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = LightBlue.copy(alpha = 0.5f)
                    )
                }

                OutlinedTextField(
                    value = defect.comment,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(color = Color.White)
                        .padding(bottom = 8.dp),
                    textStyle = Typography.body2,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = LightBackground,
                        unfocusedIndicatorColor = LightBackground,
                        textColor = DarkBlue
                    ),
                    maxLines = 3,
                    readOnly = true
                )
            }
            Divider(
                color = LightBackground,
                thickness = 2.dp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}