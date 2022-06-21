package com.example.app.presentation.screens.login_register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.ui.theme.LightBackground
import com.example.app.ui.theme.Shapes
import com.example.app.ui.theme.Typography

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.medium,
    text: String = stringResource(R.string.login_button),
    isVerified: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(enabled = isVerified) { onClick() },
        shape = shape,
        color = LightBackground
    ) {
        Row(modifier = Modifier
            .padding(horizontal = 40.dp, vertical = 8.dp)
            ) {
                        Text(text = text, style = Typography.button, color = Color.White )
        }
        }

}

@Preview
@Composable
fun loginButtonprev(
) {
    LoginButton(isVerified = true) {
        
    }
    
}