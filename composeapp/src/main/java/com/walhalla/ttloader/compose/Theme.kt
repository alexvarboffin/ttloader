package com.walhalla.ttloader.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF5B2C6F),
    onPrimary = Color.White,
    secondary = Color(0xFFE91E63),
    tertiary = Color(0xFF00897B),
    background = Color(0xFFF7F4FB),
    surface = Color.White,
    error = Color(0xFFB00020)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFCE93D8),
    secondary = Color(0xFFF48FB1),
    tertiary = Color(0xFF80CBC4)
)

@Composable
fun TtTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content
    )
}
