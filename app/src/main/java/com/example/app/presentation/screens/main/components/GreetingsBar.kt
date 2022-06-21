package com.example.app.presentation.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.app.core.advancedShadow
import com.example.app.ui.theme.DarkGray
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.Typography

@Composable
fun GreetingsBar(
    greetings: String,
    user: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .advancedShadow(
                color = DarkGray,
                alpha = 0.1f,
                offsetY = 5.dp,
                shadowBlurRadius = 10.dp,
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightBackground),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 5.dp),
                text = greetings + user,
                style = Typography.h1.copy(color = Color.White)
            )
        }
    }
}