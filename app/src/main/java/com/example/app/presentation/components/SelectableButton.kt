package com.example.app.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.Typography

@Composable
fun SelectableButton(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    minWidth: Dp = 110.dp,
    title: String,
    titleColor: Color =
        if (selected) Color.White else LightBackground,
    backgroundColor: Color = if (selected) LightBackground else Color.White,
    titleStyle: TextStyle = Typography.subtitle2,
    borderWidth: Dp = 1.dp,
    borderColor: Color = LightBackground,
    onClick: () -> Unit,
    borderShape: Shape = RoundedCornerShape(8.dp),
    icon: Boolean = false
) {
    val scale = remember { Animatable(initialValue = 1f) }

    LaunchedEffect(key1 = selected) {
        if (selected) {
            scale.animateTo(
                targetValue = 0.9f,
                animationSpec = tween(durationMillis = 50)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    Column(
        modifier = modifier
            .scale(scale = scale.value)
            .defaultMinSize(minWidth = minWidth)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = borderShape
            )
            .clip(borderShape)
            .shadow(elevation = 5.dp, shape = borderShape, clip = true)
            .background(color = backgroundColor)
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add",
                    tint = LightBackground
                )
            }
            Text(
                text = title,
                style = titleStyle,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
fun PreviewButton() {
    SelectableButton(
        title = "KLIENT",
        onClick = { })
}