package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Site
import com.example.ui.ActiveTool
import com.example.ui.NetOpsTab
import com.example.ui.NetOpsViewModel
import com.example.ui.theme.*

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

        // Network Context Details Card (DHCP Lease / Gateway Info)
        item {
            Text(
                text = "LOCAL DIAGNOSTIC STREAM",
                style = MaterialTheme.typography.labelMedium,
                color = Silver,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                            // Glowing green node LED indicator
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

                    // Context specs table
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

        // Pinned Quick Utility Widgets
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
                // Quick loopback ping card
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

                // Quick Terminal Command trigger
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

        // Active site devices status preview
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
            items(devices.size) { index ->
                val device = devices[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CyberGray, RoundedCornerShape(12.dp))
                        .clickable {
                            // Quick action: send host to ping
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
