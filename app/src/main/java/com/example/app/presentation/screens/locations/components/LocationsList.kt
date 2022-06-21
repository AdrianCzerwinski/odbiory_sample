package com.example.app.presentation.screens.locations.components

import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app.domain.model.Location
import com.example.app.ui.theme.DarkBlue
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.Typography

@Composable
fun LocationsList(
    onLocationClicked: (Location) -> Unit,
    locations: List<Location>,
    onDeleteClicked: (Location) -> Unit
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
                items = locations,
                key = { location ->
                    location.name
                }
            ) { location ->
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(14.dp)
                                .weight(0.8f)
                                .clickable {
                                    onLocationClicked(location)
                                },
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = location.name,
                                style = Typography.subtitle1,
                                color = DarkBlue
                            )
                        }

                        Column(modifier = Modifier
                            .padding(14.dp)
                            .weight(0.2f)
                            .clickable {
                                onDeleteClicked(location)
                            }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "delete",
                                tint = LightBackground,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .clickable {
                                        onDeleteClicked(location)
                                        Log.d("Delete", location.id)
                                    }
                            )
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