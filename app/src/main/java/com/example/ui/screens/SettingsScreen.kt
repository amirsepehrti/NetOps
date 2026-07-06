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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemePillButton(
                            label = "Nord Slate",
                            theme = NetOpsTheme.NORD_SLATE,
                            activeTheme = activeTheme,
                            primaryColor = Color(0xFF88C0D0),
                            onClick = { viewModel.setTheme(NetOpsTheme.NORD_SLATE) },
                            modifier = Modifier.weight(1f)
                        )
                        ThemePillButton(
                            label = "Matrix Green",
                            theme = NetOpsTheme.MATRIX_GREEN,
                            activeTheme = activeTheme,
                            primaryColor = Color(0xFF00FF41),
                            onClick = { viewModel.setTheme(NetOpsTheme.MATRIX_GREEN) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemePillButton(
                            label = "Cyberpunk Neo",
                            theme = NetOpsTheme.CYBERPUNK_NEO,
                            activeTheme = activeTheme,
                            primaryColor = Color(0xFFFF007F),
                            onClick = { viewModel.setTheme(NetOpsTheme.CYBERPUNK_NEO) },
                            modifier = Modifier.weight(1f)
                        )
                        ThemePillButton(
                            label = "Ocean Blue",
                            theme = NetOpsTheme.OCEAN_BLUE,
                            activeTheme = activeTheme,
                            primaryColor = Color(0xFF48CAE4),
                            onClick = { viewModel.setTheme(NetOpsTheme.OCEAN_BLUE) },
                            modifier = Modifier.weight(1f)
                        )
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
