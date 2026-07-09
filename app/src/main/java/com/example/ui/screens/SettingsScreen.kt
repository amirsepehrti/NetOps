package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
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
import com.example.ui.theme.NetOpsTheme
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: NetOpsViewModel,
    modifier: Modifier = Modifier
) {
    val activeTheme by viewModel.activeTheme.collectAsState()
    val backgroundMonitoringEnabled by viewModel.backgroundMonitoringEnabled.collectAsState()
    val bottomBarStyle by viewModel.bottomBarStyle.collectAsState()
    val bottomBarLabelVisibility by viewModel.bottomBarLabelVisibility.collectAsState()
    val bottomBarDensity by viewModel.bottomBarDensity.collectAsState()
    val homeLayoutStyle by viewModel.homeLayoutStyle.collectAsState()
    val homeGreetingText by viewModel.homeGreetingText.collectAsState()
    
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
                        Triple("Nord Slate", NetOpsTheme.NORD_SLATE, Color(0xFF88C0D0)),
                        Triple("Matrix Green", NetOpsTheme.MATRIX_GREEN, Color(0xFF00FF41)),
                        Triple("Cyberpunk Neo", NetOpsTheme.CYBERPUNK_NEO, Color(0xFFFF007F)),
                        Triple("Ocean Blue", NetOpsTheme.OCEAN_BLUE, Color(0xFF48CAE4)),
                        Triple("Solarized Dark", NetOpsTheme.SOLARIZED_DARK, Color(0xFF2AA198)),
                        Triple("Dracula", NetOpsTheme.DRACULA, Color(0xFF50FA7B)),
                        Triple("Monokai Pro", NetOpsTheme.MONOKAI_PRO, Color(0xFFFC5C7D)),
                        Triple("Retro Gold", NetOpsTheme.RETRO_GOLD, Color(0xFFD4AF37)),
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

        // 1b. DOCK CUSTOMIZATION CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberGray, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "BOTTOM DOCK STYLING",
                        style = MaterialTheme.typography.labelMedium,
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(12.dp))

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
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) CyberCyan.copy(alpha = 0.2f) else CyberGray)
                                    .border(1.dp, if (isSel) CyberCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setBottomBarStyle(styleKey) }
                                    .padding(8.dp),
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
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) ConsoleGreen.copy(alpha = 0.2f) else CyberGray)
                                    .border(1.dp, if (isSel) ConsoleGreen else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setBottomBarLabelVisibility(visKey) }
                                    .padding(8.dp),
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
                            "compact" to "Compact",
                            "normal" to "Standard",
                            "spacious" to "Comfort"
                        ).forEach { (densityKey, densityLabel) ->
                            val isSel = bottomBarDensity == densityKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) CyberCyan.copy(alpha = 0.2f) else CyberGray)
                                    .border(1.dp, if (isSel) CyberCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setBottomBarDensity(densityKey) }
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(densityLabel, style = MaterialTheme.typography.labelSmall, color = if (isSel) CyberCyan else OffWhite)
                            }
                        }
                    }
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
