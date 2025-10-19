package com.example.pitstop.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores personalizados
private val RedPrimary = Color(0xFFD32F2F)
private val RedDark = Color(0xFFB71C1C)
private val GrayLight = Color(0xFFF5F5F5)
private val GrayDark = Color(0xFF212121)
private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF000000)
private val GreenOk = Color(0xFF4CAF50)

// Paleta de colores para modo claro
private val LightColors = lightColorScheme(
    primary = RedPrimary,
    onPrimary = White,
    secondary = RedDark,
    onSecondary = White,
    background = GrayLight,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    error = Color(0xFFD50000),
)

// Paleta de colores para modo oscuro
private val DarkColors = darkColorScheme(
    primary = RedDark,
    onPrimary = White,
    secondary = RedPrimary,
    onSecondary = White,
    background = GrayDark,
    onBackground = White,
    surface = GrayDark,
    onSurface = White,
    error = Color(0xFFFF5252),
)

// Tema principal de la app
@Composable
fun PitStopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
