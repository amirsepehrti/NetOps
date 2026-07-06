package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  // Build colorScheme dynamically at runtime since our color constants have @Composable getters!
  val colorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = Color(0xFF15171D),
    primaryContainer = CyberCyanDim,
    onPrimaryContainer = OffWhite,
    secondary = ConsoleGreen,
    onSecondary = CyberBlack,
    background = CyberBlack,
    onBackground = Silver,
    surface = CyberGray,
    onSurface = OffWhite,
    surfaceVariant = CyberSlate,
    onSurfaceVariant = DarkSilver,
    error = CyberCrimson,
    onError = Color(0xFF601410),
    errorContainer = CyberCrimsonDim,
    onErrorContainer = CyberCrimson
  )

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
