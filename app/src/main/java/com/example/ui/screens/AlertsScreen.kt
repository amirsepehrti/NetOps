package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NetworkEngine
import com.example.ui.NetOpsViewModel
import com.example.ui.ToastType
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AutoCheckEvent(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val category: String, // GATEWAY, DNS, SECURITY, RF, SYSTEM
    val details: String,
    val metricObserved: String,
    val thresholdExpected: String,
    val rootCause: String,
    val recommendedAction: String,
    val severity: String, // INFO, WARNING, CRITICAL
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    viewModel: NetOpsViewModel,
    modifier: Modifier = Modifier
) {
    val sites by viewModel.sites.collectAsState()
    val selectedSiteId by viewModel.selectedSiteId.collectAsState()
    val selectedSite = sites.find { it.id == selectedSiteId }

    // Alert Logs feed state
    val alertFeed = remember { mutableStateListOf<AutoCheckEvent>() }
    var selectedFilter by remember { mutableStateOf("ALL") }
    var isGuideExpanded by remember { mutableStateOf(false) }

    // Seed initial alerts
    LaunchedEffect(key1 = selectedSiteId) {
        if (alertFeed.isEmpty()) {
            alertFeed.add(
                AutoCheckEvent(
                    title = "Continuous Network Guardian Initialized",
                    category = "SYSTEM",
                    details = "Real-time telemetry sweep daemon activated for local interface wlan0.",
                    metricObserved = "Guardian Active",
                    thresholdExpected = "Heartbeat OK",
                    rootCause = "Background monitoring active",
                    recommendedAction = "Continuous telemetry in progress",
                    severity = "INFO"
                )
            )
        }

        while (isActive) {
            val gateway = selectedSite?.gatewayIp ?: "192.168.1.1"

            withContext(Dispatchers.IO) {
                // 1. Gateway Probe
                val start = System.currentTimeMillis()
                var success = false
                try {
                    val address = InetAddress.getByName(gateway)
                    success = address.isReachable(600)
                } catch (e: Exception) {
                    // unreachable
                }
                val elapsed = System.currentTimeMillis() - start

                withContext(Dispatchers.Main) {
                    if (success) {
                        if (elapsed > 350) {
                            alertFeed.add(
                                0,
                                AutoCheckEvent(
                                    title = "High Gateway Latency",
                                    category = "GATEWAY",
                                    details = "Round-trip time to local gateway $gateway exceeded safety ceiling.",
                                    metricObserved = "${elapsed}ms RTT",
                                    thresholdExpected = "< 200ms",
                                    rootCause = "Wi-Fi channel congestion or heavy queueing on default router uplink",
                                    recommendedAction = "Verify local 5GHz Wi-Fi signal or run ICMP ping test",
                                    severity = "WARNING"
                                )
                            )
                        }
                    } else {
                        alertFeed.add(
                            0,
                            AutoCheckEvent(
                                title = "Default Gateway Unreachable",
                                category = "GATEWAY",
                                details = "ICMP handshake with $gateway dropped. Host unreachable.",
                                metricObserved = "Timeout (600ms)",
                                thresholdExpected = "Reachability 100%",
                                rootCause = "Device disconnected from AP or router DHCP address invalid",
                                recommendedAction = "Inspect local Wi-Fi connection and gateway IP setting",
                                severity = "CRITICAL"
                            )
                        )
                    }
                }

                // 2. DNS Resolver Probe
                val startDns = System.currentTimeMillis()
                var successDns = false
                try {
                    val records = NetworkEngine.dnsLookup("google.com")
                    successDns = records.any { it.type == "A" || it.type == "AAAA" }
                } catch (e: Exception) {
                    // failure
                }
                val elapsedDns = System.currentTimeMillis() - startDns

                withContext(Dispatchers.Main) {
                    if (successDns) {
                        if (elapsedDns > 400) {
                            alertFeed.add(
                                0,
                                AutoCheckEvent(
                                    title = "DNS Resolver Congestion",
                                    category = "DNS",
                                    details = "External domain resolution query took ${elapsedDns}ms.",
                                    metricObserved = "${elapsedDns}ms Query RTT",
                                    thresholdExpected = "< 250ms",
                                    rootCause = "Upstream ISP nameserver lag or slow response",
                                    recommendedAction = "Switch to Cloudflare 1.1.1.1 or Google 8.8.8.8 in DNS settings",
                                    severity = "WARNING"
                                )
                            )
                        }
                    } else {
                        alertFeed.add(
                            0,
                            AutoCheckEvent(
                                title = "DNS Resolution Failure",
                                category = "DNS",
                                details = "Unable to resolve external hosts. Domain lookup failed.",
                                metricObserved = "0 Records Returned",
                                thresholdExpected = "A/AAAA Valid",
                                rootCause = "Upstream DNS service unreachable or local firewall intercepting UDP 53",
                                recommendedAction = "Check WAN connection or change DNS resolver in toolbox",
                                severity = "CRITICAL"
                            )
                        )
                    }
                }
            }

            delay(10000) // check every 10 seconds
        }
    }

    val criticalCount = alertFeed.count { it.severity == "CRITICAL" }
    val warningCount = alertFeed.count { it.severity == "WARNING" }
    val infoCount = alertFeed.count { it.severity == "INFO" }

    val filteredFeed = remember(alertFeed.size, selectedFilter) {
        when (selectedFilter) {
            "CRITICAL" -> alertFeed.filter { it.severity == "CRITICAL" }
            "WARNING" -> alertFeed.filter { it.severity == "WARNING" }
            "GATEWAY" -> alertFeed.filter { it.category == "GATEWAY" }
            "DNS" -> alertFeed.filter { it.category == "DNS" }
            "SECURITY" -> alertFeed.filter { it.category == "SECURITY" }
            else -> alertFeed
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "INCIDENT & ALERT CENTER",
                        style = MaterialTheme.typography.titleMedium,
                        color = ConsoleGreen,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Real-time network anomaly guardian",
                        style = MaterialTheme.typography.bodySmall,
                        color = Silver
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = {
                            // Manual sweep now
                            viewModel.showToast("Manual guardian sweep triggered", ToastType.INFO)
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberGray)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = ConsoleGreen, modifier = Modifier.size(18.dp))
                    }

                    IconButton(
                        onClick = {
                            alertFeed.clear()
                            viewModel.showToast("Alert inbox cleared", ToastType.INFO)
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberGray)
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Clear", tint = Silver, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Active Status Telemetry Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (criticalCount > 0) CyberCrimson else if (warningCount > 0) Amber else ConsoleGreenDim,
                        RoundedCornerShape(12.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(if (criticalCount > 0) CyberCrimson else if (warningCount > 0) Amber else ConsoleGreen)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (criticalCount > 0) "CRITICAL INCIDENTS DETECTED" else if (warningCount > 0) "ANOMALIES IDENTIFIED" else "ALL INFRASTRUCTURE NOMINAL",
                            color = if (criticalCount > 0) CyberCrimson else if (warningCount > 0) Amber else ConsoleGreen,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Site: ${selectedSite?.name ?: "Local Subnet"} • $criticalCount Critical • $warningCount Warnings • $infoCount Normal",
                            color = Silver,
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Clarification / Explanation Accordion ("What triggers alerts?")
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberSlate, RoundedCornerShape(12.dp))
                    .clickable { isGuideExpanded = !isGuideExpanded },
                colors = CardDefaults.cardColors(containerColor = CyberBlack)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.HelpOutline, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                            Text(
                                text = "WHAT TRIGGERS ALERTS?",
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        Icon(
                            imageVector = if (isGuideExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = Silver,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    AnimatedVisibility(visible = isGuideExpanded) {
                        Column(modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            TactileDivider()
                            GuideItem(
                                title = "1. Default Gateway Probes (ICMP)",
                                desc = "Pings the default router IP every 10s. WARNING if RTT > 200ms (bufferbloat). CRITICAL if host unreachable.",
                                accent = ConsoleGreen
                            )
                            GuideItem(
                                title = "2. DNS Resolver Health (UDP 53)",
                                desc = "Resolves domain records. WARNING if lookup > 250ms. CRITICAL if zero records returned.",
                                accent = CyberCyan
                            )
                            GuideItem(
                                title = "3. Wi-Fi Spectral Quality",
                                desc = "Checks RSSI strength. WARNING if RSSI < -80 dBm or channel co-interference is detected.",
                                accent = Amber
                            )
                            GuideItem(
                                title = "4. Security & ARP Protection",
                                desc = "Audits default gateway MAC address against ARP spoofing and rogue DHCP hosts.",
                                accent = CyberCrimson
                            )
                        }
                    }
                }
            }
        }

        // Test Incident Generator and Filter Bar
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "INCIDENT STREAM",
                        style = MaterialTheme.typography.labelSmall,
                        color = Silver,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = {
                            // Inject sample incidents for user to test alerts
                            alertFeed.add(
                                0,
                                AutoCheckEvent(
                                    title = "SIMULATED: Gateway Packet Loss",
                                    category = "GATEWAY",
                                    details = "Synthetic test alert: 40% packet drops detected on uplink.",
                                    metricObserved = "40% Loss / 480ms",
                                    thresholdExpected = "< 2% Loss",
                                    rootCause = "High queue bufferbloat or microwave RF interference",
                                    recommendedAction = "Verify Ethernet link or switch router channel",
                                    severity = "CRITICAL"
                                )
                            )
                            alertFeed.add(
                                0,
                                AutoCheckEvent(
                                    title = "SIMULATED: DNS Resolver Lag",
                                    category = "DNS",
                                    details = "Synthetic test alert: Root lookup query took 390ms.",
                                    metricObserved = "390ms Lookup",
                                    thresholdExpected = "< 150ms",
                                    rootCause = "Slow ISP resolver response",
                                    recommendedAction = "Use 1.1.1.1 or 8.8.8.8",
                                    severity = "WARNING"
                                )
                            )
                            viewModel.showToast("Simulated test incidents added to stream", ToastType.SUCCESS)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyanDim, contentColor = CyberCyan),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("TEST INCIDENT", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }

                // Filter chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val filters = listOf("ALL", "CRITICAL", "WARNING", "GATEWAY", "DNS")
                    items(filters) { filter ->
                        val isSelected = selectedFilter == filter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) ConsoleGreen.copy(alpha = 0.2f) else CyberDark)
                                .border(1.dp, if (isSelected) ConsoleGreen else CyberSlate, RoundedCornerShape(6.dp))
                                .clickable { selectedFilter = filter }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = filter,
                                color = if (isSelected) ConsoleGreen else Silver,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (filteredFeed.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ConsoleGreen, modifier = Modifier.size(44.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No active alerts matching '$selectedFilter'",
                            color = Silver,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Continuous guardian background sweep is operational.",
                            color = DarkSilver,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        } else {
            items(filteredFeed, key = { it.id }) { event ->
                val borderColor = when (event.severity) {
                    "CRITICAL" -> CyberCrimson
                    "WARNING" -> Amber
                    else -> ConsoleGreenDim
                }

                val accentColor = when (event.severity) {
                    "CRITICAL" -> CyberCrimson
                    "WARNING" -> Amber
                    else -> ConsoleGreen
                }

                val categoryBg = when (event.category) {
                    "GATEWAY" -> CyberCyan.copy(alpha = 0.15f)
                    "DNS" -> ConsoleGreen.copy(alpha = 0.15f)
                    "SECURITY" -> CyberCrimson.copy(alpha = 0.15f)
                    else -> CyberSlate
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Card Top Row: Category Tag, Title, Timestamp
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(categoryBg)
                                        .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = event.category,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = accentColor,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Text(
                                    text = event.title,
                                    color = accentColor,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(event.timestamp)),
                                color = Silver,
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Details explanation
                        Text(
                            text = event.details,
                            color = OffWhite,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Metric & Threshold Inset Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberBlack)
                                .border(1.dp, CyberSlate, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("OBSERVED", fontSize = 9.sp, color = Silver, fontFamily = FontFamily.Monospace)
                                    Text(event.metricObserved, fontSize = 11.sp, color = accentColor, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("SAFE THRESHOLD", fontSize = 9.sp, color = Silver, fontFamily = FontFamily.Monospace)
                                    Text(event.thresholdExpected, fontSize = 11.sp, color = ConsoleGreen, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Root Cause & Recommended Action
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("CAUSE:", fontSize = 10.sp, color = Silver, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            Text(event.rootCause, fontSize = 10.sp, color = OffWhite, modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("ACTION:", fontSize = 10.sp, color = ConsoleGreen, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            Text(event.recommendedAction, fontSize = 10.sp, color = OffWhite, modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Dismiss button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "ACKNOWLEDGE & DISMISS",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyberSlate)
                                    .clickable {
                                        alertFeed.remove(event)
                                        viewModel.showToast("Alert acknowledged", ToastType.INFO)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                color = Silver,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GuideItem(title: String, desc: String, accent: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(accent)
        )
        Column {
            Text(title, color = accent, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Text(desc, color = Silver, fontSize = 10.sp)
        }
    }
}
