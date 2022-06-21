package com.example.app.presentation.screens.contractors.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.domain.model.Contractor
import com.example.app.ui.theme.DarkBlue
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.Typography

@Composable
fun ContractorsList(
    onEditClicked: (Contractor) -> Unit,
    onDeleteClicked: (Contractor) -> Unit,
    contractors: List<Contractor>,
) {

    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize()) {
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
                items = contractors,
                key = { contractor ->
                    contractor.id
                }
            ) { contractor ->
                var expandedState by remember { mutableStateOf(false) }
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(0.6f)
                                .padding(
                                    start = 28.dp,
                                    end = 12.dp
                                ),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Text(
                                text = contractor.name,
                                style = Typography.subtitle1,
                                color = DarkBlue
                            )
                        }
                        Column(
                            modifier = Modifier.weight(0.4f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (!expandedState) {
                                        Icons.Default.KeyboardArrowDown
                                    } else {
                                        Icons.Default.KeyboardArrowUp
                                    },
                                    contentDescription = "drop_down",
                                    tint = DarkBlue,
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .clickable {
                                            expandedState = !expandedState
                                        }
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete",
                                    tint = LightBackground,
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .clickable {
                                            onDeleteClicked(contractor)
                                        }
                                )
                            }
                        }
                    }

                    if (expandedState) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.name_it),
                                    style = Typography.body1.copy(color = Color.Gray)
                                )
                                Text(
                                    text = contractor.name,
                                    style = Typography.body1.copy(color = LightBackground)
                                )
                            }
                            MyDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.contact),
                                    style = Typography.body1.copy(color = Color.Gray)
                                )
                                Text(
                                    text = contractor.contactPerson,
                                    style = Typography.body1.copy(color = LightBackground)
                                )
                            }
                            MyDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.email),
                                    style = Typography.body1.copy(color = Color.Gray)
                                )
                                Text(
                                    text = contractor.email,
                                    style = Typography.body1.copy(color = LightBackground)
                                )
                            }
                            MyDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.phone_number),
                                    style = Typography.body1.copy(color = Color.Gray)
                                )
                                Text(
                                    text = contractor.phone,
                                    style = Typography.body1.copy(color = LightBackground)
                                )
                            }
                            MyDivider()
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onEditClicked(contractor)
                                    },
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.edit),
                                    color = DarkBlue,
                                    modifier = Modifier.padding(horizontal = 2.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "edit",
                                    tint = DarkBlue
                                )
                            }
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(),
                        color = LightBackground,
                        thickness = 1.dp
                    )
                }

            }

        }
    }

}

@Composable
fun MyDivider() {
    Divider(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(),
        color = DarkBlue.copy(alpha = 0.3f),
        thickness = 1.dp
    )
}

@Preview
@Composable
fun ContractorsPrev() {
    ContractorsList(onEditClicked = {}, onDeleteClicked = {}, contractors = listOf(
        Contractor(
            id = "1",
            name = "Bauhaus",
            contactPerson = "Adam",
            phone = "666565566",
            email = "asdsad@ddd.pl"
        )
    )
    )
}