package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NetOpsViewModel
import com.example.ui.ToastType
import com.example.ui.theme.NetOpsTheme
import com.example.ui.theme.*
import com.example.ui.dialogs.OperatorProfileDialog
import com.example.ui.dialogs.BackupRestoreDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: NetOpsViewModel,
    modifier: Modifier = Modifier
) {
    val activeTheme by viewModel.activeTheme.collectAsState()
    val fontScale by viewModel.fontScale.collectAsState()
    val fontFamilyOption by viewModel.fontFamilyOption.collectAsState()
    val hasPermissions by viewModel.hasPermissions.collectAsState()

    val backgroundMonitoringEnabled by viewModel.backgroundMonitoringEnabled.collectAsState()
    val navBarPosition by viewModel.navBarPosition.collectAsState()
    val bottomBarStyle by viewModel.bottomBarStyle.collectAsState()
    val bottomBarLabelVisibility by viewModel.bottomBarLabelVisibility.collectAsState()
    val bottomBarDensity by viewModel.bottomBarDensity.collectAsState()
    val homeLayoutStyle by viewModel.homeLayoutStyle.collectAsState()
    val homeGreetingText by viewModel.homeGreetingText.collectAsState()

    val operatorName by viewModel.operatorName.collectAsState()
    val operatorCallsign by viewModel.operatorCallsign.collectAsState()
    val operatorRole by viewModel.operatorRole.collectAsState()
    val operatorClearance by viewModel.operatorClearance.collectAsState()
    val operatorUnit by viewModel.operatorUnit.collectAsState()

    var showProfileDialog by remember { mutableStateOf(false) }
    var showBackupDialog by remember { mutableStateOf(false) }
    
    // Widget visibility states
    val showMatrixHeader by viewModel.showMatrixHeader.collectAsState()
    val showDiagnosticStream by viewModel.showDiagnosticStream.collectAsState()
    val showQuickStats by viewModel.showQuickStats.collectAsState()
    val showTrafficSnifferWidget by viewModel.showTrafficSnifferWidget.collectAsState()
    val showCellRadarWidget by viewModel.showCellRadarWidget.collectAsState()
    val showDeviceInventory by viewModel.showDeviceInventory.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Banner / Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberSlate, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings Icon",
                        tint = CyberCyan,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.titleMedium,
                            color = ConsoleGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "SYSTEM CONFIGURATION",
                            style = MaterialTheme.typography.labelSmall,
                            color = Silver,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
            }
        }

        // 1. THEME SELECTION CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberGray, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Brush, contentDescription = "Theme", tint = ConsoleGreen, modifier = Modifier.size(20.dp))
                        Text(
                            text = "THEME SELECTOR",
                            style = MaterialTheme.typography.labelMedium,
                            color = ConsoleGreen,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    val themesList = listOf(
                        Triple("Console Rack 🎛️", NetOpsTheme.SKEUOMORPHIC_CONSOLE, Color(0xFF22C55E)),
                        Triple("Aluminum ⚙️", NetOpsTheme.SKEUOMORPHIC_ALUMINUM, Color(0xFFF59E0B)),
                        Triple("Obsidian 🖤", NetOpsTheme.OBSIDIAN_STEALTH, Color(0xFFFB923C)),
                        Triple("Nord Slate ❄️", NetOpsTheme.NORD_SLATE, Color(0xFF88C0D0)),
                        Triple("Matrix Green 🟢", NetOpsTheme.MATRIX_GREEN, Color(0xFF00FF41)),
                        Triple("Cyberpunk Neo ⚡", NetOpsTheme.CYBERPUNK_NEO, Color(0xFFFF007F)),
                        Triple("Ocean Blue 🌊", NetOpsTheme.OCEAN_BLUE, Color(0xFF48CAE4)),
                        Triple("Solarized Dark 🪐", NetOpsTheme.SOLARIZED_DARK, Color(0xFF2AA198)),
                        Triple("Dracula 🧛", NetOpsTheme.DRACULA, Color(0xFF50FA7B)),
                        Triple("Monokai Pro 🎨", NetOpsTheme.MONOKAI_PRO, Color(0xFFFC5C7D)),
                        Triple("Retro Gold 👑", NetOpsTheme.RETRO_GOLD, Color(0xFFD4AF37)),
                        Triple("Classic Light ☀️", NetOpsTheme.CLASSIC_LIGHT, Color(0xFF1565C0))
                    )

                    themesList.chunked(2).forEach { rowThemes ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowThemes.forEach { (label, themeVal, colorVal) ->
                                ThemePillButton(
                                    label = label,
                                    theme = themeVal,
                                    activeTheme = activeTheme,
                                    primaryColor = colorVal,
                                    onClick = { viewModel.setTheme(themeVal) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowThemes.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // 1b. TYPOGRAPHY & FONT SCALING CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCyanDim, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.TextFields, contentDescription = "Font", tint = CyberCyan, modifier = Modifier.size(20.dp))
                        Text(
                            text = "FONT SCALE & TYPOGRAPHY",
                            style = MaterialTheme.typography.labelMedium,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Interface Font Size Scaling", style = MaterialTheme.typography.bodySmall, color = Silver)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            0.85f to "Compact (0.85x)",
                            1.0f to "Standard (1.0x)",
                            1.15f to "Large (1.15x)",
                            1.30f to "Huge (1.30x)"
                        ).forEach { (scale, label) ->
                            val isSel = kotlin.math.abs(fontScale - scale) < 0.05f
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) CyberCyan.copy(alpha = 0.2f) else CyberGray)
                                    .border(1.dp, if (isSel) CyberCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable {
                                        viewModel.setFontScale(scale)
                                        viewModel.showToast("Font scaled to ${(scale * 100).toInt()}%", ToastType.INFO)
                                    }
                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSel) CyberCyan else OffWhite,
                                    textAlign = TextAlign.Center,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Font Family Aesthetic", style = MaterialTheme.typography.bodySmall, color = Silver)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "monospace" to "Monospace (Cyber)",
                            "sans_serif" to "Sans-Serif (Modern)",
                            "serif" to "Serif (Classic)"
                        ).forEach { (fontKey, fontLabel) ->
                            val isSel = fontFamilyOption == fontKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) ConsoleGreen.copy(alpha = 0.2f) else CyberGray)
                                    .border(1.dp, if (isSel) ConsoleGreen else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable {
                                        viewModel.setFontFamilyOption(fontKey)
                                        viewModel.showToast("Font family set to $fontLabel", ToastType.INFO)
                                    }
                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = fontLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSel) ConsoleGreen else OffWhite,
                                    textAlign = TextAlign.Center,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Typography Preview
                    Card(
                        modifier = Modifier.fillMaxWidth().border(1.dp, CyberSlate, RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = CyberBlack)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "LIVE PREVIEW TEXT",
                                fontSize = 10.sp,
                                color = DarkSilver,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "NetOps: 192.168.1.1 [RTT: 12ms] WiFi: 5GHz CH36",
                                color = ConsoleGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 1c. NOTIFICATIONS, TOAST & POPUP FEEDBACK CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberGray, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = "Feedback", tint = ConsoleGreen, modifier = Modifier.size(20.dp))
                        Text(
                            text = "TOAST & POPUP FEEDBACK SYSTEM",
                            style = MaterialTheme.typography.labelMedium,
                            color = ConsoleGreen,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Instant in-app HUD toasts and modal popups for network alerts and confirmations.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Silver
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.showToast("Packets successfully captured & analyzed!", ToastType.SUCCESS) },
                            colors = ButtonDefaults.buttonColors(containerColor = ConsoleGreen.copy(alpha = 0.2f), contentColor = ConsoleGreen),
                            border = BorderStroke(1.dp, ConsoleGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("SUCCESS TOAST", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }

                        Button(
                            onClick = { viewModel.showToast("Wi-Fi frequency congestion detected on CH 6", ToastType.WARNING) },
                            colors = ButtonDefaults.buttonColors(containerColor = Amber.copy(alpha = 0.2f), contentColor = Amber),
                            border = BorderStroke(1.dp, Amber),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("WARN TOAST", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.showToast("Connection to gateway timed out after 3000ms", ToastType.ERROR) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCrimson.copy(alpha = 0.2f), contentColor = CyberCrimson),
                            border = BorderStroke(1.dp, CyberCrimson),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("ERROR TOAST", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }

                        Button(
                            onClick = {
                                viewModel.showPopup(
                                    title = "DIAGNOSTIC NOTICE",
                                    message = "All telemetry sensors and raw hardware sockets are operating natively with zero simulated data.",
                                    type = ToastType.INFO,
                                    confirmText = "ACKNOWLEDGE"
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan.copy(alpha = 0.2f), contentColor = CyberCyan),
                            border = BorderStroke(1.dp, CyberCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("TEST POPUP", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        // 1d. DOCK CUSTOMIZATION CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberGray, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NAVIGATION DOCK & PLACEMENT",
                            style = MaterialTheme.typography.labelMedium,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyberCyan.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(navBarPosition, color = CyberCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Dock Placement Position", style = MaterialTheme.typography.bodySmall, color = Silver)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            "BOTTOM" to "Dock at Bottom ⬇️",
                            "TOP" to "Dock at Top ⬆️"
                        ).forEach { (posKey, posLabel) ->
                            val isSel = navBarPosition == posKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) CyberCyan.copy(alpha = 0.2f) else CyberGray)
                                    .border(1.dp, if (isSel) CyberCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setNavBarPosition(posKey) }
                                    .padding(horizontal = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    posLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSel) CyberCyan else OffWhite,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Dock Visual Style", style = MaterialTheme.typography.bodySmall, color = Silver)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            "match_theme" to "Default",
                            "glassmorphism" to "Glassy Glow",
                            "cyber_border" to "Neon Accent"
                        ).forEach { (styleKey, styleLabel) ->
                            val isSel = bottomBarStyle == styleKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) CyberCyan.copy(alpha = 0.2f) else CyberGray)
                                    .border(1.dp, if (isSel) CyberCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setBottomBarStyle(styleKey) }
                                    .padding(horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(styleLabel, style = MaterialTheme.typography.labelSmall, color = if (isSel) CyberCyan else OffWhite)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Dock Tab Labels", style = MaterialTheme.typography.bodySmall, color = Silver)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            "always" to "Always Show",
                            "selected_only" to "Active Only",
                            "hidden" to "Icons Only"
                        ).forEach { (visKey, visLabel) ->
                            val isSel = bottomBarLabelVisibility == visKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) ConsoleGreen.copy(alpha = 0.2f) else CyberGray)
                                    .border(1.dp, if (isSel) ConsoleGreen else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setBottomBarLabelVisibility(visKey) }
                                    .padding(horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(visLabel, style = MaterialTheme.typography.labelSmall, color = if (isSel) ConsoleGreen else OffWhite)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Dock Size Density", style = MaterialTheme.typography.bodySmall, color = Silver)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            "compact" to "Compact (48dp)",
                            "normal" to "Standard (58dp)",
                            "spacious" to "Comfort (72dp)"
                        ).forEach { (densityKey, densityLabel) ->
                            val isSel = bottomBarDensity == densityKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) CyberCyan.copy(alpha = 0.2f) else CyberGray)
                                    .border(1.dp, if (isSel) CyberCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setBottomBarDensity(densityKey) }
                                    .padding(horizontal = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(densityLabel, style = MaterialTheme.typography.labelSmall, color = if (isSel) CyberCyan else OffWhite, fontSize = 9.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }

        // 1e. OPERATOR PROFILE & CLEARANCE SETTINGS
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ConsoleGreenDim, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Badge, contentDescription = "Badge", tint = ConsoleGreen, modifier = Modifier.size(20.dp))
                            Text(
                                text = "OPERATOR PROFILE",
                                style = MaterialTheme.typography.labelMedium,
                                color = ConsoleGreen,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Button(
                            onClick = { showProfileDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ConsoleGreenDim, contentColor = ConsoleGreen),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("EDIT PROFILE", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberBlack)
                                .border(1.dp, ConsoleGreen, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = ConsoleGreen, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "$operatorName ($operatorCallsign)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OffWhite,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "$operatorRole • $operatorUnit",
                                style = MaterialTheme.typography.bodySmall,
                                color = Silver,
                                maxLines = 1
                            )
                            Text(
                                text = "Clearance: $operatorClearance",
                                style = MaterialTheme.typography.labelSmall,
                                color = ConsoleGreen,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        // 1f. BACKUP, RESTORE & SYSTEM MIGRATION
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCyanDim, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Backup, contentDescription = "Backup", tint = CyberCyan, modifier = Modifier.size(20.dp))
                            Text(
                                text = "BACKUP & RESTORE",
                                style = MaterialTheme.typography.labelMedium,
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Button(
                            onClick = { showBackupDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyanDim, contentColor = CyberCyan),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("OPEN MANAGER", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Export and restore your customized dashboard widgets, theme, font scaling, navigation dock position, and operator identity profiles via JSON.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Silver
                    )
                }
            }
        }

        // 1c. HOME CUSTOMIZATION CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberGray, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "HOME SCREEN DECORATOR",
                        style = MaterialTheme.typography.labelMedium,
                        color = ConsoleGreen,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Header Banner Text", style = MaterialTheme.typography.bodySmall, color = Silver)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = homeGreetingText,
                        onValueChange = { viewModel.setHomeGreetingText(it) },
                        textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ConsoleGreen,
                            unfocusedBorderColor = CyberGray
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Grid Template Layout", style = MaterialTheme.typography.bodySmall, color = Silver)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            "single_column" to "Single List",
                            "two_column" to "Grid Cards",
                            "compact_focused" to "Focused HUD"
                        ).forEach { (layoutKey, layoutLabel) ->
                            val isSel = homeLayoutStyle == layoutKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) ConsoleGreen.copy(alpha = 0.2f) else CyberGray)
                                    .border(1.dp, if (isSel) ConsoleGreen else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setHomeLayoutStyle(layoutKey) }
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(layoutLabel, style = MaterialTheme.typography.labelSmall, color = if (isSel) ConsoleGreen else OffWhite)
                            }
                        }
                    }
                }
            }
        }

        // 2. BACKGROUND TELEMETRY CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberGray, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Background Telemetry",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OffWhite,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Background Telemetry Tasks",
                                style = MaterialTheme.typography.labelSmall,
                                color = Silver,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Switch(
                            checked = backgroundMonitoringEnabled,
                            onCheckedChange = { viewModel.setBackgroundMonitoring(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ConsoleGreen,
                                checkedTrackColor = ConsoleGreenDim
                            ),
                            modifier = Modifier.testTag("bg_telemetry_switch")
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Simulates ongoing background tasks like router diagnostics and latency analysis.",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkSilver
                    )
                }
            }
        }

        // 3. HOME SCREEN WIDGETS CONFIGURATION (CRITICAL TO SOLVE USER LAG)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberSlate, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Widgets", tint = CyberCyan, modifier = Modifier.size(20.dp))
                        Text(
                            text = "Home Screen Widgets",
                            style = MaterialTheme.typography.titleSmall,
                            color = ConsoleGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "CUSTOMIZE HOME SCREEN WIDGETS",
                        style = MaterialTheme.typography.labelSmall,
                        color = Silver,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "To resolve performance issues or lag on older devices, disable unnecessary or heavy widgets (such as the Matrix animation).",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyberCrimson,
                        fontWeight = FontWeight.SemiBold
                    )

                    Divider(color = CyberGray, modifier = Modifier.padding(vertical = 12.dp))

                    // 1. Matrix Header Toggle (Heavy!)
                    SettingsToggleRow(
                        title = "Matrix Rain animation",
                        description = "Matrix Rain Code Drop (CPU Heavy)",
                        checked = showMatrixHeader,
                        onCheckedChange = { viewModel.toggleWidget("matrix_header") },
                        testTag = "toggle_matrix_header"
                    )

                    Divider(color = CyberGray, modifier = Modifier.padding(vertical = 8.dp))

                    // 2. Diagnostic Stream Toggle
                    SettingsToggleRow(
                        title = "Local network diagnostics stream",
                        description = "Local Network Interface Diagnostics",
                        checked = showDiagnosticStream,
                        onCheckedChange = { viewModel.toggleWidget("diagnostic_stream") },
                        testTag = "toggle_diagnostic_stream"
                    )

                    Divider(color = CyberGray, modifier = Modifier.padding(vertical = 8.dp))

                    // 3. Telemetry Shortcuts Toggle
                    SettingsToggleRow(
                        title = "Ping and terminal shortcuts",
                        description = "Telemetry Utility Shortcuts",
                        checked = showQuickStats,
                        onCheckedChange = { viewModel.toggleWidget("quick_stats") },
                        testTag = "toggle_quick_stats"
                    )

                    Divider(color = CyberGray, modifier = Modifier.padding(vertical = 8.dp))

                    // 4. Traffic Sniffer Toggle
                    SettingsToggleRow(
                        title = "Live traffic sniffer widget",
                        description = "Live Traffic Sniffer Panel",
                        checked = showTrafficSnifferWidget,
                        onCheckedChange = { viewModel.toggleWidget("sniffer") },
                        testTag = "toggle_sniffer_widget"
                    )

                    Divider(color = CyberGray, modifier = Modifier.padding(vertical = 8.dp))

                    // 5. Cell Radar Toggle
                    SettingsToggleRow(
                        title = "Radio antenna cell radar",
                        description = "Cellular Antenna Radar Plot",
                        checked = showCellRadarWidget,
                        onCheckedChange = { viewModel.toggleWidget("cell_radar") },
                        testTag = "toggle_cell_radar"
                    )

                    Divider(color = CyberGray, modifier = Modifier.padding(vertical = 8.dp))

                    // 6. Device Inventory Toggle
                    SettingsToggleRow(
                        title = "Detected device inventory list",
                        description = "Detected Network Device Inventory",
                        checked = showDeviceInventory,
                        onCheckedChange = { viewModel.toggleWidget("device_inventory") },
                        testTag = "toggle_device_inventory"
                    )
                }
            }
        }

        // About & License section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberGray, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = DarkSilver, modifier = Modifier.size(18.dp))
                    Column {
                        Text(
                            text = "NetOps Mobile v1.1",
                            color = Silver,
                            style = MaterialTheme.typography.labelMedium,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Performance optimization and UI customization settings.",
                            color = DarkSilver,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }

    if (showProfileDialog) {
        OperatorProfileDialog(
            viewModel = viewModel,
            onDismiss = { showProfileDialog = false }
        )
    }

    if (showBackupDialog) {
        BackupRestoreDialog(
            viewModel = viewModel,
            onDismiss = { showBackupDialog = false }
        )
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (checked) OffWhite else Silver,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = DarkSilver,
                fontFamily = FontFamily.Monospace
            )
        }
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange() },
            colors = CheckboxDefaults.colors(
                checkedColor = ConsoleGreen,
                uncheckedColor = Silver
            ),
            modifier = Modifier
                .scale(0.95f)
                .testTag(testTag)
        )
    }
}
