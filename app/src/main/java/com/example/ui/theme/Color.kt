package com.example.ui.theme

import androidx.compose.ui.graphics.Color

enum class NetOpsTheme {
    NORD_SLATE,
    MATRIX_GREEN,
    CYBERPUNK_NEO,
    OCEAN_BLUE
}

data class ThemeColorPalette(
    val background: Color,
    val backgroundDark: Color,
    val surface: Color,
    val border: Color,
    val secondary: Color,
    val secondaryLight: Color,
    val secondaryDim: Color,
    val primary: Color,
    val primaryDim: Color,
    val error: Color,
    val errorDim: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color
)

object ThemeManager {
    var currentTheme: NetOpsTheme = NetOpsTheme.NORD_SLATE

    val colors: ThemeColorPalette
        get() = getPalette(currentTheme)

    fun getPalette(theme: NetOpsTheme): ThemeColorPalette {
        return when (theme) {
            NetOpsTheme.NORD_SLATE -> ThemeColorPalette(
                background = Color(0xFF1B1E24),
                backgroundDark = Color(0xFF15171D),
                surface = Color(0xFF252932),
                border = Color(0xFF2E3440),
                secondary = Color(0xFFA3BE8C),
                secondaryLight = Color(0xFFECEFF4),
                secondaryDim = Color(0xFF3D4C38),
                primary = Color(0xFF88C0D0),
                primaryDim = Color(0xFF283A41),
                error = Color(0xFFD08770),
                errorDim = Color(0xFF4C3E3B),
                textPrimary = Color(0xFFECEFF4),
                textSecondary = Color(0xFFD8DEE9),
                textMuted = Color(0xFF969FAA)
            )
            NetOpsTheme.MATRIX_GREEN -> ThemeColorPalette(
                background = Color(0xFF030A04),
                backgroundDark = Color(0xFF000501),
                surface = Color(0xFF0A1F0D),
                border = Color(0xFF143D1A),
                secondary = Color(0xFF39FF14),
                secondaryLight = Color(0xFFD4FFD4),
                secondaryDim = Color(0xFF0B240E),
                primary = Color(0xFF00FF41),
                primaryDim = Color(0xFF021B05),
                error = Color(0xFFFF3333),
                errorDim = Color(0xFF330000),
                textPrimary = Color(0xFF39FF14),
                textSecondary = Color(0xFF8CFF8C),
                textMuted = Color(0xFF005F12)
            )
            NetOpsTheme.CYBERPUNK_NEO -> ThemeColorPalette(
                background = Color(0xFF180A2B),
                backgroundDark = Color(0xFF0F051D),
                surface = Color(0xFF2B144E),
                border = Color(0xFF3F1B73),
                secondary = Color(0xFFFF007F),
                secondaryLight = Color(0xFFFFD1E8),
                secondaryDim = Color(0xFF3D001B),
                primary = Color(0xFF00FFFF),
                primaryDim = Color(0xFF00383D),
                error = Color(0xFFFF9F00),
                errorDim = Color(0xFF3D2600),
                textPrimary = Color(0xFFFFFFFF),
                textSecondary = Color(0xFFE8D5FF),
                textMuted = Color(0xFF9F83C5)
            )
            NetOpsTheme.OCEAN_BLUE -> ThemeColorPalette(
                background = Color(0xFF0B132B),
                backgroundDark = Color(0xFF070A1E),
                surface = Color(0xFF1C2541),
                border = Color(0xFF3A506B),
                secondary = Color(0xFF48CAE4),
                secondaryLight = Color(0xFFCAF0F8),
                secondaryDim = Color(0xFF004F6E),
                primary = Color(0xFF0077B6),
                primaryDim = Color(0xFF03045E),
                error = Color(0xFFFF7096),
                errorDim = Color(0xFF4A1E2B),
                textPrimary = Color(0xFFFFFFFF),
                textSecondary = Color(0xFFE0F1F7),
                textMuted = Color(0xFF5C7E9D)
            )
        }
    }
}

// Top-level non-composable properties that dynamically query the current ThemeManager active palette.
val CyberBlack: Color
    get() = ThemeManager.colors.background

val CyberDark: Color
    get() = ThemeManager.colors.backgroundDark

val CyberGray: Color
    get() = ThemeManager.colors.surface

val CyberSlate: Color
    get() = ThemeManager.colors.border

val ConsoleGreen: Color
    get() = ThemeManager.colors.secondary

val ConsoleGreenLight: Color
    get() = ThemeManager.colors.secondaryLight

val ConsoleGreenDim: Color
    get() = ThemeManager.colors.secondaryDim

val CyberCyan: Color
    get() = ThemeManager.colors.primary

val CyberCyanDim: Color
    get() = ThemeManager.colors.primaryDim

val CyberCrimson: Color
    get() = ThemeManager.colors.error

val CyberCrimsonDim: Color
    get() = ThemeManager.colors.errorDim

val OffWhite: Color
    get() = ThemeManager.colors.textPrimary

val Silver: Color
    get() = ThemeManager.colors.textSecondary

val DarkSilver: Color
    get() = ThemeManager.colors.textMuted
