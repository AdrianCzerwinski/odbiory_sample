package com.example.app.presentation.screens.defect.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun ZoomPhoto(
    uri: Uri,
    onBackClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(color= Color.LightGray.copy(alpha = 0.6f))) {
        Column(modifier = Modifier.weight(0.9f)) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uri)
                    .build()
            )
            Image(painter = painter, contentDescription = "photo", modifier = Modifier.fillMaxSize())
        }
        Column(
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth()
                .background(color = Color.Gray.copy(alpha = 0.6f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { onBackClicked() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back",
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxSize(),
                    tint = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
    
}