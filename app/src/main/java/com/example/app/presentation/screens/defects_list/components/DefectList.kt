package com.example.app.presentation.screens.defects_list.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.domain.model.Defect
import com.example.app.presentation.components.SelectableButton
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.SuperLightBlue
import com.example.app.ui.theme.Typography

@Composable
fun DefectList(
    defectList: List<Defect>?,
    onDeleteClicked: (Defect) -> Unit,
    onEditClicked: (Defect) -> Unit,
    onStatusChanged: (Defect) -> Unit,
    typeCheck: (String),
    onShowFiltersClicked: () -> Unit
) {

    var shouldShowFilters by remember { mutableStateOf(false) }
    var mutableDefectList by remember { mutableStateOf(listOf(Defect())) }

    LaunchedEffect(key1 = defectList) {
        mutableDefectList = defectList!!
    }

    var status by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 20.dp), horizontalArrangement = Arrangement.Start
            ) {
                Switch(
                    checked = status,
                    onCheckedChange = {
                        status = !status
                    },
                    modifier = Modifier
                        .size(16.dp)
                        .width(20.dp)
                        .padding(8.dp),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = LightBackground,
                        uncheckedThumbColor = Color.LightGray,
                        checkedTrackColor = LightBackground.copy(alpha = 0.1f),
                        uncheckedTrackColor = Color.LightGray.copy(alpha = 0.1f),
                        checkedTrackAlpha = 1.0f,
                        uncheckedTrackAlpha = 0.3f
                    )
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Pokaż usunięte",
                    style = Typography.body2,
                    color = if (status) LightBackground else Color.LightGray
                )
            }
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.End
            ) {
                SelectableButton(
                    title = "Filtry", onClick = {
                        shouldShowFilters = !shouldShowFilters
                        onShowFiltersClicked()
                    },
                    selected = shouldShowFilters,
                    minWidth = 0.dp
                )
            }
        }

        if (defectList != null && defectList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "emptyList",
                    tint = LightBackground.copy(alpha = 0.7f),
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = stringResource(R.string.no_defects),
                    style = Typography.body2.copy(fontWeight = FontWeight.Bold),
                    color = SuperLightBlue.copy(alpha = 0.9f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .scrollable(
                        state = scrollState,
                        enabled = true,
                        orientation = Orientation.Vertical
                    )
            ) {
                items(
                    items = if (status) mutableDefectList.filter {
                        it.checkType == typeCheck
                    } else mutableDefectList.filter {
                        (it.checkType == typeCheck) && !it.status
                    },
                    key = { defectKey ->
                        defectKey.date
                    }
                ) { defect ->

                    DefectItem(
                        defect = defect,
                        onDeleteClicked = { onDeleteClicked(defect) },
                        onEditClicked = { onEditClicked(defect) },
                        onStatusChanged = {onStatusChanged(defect)},
                        status = defect.status
                    )
                }
            }
        }
    }
}

