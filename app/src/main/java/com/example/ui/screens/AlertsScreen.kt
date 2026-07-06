package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NetworkEngine
import com.example.ui.NetOpsViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress

data class AutoCheckEvent(
    val title: String,
    val details: String,
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

    // Run active background sweeps while this screen is active!
    LaunchedEffect(key1 = selectedSiteId) {
        // Seed default alerts on startup
        alertFeed.clear()
        alertFeed.add(AutoCheckEvent("Monitor Stream Initialized", "Passive background telemetry sweep activated.", "INFO"))
        
        while (isActive) {
            val gateway = selectedSite?.gatewayIp ?: "192.168.1.1"
            
            withContext(Dispatchers.IO) {
                // Check Gateway Reachability
                val start = System.currentTimeMillis()
                var success = false
                try {
                    val address = InetAddress.getByName(gateway)
                    success = address.isReachable(500)
                } catch (e: Exception) {
                    // ignore
                }
                val elapsed = System.currentTimeMillis() - start

                withContext(Dispatchers.Main) {
                    if (success) {
                        if (elapsed > 400) {
                            alertFeed.add(0, AutoCheckEvent("High Gateway Latency", "Ping response to $gateway took ${elapsed}ms (above threshold).", "WARNING"))
                        } else {
                            // Only log INFO occasionally
                            if (Math.random() < 0.2) {
                                alertFeed.add(0, AutoCheckEvent("Gateway Diagnostics OK", "Direct connection response latency to $gateway is stable at ${elapsed}ms.", "INFO"))
                            }
                        }
                    } else {
                        alertFeed.add(0, AutoCheckEvent("Gateway Unreachable", "Failed to ping gateway at $gateway. Check local interfaces or physical cable link.", "CRITICAL"))
                    }
                }

                // Check DNS Latency (using google.com or similar)
                val startDns = System.currentTimeMillis()
                var successDns = false
                try {
                    val records = NetworkEngine.dnsLookup("google.com")
                    successDns = records.any { it.type == "A" || it.type == "AAAA" }
                } catch (e: Exception) {
                    // ignore
                }
                val elapsedDns = System.currentTimeMillis() - startDns

                withContext(Dispatchers.Main) {
                    if (successDns) {
                        if (elapsedDns > 350) {
                            alertFeed.add(0, AutoCheckEvent("Slow DNS Resolver Response", "Domain resolution for 'google.com' took ${elapsedDns}ms.", "WARNING"))
                        }
                    } else {
                        alertFeed.add(0, AutoCheckEvent("DNS Resolution Fault", "Failure resolving external domains. Local resolver may be down or unassigned.", "CRITICAL"))
                    }
                }
            }

            delay(7000) // check every 7 seconds
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "UNIFIED ALERT INBOX",
                style = MaterialTheme.typography.titleLarge,
                color = ConsoleGreen,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = "Live gateway checks and local link telemetry",
                style = MaterialTheme.typography.bodyMedium,
                color = Silver,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
        }

        // Active Status LED panel
        item {
            val criticalCount = alertFeed.count { it.severity == "CRITICAL" }
            val warningCount = alertFeed.count { it.severity == "WARNING" }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (criticalCount > 0) CyberCrimson else if (warningCount > 0) Color(0xFFF59E0B) else ConsoleGreenDim,
                        RoundedCornerShape(12.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = CyberDark)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (criticalCount > 0) CyberCrimson else if (warningCount > 0) Color(0xFFF59E0B) else ConsoleGreen)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (criticalCount > 0) "CRITICAL ALERTS DETECTED" else if (warningCount > 0) "WARNING CONDITIONS IDENTIFIED" else "ALL SYSTEMS OPERATIONAL",
                            color = if (criticalCount > 0) CyberCrimson else if (warningCount > 0) Color(0xFFF59E0B) else ConsoleGreen,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Site Sweep: ${selectedSite?.name ?: "No Profile"}. Status based on background ICMP metrics.",
                            color = Silver,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        if (alertFeed.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ConsoleGreen)
                }
            }
        } else {
            items(alertFeed) { event ->
                val cardColor = when (event.severity) {
                    "CRITICAL" -> CyberCrimsonDim
                    "WARNING" -> Color(0xFF78350F)
                    else -> CyberGray
                }
                
                val borderColor = when (event.severity) {
                    "CRITICAL" -> CyberCrimson
                    "WARNING" -> Color(0xFFF59E0B)
                    else -> CyberSlate
                }

                val textColor = when (event.severity) {
                    "CRITICAL" -> CyberCrimson
                    "WARNING" -> Color(0xFFFBBF24)
                    else -> ConsoleGreen
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, borderColor, RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(textColor)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = event.title.uppercase(),
                                    color = textColor,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date(event.timestamp)),
                                    color = Silver,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = event.details,
                                color = OffWhite,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}
