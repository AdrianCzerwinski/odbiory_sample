package com.example.app.presentation.screens.main.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.domain.model.Project
import com.example.app.ui.theme.*

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    projects: List<Project>,
    onNewProjectClick: () -> Unit,
    value: String = "",
    onValueChange: (String) -> Unit,
    bottomSheetHide: () -> Unit,
    bottomSheetShow: () -> Unit,
    onClearClick: () -> Unit,
    onDeleteClicked: (Project) -> Unit,
    onNavigateToLocations: (Project) -> Unit,
    onNavigateToDefects: (Project) -> Unit,
    onShareClicked: (Project) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val lazyColumnWeight = remember {
        mutableStateOf(0.8f)
    }
    val newProjectWeight = remember {
        mutableStateOf(0.2f)
    }
    Surface(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight(0.9f)
            .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.weight(lazyColumnWeight.value)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .scrollable(
                            state = scrollState,
                            enabled = true,
                            orientation = Orientation.Vertical
                        ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        items = projects,
                        key = { project ->
                            project.creationDate
                        }
                    ) { project ->
                        Item(
                            project = project,
                            onDeleteClicked = onDeleteClicked,
                            onNavigateToLocations = onNavigateToLocations,
                            onNavigateToDefects = onNavigateToDefects,
                            onShareClicked = { onShareClicked(project) }
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(newProjectWeight.value)) {
                NewProject(
                    onNewProjectClick = {
                        focusManager.clearFocus()
                        onNewProjectClick()
                        onClearClick()
                    },
                    value = value,
                    onValueChange = onValueChange,
                    onClearClick = {
                        focusManager.clearFocus()
                        onClearClick()
                    },
                    bottomSheetHide = {
                        bottomSheetHide()
                        lazyColumnWeight.value = 0.5f
                        newProjectWeight.value = 0.5f
                                      },
                    bottomSheetShow = {
                        bottomSheetShow()
                        lazyColumnWeight.value = 0.8f
                        newProjectWeight.value = 0.2f
                    }
                )
            }


        }
    }

}


@Composable
fun Item(
    project: Project,
    onDeleteClicked: (Project) -> Unit,
    onNavigateToLocations: (Project) -> Unit,
    onNavigateToDefects: (Project) -> Unit,
    onShareClicked: () -> Unit
) {
    var expandedState by remember { mutableStateOf(false)}

    Card(
        modifier = Modifier.animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)

        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .clickable {
                        expandedState = !expandedState
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(6f),
                    text = project.name,
                    style = Typography.subtitle1.copy(color = DarkBlue),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(

                    modifier = Modifier
                        .size(25.dp)
                        .weight(1f),
                    onClick = {expandedState = !expandedState}
                ) {
                    Icon(
                        imageVector =
                                      if (!expandedState)  {Icons.Default.KeyboardArrowDown }
                                      else {Icons.Filled.KeyboardArrowUp}
                                      ,
                        contentDescription = "DropDownArrow",
                        tint = DarkBlue
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
                                   onShareClicked()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.share_uppercases),
                        style = Typography.subtitle2.copy(color = LightBlue)
                    )
                    Icon(
                        imageVector = Icons.Default.Share,
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
                            onNavigateToLocations(project)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                   Text(
                       modifier = Modifier.padding(horizontal = 16.dp),
                       text = stringResource(R.string.locals_uppercase),
                       style = Typography.subtitle2.copy(color = LightBlue)
                   )
                   Icon(
                       imageVector = Icons.Default.ArrowForward,
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
                            onNavigateToDefects(project)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.defects).uppercase(),
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
                            onDeleteClicked(project)
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

            }

            Divider(color = LightBackground, thickness = 2.dp)
        }

    }

}

@Composable
fun NewProject(
    onNewProjectClick: () -> Unit,
    value: String = "",
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    bottomSheetHide: () -> Unit,
    bottomSheetShow: () -> Unit
) {
    var dismissIconState by remember {
        mutableStateOf(false)
    }
    val localFocusManager = LocalFocusManager.current
    Divider(color = Color.Transparent, thickness = 4.dp)
    Text(
        text = "Dodaj nowy projekt",
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
                dismissIconState = if (it.isFocused) {
                    bottomSheetHide()
                    true
                } else {
                    bottomSheetShow()
                    false
                }
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
                IconButton(onClick = { onNewProjectClick() }) {
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
                onNewProjectClick()
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

//@Preview
//@Composable
//fun MainPrev() {
//    var value by remember {
//        mutableStateOf("")
//    }
////    MainContent(projects = listOf(Project(id = "blabla", name = "Sample project")), onNewProjectClick = { /*TODO*/ }, onValueChange = {value = it}, value = value)
//}
//
