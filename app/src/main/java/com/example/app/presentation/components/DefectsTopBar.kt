package com.example.app.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.domain.model.CheckType
import com.example.app.ui.theme.LightBlue

@Composable
fun DefectsTopBar(
    selectedItem: CheckType?,
    onClick: (CheckType) -> Unit
) {
    val options = listOf(CheckType.CLIENT, CheckType.INVESTOR, CheckType.PARTIAL)
    val position1 = CheckType.CLIENT
    val position2 = CheckType.INVESTOR
    val position3 = CheckType.PARTIAL

    var horizontalBias by remember { mutableStateOf(0.68f) }
    val alignment by animateHorizontalAlignmentAsState(horizontalBias)

    LaunchedEffect(key1 = selectedItem){
        when (selectedItem) {
            position1 -> { horizontalBias = -0.68f}
            position2 -> { horizontalBias = 0f}
            position3 -> { horizontalBias = 0.68f}
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround) {
            options.forEach{ type ->
                Column() {
                    SelectableButton(
                        selected = selectedItem == type,
                        title = type.type ,
                        onClick = {
                            onClick(type)
                        })
                }
            }

        }
        Row() {
            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 4.dp),
                horizontalAlignment = alignment
            ){

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(shape = CircleShape)
                        .background(color = LightBlue),
                    )

            }
        }
    }
}

@Composable
private fun animateHorizontalAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment.Horizontal> {
    val bias by animateFloatAsState(
       targetValue = targetBiasValue,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    return derivedStateOf { BiasAlignment.Horizontal(bias) }
}

@Preview
@Composable
fun DefectsTopBarPreview() {
    DefectsTopBar(selectedItem = CheckType.INVESTOR, onClick = { })
}

