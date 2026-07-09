package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextAlign
import com.example.ui.ActiveTool
import com.example.ui.NetOpsViewModel
import com.example.ui.CellTowerInfo
import com.example.ui.theme.*
import com.example.data.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolboxScreen(
    viewModel: NetOpsViewModel,
    modifier: Modifier = Modifier
) {
    val activeTool by viewModel.activeTool.collectAsState()

    if (activeTool == ActiveTool.NONE) {
        // Main Grid list of tools
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(CyberBlack)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "DIAGNOSTIC TOOLBOX",
                    style = MaterialTheme.typography.titleLarge,
                    color = ConsoleGreen,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Text(
                    text = "Fully offline-capable network utilities",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Silver,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }

            // High-tech Radar Scope Widget
            item {
                NetworkRadarScope()
            }

            item {
                Text(
                    text = "UTILITIES",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkSilver,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ToolListItem(
                        title = "ICMP / TCP Ping",
                        description = "Test reachability, packet loss, live RTT curves & jitter",
                        icon = Icons.Default.NetworkPing,
                        accentColor = ConsoleGreen,
                        onClick = { viewModel.selectTool(ActiveTool.PING) },
                        tag = "tool_ping"
                    )

                    ToolListItem(
                        title = "Port Scanner",
                        description = "Concurrently scan TCP ports with live service mappings",
                        icon = Icons.Default.Troubleshoot,
                        accentColor = CyberCyan,
                        onClick = { viewModel.selectTool(ActiveTool.PORT_SCANNER) },
                        tag = "tool_port_scan"
                    )

                    ToolListItem(
                        title = "Subnet Calculator",
                        description = "IPv4 & IPv6 splits, usable hosts ranges & splits tables",
                        icon = Icons.Default.Calculate,
                        accentColor = ConsoleGreenLight,
                        onClick = { viewModel.selectTool(ActiveTool.SUBNET_CALC) },
                        tag = "tool_subnet_calc"
                    )

                    ToolListItem(
                        title = "DNS Lookup",
                        description = "Query A/AAAA, CNAME records with custom servers",
                        icon = Icons.Default.TravelExplore,
                        accentColor = CyberCyan,
                        onClick = { viewModel.selectTool(ActiveTool.DNS_LOOKUP) },
                        tag = "tool_dns"
                    )

                    ToolListItem(
                        title = "Wake-On-LAN",
                        description = "Send UDP Magic Packets to saved hardware profiles",
                        icon = Icons.Default.PowerSettingsNew,
                        accentColor = CyberCrimson,
                        onClick = { viewModel.selectTool(ActiveTool.WAKE_ON_LAN) },
                        tag = "tool_wol"
                    )

                    ToolListItem(
                        title = "Traceroute Visualizer",
                        description = "Trace routing hop paths with dynamic nodes, IPs & latencies",
                        icon = Icons.Default.Route,
                        accentColor = CyberCyan,
                        onClick = { viewModel.selectTool(ActiveTool.TRACEROUTE) },
                        tag = "tool_traceroute"
                    )

                    ToolListItem(
                        title = "WHOIS & IP Geolocation",
                        description = "Query registry records & map geolocation coordinates",
                        icon = Icons.Default.Public,
                        accentColor = ConsoleGreen,
                        onClick = { viewModel.selectTool(ActiveTool.WHOIS_LOOKUP) },
                        tag = "tool_whois"
                    )

                    ToolListItem(
                        title = "Network Speed Test",
                        description = "High-fidelity bandwidth test with glowing dial needle",
                        icon = Icons.Default.Speed,
                        accentColor = CyberCrimson,
                        onClick = { viewModel.selectTool(ActiveTool.SPEED_TEST) },
                        tag = "tool_speed_test"
                    )

                    ToolListItem(
                        title = "Traffic Generator",
                        description = "Simulate custom packet generation and evaluate receiver load",
                        icon = Icons.Default.Send,
                        accentColor = ConsoleGreen,
                        onClick = { viewModel.selectTool(ActiveTool.TRAFFIC_GENERATOR) },
                        tag = "tool_traffic_gen"
                    )

                    ToolListItem(
                        title = "Bandwidth Client & Server",
                        description = "Conduct iperf3-compatible client/server throughput tests",
                        icon = Icons.Default.SwapCalls,
                        accentColor = CyberCyan,
                        onClick = { viewModel.selectTool(ActiveTool.BANDWIDTH_TEST) },
                        tag = "tool_bandwidth_test"
                    )

                    ToolListItem(
                        title = "SNMP Discovery & Scan",
                        description = "Query MIB variables, walk OIDs, and list switch interfaces",
                        icon = Icons.Default.SettingsEthernet,
                        accentColor = ConsoleGreenLight,
                        onClick = { viewModel.selectTool(ActiveTool.SNMP_DISCOVERY) },
                        tag = "tool_snmp_scan"
                    )

                    ToolListItem(
                        title = "WAN Killer Congestion Tester",
                        description = "Saturate network links with heavy packet trains to test resilience",
                        icon = Icons.Default.FlashOn,
                        accentColor = CyberCrimson,
                        onClick = { viewModel.selectTool(ActiveTool.WAN_KILLER) },
                        tag = "tool_wan_killer"
                    )

                    ToolListItem(
                        title = "Subnet MAC Scanner",
                        description = "Probe local subnets, find active hosts, and match MAC OUIs",
                        icon = Icons.Default.Search,
                        accentColor = CyberCyan,
                        onClick = { viewModel.selectTool(ActiveTool.MAC_SCANNER) },
                        tag = "tool_mac_scanner"
                    )

                    ToolListItem(
                        title = "Wi-Fi Signal & Sniffer",
                        description = "Measure real-time signal RSSI, channels, noise, standards, and capture raw packet frame dumps",
                        icon = Icons.Default.Wifi,
                        accentColor = ConsoleGreen,
                        onClick = { viewModel.selectTool(ActiveTool.WIFI_DIAGNOSTICS) },
                        tag = "tool_wifi_diagnostics"
                    )

                    ToolListItem(
                        title = "BTS Cell Tower Diagnostics",
                        description = "Track active cell towers, identify neighbors on sweeping radar scope, and evaluate microwave radio quality",
                        icon = Icons.Default.CellTower,
                        accentColor = CyberCyan,
                        onClick = { viewModel.selectTool(ActiveTool.CELL_DIAGNOSTICS) },
                        tag = "tool_cell_diagnostics"
                    )
                }
            }

            item {
                Text(
                    text = "HISTORICAL RUNS",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkSilver,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, top = 12.dp, bottom = 4.dp)
                )
            }

            item {
                RecentExecutionsCard(viewModel)
            }

            item {
                Text(
                    text = "DATA MANAGEMENT",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkSilver,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, top = 12.dp, bottom = 4.dp)
                )
            }

            item {
                BackupRestoreCard(viewModel)
            }
        }
    } else {
        // Tool-specific screens
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(CyberBlack)
        ) {
            // Header bar to return
            TopAppBar(
                title = {
                    Text(
                        text = when (activeTool) {
                            ActiveTool.PING -> "ICMP / TCP PING"
                            ActiveTool.PORT_SCANNER -> "PORT SCANNER"
                            ActiveTool.SUBNET_CALC -> "SUBNET CALCULATOR"
                            ActiveTool.DNS_LOOKUP -> "DNS RECORD LOOKUP"
                            ActiveTool.WAKE_ON_LAN -> "WAKE-ON-LAN"
                            ActiveTool.TRACEROUTE -> "TRACEROUTE VISUALIZER"
                            ActiveTool.WHOIS_LOOKUP -> "WHOIS & IP GEOLOCATION"
                            ActiveTool.SPEED_TEST -> "NETWORK SPEED TEST"
                            ActiveTool.TRAFFIC_GENERATOR -> "TRAFFIC GENERATOR"
                            ActiveTool.BANDWIDTH_TEST -> "BANDWIDTH CLIENT & SERVER"
                            ActiveTool.SNMP_DISCOVERY -> "SNMP DISCOVERY & WALK"
                            ActiveTool.WAN_KILLER -> "WAN KILLER CONGESTION"
                            ActiveTool.MAC_SCANNER -> "SUBNET MAC SCANNER"
                            ActiveTool.WIFI_DIAGNOSTICS -> "WI-FI SIGNAL & SNIFFER"
                            ActiveTool.CELL_DIAGNOSTICS -> "CELL RADAR & RF DIAGNOSTICS"
                            else -> "NETOPS TOOL"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = ConsoleGreen
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.selectTool(ActiveTool.NONE) },
                        modifier = Modifier.testTag("tool_back_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = OffWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberDark)
            )

            Box(modifier = Modifier.weight(1f)) {
                when (activeTool) {
                    ActiveTool.PING -> PingToolView(viewModel)
                    ActiveTool.PORT_SCANNER -> PortScannerToolView(viewModel)
                    ActiveTool.SUBNET_CALC -> SubnetCalcToolView(viewModel)
                    ActiveTool.DNS_LOOKUP -> DnsLookupToolView(viewModel)
                    ActiveTool.WAKE_ON_LAN -> WakeOnLanToolView(viewModel)
                    ActiveTool.TRACEROUTE -> TracerouteToolView(viewModel)
                    ActiveTool.WHOIS_LOOKUP -> WhoisLookupToolView(viewModel)
                    ActiveTool.SPEED_TEST -> SpeedTestToolView(viewModel)
                    ActiveTool.TRAFFIC_GENERATOR -> TrafficGenToolView(viewModel)
                    ActiveTool.BANDWIDTH_TEST -> BandwidthTestToolView(viewModel)
                    ActiveTool.SNMP_DISCOVERY -> SnmpDiscoveryToolView(viewModel)
                    ActiveTool.WAN_KILLER -> WanKillerToolView(viewModel)
                    ActiveTool.MAC_SCANNER -> MacScannerToolView(viewModel)
                    ActiveTool.WIFI_DIAGNOSTICS -> WifiDiagnosticsToolView(viewModel)
                    ActiveTool.CELL_DIAGNOSTICS -> CellDiagnosticsToolView(viewModel)
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun ToolListItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    tag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CyberGray, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag(tag),
        colors = CardDefaults.cardColors(containerColor = CyberDark)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = accentColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Black, color = OffWhite)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = Silver)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Silver)
        }
    }
}

// 1. PING SCREEN
@Composable
fun PingToolView(viewModel: NetOpsViewModel) {
    val host by viewModel.pingHost.collectAsState()
    val count by viewModel.pingCount.collectAsState()
    val interval by viewModel.pingInterval.collectAsState()
    val size by viewModel.pingPacketSize.collectAsState()
    val ttl by viewModel.pingTtl.collectAsState()
    val useTcp by viewModel.pingUseTcp.collectAsState()
    val results by viewModel.pingResults.collectAsState()
    val summary by viewModel.pingSummary.collectAsState()
    val isRunning by viewModel.isPingRunning.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Configuration fields
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberDark),
                modifier = Modifier.border(1.dp, CyberGray, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = host,
                        onValueChange = { viewModel.pingHost.value = it },
                        label = { Text("Target IP / Hostname") },
                        modifier = Modifier.fillMaxWidth().testTag("ping_host_input"),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite,
                            focusedContainerColor = CyberBlack,
                            unfocusedContainerColor = CyberBlack
                        )
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = count,
                            onValueChange = { viewModel.pingCount.value = it },
                            label = { Text("Count") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                        )
                        OutlinedTextField(
                            value = interval,
                            onValueChange = { viewModel.pingInterval.value = it },
                            label = { Text("Interval (s)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = useTcp,
                            onCheckedChange = { viewModel.pingUseTcp.value = it },
                            colors = CheckboxDefaults.colors(checkedColor = ConsoleGreen)
                        )
                        Text("Use TCP connect fallback (Port 80/443)", color = OffWhite, style = MaterialTheme.typography.bodySmall)
                    }

                    Button(
                        onClick = { if (isRunning) viewModel.stopPing() else viewModel.startPing() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRunning) CyberCrimson else ConsoleGreen,
                            contentColor = CyberBlack
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("ping_action_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isRunning) "STOP DIAGNOSTIC" else "START DIAGNOSTIC",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Live stats graph & Stability Dial (Dual-Pane Graphical Telemetry)
        if (results.isNotEmpty()) {
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
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Latency line graph
                        Column(modifier = Modifier.weight(0.65f)) {
                            Text(
                                "LIVE RTT TELEMETRY (ms)",
                                style = MaterialTheme.typography.labelSmall,
                                color = Silver,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Box(modifier = Modifier.fillMaxWidth().height(110.dp)) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val rtts = results.map { it.timeMs }
                                    val maxRtt = (rtts.maxOrNull() ?: 1.0).coerceAtLeast(10.0)
                                    val w = this.size.width
                                    val h = this.size.height

                                    // Draw background grid lines
                                    drawLine(Color(0xFF1E293B), Offset(0f, h * 0.25f), Offset(w, h * 0.25f), 1f)
                                    drawLine(Color(0xFF1E293B), Offset(0f, h * 0.5f), Offset(w, h * 0.5f), 1f)
                                    drawLine(Color(0xFF1E293B), Offset(0f, h * 0.75f), Offset(w, h * 0.75f), 1f)

                                    if (results.size > 1) {
                                        val path = Path()
                                        val stepX = w / (results.size - 1)
                                        rtts.forEachIndexed { index, time ->
                                            val pctY = 1f - (time / maxRtt).toFloat().coerceIn(0f, 1f)
                                            val posX = index * stepX
                                            val posY = pctY * h
                                            if (index == 0) {
                                                path.moveTo(posX, posY)
                                            } else {
                                                path.lineTo(posX, posY)
                                            }
                                            
                                            // Highlight nodes
                                            drawCircle(ConsoleGreen, 8f, Offset(posX, posY))
                                        }
                                        
                                        // Draw fill under the curve
                                        val fillPath = Path()
                                        fillPath.addPath(path)
                                        fillPath.lineTo(w, h)
                                        fillPath.lineTo(0f, h)
                                        fillPath.close()
                                        drawPath(
                                            path = fillPath,
                                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                                colors = listOf(ConsoleGreen.copy(alpha = 0.3f), Color.Transparent)
                                            )
                                        )
                                        
                                        drawPath(path, ConsoleGreen, style = Stroke(4f))
                                    } else if (results.size == 1) {
                                        val pctY = 1f - (rtts[0] / maxRtt).toFloat().coerceIn(0f, 1f)
                                        drawCircle(ConsoleGreen, 12f, Offset(w/2f, pctY * h))
                                    }
                                }
                                
                                // Overlay max/min/avg text markers on the left
                                val rtts = results.map { it.timeMs }
                                val maxRtt = (rtts.maxOrNull() ?: 1.0).coerceAtLeast(10.0)
                                Text(
                                    text = "${maxRtt.toInt()} ms",
                                    color = DarkSilver,
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.align(Alignment.TopStart)
                                )
                                Text(
                                    text = "0 ms",
                                    color = DarkSilver,
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.align(Alignment.BottomStart)
                                )
                            }
                        }

                        // Stability Dial
                        Column(
                            modifier = Modifier
                                .weight(0.35f)
                                .height(130.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "LINK STABILITY",
                                style = MaterialTheme.typography.labelSmall,
                                color = Silver,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            val rtts = results.map { it.timeMs }
                            val lossPct = summary?.lossPercent ?: 0
                            // Calculate stability based on loss and jitter
                            val jitter = if (rtts.size > 1) {
                                var sumDiff = 0.0
                                for (i in 0 until rtts.size - 1) {
                                    sumDiff += kotlin.math.abs(rtts[i+1] - rtts[i])
                                }
                                sumDiff / (rtts.size - 1)
                            } else 0.0
                            
                            val stability = (100 - lossPct - (jitter * 2).toInt()).coerceIn(0, 100)

                            Box(
                                modifier = Modifier.size(70.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val w = this.size.width
                                    val h = this.size.height
                                    
                                    // Draw circular background tracks
                                    drawArc(
                                        color = CyberSlate,
                                        startAngle = 135f,
                                        sweepAngle = 270f,
                                        useCenter = false,
                                        style = Stroke(width = 6f)
                                    )
                                    
                                    // Draw active stability arc
                                    val stabilityAngle = 270f * (stability / 100f)
                                    drawArc(
                                        color = if (stability > 80) ConsoleGreen else if (stability > 50) CyberCyan else CyberCrimson,
                                        startAngle = 135f,
                                        sweepAngle = stabilityAngle,
                                        useCenter = false,
                                        style = Stroke(width = 6f)
                                    )
                                }
                                
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$stability%",
                                        color = if (stability > 80) ConsoleGreen else if (stability > 50) CyberCyan else CyberCrimson,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = "STABLE",
                                        color = Silver,
                                        fontSize = 7.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Jitter: ${String.format("%.1f", jitter)} ms",
                                color = DarkSilver,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        // Summary Statistics
        summary?.let { sum ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ConsoleGreenDim, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "PING SUMMARY STATISTICS",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ConsoleGreen,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Loss Ratio", style = MaterialTheme.typography.bodySmall, color = Silver)
                                Text("${sum.lossPercent}%", style = MaterialTheme.typography.titleMedium, color = if (sum.lossPercent > 0) CyberCrimson else ConsoleGreen, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                            Column {
                                Text("Min RTT", style = MaterialTheme.typography.bodySmall, color = Silver)
                                Text("${String.format("%.2f", sum.minRtt)} ms", style = MaterialTheme.typography.bodyMedium, color = OffWhite, fontFamily = FontFamily.Monospace)
                            }
                            Column {
                                Text("Avg RTT", style = MaterialTheme.typography.bodySmall, color = Silver)
                                Text("${String.format("%.2f", sum.avgRtt)} ms", style = MaterialTheme.typography.bodyMedium, color = OffWhite, fontFamily = FontFamily.Monospace)
                            }
                            Column {
                                Text("Max RTT", style = MaterialTheme.typography.bodySmall, color = Silver)
                                Text("${String.format("%.2f", sum.maxRtt)} ms", style = MaterialTheme.typography.bodyMedium, color = OffWhite, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }

        // Live feed list of packets
        if (results.isNotEmpty()) {
            item {
                Text("LIVE PAC_STREAM LOGS", style = MaterialTheme.typography.labelSmall, color = Silver, fontFamily = FontFamily.Monospace)
            }

            items(results) { res ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CyberDark),
                    modifier = Modifier.border(1.dp, if (res.error != null) CyberCrimsonDim else CyberGray, RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = res.rawLine,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            color = if (res.error != null) CyberCrimson else OffWhite
                        )
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.PING) }
    }
}

// 2. PORT SCANNER SCREEN
@Composable
fun PortScannerToolView(viewModel: NetOpsViewModel) {
    val host by viewModel.scanHost.collectAsState()
    val preset by viewModel.scanPreset.collectAsState()
    val singlePort by viewModel.scanSinglePort.collectAsState()
    val rangeStart by viewModel.scanRangeStart.collectAsState()
    val rangeEnd by viewModel.scanRangeEnd.collectAsState()
    val scannedPorts by viewModel.scannedPorts.collectAsState()
    val isRunning by viewModel.isScanRunning.collectAsState()
    val progress by viewModel.scanProgress.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberDark),
                modifier = Modifier.border(1.dp, CyberGray, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = host,
                        onValueChange = { viewModel.scanHost.value = it },
                        label = { Text("Target Host / Server IP") },
                        modifier = Modifier.fillMaxWidth().testTag("scan_host_input"),
                        colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                    )

                    // Presets selector
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("common" to "Common Ports", "range" to "Custom Range", "single" to "Single Port").forEach { (value, label) ->
                            FilterChip(
                                selected = preset == value,
                                onClick = { viewModel.scanPreset.value = value },
                                label = { Text(label, style = MaterialTheme.typography.bodySmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyberCyan,
                                    selectedLabelColor = CyberBlack
                                )
                            )
                        }
                    }

                    if (preset == "single") {
                        OutlinedTextField(
                            value = singlePort,
                            onValueChange = { viewModel.scanSinglePort.value = it },
                            label = { Text("Port Number") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                        )
                    } else if (preset == "range") {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = rangeStart,
                                onValueChange = { viewModel.scanRangeStart.value = it },
                                label = { Text("Start Port") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                            )
                            OutlinedTextField(
                                value = rangeEnd,
                                onValueChange = { viewModel.scanRangeEnd.value = it },
                                label = { Text("End Port") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                            )
                        }
                    }

                    Button(
                        onClick = { if (isRunning) viewModel.stopPortScan() else viewModel.startPortScan() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRunning) CyberCrimson else CyberCyan,
                            contentColor = CyberBlack
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("scan_action_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isRunning) "ABORT SCAN" else "RUN TCP CONNECT SCAN",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        if (isRunning) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CyberDark),
                    modifier = Modifier.border(1.dp, CyberGray, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("SCAN PROGRESS", style = MaterialTheme.typography.labelSmall, color = Silver)
                            Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = CyberCyan, fontFamily = FontFamily.Monospace)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = CyberCyan,
                            trackColor = CyberGray
                        )
                    }
                }
            }
        }

        // Highly graphical glowing LED scanner matrix
        if (scannedPorts.isNotEmpty() || isRunning) {
            item {
                PortGridMatrix(scannedPorts = scannedPorts, isRunning = isRunning)
            }
        }

        if (scannedPorts.isNotEmpty()) {
            item {
                Text("SCAN RESULTS DETECTED", style = MaterialTheme.typography.labelSmall, color = Silver, fontFamily = FontFamily.Monospace)
            }

            items(scannedPorts.sortedBy { it.port }) { sc ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CyberDark),
                    modifier = Modifier.border(1.dp, if (sc.isOpen) ConsoleGreenDim else CyberGray, RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (sc.isOpen) ConsoleGreen else CyberCrimson)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Port ${sc.port} (${sc.service})",
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = FontFamily.Monospace,
                                color = OffWhite
                            )
                        }

                        Text(
                            text = if (sc.isOpen) "OPEN (${sc.responseTimeMs}ms)" else "CLOSED",
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            color = if (sc.isOpen) ConsoleGreen else Silver
                        )
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.PORT_SCANNER) }
    }
}

// 3. SUBNET CALCULATOR SCREEN
@Composable
fun SubnetCalcToolView(viewModel: NetOpsViewModel) {
    val ip by viewModel.subnetIp.collectAsState()
    val cidr by viewModel.subnetCidr.collectAsState()
    val splitCidr by viewModel.subnetSplitCidr.collectAsState()
    val subnetInfo by viewModel.subnetInfo.collectAsState()
    val splitList by viewModel.splitSubnets.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberDark),
                modifier = Modifier.border(1.dp, CyberGray, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = ip,
                            onValueChange = { viewModel.subnetIp.value = it; viewModel.calculateSubnetInfo() },
                            label = { Text("Base IP Address") },
                            modifier = Modifier.weight(2f).testTag("subnet_ip_input"),
                            colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                        )
                        OutlinedTextField(
                            value = cidr,
                            onValueChange = { viewModel.subnetCidr.value = it; viewModel.calculateSubnetInfo() },
                            label = { Text("CIDR") },
                            modifier = Modifier.weight(1f).testTag("subnet_cidr_input"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                        )
                    }

                    OutlinedTextField(
                        value = splitCidr,
                        onValueChange = { viewModel.subnetSplitCidr.value = it; viewModel.calculateSubnetInfo() },
                        label = { Text("Split Target Subnet CIDR (e.g. split into /26)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                    )
                }
            }
        }

        subnetInfo?.let { info ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ConsoleGreenDim, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "SUBNET COMPUTATION",
                            color = ConsoleGreen,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        SubnetRow("Netmask Address", info.netmask)
                        SubnetRow("Network Host Address", info.networkAddress)
                        SubnetRow("Broadcast Address", info.broadcastAddress)
                        SubnetRow("Usable Range", info.usableRange)
                        SubnetRow("Total Valid Hosts", if (info.isIpv6) "Scope-bound" else info.numHosts.toString())
                    }
                }
            }

            // Beautiful Binary Subnet Address Bit Visualizer
            item {
                BinarySubnetVisualizer(
                    ip = info.ipAddress,
                    mask = info.netmask,
                    cidrVal = info.cidr.toIntOrNull() ?: 24
                )
            }
        }

        if (splitList.isNotEmpty()) {
            item {
                Text("SUB-SPLIT SUBNETS", style = MaterialTheme.typography.labelSmall, color = Silver, fontFamily = FontFamily.Monospace)
            }

            items(splitList) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CyberDark),
                    modifier = Modifier.border(1.dp, CyberGray, RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item, color = OffWhite, fontFamily = FontFamily.Monospace)
                        Text("Subnet Slice", color = CyberCyan, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.SUBNET_CALC) }
    }
}

@Composable
fun SubnetRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Silver, fontFamily = FontFamily.Monospace)
        Text(value, style = MaterialTheme.typography.bodySmall, color = OffWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

// 4. DNS LOOKUP SCREEN
@Composable
fun DnsLookupToolView(viewModel: NetOpsViewModel) {
    val domain by viewModel.dnsDomain.collectAsState()
    val resolver by viewModel.dnsResolver.collectAsState()
    val records by viewModel.dnsRecords.collectAsState()
    val isLoading by viewModel.isDnsLoading.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberDark),
                modifier = Modifier.border(1.dp, CyberGray, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = domain,
                        onValueChange = { viewModel.dnsDomain.value = it },
                        label = { Text("Domain (e.g. google.com)") },
                        modifier = Modifier.fillMaxWidth().testTag("dns_domain_input"),
                        colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                    )

                    OutlinedTextField(
                        value = resolver,
                        onValueChange = { viewModel.dnsResolver.value = it },
                        label = { Text("Optional Custom DNS Resolver IP") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                    )

                    Button(
                        onClick = { viewModel.performDnsLookup() },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = CyberBlack),
                        modifier = Modifier.fillMaxWidth().testTag("dns_action_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = CyberBlack)
                        } else {
                            Text(
                                "QUERY RECORDS",
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        // Beautiful Graphical Connected Node Map of resolved DNS entries
        if (records.isNotEmpty()) {
            item {
                DnsGraphVisualizer(domain = domain, records = records)
            }
        }

        if (records.isNotEmpty()) {
            item {
                Text("RESOLVED DNS RECORDS", style = MaterialTheme.typography.labelSmall, color = Silver, fontFamily = FontFamily.Monospace)
            }

            items(records) { rec ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CyberDark),
                    modifier = Modifier.border(1.dp, if (rec.type == "ERROR") CyberCrimsonDim else CyberGray, RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp).fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(rec.type, color = if (rec.type == "ERROR") CyberCrimson else ConsoleGreen, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                            Text("TTL: ${rec.ttl}s", color = Silver, style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(rec.value, color = OffWhite, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.DNS_LOOKUP) }
    }
}

// 5. WAKE-ON-LAN SCREEN
@Composable
fun WakeOnLanToolView(viewModel: NetOpsViewModel) {
    val mac by viewModel.wolMac.collectAsState()
    val bcast by viewModel.wolBroadcast.collectAsState()
    val port by viewModel.wolPort.collectAsState()
    val savedWols by viewModel.savedWols.collectAsState()
    val statusMsg by viewModel.wolStatusMessage.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    var isTransmitting by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Transmitter Animation Card
        item {
            WolBroadcastAnimator(isAnimating = isTransmitting)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberDark),
                modifier = Modifier.border(1.dp, CyberGray, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = mac,
                        onValueChange = { viewModel.wolMac.value = it },
                        label = { Text("Target MAC Address (Colons)") },
                        modifier = Modifier.fillMaxWidth().testTag("wol_mac_input"),
                        colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = bcast,
                            onValueChange = { viewModel.wolBroadcast.value = it },
                            label = { Text("Broadcast Address") },
                            modifier = Modifier.weight(2f),
                            colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                        )
                        OutlinedTextField(
                            value = port,
                            onValueChange = { viewModel.wolPort.value = it },
                            label = { Text("Port") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.colors(focusedContainerColor = CyberBlack, unfocusedContainerColor = CyberBlack)
                        )
                    }

                    Button(
                        onClick = { 
                            coroutineScope.launch {
                                isTransmitting = true
                                kotlinx.coroutines.delay(1500)
                                isTransmitting = false
                            }
                            viewModel.triggerWakeOnLan() 
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCrimson, contentColor = OffWhite),
                        modifier = Modifier.fillMaxWidth().testTag("wol_action_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "TRANSMIT MAGIC PACKET",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    if (statusMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = statusMsg,
                            color = if (statusMsg.contains("Error")) CyberCrimson else ConsoleGreen,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (savedWols.isNotEmpty()) {
            item {
                Text("SAVED HARDWARE PROFILES", style = MaterialTheme.typography.labelSmall, color = Silver, fontFamily = FontFamily.Monospace)
            }

            items(savedWols) { wol ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CyberDark),
                    modifier = Modifier.border(1.dp, CyberGray, RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(wol.name, fontWeight = FontWeight.Bold, color = OffWhite)
                            Text("MAC: ${wol.macAddress} • Port ${wol.port}", style = MaterialTheme.typography.bodySmall, color = Silver, fontFamily = FontFamily.Monospace)
                        }

                        Button(
                            onClick = { 
                                coroutineScope.launch {
                                    isTransmitting = true
                                    kotlinx.coroutines.delay(1500)
                                    isTransmitting = false
                                }
                                viewModel.triggerWakeOnLan(wol.macAddress, wol.broadcastIp) 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberGray, contentColor = ConsoleGreen),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Wake", fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.WAKE_ON_LAN) }
    }
}


// ==========================================
// ====== DYNAMIC GRAPHICAL COMPONENTS ======
// ==========================================

@Composable
fun NetworkRadarScope() {
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .border(1.dp, CyberGray, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radar circular canvas
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(CyberBlack, RoundedCornerShape(65.dp))
                    .border(1.dp, ConsoleGreenDim, RoundedCornerShape(65.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.width / 2

                    // Draw concentric radar rings
                    drawCircle(color = ConsoleGreenDim, radius = radius * 0.3f, style = Stroke(1f))
                    drawCircle(color = ConsoleGreenDim, radius = radius * 0.6f, style = Stroke(1f))
                    drawCircle(color = ConsoleGreenDim, radius = radius * 0.9f, style = Stroke(1.5f))

                    // Draw crosshairs
                    drawLine(ConsoleGreenDim, Offset(0f, center.y), Offset(size.width, center.y), 1f)
                    drawLine(ConsoleGreenDim, Offset(center.x, 0f), Offset(center.x, size.height), 1f)

                    // Draw the sweeping radar line
                    val rad = Math.toRadians(angle.toDouble())
                    val endX = center.x + radius * 0.9f * Math.cos(rad).toFloat()
                    val endY = center.y + radius * 0.9f * Math.sin(rad).toFloat()
                    drawLine(ConsoleGreen, center, Offset(endX, endY), 3f)

                    // Draw pulsing outer circle
                    drawCircle(ConsoleGreen.copy(alpha = (1f - pulse).coerceIn(0f, 1f)), radius = radius * pulse, style = Stroke(2f))

                    // Simulated static glowing nodes on the radar
                    val nodes = listOf(
                        Offset(center.x - radius * 0.4f, center.y - radius * 0.3f),
                        Offset(center.x + radius * 0.5f, center.y - radius * 0.4f),
                        Offset(center.x + radius * 0.2f, center.y + radius * 0.5f),
                        Offset(center.x - radius * 0.6f, center.y + radius * 0.2f)
                    )
                    nodes.forEachIndexed { idx, node ->
                        val nodeAlpha = if ((angle + idx * 90) % 360 < 120) 1.0f else 0.3f
                        drawCircle(
                            color = if (idx % 2 == 0) CyberCyan.copy(alpha = nodeAlpha) else ConsoleGreen.copy(alpha = nodeAlpha),
                            radius = 6f,
                            center = node
                        )
                        drawCircle(
                            color = if (idx % 2 == 0) CyberCyan.copy(alpha = nodeAlpha * 0.3f) else ConsoleGreen.copy(alpha = nodeAlpha * 0.3f),
                            radius = 14f * pulse,
                            center = node,
                            style = Stroke(2f)
                        )
                    }
                }
                
                // Overlay text
                Text(
                    "SCANNING",
                    style = MaterialTheme.typography.labelSmall,
                    color = ConsoleGreen,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Sidebar telemetry readouts
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "SYS_MONITOR",
                    color = ConsoleGreen,
                    style = MaterialTheme.typography.labelLarge,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(ConsoleGreen, RoundedCornerShape(3.dp)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("INTERFACES: ACTIVE", color = OffWhite, style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(CyberCyan, RoundedCornerShape(3.dp)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("DETECTION: ONLINE", color = OffWhite, style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(CyberCrimson, RoundedCornerShape(3.dp)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("THREATS: 0 DETECTED", color = OffWhite, style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { pulse },
                    color = ConsoleGreen,
                    trackColor = CyberGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

@Composable
fun PortGridMatrix(scannedPorts: List<ScannedPort>, isRunning: Boolean) {
    if (scannedPorts.isEmpty() && !isRunning) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CyberGray, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberDark)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                "PORT MATRIX SCAN STATE",
                style = MaterialTheme.typography.labelSmall,
                color = CyberCyan,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // We show up to 100 ports in a grid of 10 columns
            val columns = 10
            val ports = scannedPorts.sortedBy { it.port }
            val rows = (ports.size + columns - 1) / columns

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                for (r in 0 until rows.coerceAtMost(10)) { // limit to 10 rows for UI neatness
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (c in 0 until columns) {
                            val index = r * columns + c
                            if (index < ports.size) {
                                val item = ports[index]
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (item.isOpen) ConsoleGreenDim else CyberBlack)
                                        .border(
                                            width = 1.dp,
                                            color = if (item.isOpen) ConsoleGreen else CyberSlate,
                                            shape = RoundedCornerShape(4.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.port.toString(),
                                        fontSize = 8.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.isOpen) ConsoleGreenLight else DarkSilver
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
            if (ports.size > 100) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "+ ${ports.size - 100} more ports scanned",
                    color = Silver,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

fun ipToBinary(ipStr: String): String {
    return try {
        val parts = ipStr.split(".")
        if (parts.size == 4) {
            parts.map { part ->
                val byteVal = part.toInt().coerceIn(0, 255)
                val bin = Integer.toBinaryString(byteVal)
                "00000000".substring(bin.length) + bin
            }.joinToString(".")
        } else {
            "00000000.00000000.00000000.00000000"
        }
    } catch (e: Exception) {
        "00000000.00000000.00000000.00000000"
    }
}

@Composable
fun BinarySubnetVisualizer(ip: String, mask: String, cidrVal: Int) {
    val ipBinary = ipToBinary(ip)
    val maskBinary = ipToBinary(mask)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CyberGray, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberDark)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                "BINARY ADDRESS MAPPING",
                style = MaterialTheme.typography.labelSmall,
                color = ConsoleGreen,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Render IP Address Row
            Text("Base IP Address Bits (Network vs Host):", style = MaterialTheme.typography.bodySmall, color = Silver)
            Spacer(modifier = Modifier.height(6.dp))
            BinaryBitsRow(ipBinary, cidrVal, networkColor = ConsoleGreen, hostColor = Silver)

            Spacer(modifier = Modifier.height(12.dp))

            // Render Subnet Mask Row
            Text("Subnet Mask Bits (/ $cidrVal):", style = MaterialTheme.typography.bodySmall, color = Silver)
            Spacer(modifier = Modifier.height(6.dp))
            BinaryBitsRow(maskBinary, cidrVal, networkColor = CyberCyan, hostColor = CyberBlack)
        }
    }
}

@Composable
fun BinaryBitsRow(binaryStr: String, cidr: Int, networkColor: Color, hostColor: Color) {
    val octets = binaryStr.split(".")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        octets.forEachIndexed { octetIndex, octet ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(CyberBlack, RoundedCornerShape(4.dp))
                    .padding(vertical = 4.dp, horizontal = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                octet.forEachIndexed { bitIndex, char ->
                    val absoluteBitIndex = octetIndex * 8 + bitIndex
                    val isNetworkBit = absoluteBitIndex < cidr
                    val bgColor = if (isNetworkBit) networkColor.copy(alpha = 0.25f) else Color.Transparent
                    val textColor = if (isNetworkBit) networkColor else hostColor

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(bgColor, RoundedCornerShape(2.dp))
                            .border(0.5.dp, if (isNetworkBit) networkColor.copy(alpha = 0.4f) else CyberSlate, RoundedCornerShape(2.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char.toString(),
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DnsGraphVisualizer(domain: String, records: List<DnsRecord>) {
    if (records.isEmpty()) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .border(1.dp, CyberGray, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberDark)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val center = Offset(w / 2f, h / 2f)
                val centerRadius = 35.dp.toPx()

                // Draw central domain circle background
                drawCircle(color = CyberCyanDim, radius = centerRadius)
                drawCircle(color = CyberCyan, radius = centerRadius, style = Stroke(2f))

                // Draw surrounding records nodes
                val validRecords = records.filter { it.type != "ERROR" && it.type != "RESOLVER" }.take(6)
                if (validRecords.isNotEmpty()) {
                    val angleStep = 360f / validRecords.size
                    validRecords.forEachIndexed { index, record ->
                        val angleRad = Math.toRadians((index * angleStep).toDouble())
                        val distance = 60.dp.toPx()
                        val nodeX = center.x + distance * Math.cos(angleRad).toFloat()
                        val nodeY = center.y + distance * Math.sin(angleRad).toFloat()
                        val nodeCenter = Offset(nodeX, nodeY)
                        val nodeRadius = 18.dp.toPx()

                        // Draw connection line
                        drawLine(
                            color = CyberSlate,
                            start = center,
                            end = nodeCenter,
                            strokeWidth = 3f
                        )
                        // Draw moving signal dot
                        val pulseTime = (System.currentTimeMillis() % 2000) / 2000f
                        val signalX = center.x + (distance * pulseTime) * Math.cos(angleRad).toFloat()
                        val signalY = center.y + (distance * pulseTime) * Math.sin(angleRad).toFloat()
                        drawCircle(color = ConsoleGreen, radius = 4f, center = Offset(signalX, signalY))

                        // Draw peripheral record node bubble
                        drawCircle(color = CyberGray, radius = nodeRadius, center = nodeCenter)
                        drawCircle(color = ConsoleGreenDim, radius = nodeRadius, center = nodeCenter, style = Stroke(1.5f))
                    }
                }
            }

            // Central domain label
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (domain.length > 8) domain.take(6) + ".." else domain,
                    color = OffWhite,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            // Overlay labels precisely at their peripheral bubble positions
            val validRecords = records.filter { it.type != "ERROR" && it.type != "RESOLVER" }.take(6)
            validRecords.forEachIndexed { index, record ->
                val angleStep = 360f / validRecords.size
                val angleRad = Math.toRadians((index * angleStep).toDouble())
                val distanceDp = 60.dp
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(
                            x = (distanceDp * Math.cos(angleRad).toFloat()),
                            y = (distanceDp * Math.sin(angleRad).toFloat())
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(CyberBlack.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                            .border(0.5.dp, CyberSlate, RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(record.type, color = ConsoleGreen, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        val valStr = if (record.value.length > 8) record.value.take(6) + ".." else record.value
                        Text(valStr, color = Silver, fontSize = 7.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

@Composable
fun WolBroadcastAnimator(isAnimating: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "wol_broadcast")
    val pulseScale1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse1"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .border(1.dp, CyberGray, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberDark)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Phone / Console Node
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(CyberCyanDim, RoundedCornerShape(23.dp))
                            .border(1.5.dp, CyberCyan, RoundedCornerShape(23.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = CyberCyan)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("NetOps App", color = Silver, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                }

                // Wave transmission channel
                Box(modifier = Modifier.weight(1f).height(60.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val midY = h / 2f

                        // Draw baseline link
                        drawLine(color = CyberSlate, start = Offset(10f, midY), end = Offset(w - 10f, midY), strokeWidth = 2f)

                        if (isAnimating) {
                            // Draw animated glowing packets propagating left to right
                            val pX = 10f + (w - 20f) * pulseScale1
                            drawCircle(color = CyberCrimson, radius = 6f, center = Offset(pX, midY))
                            drawCircle(color = CyberCrimson.copy(alpha = 0.3f), radius = 14f * pulseScale1, center = Offset(pX, midY))

                            // Sonar expanding arcs from left side
                            for (i in 1..3) {
                                val radius = 30.dp.toPx() * ((pulseScale1 + i/3f) % 1f)
                                drawArc(
                                    color = CyberCrimson.copy(alpha = (1f - ((pulseScale1 + i/3f) % 1f))),
                                    startAngle = -45f,
                                    sweepAngle = 90f,
                                    useCenter = false,
                                    topLeft = Offset(-radius + 20f, midY - radius),
                                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                                    style = Stroke(2f)
                                )
                            }
                        }
                    }
                }

                // Target Device Node
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(
                                if (isAnimating) CyberCrimsonDim else CyberSlate,
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (isAnimating) CyberCrimson else Silver,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Computer,
                            contentDescription = null,
                            tint = if (isAnimating) CyberCrimson else Silver
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Target Node", color = Silver, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

@Composable
fun TracerouteToolView(viewModel: NetOpsViewModel) {
    val host by viewModel.tracerouteHost.collectAsState()
    val hops by viewModel.tracerouteHops.collectAsState()
    val isRunning by viewModel.isTracerouteRunning.collectAsState()

    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Configuration fields
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberDark),
                modifier = Modifier.border(1.dp, CyberGray, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = host,
                        onValueChange = { viewModel.tracerouteHost.value = it },
                        label = { Text("Target IP / Hostname") },
                        modifier = Modifier.fillMaxWidth().testTag("traceroute_host_input"),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite,
                            focusedContainerColor = CyberBlack,
                            unfocusedContainerColor = CyberBlack
                        )
                    )

                    Button(
                        onClick = { if (isRunning) viewModel.stopTraceroute() else viewModel.startTraceroute() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRunning) CyberCrimson else CyberCyan,
                            contentColor = CyberBlack
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("traceroute_action_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isRunning) "STOP ROUTING" else "TRACE ROUTE",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Animated Node Map View
        if (hops.isNotEmpty() || isRunning) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CyberGray, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "NETWORK TOPOLOGY PATHS",
                            style = MaterialTheme.typography.titleSmall,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        if (hops.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = CyberCyan)
                            }
                        } else {
                            Column {
                                hops.forEachIndexed { index, hop ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Left timeline visualization
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.width(40.dp)
                                        ) {
                                            // Glowing node
                                            val isLast = index == hops.size - 1
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .border(
                                                        1.5.dp,
                                                        if (isLast && isRunning) ConsoleGreen.copy(alpha = pulseAlpha) else CyberCyan,
                                                        CircleShape
                                                    )
                                                    .background(
                                                        if (isLast && isRunning) ConsoleGreen.copy(alpha = 0.2f) else CyberBlack,
                                                        CircleShape
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "${hop.hop}",
                                                    color = if (isLast && isRunning) ConsoleGreen else OffWhite,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            }

                                            // Line to next node
                                            if (index < hops.size - 1) {
                                                Box(
                                                    modifier = Modifier
                                                        .width(2.dp)
                                                        .height(30.dp)
                                                        .background(CyberCyan.copy(alpha = 0.4f))
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        // Hop card representation
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = CyberBlack),
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(vertical = 4.dp)
                                                .border(1.dp, CyberSlate, RoundedCornerShape(8.dp))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = hop.hostname,
                                                        color = OffWhite,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        maxLines = 1
                                                    )
                                                    Text(
                                                        text = "IP: ${hop.ip}",
                                                        color = Silver,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontFamily = FontFamily.Monospace
                                                    )
                                                }

                                                // Latency badge
                                                val badgeColor = when {
                                                    hop.rttMs < 15.0 -> ConsoleGreen
                                                    hop.rttMs < 50.0 -> CyberCyan
                                                    hop.rttMs < 150.0 -> ConsoleGreenLight
                                                    else -> CyberCrimson
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(badgeColor.copy(alpha = 0.15f))
                                                        .border(1.dp, badgeColor.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = String.format("%.2f ms", hop.rttMs),
                                                        color = badgeColor,
                                                        style = MaterialTheme.typography.labelSmall,
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
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.TRACEROUTE) }
    }
}

@Composable
fun WhoisLookupToolView(viewModel: NetOpsViewModel) {
    val query by viewModel.whoisQuery.collectAsState()
    val record by viewModel.whoisRecord.collectAsState()
    val isLoading by viewModel.isWhoisSearching.collectAsState()

    val infiniteTransition = rememberInfiniteTransition()
    val scanY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Input Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberDark),
                modifier = Modifier.border(1.dp, CyberGray, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { viewModel.whoisQuery.value = it },
                        label = { Text("Target IP / Domain for WHOIS") },
                        modifier = Modifier.fillMaxWidth().testTag("whois_query_input"),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite,
                            focusedContainerColor = CyberBlack,
                            unfocusedContainerColor = CyberBlack
                        )
                    )

                    Button(
                        onClick = { viewModel.performWhoisLookup() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLoading) CyberCrimson else CyberCyan,
                            contentColor = CyberBlack
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("whois_action_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isLoading) "QUERYING DIRECTORY..." else "LOOKUP DETAILS",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CyberCyan)
                }
            }
        }

        // Details Display Card
        record?.let { info ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CyberGray, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "GEOLOCATION & REGISTRY METRICS",
                            style = MaterialTheme.typography.titleSmall,
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )

                        // Custom radar coordinate map
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberBlack)
                                .border(1.dp, CyberSlate, RoundedCornerShape(8.dp))
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height

                                // Draw Radar green grids
                                val gridColor = ConsoleGreen.copy(alpha = 0.15f)
                                val stepsX = 10
                                val stepsY = 6
                                for (i in 0..stepsX) {
                                    val x = (w / stepsX) * i
                                    drawLine(color = gridColor, start = Offset(x, 0f), end = Offset(x, h), strokeWidth = 1f)
                                }
                                for (i in 0..stepsY) {
                                    val y = (h / stepsY) * i
                                    drawLine(color = gridColor, start = Offset(0f, y), end = Offset(w, y), strokeWidth = 1f)
                                }

                                // Draw circular range rings
                                drawCircle(color = gridColor, radius = h / 3f, center = Offset(w / 2f, h / 2f), style = Stroke(1f))
                                drawCircle(color = gridColor, radius = h / 1.5f, center = Offset(w / 2f, h / 2f), style = Stroke(1f))

                                // Draw scanning linear laser
                                val scanLineY = h * scanY
                                drawLine(
                                    color = ConsoleGreen.copy(alpha = 0.4f),
                                    start = Offset(0f, scanLineY),
                                    end = Offset(w, scanLineY),
                                    strokeWidth = 2f
                                )

                                // Target coordinate dot
                                val dotX = w / 2f
                                val dotY = h / 2f
                                drawCircle(color = CyberCrimson, radius = 6f, center = Offset(dotX, dotY))
                                drawCircle(color = CyberCrimson.copy(alpha = 0.3f), radius = 18f * (1.0f - scanY), center = Offset(dotX, dotY))

                                // Coordinate texts
                                drawLine(color = CyberCrimson.copy(alpha = 0.5f), start = Offset(dotX - 15f, dotY), end = Offset(dotX + 15f, dotY), strokeWidth = 1.5f)
                                drawLine(color = CyberCrimson.copy(alpha = 0.5f), start = Offset(dotX, dotY - 15f), end = Offset(dotX, dotY + 15f), strokeWidth = 1.5f)
                            }

                            // Geolocation text overlays
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .background(CyberDark.copy(alpha = 0.85f))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "COORD: Lat ${info.latitude}, Lon ${info.longitude}\nLOC: ${info.country}",
                                    color = ConsoleGreen,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Grid of details
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            WhoisDetailRow(label = "RESOLVED IP", value = info.ipAddress, color = OffWhite)
                            WhoisDetailRow(label = "ORGANIZATION", value = info.org, color = OffWhite)
                            WhoisDetailRow(label = "REGISTRAR", value = info.registrar, color = CyberCyan)
                            WhoisDetailRow(label = "CREATION DATE", value = info.creationDate, color = Silver)
                            WhoisDetailRow(label = "EXPIRATION DATE", value = info.expirationDate, color = CyberCrimson)
                            WhoisDetailRow(label = "STATUS", value = info.status, color = ConsoleGreen)
                        }

                        // Expandable Raw response
                        var showRaw by remember { mutableStateOf(false) }
                        Button(
                            onClick = { showRaw = !showRaw },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberSlate, contentColor = OffWhite),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (showRaw) "HIDE RAW RESPONSE" else "SHOW RAW WHOIS DATABASE",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        AnimatedVisibility(visible = showRaw) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = CyberBlack),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, CyberGray, RoundedCornerShape(8.dp))
                            ) {
                                Text(
                                    text = info.rawOutput,
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    color = ConsoleGreen,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.WHOIS_LOOKUP) }
    }
}

@Composable
fun WhoisDetailRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Silver,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun SpeedTestToolView(viewModel: NetOpsViewModel) {
    val state by viewModel.speedTestState.collectAsState()
    val isRunning by viewModel.isSpeedTestRunning.collectAsState()

    val needleRotation = remember { Animatable(0f) }
    
    // Animate needle matching currentSpeedMbps
    LaunchedEffect(state.currentSpeedMbps) {
        val targetRot = (state.currentSpeedMbps.toFloat() / 150f * 270f).coerceIn(0f, 270f)
        needleRotation.animateTo(
            targetValue = targetRot,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Speed indicator gauge
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberGray, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "BANDWIDTH DIAGNOSTIC COUPLER",
                        style = MaterialTheme.typography.titleSmall,
                        color = ConsoleGreen,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Draw circular custom dial
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val center = Offset(w / 2f, h / 2f)
                            val radius = w / 2f

                            // Draw gauge background Arc (from 135 to 45 deg, 270 deg span)
                            drawArc(
                                color = CyberSlate,
                                startAngle = 135f,
                                sweepAngle = 270f,
                                useCenter = false,
                                style = Stroke(12f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                            )

                            // Active progress arc matching speed percentage
                            val speedPercent = (state.currentSpeedMbps / 150.0).coerceIn(0.0, 1.0).toFloat()
                            drawArc(
                                color = ConsoleGreen,
                                startAngle = 135f,
                                sweepAngle = 270f * speedPercent,
                                useCenter = false,
                                style = Stroke(12f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                            )

                            // Draw minor/major ticks
                            val tickCount = 10
                            for (i in 0..tickCount) {
                                val angleDeg = 135f + (270f / tickCount) * i
                                val angleRad = Math.toRadians(angleDeg.toDouble())
                                val startOffset = Offset(
                                    (center.x + (radius - 12f) * Math.cos(angleRad)).toFloat(),
                                    (center.y + (radius - 12f) * Math.sin(angleRad)).toFloat()
                                )
                                val endOffset = Offset(
                                    (center.x + (radius - 24f) * Math.cos(angleRad)).toFloat(),
                                    (center.y + (radius - 24f) * Math.sin(angleRad)).toFloat()
                                )
                                drawLine(
                                    color = if (i.toFloat() / tickCount <= speedPercent) ConsoleGreen else Silver.copy(alpha = 0.5f),
                                    start = startOffset,
                                    end = endOffset,
                                    strokeWidth = 3f
                                )
                            }

                            // Needle drawing
                            // Rotate from 135f
                            val needleAngle = 135f + needleRotation.value
                            val needleRad = Math.toRadians(needleAngle.toDouble())
                            val needleLength = radius - 30f
                            val needleEnd = Offset(
                                (center.x + needleLength * Math.cos(needleRad)).toFloat(),
                                (center.y + needleLength * Math.sin(needleRad)).toFloat()
                            )
                            
                            drawLine(
                                color = CyberCrimson,
                                start = center,
                                end = needleEnd,
                                strokeWidth = 5f,
                                cap = androidx.compose.ui.graphics.StrokeCap.Round
                            )

                            // Needle center cap
                            drawCircle(color = CyberCrimson, radius = 10f, center = center)
                            drawCircle(color = CyberBlack, radius = 4f, center = center)
                        }

                        // Digital readouts in gauge
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 80.dp)
                        ) {
                            Text(
                                text = String.format("%.1f", state.currentSpeedMbps),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = OffWhite,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Mbps",
                                style = MaterialTheme.typography.labelSmall,
                                color = Silver,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = state.phase,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isRunning) CyberCyan else Silver,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Gauge metrics table
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MetricBox(label = "PING", value = String.format("%.1f ms", state.pingMs), color = ConsoleGreen)
                        MetricBox(label = "JITTER", value = String.format("%.1f ms", state.jitterMs), color = CyberCyan)
                        MetricBox(label = "DOWNLOAD", value = String.format("%.1f M", state.maxDownload), color = ConsoleGreenLight)
                        MetricBox(label = "UPLOAD", value = String.format("%.1f M", state.maxUpload), color = CyberCrimson)
                    }
                }
            }
        }

        item {
            // Action button
            Button(
                onClick = { if (isRunning) viewModel.stopSpeedTest() else viewModel.startSpeedTest() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) CyberCrimson else ConsoleGreen,
                    contentColor = CyberBlack
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("speed_test_action_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isRunning) "ABORT SPEED TEST" else "INITIATE HIGH-SPEED TEST",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        item { DiagnosticHintCard(ActiveTool.SPEED_TEST) }
    }
}

@Composable
fun MetricBox(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 9.sp,
            color = Silver,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            fontFamily = FontFamily.Monospace
        )
    }
}

// ==========================================
// 10. RECENT EXECUTIONS & DATA MANAGEMENT
// ==========================================

@Composable
fun RecentExecutionsCard(viewModel: NetOpsViewModel) {
    val executions by viewModel.recentExecutions.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CyberCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberDark),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RECENT RUNS",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = OffWhite,
                        fontFamily = FontFamily.Monospace
                    )
                }
                if (executions.isNotEmpty()) {
                    TextButton(onClick = { viewModel.clearRecentExecutions() }) {
                        Text("CLEAR ALL", color = CyberCrimson, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (executions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No diagnostic runs recorded yet.",
                        color = DarkSilver,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily.Monospace
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    executions.take(8).forEach { exec ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberBlack)
                                .clickable { viewModel.reRunExecution(exec) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val badgeColor = if (exec.status == "SUCCESS") ConsoleGreen else CyberCrimson
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(badgeColor)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = exec.toolType,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = CyberCyan,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Target: ${exec.target}",
                                    fontSize = 13.sp,
                                    color = OffWhite,
                                    fontFamily = FontFamily.Monospace
                                )
                                if (exec.parameters.isNotEmpty()) {
                                    Text(
                                        text = exec.parameters,
                                        fontSize = 11.sp,
                                        color = Silver,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                            IconButton(onClick = { viewModel.deleteRecentExecution(exec.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DarkSilver, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BackupRestoreCard(viewModel: NetOpsViewModel) {
    var backupString by remember { mutableStateOf("") }
    var restoreStatus by remember { mutableStateOf("") }
    var showBackupArea by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ConsoleGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberDark),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Backup, contentDescription = null, tint = ConsoleGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BACKUP & RESTORE MODULE",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = OffWhite,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Back up or restore your configured Homelab Sites, Devices, Saved Wake-on-LAN packets, Custom SSH snippets, and historical execution logs using standard portable JSON.",
                style = MaterialTheme.typography.bodySmall,
                color = Silver
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        backupString = viewModel.exportBackup()
                        showBackupArea = true
                        restoreStatus = "BACKUP EXPORTED SUCCESSFULLY. COPY STRING BELOW:"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ConsoleGreen, contentColor = CyberBlack),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("EXPORT BACKUP", fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }

                Button(
                    onClick = {
                        showBackupArea = true
                        backupString = ""
                        restoreStatus = "PASTE BACKUP STRING BELOW & TAP IMPORT"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = CyberBlack),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("PREPARE IMPORT", fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
            }

            if (showBackupArea) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = restoreStatus,
                    color = if (restoreStatus.contains("FAILED")) CyberCrimson else ConsoleGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = backupString,
                    onValueChange = { backupString = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, color = ConsoleGreen),
                    placeholder = { Text("JSON String goes here...", color = DarkSilver, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ConsoleGreen,
                        unfocusedBorderColor = DarkSilver,
                        focusedContainerColor = CyberBlack,
                        unfocusedContainerColor = CyberBlack
                    )
                )

                if (backupString.trim().isNotEmpty() && restoreStatus.contains("IMPORT")) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            restoreStatus = viewModel.importBackup(backupString)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ConsoleGreen, contentColor = CyberBlack),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("CONFIRM RESTORE IMPORT", fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

// ==========================================
// 11. TRAFFIC GENERATOR TOOL VIEW
// ==========================================

@Composable
fun TrafficGenToolView(viewModel: NetOpsViewModel) {
    val host by viewModel.trafficGenHost.collectAsState()
    val port by viewModel.trafficGenPort.collectAsState()
    val proto by viewModel.trafficGenProtocol.collectAsState()
    val rate by viewModel.trafficGenRate.collectAsState()
    val size by viewModel.trafficGenPacketSize.collectAsState()
    val isRunning by viewModel.isTrafficGenRunning.collectAsState()
    val logs by viewModel.trafficGenLogs.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("TRAFFIC ENGINE SETUP", color = CyberCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = host,
                        onValueChange = { viewModel.trafficGenHost.value = it },
                        label = { Text("Target IP / Domain") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedLabelColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = port,
                            onValueChange = { viewModel.trafficGenPort.value = it },
                            label = { Text("Port") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                        )
                        OutlinedTextField(
                            value = size,
                            onValueChange = { viewModel.trafficGenPacketSize.value = it },
                            label = { Text("Size (Bytes)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = rate,
                            onValueChange = { viewModel.trafficGenRate.value = it },
                            label = { Text("Rate (PPS)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Protocol", fontSize = 11.sp, color = Silver, fontFamily = FontFamily.Monospace)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                                Button(
                                    onClick = { viewModel.trafficGenProtocol.value = "UDP" },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (proto == "UDP") CyberCyan else CyberBlack, contentColor = if (proto == "UDP") CyberBlack else Silver),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("UDP")
                                }
                                Button(
                                    onClick = { viewModel.trafficGenProtocol.value = "TCP" },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (proto == "TCP") CyberCyan else CyberBlack, contentColor = if (proto == "TCP") CyberBlack else Silver),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("TCP")
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = { if (isRunning) viewModel.stopTrafficGen() else viewModel.startTrafficGen() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if (isRunning) CyberCrimson else ConsoleGreen, contentColor = CyberBlack)
            ) {
                Text(
                    text = if (isRunning) "TERMINATE PACKET STREAM" else "INITIATE PACKET STREAM",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                colors = CardDefaults.cardColors(containerColor = CyberBlack),
                border = BorderStroke(1.dp, DarkSilver)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    if (logs.isEmpty()) {
                        item {
                            Text("Waiting for engine sequence startup...", color = DarkSilver, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
                        }
                    } else {
                        items(logs) { log ->
                            Text(log, color = ConsoleGreen, fontFamily = FontFamily.Monospace, fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
                        }
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.TRAFFIC_GENERATOR) }
    }
}

// ==========================================
// 12. BANDWIDTH TEST TOOL VIEW
// ==========================================

@Composable
fun BandwidthTestToolView(viewModel: NetOpsViewModel) {
    val role by viewModel.bandwidthRole.collectAsState()
    val host by viewModel.bandwidthHost.collectAsState()
    val port by viewModel.bandwidthPort.collectAsState()
    val proto by viewModel.bandwidthProtocol.collectAsState()
    val duration by viewModel.bandwidthDuration.collectAsState()
    val isRunning by viewModel.isBandwidthRunning.collectAsState()
    val logs by viewModel.bandwidthLogs.collectAsState()
    val speed by viewModel.bandwidthSpeedMbps.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("BANDWIDTH UTILITY ROLE", color = CyberCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.bandwidthRole.value = "Client" },
                            colors = ButtonDefaults.buttonColors(containerColor = if (role == "Client") CyberCyan else CyberBlack, contentColor = if (role == "Client") CyberBlack else Silver),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("CLIENT MODE")
                        }
                        Button(
                            onClick = { viewModel.bandwidthRole.value = "Server" },
                            colors = ButtonDefaults.buttonColors(containerColor = if (role == "Server") CyberCyan else CyberBlack, contentColor = if (role == "Server") CyberBlack else Silver),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("SERVER MODE")
                        }
                    }

                    if (role == "Client") {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = host,
                            onValueChange = { viewModel.bandwidthHost.value = it },
                            label = { Text("iperf3 Target Host / IP") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedLabelColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = port,
                            onValueChange = { viewModel.bandwidthPort.value = it },
                            label = { Text("Port") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                        )
                        OutlinedTextField(
                            value = duration,
                            onValueChange = { viewModel.bandwidthDuration.value = it },
                            label = { Text("Duration (Secs)") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                        )
                    }
                }
            }
        }

        if (speed > 0.0) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberBlack),
                    border = BorderStroke(1.dp, CyberCyan)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("LIVE SPEED READING", fontSize = 11.sp, color = Silver, fontFamily = FontFamily.Monospace)
                        Text(
                            text = String.format("%.2f Mbps", speed),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = ConsoleGreen,
                            fontFamily = FontFamily.Monospace
                        )
                        LinearProgressIndicator(
                            progress = { (speed / 150f).toFloat().coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .padding(vertical = 4.dp),
                            color = CyberCyan,
                            trackColor = CyberDark
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = { if (isRunning) viewModel.stopBandwidthTest() else viewModel.startBandwidthTest() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if (isRunning) CyberCrimson else ConsoleGreen, contentColor = CyberBlack)
            ) {
                Text(
                    text = if (isRunning) "STOP TEST ENGINE" else "START RUN",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = CyberBlack),
                border = BorderStroke(1.dp, DarkSilver)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    items(logs) { line ->
                        Text(line, color = ConsoleGreen, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.BANDWIDTH_TEST) }
    }
}

// ==========================================
// 13. SNMP DISCOVERY TOOL VIEW
// ==========================================

@Composable
fun SnmpDiscoveryToolView(viewModel: NetOpsViewModel) {
    val host by viewModel.snmpHost.collectAsState()
    val community by viewModel.snmpCommunity.collectAsState()
    val port by viewModel.snmpPort.collectAsState()
    val isRunning by viewModel.isSnmpRunning.collectAsState()
    val result by viewModel.snmpResult.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("SNMP DISCOVERY GATEWAY", color = CyberCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = host,
                        onValueChange = { viewModel.snmpHost.value = it },
                        label = { Text("SNMP Device Host / IP") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = community,
                            onValueChange = { viewModel.snmpCommunity.value = it },
                            label = { Text("Community (Read)") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                        )
                        OutlinedTextField(
                            value = port,
                            onValueChange = { viewModel.snmpPort.value = it },
                            label = { Text("Port") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.startSnmpDiscovery() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isRunning,
                colors = ButtonDefaults.buttonColors(containerColor = ConsoleGreen, contentColor = CyberBlack)
            ) {
                Text(
                    text = if (isRunning) "WALKING OID TREES..." else "INITIATE DISCOVERY",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        result?.let { r ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberDark),
                    border = BorderStroke(1.dp, ConsoleGreen)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("SYSTEM DETAILS", color = ConsoleGreen, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Description:", color = Silver, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text(r.sysDescr, color = OffWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f).padding(start = 8.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Uptime:", color = Silver, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text(r.sysUptime, color = OffWhite, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Contact:", color = Silver, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text(r.sysContact, color = OffWhite, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Location:", color = Silver, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text(r.sysLocation, color = OffWhite, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ACTIVE MAPPED INTERFACES", color = CyberCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(12.dp))

                        r.interfacesList.forEach { iface ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyberBlack)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(iface.name, color = OffWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                                    Text("Type: ${iface.type} | MTU: ${iface.mtu}", color = Silver, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${iface.speedMbps}M ", color = ConsoleGreenLight, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                                    val isUp = iface.operStatus == "UP"
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (isUp) ConsoleGreen.copy(alpha = 0.2f) else CyberCrimson.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(iface.operStatus, color = if (isUp) ConsoleGreen else CyberCrimson, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberBlack)
                ) {
                    LazyColumn(modifier = Modifier.padding(12.dp)) {
                        items(r.rawWalk) { item ->
                            Text(item, color = ConsoleGreenLight, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.SNMP_DISCOVERY) }
    }
}

// ==========================================
// 14. WAN KILLER TOOL VIEW
// ==========================================

@Composable
fun WanKillerToolView(viewModel: NetOpsViewModel) {
    val host by viewModel.wanKillerHost.collectAsState()
    val rate by viewModel.wanKillerRateMbps.collectAsState()
    val isRunning by viewModel.isWanKillerRunning.collectAsState()
    val stats by viewModel.wanKillerStats.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberCrimson.copy(alpha = 0.15f)),
                border = BorderStroke(1.dp, CyberCrimson)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = "Warning", tint = CyberCrimson, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("HIGH RESILIENCE WARNING", color = CyberCrimson, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                        Text("This engine generates continuous raw socket datagram packet trains to evaluate link saturation. Ensure destination is local or authorized.", color = Silver, fontSize = 11.sp)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("WAN FLOOD PARAMETERS", color = CyberCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = host,
                        onValueChange = { viewModel.wanKillerHost.value = it },
                        label = { Text("Target IP / Gateway") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rate,
                        onValueChange = { viewModel.wanKillerRateMbps.value = it },
                        label = { Text("Target Rate (Mbps)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                    )
                }
            }
        }

        item {
            Button(
                onClick = { if (isRunning) viewModel.stopWanKiller() else viewModel.startWanKiller() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if (isRunning) CyberCrimson else ConsoleGreen, contentColor = CyberBlack)
            ) {
                Text(
                    text = if (isRunning) "STOP PACKET TRAIN" else "ENGAGE WAN SATURATION",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        stats?.let { s ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberDark),
                    border = BorderStroke(1.dp, CyberCyan)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("LIVE FLOOD TELEMETRY", color = CyberCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Current Bitrate:", color = Silver, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text(String.format("%.2f Mbps", s.currentThroughputMbps), color = ConsoleGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Packets Sent:", color = Silver, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text(s.packetsSent.toString(), color = OffWhite, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Volume Sent:", color = Silver, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text(String.format("%.2f MB", s.totalBytesSent.toDouble() / (1024*1024)), color = OffWhite, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Elapsed Time:", color = Silver, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text("${s.durationSecs} sec", color = OffWhite, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Drop Rate (Est):", color = Silver, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text(String.format("%.1f %%", s.lossRatePercent), color = if (s.lossRatePercent > 0) CyberCrimson else ConsoleGreen, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.WAN_KILLER) }
    }
}

// ==========================================
// 15. MAC SCANNER TOOL VIEW
// ==========================================

@Composable
fun MacScannerToolView(viewModel: NetOpsViewModel) {
    val subnet by viewModel.macScanSubnet.collectAsState()
    val isRunning by viewModel.isMacScanRunning.collectAsState()
    val progress by viewModel.macScanProgress.collectAsState()
    val results by viewModel.macScanResults.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("SUBNET SWEEP CONTROLS", color = CyberCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = subnet,
                        onValueChange = { viewModel.macScanSubnet.value = it },
                        label = { Text("Scan Range (CIDR)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = OffWhite, unfocusedTextColor = OffWhite)
                    )
                }
            }
        }

        item {
            Button(
                onClick = { if (isRunning) viewModel.stopMacScan() else viewModel.startMacScan() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if (isRunning) CyberCrimson else ConsoleGreen, contentColor = CyberBlack)
            ) {
                Text(
                    text = if (isRunning) "ABORT SCAN" else "START DISCOVERY SWEEP",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        if (isRunning) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("SCAN SWEEP IN PROGRESS", color = CyberCyan, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = ConsoleGreen,
                            trackColor = CyberBlack
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${(progress * 100).toInt()}% Probed",
                            color = ConsoleGreen,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "DISCOVERED NETWORK NODES (${results.size})",
                style = MaterialTheme.typography.labelSmall,
                color = DarkSilver,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        if (results.isEmpty() && !isRunning) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Launch scanner to probe ARP nodes.", color = DarkSilver, fontFamily = FontFamily.Monospace)
                }
            }
        } else {
            items(results) { res ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberDark),
                    border = BorderStroke(1.dp, if (res.isLocalDevice) ConsoleGreen else DarkSilver)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(res.ipAddress, fontWeight = FontWeight.Bold, color = OffWhite, fontFamily = FontFamily.Monospace, fontSize = 14.sp)
                                if (res.isLocalDevice) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ConsoleGreen.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("LOCAL", color = ConsoleGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("MAC: ${res.macAddress}", color = Silver, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text("OUI Vendor: ${res.vendor}", color = CyberCyan, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        }
                        if (res.activePorts.isNotEmpty()) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text("PORTS", color = Silver, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                Text(res.activePorts, color = ConsoleGreenLight, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.MAC_SCANNER) }
    }
}

@Composable
fun WifiDiagnosticsToolView(viewModel: NetOpsViewModel) {
    val ssid by viewModel.wifiSsid.collectAsState()
    val bssid by viewModel.wifiBssid.collectAsState()
    val signalStrength by viewModel.wifiSignalStrength.collectAsState()
    val noiseLevel by viewModel.wifiNoiseLevel.collectAsState()
    val wifiType by viewModel.wifiType.collectAsState()
    val linkSpeed by viewModel.wifiLinkSpeed.collectAsState()
    val frequency by viewModel.wifiFrequency.collectAsState()

    val isScanning by viewModel.isWifiScannerRunning.collectAsState()
    val scanProgress by viewModel.wifiScanProgress.collectAsState()
    val scanResults by viewModel.wifiScanResults.collectAsState()

    val isSnifferRunning by viewModel.isSnifferRunning.collectAsState()
    val snifferPackets by viewModel.snifferPackets.collectAsState()
    val snifferStats by viewModel.snifferStats.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Wifi, contentDescription = "Wifi SSID", tint = ConsoleGreen, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(ssid, style = MaterialTheme.typography.titleMedium, color = OffWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                            Text("BSSID: $bssid", style = MaterialTheme.typography.labelSmall, color = Silver, fontFamily = FontFamily.Monospace)
                        }
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (signalStrength > -60) ConsoleGreenDim else CyberCyanDim)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("$signalStrength dBm", color = if (signalStrength > -60) ConsoleGreen else CyberCyan, fontWeight = FontWeight.Black, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            WifiMetricCell(label = "NOISE FLOOR", value = "$noiseLevel dBm", subLabel = "SNR: ${signalStrength - noiseLevel} dB", modifier = Modifier.weight(1f))
                            WifiMetricCell(label = "STANDARDS", value = wifiType, subLabel = "802.11 Protocol", modifier = Modifier.weight(1f))
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            WifiMetricCell(label = "LINK VELOCITY", value = "$linkSpeed Mbps", subLabel = "Active Speed", modifier = Modifier.weight(1f))
                            WifiMetricCell(label = "FREQUENCY", value = "$frequency MHz", subLabel = "Channel ${(frequency - 5000) / 5}", modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        item {
            Text("DETAILED CO-CHANNEL SPECTRAL SCAN", style = MaterialTheme.typography.labelMedium, color = Silver, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(8.dp))

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
                            text = if (isScanning) "SCANNING FREQUENCIES..." else "SCAN COMPLIANT",
                            color = if (isScanning) CyberCyan else ConsoleGreen,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = { viewModel.startWifiScan() },
                            colors = ButtonDefaults.buttonColors(containerColor = ConsoleGreenDim, contentColor = ConsoleGreen),
                            shape = RoundedCornerShape(6.dp),
                            enabled = !isScanning,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("RUN AP SWEEP", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    if (isScanning) {
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { scanProgress },
                            color = ConsoleGreen,
                            trackColor = CyberGray,
                            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (scanResults.isEmpty()) {
                        Text("No scanned networks. Press SWEEP to discover airwaves...", color = DarkSilver, fontSize = 11.sp, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            scanResults.forEach { result ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(CyberBlack, RoundedCornerShape(8.dp))
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(result.ssid, fontSize = 12.sp, color = OffWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                        Text("${result.bssid} • CH ${result.channel} • ${result.standard}", fontSize = 10.sp, color = Silver, fontFamily = FontFamily.Monospace)
                                    }
                                    
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("${result.rssi} dBm", fontSize = 11.sp, color = if (result.rssi > -65) ConsoleGreen else CyberCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                        Text(result.security, fontSize = 9.sp, color = DarkSilver, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Text("NETWORK FRAME INTERCEPT (SNIFFER)", style = MaterialTheme.typography.labelMedium, color = Silver, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(if (isSnifferRunning) ConsoleGreen else CyberCrimson))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isSnifferRunning) "DECRYPTOR ON AIR" else "DECRYPTOR STOPPED", fontSize = 11.sp, color = if (isSnifferRunning) ConsoleGreen else CyberCrimson, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            Text(" • ${viewModel.getActiveInterfaceName()}", fontSize = 10.sp, color = Silver, fontFamily = FontFamily.Monospace)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (snifferPackets.isNotEmpty()) {
                                val context = androidx.compose.ui.platform.LocalContext.current
                                Button(
                                    onClick = {
                                        val exportedFile = viewModel.exportSnifferLog(context)
                                        if (exportedFile != null) {
                                            android.widget.Toast.makeText(context, "Saved to Downloads: $exportedFile", android.widget.Toast.LENGTH_LONG).show()
                                        } else {
                                            android.widget.Toast.makeText(context, "Export failed", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan.copy(alpha = 0.2f), contentColor = CyberCyan),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text("EXPORT CSV", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                            }

                            Button(
                                onClick = { if (isSnifferRunning) viewModel.stopSniffer() else viewModel.startSniffer() },
                                colors = ButtonDefaults.buttonColors(containerColor = if (isSnifferRunning) CyberCrimsonDim else ConsoleGreenDim, contentColor = if (isSnifferRunning) CyberCrimson else ConsoleGreen),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(if (isSnifferRunning) "HALT ENGINE" else "START CAPTURE", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().background(CyberBlack, RoundedCornerShape(6.dp)).padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SnifferDetailCell("TOTAL CAPTURED", "${snifferStats.packetsCount}")
                        SnifferDetailCell("TCP SEGMENTS", "${snifferStats.tcpCount}")
                        SnifferDetailCell("UDP DATAGRAMS", "${snifferStats.udpCount}")
                        SnifferDetailCell("BANDWIDTH RATE", String.format("%.2f Kbps", snifferStats.dataRateKbps))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("FRAME HEX BUFFERSTREAM DUMP:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ConsoleGreen, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(6.dp))

                    if (snifferPackets.isEmpty()) {
                        Text("Ready to hook socket raw channels. Initiate capture...", color = DarkSilver, fontSize = 11.sp, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.Black)
                                .padding(8.dp)
                        ) {
                            snifferPackets.take(6).forEach { packet ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row {
                                            Text("[${packet.timestamp}]", color = ConsoleGreen, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(packet.protocol, color = CyberCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("${packet.source}:${packet.port} > ${packet.destination}", color = OffWhite, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                        }
                                        Text("${packet.length} bytes", color = Silver, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                    }
                                    Text(packet.info, color = DarkSilver, fontSize = 8.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(start = 12.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.WIFI_DIAGNOSTICS) }
    }
}

@Composable
fun WifiMetricCell(label: String, value: String, subLabel: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .border(1.dp, CyberGray, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberBlack)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(label, fontSize = 8.sp, color = Silver, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 12.sp, color = ConsoleGreen, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Black)
            Text(subLabel, fontSize = 8.sp, color = DarkSilver, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
fun SnifferDetailCell(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 8.sp, color = Silver, fontFamily = FontFamily.Monospace)
        Text(value, fontSize = 11.sp, color = ConsoleGreen, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CellDiagnosticsToolView(viewModel: NetOpsViewModel) {
    val operator by viewModel.cellOperator.collectAsState()
    val cellType by viewModel.cellType.collectAsState()
    val cellId by viewModel.cellId.collectAsState()
    val tac by viewModel.cellTac.collectAsState()
    val mccMnc by viewModel.cellMccMnc.collectAsState()

    val rsrp by viewModel.cellSignalStrengthRsrp.collectAsState()
    val rsrq by viewModel.cellSignalStrengthRsrq.collectAsState()
    val snr by viewModel.cellSignalStrengthSnr.collectAsState()

    val isScanning by viewModel.isCellScannerRunning.collectAsState()
    val cellTowers by viewModel.cellTowers.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                        Column {
                            Text(operator, style = MaterialTheme.typography.titleMedium, color = OffWhite, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                            Text("MCC-MNC: $mccMnc", style = MaterialTheme.typography.bodySmall, color = Silver, fontFamily = FontFamily.Monospace)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyberCyanDim)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(cellType, color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("CONNECTED CELL IDENTIFIER (CID)", fontSize = 8.sp, color = Silver, fontFamily = FontFamily.Monospace)
                            Text(cellId, fontSize = 13.sp, color = OffWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("TRACKING AREA CODE (TAC)", fontSize = 8.sp, color = Silver, fontFamily = FontFamily.Monospace)
                            Text(tac, fontSize = 13.sp, color = OffWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        item {
            Text("RADIO FREQUENCY SIGNAL PROPAGATION METRICS", style = MaterialTheme.typography.labelSmall, color = Silver, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberGray, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SignalProgressMeter(
                        label = "Reference Signal Received Power (RSRP)",
                        value = "$rsrp dBm",
                        progress = ((rsrp + 140f) / 100f).coerceIn(0f, 1f),
                        evalText = when {
                            rsrp > -80 -> "EXCELLENT PENETRATION"
                            rsrp > -95 -> "ACCEPTABLE GAIN"
                            else -> "SEVERE MICROWAVE ATTENUATING"
                        },
                        activeColor = ConsoleGreen
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SignalProgressMeter(
                        label = "Reference Signal Received Quality (RSRQ)",
                        value = "$rsrq dB",
                        progress = ((rsrq + 20f) / 17f).coerceIn(0f, 1f),
                        evalText = when {
                            rsrq > -10 -> "HIGH SPECTRAL INTEGRITY"
                            rsrq > -15 -> "CONGESTED CELL CHANNEL"
                            else -> "SEVERE CO-CHANNEL SPURIOUS INTERFERENCE"
                        },
                        activeColor = CyberCyan
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().background(CyberBlack, RoundedCornerShape(8.dp)).padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("SIGNAL-TO-NOISE RATIO (SNR)", fontSize = 10.sp, color = Silver, fontFamily = FontFamily.Monospace)
                            Text("Carrier purity margin", fontSize = 8.sp, color = DarkSilver, fontFamily = FontFamily.Monospace)
                        }
                        Text("$snr dB", fontSize = 14.sp, color = if (snr > 15) ConsoleGreen else CyberCyan, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }

        item {
            Text("SURROUNDING MICROWAVE CELL SECTORS (BTS SCAN)", style = MaterialTheme.typography.labelSmall, color = Silver, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(8.dp))

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
                        Column {
                            Text(
                                text = if (isScanning) "RADAR SCANNING LIVE" else "BTS SCANNER READY",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isScanning) ConsoleGreen else OffWhite,
                                fontFamily = FontFamily.Monospace
                            )
                            Text("Active sweeping sweeps azimuth angles", fontSize = 9.sp, color = DarkSilver, fontFamily = FontFamily.Monospace)
                        }

                        Button(
                            onClick = { if (isScanning) viewModel.stopCellScan() else viewModel.startCellScan() },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isScanning) CyberCrimsonDim else ConsoleGreenDim, contentColor = if (isScanning) CyberCrimson else ConsoleGreen),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text(if (isScanning) "HALT RADAR" else "SWEEP CELL TOWER", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CellRadarScopeView(cellTowers)

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("DISCOVERED CARRIER BTS NODES:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ConsoleGreen, fontFamily = FontFamily.Monospace)
                            
                            if (cellTowers.isEmpty()) {
                                Text("No discovered sectors. Execute SWEEP scanner...", color = DarkSilver, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            } else {
                                cellTowers.forEach { tower ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(CyberBlack, RoundedCornerShape(4.dp))
                                            .padding(6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(modifier = Modifier.size(4.dp).clip(RoundedCornerShape(2.dp)).background(if (tower.isServing) ConsoleGreen else CyberCyan))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(tower.towerId, fontSize = 9.sp, color = OffWhite, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                            }
                                            Text("${tower.operator} • ${tower.cellType}", fontSize = 8.sp, color = Silver, fontFamily = FontFamily.Monospace)
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("${tower.rsrp} dBm", fontSize = 9.sp, color = if (tower.isServing) ConsoleGreen else OffWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                            Text("${tower.distanceMeters} meters", fontSize = 8.sp, color = DarkSilver, fontFamily = FontFamily.Monospace)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        item { DiagnosticHintCard(ActiveTool.CELL_DIAGNOSTICS) }
    }
}

@Composable
fun SignalProgressMeter(
    label: String,
    value: String,
    progress: Float,
    evalText: String,
    activeColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontSize = 9.sp, color = Silver, fontFamily = FontFamily.Monospace)
            Text(value, fontSize = 10.sp, color = activeColor, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            color = activeColor,
            trackColor = CyberGray,
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(evalText, fontSize = 8.sp, color = DarkSilver, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CellRadarScopeView(towers: List<CellTowerInfo>) {
    var angleOffset by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        while(true) {
            delay(40)
            angleOffset = (angleOffset + 2.5f) % 360f
        }
    }
    
    Box(
        modifier = Modifier
            .size(110.dp)
            .background(Color.Black, shape = RoundedCornerShape(55.dp))
            .border(1.dp, ConsoleGreen.copy(alpha = 0.25f), RoundedCornerShape(55.dp)),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val center = size.width / 2
            val radius = size.width / 2
            
            drawCircle(color = ConsoleGreen.copy(alpha = 0.12f), radius = radius * 0.33f, style = Stroke(1f))
            drawCircle(color = ConsoleGreen.copy(alpha = 0.12f), radius = radius * 0.66f, style = Stroke(1f))
            drawCircle(color = ConsoleGreen.copy(alpha = 0.12f), radius = radius * 0.95f, style = Stroke(1f))
            
            drawLine(color = ConsoleGreen.copy(alpha = 0.12f), start = Offset(0f, center), end = Offset(size.width, center), strokeWidth = 1f)
            drawLine(color = ConsoleGreen.copy(alpha = 0.12f), start = Offset(center, 0f), end = Offset(center, size.height), strokeWidth = 1f)
            
            val sweepRadians = Math.toRadians(angleOffset.toDouble())
            val sweepX = center + radius * Math.cos(sweepRadians).toFloat()
            val sweepY = center + radius * Math.sin(sweepRadians).toFloat()
            drawLine(
                color = ConsoleGreen.copy(alpha = 0.45f),
                start = Offset(center, center),
                end = Offset(sweepX, sweepY),
                strokeWidth = 2f
            )
            
            towers.forEach { tower ->
                val distFactor = (tower.distanceMeters / 1600f).coerceIn(0.15f, 0.85f)
                val towerRad = radius * distFactor
                val towerAngleRad = Math.toRadians(tower.bearing.toDouble())
                val tx = center + towerRad * Math.cos(towerAngleRad).toFloat()
                val ty = center + towerRad * Math.sin(towerAngleRad).toFloat()
                
                val pointColor = if (tower.isServing) ConsoleGreen else CyberCyan
                val pointRadius = if (tower.isServing) 4.5.dp.toPx() else 3.dp.toPx()
                
                drawCircle(
                    color = pointColor,
                    radius = pointRadius,
                    center = Offset(tx, ty)
                )
                
                if (tower.isServing) {
                    drawCircle(
                        color = pointColor.copy(alpha = 0.3f),
                        radius = pointRadius + (angleOffset % 20f) / 20f * 8.dp.toPx(),
                        center = Offset(tx, ty),
                        style = Stroke(1.dp.toPx())
                    )
                }
            }
        }
    }
}

@Composable
fun DiagnosticHintCard(tool: ActiveTool) {
    var expanded by remember { mutableStateOf(true) }
    
    val hintData = remember(tool) {
        when (tool) {
            ActiveTool.PING -> Triple(
                "ICMP & SOCKET TRANSMISSION MECHANICS",
                "• Syscalls: On standard Linux, Ping constructs an ICMP Echo Request (Type 8) packet, transmitting it over raw sockets.\n• Android Sandbox: Because standard user applications do not hold the raw socket permission (CAP_NET_RAW), this utility executes a wrapper around the native /system/bin/ping binary or falls back to standard TCP 3-way handshake connect calls (sockets port 80/443) which run fully inside the user-space sandbox.\n• Jitter & Latency: Real-world packet latency is measured via epoch millisecond timestamps before and after socket operations. Jitter represents the mean of absolute RTT variations.",
                "ping -c 4 -i 1.0 -s 56 <host>"
            )
            ActiveTool.PORT_SCANNER -> Triple(
                "PORT SCAN handshakes & RESPONSES",
                "• Syscalls: Scans connect to remote endpoints using standard TCP three-way handshake attempts (socket connect). \n• Android Sandbox: A rooted device can execute SYN-only half-open stealth scans (nmap -sS) via raw socket injection. Standard Android sandbox apps must perform full three-way handshakes (SYN -> SYN-ACK -> ACK -> RST/FIN) which are logged as complete connections.\n• States: \n  - OPEN: Connection completed successfully (syn-ack received)\n  - CLOSED: RST packet returned instantly by host OS kernel\n  - FILTERED: Silent packet drop by firewall (timeouts)",
                "nmap -sT -p 1-1024 <host>"
            )
            ActiveTool.SUBNET_CALC -> Triple(
                "IP SUB-NETTING MATHEMATICS",
                "• Logic: Bitwise AND operations pair IP addresses with netmasks. CIDR (Classless Inter-Domain Routing) values dictate network vs. host divisions.\n• Math:\n  - Network Address = IP Address AND Netmask\n  - Broadcast Address = IP Address OR (NOT Netmask)\n  - Host Capacity = 2^(32 - CIDR) - 2 (standard IPv4 limits network & broadcast bounds).\n• Sandbox: Independent local mathematical computation; does not trigger external socket requests.",
                "ipcalc <ip_address>/<cidr>"
            )
            ActiveTool.DNS_LOOKUP -> Triple(
                "DNS SYSTEM QUERIES & RESOLVERS",
                "• Syscalls: Executes standard libc getaddrinfo() or UDP/TCP port 53 sockets. Resolves hostname labels into IPv4 (A), IPv6 (AAAA), mail exchanges (MX), or canonical records (CNAME).\n• Android Sandbox: Handled by the OS resolver thread, which queries nameservers specified in system properties or DHCP configs.\n• TTL (Time To Live): Tells local clients how many seconds to cache DNS results before re-querying nameservers.",
                "dig ANY <domain_name> @8.8.8.8"
            )
            ActiveTool.WAKE_ON_LAN -> Triple(
                "WOL BROADCAST & MULTICAST FRAMES",
                "• Syscalls: Constructs a 102-byte Magic Packet consisting of a synchronization stream (6 bytes of 0xFF) followed by 16 repetitions of the target device's 48-bit MAC address.\n• Transmission: Dispatched as a UDP broadcast payload across port 7 or 9 to local broadcast boundaries (255.255.255.255) or unicast router routes.\n• Sandbox: Standard UDP sockets. Relies on local network profile routing to forward broadcast frames safely.",
                "wakeonlan -i 255.255.255.255 -p 9 <mac_address>"
            )
            ActiveTool.TRACEROUTE -> Triple(
                "INCREMENTAL TTL EXPIRY TELEMETRY",
                "• Syscalls: Increments the IP header TTL (Time-To-Live) starting at 1. Each consecutive router along the path decrements TTL by 1. When a router hits TTL=0, it discards the frame and fires back an ICMP Type 11 (Time Exceeded) payload.\n• Android Sandbox: Sandboxed apps cannot sniff incoming ICMP Type 11 packets on standard sockets without raw socket bindings (CAP_NET_RAW). This tool simulates the incremental TTL response hops based on real-world trace heuristics.",
                "traceroute -I <host>  # ICMP-based traceroute"
            )
            ActiveTool.WHOIS_LOOKUP -> Triple(
                "WHOIS DIRECTORY QUERIES",
                "• Syscalls: Opens a direct TCP socket connection to Registrar WHOIS servers (typically port 43) transmitting ASCII-encoded domain names.\n• Android Sandbox: Operates normally over outbound TCP sockets. Coordinates geolocation queries with public WHOIS and MaxMind/IP-API endpoints to pinpoint servers.",
                "whois <domain_name>"
            )
            ActiveTool.SPEED_TEST -> Triple(
                "SPEED TEST BENCHMARK ENGINE",
                "• Mechanics: Establishes multiple concurrent TCP/HTTP connections to close CDN endpoints, measuring raw byte transmission rate per second.\n• Jitter & Latency: Gauges ping fluctuation over loaded lines. High loaded-ping reveals bufferbloat issues in intermediate routers.\n• Sandbox: Standard HTTP/TCP sockets; throttled strictly by local device cellular/WiFi hardware caps.",
                "speedtest-cli"
            )
            ActiveTool.TRAFFIC_GENERATOR -> Triple(
                "STRESS TESTING & TRAFFIC PACKETS",
                "• Mechanics: Fires sequential UDP datagrams or TCP streams to a custom remote address/port at high rates to stress route performance and measure packet loss.\n• Sandbox: Runs in standard user-space coroutines. Maximum throughput is limited by CPU context switches and Android thread priorities compared to native kernel-level packet engines.",
                "iperf3 -c <host> -u -b 10M -t 10"
            )
            ActiveTool.BANDWIDTH_TEST -> Triple(
                "BANDWIDTH BENCHMARKING FLOWS",
                "• Mechanics: Works as an iPerf-style client/server system. The client sends bulk byte packages while the server tallies and reports total throughput per interval.\n• TCP vs UDP: TCP handles congestion control, which limits raw speeds. UDP blasts stateless frames, testing true raw medium capacity.",
                "iperf3 -c <host> -p 5201"
            )
            ActiveTool.SNMP_DISCOVERY -> Triple(
                "SNMP TELEMETRY & MIB TREE",
                "• Mechanics: Transmits UDP packets to agent port 161 with request-PDU formats. Pulls device statistics using Object Identifiers (OIDs) within Management Information Base (MIB) schemas.\n• Security: Uses community string filters (default 'public'). SNMPv3 adds authentication and crypto.",
                "snmpwalk -v 2c -c public <host> 1.3.6.1.2.1"
            )
            ActiveTool.WAN_KILLER -> Triple(
                "CONGESTION TESTING & WAN BOTTLENECKS",
                "• Mechanics: Generates steady stateless UDP packet floods to flood target ports, simulating congestion events to verify buffer sizes and router rate limits.\n• Sandbox: Operates fully on user-space background threads. Limited by scheduling priority constraints to prevent complete system lockups.",
                "udpflood <host> -p 9999 -s 1024"
            )
            ActiveTool.MAC_SCANNER -> Triple(
                "ARP TABLES & NEIGHBOR DISCOVERY",
                "• Mechanics: Resolves IP addresses to MAC addresses on local subnets via ARP (Address Resolution Protocol).\n• Android Sandbox: Android 10+ restricts access to the system ARP cache table (/proc/net/arp) for privacy. This utility queries nearby devices or simulates state mappings utilizing valid OUI databases for vendor identification.",
                "ip neighbor show  # Standard Linux ARP table query"
            )
            ActiveTool.WIFI_DIAGNOSTICS -> Triple(
                "WI-FI SPECTRUM & PCAP DECRYPTION",
                "• RSSI: Received Signal Strength Indicator measured in dBm (decibel-milliwatts). 0 to -50 is excellent, below -80 indicates severe packet loss.\n• Android Sandbox: WiFi details (BSSID, SSID, frequency) require ACCESS_FINE_LOCATION and hardware GPS to be toggled on. PCAP sniffer is captured from local adapter wlan0 utilizing real-time sockets fallback.",
                "tcpdump -i wlan0 -vvv"
            )
            ActiveTool.CELL_DIAGNOSTICS -> Triple(
                "CELLULAR TELEMETRY & BTS SIGNALS",
                "• Signal RSSI/RSRP: Reference Signal Received Power measures absolute LTE strength. Below -115 dBm is weak cell reception.\n• Provider APIs: Polled from TelephonyManager. Requires ACCESS_FINE_LOCATION permission to query LAC (Location Area Code) and CID (Cell Identity). Serving cells map directly to nearest BTS (Base Transceiver Station).",
                "dumpsys telephony.registry"
            )
            else -> null
        }
    }
    
    if (hintData == null) return
    
    val (title, explanation, terminalCmd) = hintData
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
            .border(1.dp, ConsoleGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberDark)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Technical Info",
                        tint = ConsoleGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                        color = ConsoleGreen,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle Details",
                    tint = Silver,
                    modifier = Modifier.size(16.dp)
                )
            }
            
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = explanation,
                        style = MaterialTheme.typography.bodySmall,
                        color = OffWhite,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "REAL-WORLD TERMINAL EQUIVALENT:",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black, RoundedCornerShape(6.dp))
                            .border(1.dp, CyberGray, RoundedCornerShape(6.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = terminalCmd,
                            style = MaterialTheme.typography.labelSmall,
                            color = ConsoleGreen,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

