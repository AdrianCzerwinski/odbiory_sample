package com.example.app.presentation.screens.defect.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.app.core.Constants.SEARCH
import com.example.app.domain.model.DefectType
import com.example.app.presentation.components.SearchTopBar
import com.example.app.ui.theme.DarkBlue
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.Typography

@Composable
fun DropDownSearchDefectsTypes(
    items: List<DefectType>,
    value: String,
    onValueChange: (String) -> Unit,
    searchValue: String,
    onSearchValueClicked: () -> Unit,
    onSearchValueChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    expanded: Boolean,
    onClearClicked: () -> Unit,
    onSelectedClicked: (DefectType) -> Unit,
    onExpandedClicked: () -> Unit,
    label: String,
    onAddClick: () -> Unit,
    onValueErase: () -> Unit
) {
    var textFieldWidth by remember { mutableStateOf(Size.Zero) }
    var itemsList by remember { mutableStateOf(items) }
    var trailingIconState by remember { mutableStateOf(Icons.Default.Close)}
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column() {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldWidth = coordinates.size.toSize()
                }.clickable {
                    onExpandedClicked()
                },
            label = { Text(label, color = DarkBlue, style =Typography.body2) },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    tint = DarkBlue,
                    contentDescription = "expand",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { onExpandedClicked() }
                )
            },
            textStyle = Typography.body2,
            readOnly = true,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                focusedIndicatorColor = LightBackground,
                unfocusedIndicatorColor = LightBackground,
                textColor = DarkBlue
            ),
            enabled = false
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedClicked() },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldWidth.width.toDp() })
        ) {
            DropdownMenuItem(onClick = {}) {
                SearchTopBar(
                    onClearClick = {
                        if (trailingIconState == Icons.Default.Close) {
                            onClearClicked()
                        } else {
                            onAddClick()
                        }
                        itemsList = items
                    },
                    onSearchClick = {
                                    onSearchClicked()
                    },
                    onValueChange = {
                        onSearchValueChange(it)
                        itemsList.filter { item ->
                            item.name.startsWith(searchValue)
                        }.also {
                            if (it.isEmpty()){
                                trailingIconState = Icons.Default.Add
                            }
                        }
                    },
                    valueEraser = {
                        onSearchValueClicked()
                        itemsList = items
                        onValueErase()
                    },
                    value = searchValue,
                    trailingIcon = trailingIconState
                )
            }
            if (items.isNotEmpty()) {

                Column(modifier = Modifier.fillMaxHeight(0.5f)) {
                    if (searchValue == SEARCH) {
                        itemsList.forEach { project ->
                            DropdownMenuItem(onClick = {
                                onSelectedClicked(project)
                            }) {
                                Text(
                                    text = project.name,
                                    style = Typography.body1,
                                    color = DarkBlue
                                )
                            }
                        }
                    } else {
                        itemsList.filter {
                            it.name.lowercase().contains(searchValue.lowercase())
                        }.forEach { project ->
                            DropdownMenuItem(onClick = {
                                onSelectedClicked(project)
                            }) {
                                Text(
                                    text = project.name,
                                    style = Typography.body1,
                                    color = DarkBlue
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewSearchDropDown() {
//    var projectName by remember { mutableStateOf("")}
//    DropDownSearchProjects(
//        projects = listOf(Project(id = "1", name = "Testowy")),
//        value = projectName,
//        onValueChange = { projectName = it })
//}
