package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = CyberCyan,             // D0BCFF (lavender accent)
    onPrimary = Color(0xFF21005D),    // Deep violet text on active buttons/nav
    primaryContainer = CyberCyanDim, // Deep violet background container
    onPrimaryContainer = OffWhite,
    secondary = ConsoleGreen,        // B2F2BB (mint green status)
    onSecondary = CyberBlack,
    background = CyberBlack,          // 1C1B1F (charcoal background)
    onBackground = Silver,           // E6E1E5 (grey text)
    surface = CyberGray,             // 332D41 (purple-grey cards)
    onSurface = OffWhite,
    surfaceVariant = CyberSlate,     // 49454F (medium grey accents)
    onSurfaceVariant = DarkSilver,   // CAC4D0
    error = CyberCrimson,            // F2B8B5
    onError = Color(0xFF601410),
    errorContainer = CyberCrimsonDim, // 8C1D18
    onErrorContainer = CyberCrimson
  )

private val LightColorScheme = DarkColorScheme // Keep consistent styling in both modes


@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Force custom theme colors
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
