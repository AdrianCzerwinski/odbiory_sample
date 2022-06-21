package com.example.app.presentation.screens.contractors.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.Typography

@Composable
fun TopSubScreenBar(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onHomeClicked: () -> Unit,
    iconTint: Color = Color.White,
    textColor: Color = Color.White,
    iconSize: Dp = 40.dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.Center) {
            IconButton(onClick = { onBackClicked() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "BackButton",
                    tint = iconTint
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.clickable {
                    onHomeClicked()
                }
            ) {
                Text(
                    text = stringResource(R.string.home),
                    style = Typography.subtitle2,
                    color = textColor,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Image(
                    modifier = Modifier
                        .size(iconSize)
                        .padding(vertical = 8.dp),
                    painter = painterResource(id = R.drawable.ic_logo_helmet),
                    contentDescription = null
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun BarPrev() {
//    TopSubScreenBar(onBackClicked = { /*TODO*/ }) {
//
//    }
//}