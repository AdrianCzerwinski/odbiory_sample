package com.example.app.presentation.screens.defect.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.ui.theme.InfoGreen
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.Typography

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit,
    onStorageClick: () -> Unit,
    onCameraClick: () -> Unit,
    saveEnabled: Boolean,
) {
    var expandedState by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.wrapContentHeight()
        ) {
        Divider(
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 12.dp),
            color = LightBackground
        )
        if (expandedState) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp)
                ,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = "camera",
                    tint = LightBackground,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {
                            onCameraClick()
                        }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_storage),
                    contentDescription = "storage",
                    tint = LightBackground,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {
                            onStorageClick()
                        }
                )

            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(
                modifier = Modifier.padding(6.dp),
                verticalArrangement = Arrangement.Bottom
            ) {

                IconButton(
                    modifier = Modifier.padding(6.dp),
                    onClick = { expandedState = !expandedState }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add",
                            tint = LightBackground
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.add_photo),
                            style = Typography.body1,
                            color = LightBackground
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    modifier = Modifier.padding(6.dp),
                    onClick = { onSaveClick() },
                    enabled = saveEnabled
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_save),
                            contentDescription = "save",
                            tint =
                            if (saveEnabled) InfoGreen else LightBackground.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.save),
                            style = Typography.body1,
                            color =
                            if (saveEnabled) InfoGreen else LightBackground.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }

}

//@Preview
//@Composable
//fun BottomBarPreview() {
//    BottomBar(onSaveClick = { /*TODO*/ }, onStorageClick = { /*TODO*/ }) {
//
//    }
//}