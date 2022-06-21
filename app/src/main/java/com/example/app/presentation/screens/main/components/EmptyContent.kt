package com.example.app.presentation.screens.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.Typography

@Composable
fun EmptyContent(
    onNewProjectClick: () -> Unit,
    value: String = "",
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    bottomSheetHide: () -> Unit,
    bottomSheetShow: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var lazyColumnWeight by remember {
        mutableStateOf(0.8f)
    }
    var newProjectWeight by remember {
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
            Column(
                modifier = Modifier
                    .weight(lazyColumnWeight)
                    .alpha(0.6f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
            ) {

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.no_project),
                    style = Typography.subtitle1.copy(color = LightBackground)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    Modifier.size(50.dp)
                )
            }

            Column(modifier = Modifier.weight(newProjectWeight)) {
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
                        lazyColumnWeight = 0.5f
                        newProjectWeight = 0.5f
                    },
                    bottomSheetShow = {
                        bottomSheetShow()
                        lazyColumnWeight = 0.8f
                        newProjectWeight = 0.2f
                    }
                )
            }


        }
    }

}