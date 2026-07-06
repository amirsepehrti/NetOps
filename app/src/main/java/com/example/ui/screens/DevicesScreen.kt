package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Device
import com.example.data.Site
import com.example.ui.NetOpsViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesScreen(
    viewModel: NetOpsViewModel,
    modifier: Modifier = Modifier
) {
    val sites by viewModel.sites.collectAsState()
    val selectedSiteId by viewModel.selectedSiteId.collectAsState()
    val devices by viewModel.selectedSiteDevices.collectAsState()

    var showAddSiteDialog by remember { mutableStateOf(false) }
    var showAddDeviceDialog by remember { mutableStateOf(false) }

    // Dialog input states
    var siteName by remember { mutableStateOf("") }
    var siteGatewayIp by remember { mutableStateOf("") }
    var siteSubnet by remember { mutableStateOf("24") }
    var siteVpnConfig by remember { mutableStateOf("") }
    var siteNotes by remember { mutableStateOf("") }

    var devName by remember { mutableStateOf("") }
    var devIpAddress by remember { mutableStateOf("") }
    var devType by remember { mutableStateOf("Router") }
    var devVendor by remember { mutableStateOf("MikroTik") }
    var devNotes by remember { mutableStateOf("") }

    val selectedSite = sites.find { it.id == selectedSiteId }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section header for network profiles
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SITES & PROFILES",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = ConsoleGreen,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Manage your infrastructure environments",
                        style = MaterialTheme.typography.bodySmall,
                        color = Silver
                    )
                }

                IconButton(
                    onClick = { showAddSiteDialog = true },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberGray)
                        .testTag("add_site_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Site", tint = ConsoleGreen)
                }
            }
        }

        // Horizontal listing of sites
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sites.forEach { site ->
                    val isSelected = site.id == selectedSiteId
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, if (isSelected) ConsoleGreen else CyberGray, RoundedCornerShape(8.dp))
                            .clickable { viewModel.selectSite(site.id) },
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) CyberDark else CyberBlack)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = site.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) ConsoleGreen else OffWhite,
                                maxLines = 1,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Gateway: ${site.gatewayIp}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Silver
                            )
                        }
                    }
                }
            }
        }

        // Selected profile detailed specs
        selectedSite?.let { site ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CyberGray, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberDark)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PROFILE META INFO",
                                style = MaterialTheme.typography.labelSmall,
                                color = Silver,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )

                            Text(
                                text = "DELETE SITE",
                                modifier = Modifier
                                    .clickable { viewModel.deleteSite(site.id) }
                                    .padding(4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = CyberCrimson,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Subnet Prefix: /${site.subnetMask}", style = MaterialTheme.typography.bodySmall, color = OffWhite, fontFamily = FontFamily.Monospace)
                        Text("VPN Connect ID: ${site.vpnConfig.ifEmpty { "None mapped" }}", style = MaterialTheme.typography.bodySmall, color = OffWhite, fontFamily = FontFamily.Monospace)
                        if (site.notes.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Notes: ${site.notes}", style = MaterialTheme.typography.bodySmall, color = Silver)
                        }
                    }
                }
            }
        }

        // Section header for active site recorded devices
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "DEVICES INVENTORY",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = CyberCyan,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Recorded infrastructure nodes",
                        style = MaterialTheme.typography.bodySmall,
                        color = Silver
                    )
                }

                if (selectedSiteId != null) {
                    IconButton(
                        onClick = { showAddDeviceDialog = true },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberGray)
                            .testTag("add_device_fab")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Device", tint = CyberCyan)
                    }
                }
            }
        }

        if (devices.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.DevicesOther, contentDescription = null, tint = CyberGray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No recorded hardware profiles found.\nTap '+' to append routers, switches, or hypervisors.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Silver,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        } else {
            items(devices) { dev ->
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyberGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (dev.deviceType) {
                                        "Router" -> Icons.Default.Router
                                        "Switch" -> Icons.Default.SettingsInputHdmi
                                        "Server" -> Icons.Default.Dns
                                        "VM" -> Icons.Default.Computer
                                        else -> Icons.Default.DeveloperBoard
                                    },
                                    contentDescription = null,
                                    tint = ConsoleGreen
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(dev.name, fontWeight = FontWeight.Bold, color = OffWhite, fontFamily = FontFamily.Monospace)
                                Text("${dev.vendor} • IP: ${dev.ipAddress}", style = MaterialTheme.typography.bodySmall, color = Silver, fontFamily = FontFamily.Monospace)
                                if (dev.notes.isNotEmpty()) {
                                    Text(dev.notes, style = MaterialTheme.typography.labelSmall, color = DarkSilver)
                                }
                            }
                        }

                        IconButton(
                            onClick = { viewModel.deleteDevice(dev.id) },
                            modifier = Modifier.testTag("delete_device_${dev.id}")
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete device", tint = CyberCrimson)
                        }
                    }
                }
            }
        }
    }

    // Add Site Dialog
    if (showAddSiteDialog) {
        AlertDialog(
            onDismissRequest = { showAddSiteDialog = false },
            title = { Text("ADD NETWORK SITE", fontFamily = FontFamily.Monospace, color = ConsoleGreen) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = siteName,
                        onValueChange = { siteName = it },
                        label = { Text("Site / Profile Name") },
                        modifier = Modifier.fillMaxWidth().testTag("add_site_name_input")
                    )
                    OutlinedTextField(
                        value = siteGatewayIp,
                        onValueChange = { siteGatewayIp = it },
                        label = { Text("Default Gateway IP Address") },
                        modifier = Modifier.fillMaxWidth().testTag("add_site_gateway_input")
                    )
                    OutlinedTextField(
                        value = siteSubnet,
                        onValueChange = { siteSubnet = it },
                        label = { Text("Subnet CIDR Prefix (e.g. 24)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = siteVpnConfig,
                        onValueChange = { siteVpnConfig = it },
                        label = { Text("VPN Connection Shortcut Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = siteNotes,
                        onValueChange = { siteNotes = it },
                        label = { Text("Custom Notes") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (siteName.isNotEmpty()) {
                            viewModel.addSite(siteName, siteGatewayIp, siteSubnet, siteVpnConfig, siteNotes)
                            showAddSiteDialog = false
                            // Reset inputs
                            siteName = ""
                            siteGatewayIp = ""
                            siteSubnet = "24"
                            siteVpnConfig = ""
                            siteNotes = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ConsoleGreen, contentColor = CyberBlack),
                    modifier = Modifier.testTag("add_site_confirm_button")
                ) {
                    Text("ADD PROFILE")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSiteDialog = false }) {
                    Text("CANCEL")
                }
            },
            containerColor = CyberDark
        )
    }

    // Add Device Dialog
    if (showAddDeviceDialog) {
        val typeOptions = listOf("Router", "Switch", "Server", "VM", "Other")
        val vendorOptions = listOf("MikroTik", "Cisco", "Proxmox", "Generic")

        AlertDialog(
            onDismissRequest = { showAddDeviceDialog = false },
            title = { Text("ADD NETWORK DEVICE", fontFamily = FontFamily.Monospace, color = CyberCyan) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = devName,
                        onValueChange = { devName = it },
                        label = { Text("Device Name") },
                        modifier = Modifier.fillMaxWidth().testTag("add_device_name_input")
                    )
                    OutlinedTextField(
                        value = devIpAddress,
                        onValueChange = { devIpAddress = it },
                        label = { Text("IPv4 Address") },
                        modifier = Modifier.fillMaxWidth().testTag("add_device_ip_input")
                    )

                    // Device type picker
                    Column {
                        Text("Device Type", style = MaterialTheme.typography.bodySmall, color = Silver)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            typeOptions.forEach { type ->
                                FilterChip(
                                    selected = devType == type,
                                    onClick = { devType = type },
                                    label = { Text(type, style = MaterialTheme.typography.labelSmall) },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyberCyan, selectedLabelColor = CyberBlack)
                                )
                            }
                        }
                    }

                    // Vendor picker
                    Column {
                        Text("Hardware Vendor", style = MaterialTheme.typography.bodySmall, color = Silver)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            vendorOptions.forEach { vendor ->
                                FilterChip(
                                    selected = devVendor == vendor,
                                    onClick = { devVendor = vendor },
                                    label = { Text(vendor, style = MaterialTheme.typography.labelSmall) },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = ConsoleGreen, selectedLabelColor = CyberBlack)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = devNotes,
                        onValueChange = { devNotes = it },
                        label = { Text("Custom Specs Notes") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val activeSiteId = selectedSiteId
                        if (devName.isNotEmpty() && activeSiteId != null) {
                            viewModel.addDevice(activeSiteId, devName, devIpAddress, devType, devVendor, devNotes)
                            showAddDeviceDialog = false
                            // Reset inputs
                            devName = ""
                            devIpAddress = ""
                            devType = "Router"
                            devVendor = "MikroTik"
                            devNotes = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = CyberBlack),
                    modifier = Modifier.testTag("add_device_confirm_button")
                ) {
                    Text("RECORD DEVICE")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDeviceDialog = false }) {
                    Text("CANCEL")
                }
            },
            containerColor = CyberDark
        )
    }
}
