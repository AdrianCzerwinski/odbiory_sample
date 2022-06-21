@file:OptIn(ExperimentalFoundationApi::class)

package com.example.app.presentation.screens.defect.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.app.R
import com.example.app.ui.theme.InfoGreen
import com.example.app.ui.theme.SuperLightBlue
import com.example.app.ui.theme.Typography

@Composable
fun EditDefectPhotoList(
    modifier: Modifier = Modifier,
    photosList: Map<String, Boolean>,
    onChangePhotoDoneStatus: (Pair<String, Boolean>) -> Unit,
    onImageClicked: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    if (photosList.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "no photos",
                tint = SuperLightBlue.copy(alpha = 0.7f),
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = stringResource(R.string.no_photos),
                style = Typography.body2.copy(fontWeight = FontWeight.Bold),
                color = SuperLightBlue.copy(alpha = 0.9f)
            )
        }
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .scrollable(
                    state = scrollState,
                    orientation = Orientation.Horizontal,
                    enabled = true
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            items(
                items = photosList.toList(),
                key = { photoKey ->
                    photoKey.first
                }
            ) { photo ->

                var checkState by remember { mutableStateOf(photo.second)}

                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 6.dp)
                        .height(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier.size(70.dp)) {

                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photo.first)
                                .placeholder(R.drawable.ic_baseline_broken_image_24)
                                .build()
                        )
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .matchParentSize()
                                .combinedClickable(
                                    onLongClick = { onImageClicked(photo.first) },
                                    enabled = true,
                                    onClick = {},
                                    onDoubleClick = {},
                                    onClickLabel = "fullSize",
                                    role = Role.Button
                                )
                        )
                    }
                    Switch(
                        checked = checkState,
                        onCheckedChange = {
                            checkState = !checkState
                            onChangePhotoDoneStatus(photo)
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
        }
    }
}