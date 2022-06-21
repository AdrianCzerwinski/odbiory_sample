package com.example.app.presentation.screens.login_register.components

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    startAnimation: Boolean
) {
    AnimatedVisibility(
        visible = startAnimation,
        enter = slideInVertically(animationSpec = tween(durationMillis = 1000)) { fullHeight ->
            fullHeight / 1
        } + fadeIn(
            animationSpec = tween(durationMillis = 200)
        ),
        exit = slideOutVertically(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
            100
        } + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 30.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topEnd = 36.dp,
                        topStart = 36.dp
                    )
                ).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()


            }
        }
    }
}

@Preview
@Composable
fun DrawerPrev() {

}