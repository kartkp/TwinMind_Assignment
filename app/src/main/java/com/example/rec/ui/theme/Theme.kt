package com.example.rec.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = Night900,
    secondary = NeonPurple,
    onSecondary = Night900,
    tertiary = Aqua,
    background = Night950,
    onBackground = TextPrimary,
    surface = Night900,
    onSurface = TextPrimary,
    surfaceVariant = Night800,
    onSurfaceVariant = TextSecondary,
    outline = Night700,
    error = DangerRed
)

@Composable
fun RecTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = if (darkTheme) colorScheme else colorScheme,
        typography = Typography,
        content = content
    )
}