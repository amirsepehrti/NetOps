package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Site
import com.example.ui.ActiveTool
import com.example.ui.NetOpsTab
import com.example.ui.NetOpsViewModel
import com.example.ui.CellTowerInfo
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: NetOpsViewModel,
    modifier: Modifier = Modifier
) {
    val sites by viewModel.sites.collectAsState()
    val selectedSiteId by viewModel.selectedSiteId.collectAsState()
    val localContext by viewModel.localNetworkContext.collectAsState()
    val devices by viewModel.selectedSiteDevices.collectAsState()
    
    val selectedSite = sites.find { it.id == selectedSiteId }
    var expandedDropdown by remember { mutableStateOf(false) }

    // Customization & Visibility States
    val activeTheme by viewModel.activeTheme.collectAsState()
    val backgroundMonitoringEnabled by viewModel.backgroundMonitoringEnabled.collectAsState()
    val showQuickStats by viewModel.showQuickStats.collectAsState()
    val showRecentIncidents by viewModel.showRecentIncidents.collectAsState()
    val showTrafficSnifferWidget by viewModel.showTrafficSnifferWidget.collectAsState()
    val showCellRadarWidget by viewModel.showCellRadarWidget.collectAsState()
    val showMatrixHeader by viewModel.showMatrixHeader.collectAsState()
    val showDiagnosticStream by viewModel.showDiagnosticStream.collectAsState()
    val showDeviceInventory by viewModel.showDeviceInventory.collectAsState()

    val homeLayoutStyle by viewModel.homeLayoutStyle.collectAsState()
    val homeGreetingText by viewModel.homeGreetingText.collectAsState()

    // Sub-tool states
    val snifferPackets by viewModel.snifferPackets.collectAsState()
    val isSnifferRunning by viewModel.isSnifferRunning.collectAsState()
    val snifferStats by viewModel.snifferStats.collectAsState()

    val cellTowers by viewModel.cellTowers.collectAsState()
    val isCellScannerRunning by viewModel.isCellScannerRunning.collectAsState()
    val cellOperator by viewModel.cellOperator.collectAsState()
    val cellType by viewModel.cellType.collectAsState()
    val cellRsrp by viewModel.cellSignalStrengthRsrp.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Site Profile Selector Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberGray, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NETWORK PROFILE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Silver,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = selectedSite?.name ?: "No Site Selected",
                            style = MaterialTheme.typography.titleMedium,
                            color = ConsoleGreen,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Box {
                        Button(
                            onClick = { expandedDropdown = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberGray,
                                contentColor = OffWhite
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("profile_selector_button")
                        ) {
                            Text("Switch Profile", style = MaterialTheme.typography.labelMedium)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                        }

                        DropdownMenu(
                            expanded = expandedDropdown,
                            onDismissRequest = { expandedDropdown = false },
                            modifier = Modifier.background(CyberDark)
                        ) {
                            sites.forEach { site ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            site.name,
                                            color = if (site.id == selectedSiteId) ConsoleGreen else OffWhite,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    },
                                    onClick = {
                                        viewModel.selectSite(site.id)
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Hero / Greeting Custom Banner with Quick Customize Action
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ConsoleGreenDim, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CONSOLE MONITOR",
                            style = MaterialTheme.typography.labelSmall,
                            color = Silver,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = homeGreetingText.ifEmpty { "SYSTEM DIAGNOSTICS ACTIVE" },
                            style = MaterialTheme.typography.titleMedium,
                            color = ConsoleGreen,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = { viewModel.setCustomizeDashboardOpen(true) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ConsoleGreen.copy(alpha = 0.15f),
                            contentColor = ConsoleGreen
                        ),
                        border = BorderStroke(1.dp, ConsoleGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Customize Dashboard", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("CUSTOMIZE", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        // 1. Matrix Code Fall Interactive Header Canvas
        if (showMatrixHeader && homeLayoutStyle != "compact_focused") {
            item {
                MatrixRainHeader()
            }
        }

        // Local network context diagnostics
        if (showDiagnosticStream) {
            item {
                Text(
                    text = "LOCAL DIAGNOSTIC STREAM",
                    style = MaterialTheme.typography.labelMedium,
                    color = Silver,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                )
            }

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
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(ConsoleGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "INTERFACE STATUS: COMPLIANT",
                                    color = ConsoleGreen,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            IconButton(
                                onClick = { viewModel.updateLocalNetworkContext() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh network context",
                                    tint = CyberCyan,
                                    modifier = Modifier.size(16.dp)
                                  )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val contextList = localContext.toList()
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            contextList.forEach { (key, value) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = key,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Silver,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = value,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = OffWhite,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // TELEMETRY SHORTCUTS WIDGET
        if (showQuickStats) {
            item {
                Text(
                    text = "TELEMETRY SHORTCUTS",
                    style = MaterialTheme.typography.labelMedium,
                    color = Silver,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 12.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .border(1.dp, CyberGray, RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.pingHost.value = selectedSite?.gatewayIp ?: "8.8.8.8"
                                viewModel.selectTool(ActiveTool.PING)
                                viewModel.selectTab(NetOpsTab.TOOLBOX)
                            },
                        colors = CardDefaults.cardColors(containerColor = CyberDark)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = Icons.Default.NetworkPing,
                                contentDescription = "Quick Ping",
                                tint = CyberCyan,
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    "Quick Ping",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OffWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Target: ${selectedSite?.gatewayIp ?: "Gateway"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Silver,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .border(1.dp, CyberGray, RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.selectTab(NetOpsTab.TERMINAL)
                            },
                        colors = CardDefaults.cardColors(containerColor = CyberDark)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = "Raw Terminal",
                                tint = ConsoleGreen,
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    "SSH Snippets",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OffWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Remote console CLI",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Silver
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. REAL-TIME WI-FI TRAFFIC SNIFFER WIDGET
        if (showTrafficSnifferWidget && homeLayoutStyle != "compact_focused") {
            item {
                Text(
                    text = "LIVE SNIFFER STREAM",
                    style = MaterialTheme.typography.labelMedium,
                    color = Silver,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CyberGray, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isSnifferRunning) ConsoleGreen else CyberCrimson)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isSnifferRunning) "SNIFFER ACTIVE" else "SNIFFER IDLE",
                                    color = if (isSnifferRunning) ConsoleGreen else CyberCrimson,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        if (isSnifferRunning) viewModel.stopSniffer() else viewModel.startSniffer()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSnifferRunning) CyberCrimsonDim else ConsoleGreenDim,
                                        contentColor = if (isSnifferRunning) CyberCrimson else ConsoleGreen
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(if (isSnifferRunning) "STOP" else "START", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Stats counters
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberBlack, RoundedCornerShape(6.dp))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SnifferMiniStat("PACKETS", "${snifferStats.packetsCount}")
                            SnifferMiniStat("TCP", "${snifferStats.tcpCount}")
                            SnifferMiniStat("UDP", "${snifferStats.udpCount}")
                            SnifferMiniStat("BANDWIDTH", String.format("%.1f Kbps", snifferStats.dataRateKbps))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Live packets scroll preview
                        if (snifferPackets.isEmpty()) {
                            Text(
                                text = "Sniffer is idle. Tap start to intercept local frames...",
                                color = DarkSilver,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            )
                        } else {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.Black)
                                    .padding(6.dp)
                            ) {
                                snifferPackets.take(3).forEach { packet ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(
                                                        when (packet.protocol) {
                                                            "TCP" -> CyberCyanDim
                                                            "UDP" -> ConsoleGreenDim
                                                            "DNS" -> Color(0xFF3B1E4A)
                                                            else -> CyberGray
                                                        }
                                                    )
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    packet.protocol,
                                                    fontSize = 8.sp,
                                                    color = when (packet.protocol) {
                                                        "TCP" -> CyberCyan
                                                        "UDP" -> ConsoleGreen
                                                        "DNS" -> Color(0xFFE8D5FF)
                                                        else -> OffWhite
                                                    },
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "${packet.source} > ${packet.destination}",
                                                color = Silver,
                                                fontSize = 9.sp,
                                                fontFamily = FontFamily.Monospace,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Text(
                                            text = "${packet.length}B",
                                            color = DarkSilver,
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. FUTURISTIC CELL TOWER RADAR WIDGET
        if (showCellRadarWidget && homeLayoutStyle != "compact_focused") {
            item {
                Text(
                    text = "CELLULAR RADAR & BTS TELEMETRY",
                    style = MaterialTheme.typography.labelMedium,
                    color = Silver,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CyberGray, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = cellOperator,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = OffWhite,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "$cellType • SNR: 16dB",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Silver,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Button(
                                onClick = {
                                    if (isCellScannerRunning) viewModel.stopCellScan() else viewModel.startCellScan()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCellScannerRunning) CyberCrimsonDim else ConsoleGreenDim,
                                    contentColor = if (isCellScannerRunning) CyberCrimson else ConsoleGreen
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(if (isCellScannerRunning) "BTS OFF" else "BTS SCAN", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Radar Canvas Plot
                            CellRadarPlot(cellTowers, cellRsrp)

                            // Quick cell list / stats
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    "DETECTED BTS NEIGHBORS:",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ConsoleGreen,
                                    fontFamily = FontFamily.Monospace
                                )

                                if (cellTowers.isEmpty()) {
                                    Text(
                                        "BTS Scanner Idle. Tap BTS SCAN to measure microwave antenna proximity...",
                                        fontSize = 10.sp,
                                        color = DarkSilver,
                                        fontFamily = FontFamily.Monospace
                                    )
                                } else {
                                    cellTowers.take(3).forEach { tower ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(5.dp)
                                                        .clip(RoundedCornerShape(2.5.dp))
                                                        .background(if (tower.isServing) ConsoleGreen else CyberCyan)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = tower.cellType,
                                                    fontSize = 10.sp,
                                                    color = OffWhite,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            }
                                            Text(
                                                text = "${tower.rsrp}dBm • ${tower.distanceMeters}m",
                                                fontSize = 10.sp,
                                                color = if (tower.isServing) ConsoleGreen else Silver,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = if (tower.isServing) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active site devices status preview
        if (showDeviceInventory) {
            item {
                Text(
                    text = "INVENTORY DETECTED (${devices.size} Devices)",
                    style = MaterialTheme.typography.labelMedium,
                    color = Silver,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 12.dp)
                )
            }

            if (devices.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No recorded devices found in this network profile.\nAdd them via the Devices inventory tab.",
                            color = Silver,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                if (homeLayoutStyle == "two_column") {
                    val chunkedDevices = devices.chunked(2)
                    items(chunkedDevices.size) { rowIndex ->
                        val rowDevs = chunkedDevices[rowIndex]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowDevs.forEach { device ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, CyberGray, RoundedCornerShape(12.dp))
                                        .clickable {
                                            viewModel.pingHost.value = device.ipAddress
                                            viewModel.selectTool(ActiveTool.PING)
                                            viewModel.selectTab(NetOpsTab.TOOLBOX)
                                        },
                                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(CyberGray),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = when (device.deviceType) {
                                                        "Router" -> Icons.Default.Router
                                                        "Switch" -> Icons.Default.SettingsInputHdmi
                                                        "Server" -> Icons.Default.Dns
                                                        "VM" -> Icons.Default.Computer
                                                        else -> Icons.Default.DeveloperBoard
                                                    },
                                                    contentDescription = "Device Type",
                                                    tint = ConsoleGreen,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = device.name,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = OffWhite,
                                                    fontFamily = FontFamily.Monospace,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = device.ipAddress,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Silver,
                                                    fontFamily = FontFamily.Monospace,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            if (rowDevs.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                } else {
                    items(devices.size) { index ->
                        val device = devices[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, CyberGray, RoundedCornerShape(12.dp))
                                .clickable {
                                    viewModel.pingHost.value = device.ipAddress
                                    viewModel.selectTool(ActiveTool.PING)
                                    viewModel.selectTab(NetOpsTab.TOOLBOX)
                                },
                            colors = CardDefaults.cardColors(containerColor = CyberDark)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(CyberGray),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when (device.deviceType) {
                                                "Router" -> Icons.Default.Router
                                                "Switch" -> Icons.Default.SettingsInputHdmi
                                                "Server" -> Icons.Default.Dns
                                                "VM" -> Icons.Default.Computer
                                                else -> Icons.Default.DeveloperBoard
                                            },
                                            contentDescription = "Device Type",
                                            tint = ConsoleGreen
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = device.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = OffWhite,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = "${device.vendor} • ${device.ipAddress}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Silver,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                                
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "Quick Ping",
                                        color = CyberCyan,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = "Ping device",
                                        tint = Silver,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Developer Guidelines Disclaimer / Signature
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "NetOps Mobile v1.0",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkSilver,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "FOSS • GPL-3.0 • Direct Offline Engine",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkSilver,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
fun ThemePillButton(
    label: String,
    theme: NetOpsTheme,
    activeTheme: NetOpsTheme,
    primaryColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isActive = theme == activeTheme
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) primaryColor.copy(alpha = 0.2f) else CyberGray,
            contentColor = if (isActive) primaryColor else Silver
        ),
        shape = RoundedCornerShape(8.dp),
        border = if (isActive) BorderStroke(1.5.dp, primaryColor) else BorderStroke(1.dp, Color.Transparent),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
        modifier = modifier.height(38.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(primaryColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun WidgetVisibilityToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onCheckedChange() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange() },
            colors = CheckboxDefaults.colors(
                checkedColor = ConsoleGreen,
                uncheckedColor = Silver
            ),
            modifier = Modifier.scale(0.85f)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (checked) OffWhite else DarkSilver,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (checked) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun SnifferMiniStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 8.sp, color = Silver, fontFamily = FontFamily.Monospace)
        Text(value, fontSize = 10.sp, color = ConsoleGreen, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MatrixRainHeader() {
    val characters = remember { "010101010101ABCDEFHIJKLMNOPQRSTUVWXYZ#@$&*".toCharArray() }
    val columnCount = 22
    val streamHeights = remember { Array(columnCount) { (4..12).random() } }
    val streamY = remember { Array(columnCount) { (-15..0).random().toFloat() } }
    val streamSpeed = remember { Array(columnCount) { (2..4).random().toFloat() } }
    
    var triggerRecomposition by remember { mutableStateOf(0) }
    
    LaunchedEffect(Unit) {
        while(true) {
            delay(60)
            for (i in 0 until columnCount) {
                streamY[i] += streamSpeed[i] * 0.35f
                if (streamY[i] > 20) {
                    streamY[i] = -12f
                    streamHeights[i] = (4..12).random()
                    streamSpeed[i] = (2..4).random().toFloat()
                }
            }
            triggerRecomposition++
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .border(1.dp, ConsoleGreen.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val cellWidth = size.width / columnCount
            val fontSize = 11.sp.toPx()
            val dummy = triggerRecomposition
            
            for (col in 0 until columnCount) {
                val x = col * cellWidth + cellWidth / 2
                val currentY = streamY[col]
                val height = streamHeights[col]
                
                for (charIdx in 0 until height) {
                    val y = (currentY - charIdx) * fontSize
                    if (y in 0f..size.height) {
                        val alpha = (1f - (charIdx.toFloat() / height)).coerceIn(0f, 1f)
                        val color = if (charIdx == 0) Color.White else ConsoleGreen
                        val char = characters[(col + charIdx + dummy) % characters.size]
                        
                        drawContext.canvas.nativeCanvas.drawText(
                            char.toString(),
                            x,
                            y,
                            android.graphics.Paint().apply {
                                this.color = color.copy(alpha = alpha).toArgb()
                                this.textSize = fontSize
                                this.typeface = android.graphics.Typeface.MONOSPACE
                                this.textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)))),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(ConsoleGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "NEURAL MATRIX DECRYPTOR",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Text(
                    text = "Frame capture optimized • Core speed compliant",
                    color = ConsoleGreen,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun CellRadarPlot(towers: List<CellTowerInfo>, rsrp: Int) {
    var angleOffset by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        while(true) {
            delay(50)
            angleOffset = (angleOffset + 2f) % 360f
        }
    }
    
    Box(
        modifier = Modifier
            .size(110.dp)
            .background(Color.Black, shape = RoundedCornerShape(55.dp))
            .border(1.dp, ConsoleGreen.copy(alpha = 0.2f), RoundedCornerShape(55.dp)),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val center = size.width / 2
            val radius = size.width / 2
            
            drawCircle(color = ConsoleGreen.copy(alpha = 0.12f), radius = radius * 0.33f, style = androidx.compose.ui.graphics.drawscope.Stroke(1f))
            drawCircle(color = ConsoleGreen.copy(alpha = 0.12f), radius = radius * 0.66f, style = androidx.compose.ui.graphics.drawscope.Stroke(1f))
            drawCircle(color = ConsoleGreen.copy(alpha = 0.12f), radius = radius * 0.95f, style = androidx.compose.ui.graphics.drawscope.Stroke(1f))
            
            drawLine(color = ConsoleGreen.copy(alpha = 0.12f), start = androidx.compose.ui.geometry.Offset(0f, center), end = androidx.compose.ui.geometry.Offset(size.width, center), strokeWidth = 1f)
            drawLine(color = ConsoleGreen.copy(alpha = 0.12f), start = androidx.compose.ui.geometry.Offset(center, 0f), end = androidx.compose.ui.geometry.Offset(center, size.height), strokeWidth = 1f)
            
            val sweepRadians = Math.toRadians(angleOffset.toDouble())
            val sweepX = center + radius * Math.cos(sweepRadians).toFloat()
            val sweepY = center + radius * Math.sin(sweepRadians).toFloat()
            drawLine(
                color = ConsoleGreen.copy(alpha = 0.4f),
                start = androidx.compose.ui.geometry.Offset(center, center),
                end = androidx.compose.ui.geometry.Offset(sweepX, sweepY),
                strokeWidth = 2f
            )
            
            towers.forEach { tower ->
                val distFactor = (tower.distanceMeters / 1600f).coerceIn(0.15f, 0.85f)
                val towerRad = radius * distFactor
                val towerAngleRad = Math.toRadians(tower.bearing.toDouble())
                val tx = center + towerRad * Math.cos(towerAngleRad).toFloat()
                val ty = center + towerRad * Math.sin(towerAngleRad).toFloat()
                
                val pointColor = if (tower.isServing) ConsoleGreen else CyberCyan
                val pointRadius = if (tower.isServing) 5.dp.toPx() else 3.5.dp.toPx()
                
                drawCircle(
                    color = pointColor,
                    radius = pointRadius,
                    center = androidx.compose.ui.geometry.Offset(tx, ty)
                )
                
                if (tower.isServing) {
                    drawCircle(
                        color = pointColor.copy(alpha = 0.3f),
                        radius = pointRadius + (angleOffset % 24f) / 24f * 8.dp.toPx(),
                        center = androidx.compose.ui.geometry.Offset(tx, ty),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx())
                    )
                }
            }
        }
    }
}
