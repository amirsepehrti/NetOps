package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class NetOpsTheme {
    SKEUOMORPHIC_CONSOLE,
    SKEUOMORPHIC_ALUMINUM,
    NORD_SLATE,
    MATRIX_GREEN,
    CYBERPUNK_NEO,
    OCEAN_BLUE,
    SOLARIZED_DARK,
    DRACULA,
    MONOKAI_PRO,
    RETRO_GOLD,
    OBSIDIAN_STEALTH,
    CLASSIC_LIGHT
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
    var currentTheme: NetOpsTheme = NetOpsTheme.SKEUOMORPHIC_CONSOLE

    val colors: ThemeColorPalette
        get() = getPalette(currentTheme)

    fun getPalette(theme: NetOpsTheme): ThemeColorPalette {
        return when (theme) {
            NetOpsTheme.SKEUOMORPHIC_CONSOLE -> ThemeColorPalette(
                background = Color(0xFF13171C),       // Heavy equipment matte chassis
                backgroundDark = Color(0xFF0B0E11),   // Recessed instrument bay
                surface = Color(0xFF1C222A),          // Brushed rack faceplate
                border = Color(0xFF333E4C),           // Machined chamfered bezel
                secondary = Color(0xFF22C55E),        // Phosphor green LED indicator
                secondaryLight = Color(0xFFDCFCE7),   // Bright phosphor glow
                secondaryDim = Color(0xFF14381C),     // Deep LED diode housing
                primary = Color(0xFF38BDF8),          // Analog telemetry gauge cyan
                primaryDim = Color(0xFF0C2B3B),       // Gauge shadow bezel
                error = Color(0xFFEF4444),            // Warning beacon red
                errorDim = Color(0xFF3C1212),         // Dark red diode housing
                textPrimary = Color(0xFFF8FAFC),      // High-contrast engraved lettering
                textSecondary = Color(0xFFCBD5E1),    // Secondary dial legend
                textMuted = Color(0xFF64748B)         // Recessed chassis markings
            )
            NetOpsTheme.SKEUOMORPHIC_ALUMINUM -> ThemeColorPalette(
                background = Color(0xFF24272D),       // Dark anodized aluminum body
                backgroundDark = Color(0xFF1B1D22),   // Cast metal base
                surface = Color(0xFF2F343E),          // Brushed silver alloy panel
                border = Color(0xFF4A5260),           // Polished metal edge
                secondary = Color(0xFFF59E0B),        // Industrial amber incandescent lamp
                secondaryLight = Color(0xFFFEF3C7),   // Amber bulb reflection
                secondaryDim = Color(0xFF452B05),     // Bulb cavity
                primary = Color(0xFF60A5FA),          // Voltage meter blue
                primaryDim = Color(0xFF1E3A8A),       // Meter shroud
                error = Color(0xFFF87171),            // Hazard blinker
                errorDim = Color(0xFF450A0A),         // Hazard housing
                textPrimary = Color(0xFFF1F5F9),      // Stamped metal label
                textSecondary = Color(0xFF94A3B8),    // Engraved serial print
                textMuted = Color(0xFF64748B)         // Screw and rivet tone
            )
            NetOpsTheme.OBSIDIAN_STEALTH -> ThemeColorPalette(
                background = Color(0xFF0F1115),       // Tactical stealth matte
                backgroundDark = Color(0xFF08090C),   // Pure shadow
                surface = Color(0xFF181B20),          // Textured composite panel
                border = Color(0xFF2A303A),           // Dark steel trim
                secondary = Color(0xFFFB923C),        // Tactical amber warning
                secondaryLight = Color(0xFFFFEDD5),   // Highlight amber
                secondaryDim = Color(0xFF431407),     // Deep ember
                primary = Color(0xFF2DD4BF),          // Night vision teal
                primaryDim = Color(0xFF042F2E),       // Sensor night filter
                error = Color(0xFFF43F5E),            // Critical lockout
                errorDim = Color(0xFF4C0519),         // Lockout housing
                textPrimary = Color(0xFFFAFAFA),      // White phosphor
                textSecondary = Color(0xFFA1A1AA),    // Sub-panel markings
                textMuted = Color(0xFF52525B)         // Chassis etchings
            )
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
            NetOpsTheme.SOLARIZED_DARK -> ThemeColorPalette(
                background = Color(0xFF002B36),
                backgroundDark = Color(0xFF073642),
                surface = Color(0xFF073642),
                border = Color(0xFF586E75),
                secondary = Color(0xFFB58900),
                secondaryLight = Color(0xFFFDF6E3),
                secondaryDim = Color(0xFF3D2E00),
                primary = Color(0xFF2AA198),
                primaryDim = Color(0xFF002222),
                error = Color(0xFFDC322F),
                errorDim = Color(0xFF441110),
                textPrimary = Color(0xFF93A1A1),
                textSecondary = Color(0xFF839496),
                textMuted = Color(0xFF586E75)
            )
            NetOpsTheme.DRACULA -> ThemeColorPalette(
                background = Color(0xFF282A36),
                backgroundDark = Color(0xFF21222C),
                surface = Color(0xFF343746),
                border = Color(0xFF44475A),
                secondary = Color(0xFFFF79C6),
                secondaryLight = Color(0xFFF8F8F2),
                secondaryDim = Color(0xFF4A1035),
                primary = Color(0xFF50FA7B),
                primaryDim = Color(0xFF0F3A18),
                error = Color(0xFFFF5555),
                errorDim = Color(0xFF4A1010),
                textPrimary = Color(0xFFF8F8F2),
                textSecondary = Color(0xFFBD93F9),
                textMuted = Color(0xFF6272A4)
            )
            NetOpsTheme.MONOKAI_PRO -> ThemeColorPalette(
                background = Color(0xFF2D2A2E),
                backgroundDark = Color(0xFF221F22),
                surface = Color(0xFF3A363B),
                border = Color(0xFF49454C),
                secondary = Color(0xFFFFD866),
                secondaryLight = Color(0xFFFCFCFA),
                secondaryDim = Color(0xFF4A3C10),
                primary = Color(0xFFFC5C7D),
                primaryDim = Color(0xFF4A1020),
                error = Color(0xFFFF6188),
                errorDim = Color(0xFF4A1020),
                textPrimary = Color(0xFFFCFCFA),
                textSecondary = Color(0xFFA9DC76),
                textMuted = Color(0xFF726E75)
            )
            NetOpsTheme.RETRO_GOLD -> ThemeColorPalette(
                background = Color(0xFF1E1E1E),
                backgroundDark = Color(0xFF141414),
                surface = Color(0xFF292929),
                border = Color(0xFF3C3C3C),
                secondary = Color(0xFFFFB300),
                secondaryLight = Color(0xFFFFF8E1),
                secondaryDim = Color(0xFF4A3200),
                primary = Color(0xFFD4AF37),
                primaryDim = Color(0xFF3D2E0A),
                error = Color(0xFFCF6679),
                errorDim = Color(0xFF4A101D),
                textPrimary = Color(0xFFE0E0E0),
                textSecondary = Color(0xFFB0BEC5),
                textMuted = Color(0xFF78909C)
            )
            NetOpsTheme.CLASSIC_LIGHT -> ThemeColorPalette(
                background = Color(0xFFF5F7FA),
                backgroundDark = Color(0xFFECEFF1),
                surface = Color(0xFFFFFFFF),
                border = Color(0xFFCFD8DC),
                secondary = Color(0xFF2E7D32),
                secondaryLight = Color(0xFF1B5E20),
                secondaryDim = Color(0xFFC8E6C9),
                primary = Color(0xFF1565C0),
                primaryDim = Color(0xFFBBDEFB),
                error = Color(0xFFD32F2F),
                errorDim = Color(0xFFFFCDD2),
                textPrimary = Color(0xFF1E293B),
                textSecondary = Color(0xFF475569),
                textMuted = Color(0xFF94A3B8)
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

val Amber: Color = Color(0xFFFFB300)
val AmberDim: Color = Color(0xFF5A3E00)

// =========================================================================
// --- SKEUOMORPHIC HARDWARE GROOVES & TACTILE SECTION DIVIDERS ---
// =========================================================================

/**
 * Creates a physical engraved chassis groove (dark inset shadow + light highlight edge).
 * Eliminates visual collision between adjacent widgets and texts.
 */
@Composable
fun TactileDivider(
    modifier: Modifier = Modifier,
    label: String? = null,
    accentColor: Color? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        if (label != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Left engraved line
                Box(
                    modifier = Modifier
                        .weight(0.12f)
                        .height(2.dp)
                        .background(CyberSlate.copy(alpha = 0.5f))
                )

                // Tactile physical badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyberDark)
                        .border(
                            1.dp,
                            accentColor ?: CyberSlate,
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = label.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = accentColor ?: ConsoleGreen,
                        letterSpacing = 1.2.sp
                    )
                }

                // Right engraved line
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(CyberSlate.copy(alpha = 0.5f))
                )
            }
        } else {
            // Recessed groove: 1dp dark channel + 1dp rim highlight
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFF0A0C0E))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(CyberSlate.copy(alpha = 0.4f))
            )
        }
    }
}

