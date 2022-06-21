package com.example.app.presentation.components

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.app.R
import com.example.app.presentation.navigation.Screen
import com.example.app.presentation.screens.splash.SplashViewModel
import kotlinx.coroutines.delay
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel()
) {


    var startAnimation by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500)
        if(viewModel.checkUserState()){
            navController.popBackStack()
            navController.navigate(Screen.Main.route)
        } else{
            navController.popBackStack()
            navController.navigate(Screen.Login.route)
        }
    }

    if (startAnimation) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Pulsating {
                Surface(
                    color = Color.White,
                    shape = RectangleShape,
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        Image(
                            modifier = Modifier.size(100.dp),
                            painter = painterResource(id = R.drawable.ic_logo_helmet),
                            contentDescription = ""
                        )
                    }
                )
            }
        }

    }
}

@Composable
fun Pulsating(pulseFraction: Float = 1f, content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = pulseFraction,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.scale(scale)) {
        content()
    }
}



