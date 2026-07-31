package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = UnionRedPrimary,
    onPrimary = Color.White,
    primaryContainer = UnionRedDark,
    onPrimaryContainer = Color.White,
    secondary = UnionGoldAccent,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF5C4000),
    onSecondaryContainer = UnionGoldBright,
    background = UnionDarkBackground,
    onBackground = UnionTextPrimaryDark,
    surface = UnionDarkSurface,
    onSurface = UnionTextPrimaryDark,
    surfaceVariant = UnionDarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCCCCCC)
)

private val LightColorScheme = lightColorScheme(
    primary = UnionRedPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = UnionRedDark,
    secondary = UnionGoldAccent,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFF0C2),
    onSecondaryContainer = Color(0xFF3E2C00),
    background = UnionLightBackground,
    onBackground = UnionTextPrimaryLight,
    surface = UnionLightSurface,
    onSurface = UnionTextPrimaryLight,
    surfaceVariant = UnionLightSurfaceVariant,
    onSurfaceVariant = Color(0xFF4A4540)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
