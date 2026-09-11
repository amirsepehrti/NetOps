package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.NetOpsTab
import com.example.ui.NetOpsViewModel
import com.example.ui.ToastType
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: NetOpsViewModel = viewModel()
            val activeTheme by viewModel.activeTheme.collectAsState()
            val fontScale by viewModel.fontScale.collectAsState()
            val activeToast by viewModel.activeToast.collectAsState()
            val activePopup by viewModel.activePopup.collectAsState()
            val hasPermissions by viewModel.hasPermissions.collectAsState()
            val isCustomizeDashboardOpen by viewModel.isCustomizeDashboardOpen.collectAsState()

            val context = LocalContext.current

            // 1. Runtime Permissions Setup & Startup Launch
            val permissionsToRequest = remember {
                val list = mutableListOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    list.add(Manifest.permission.NEARBY_WIFI_DEVICES)
                }
                list.toTypedArray()
            }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { perms ->
                val fine = perms[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                val coarse = perms[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
                val phone = perms[Manifest.permission.READ_PHONE_STATE] ?: false
                val anyGranted = fine || coarse || phone
                viewModel.updatePermissionStatus(anyGranted)
                if (anyGranted) {
                    viewModel.showToast("Hardware telemetry permissions granted!", ToastType.SUCCESS)
                } else {
                    viewModel.showToast("Location permissions denied. Wi-Fi & Tower scans may be limited.", ToastType.WARNING)
                }
            }

            // Auto-trigger permission dialog on app launch
            LaunchedEffect(Unit) {
                val needsRequest = permissionsToRequest.any {
                    ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
                }
                if (needsRequest) {
                    permissionLauncher.launch(permissionsToRequest)
                } else {
                    viewModel.updatePermissionStatus(true)
                }
            }

            // 2. Global Font Scaling & Typography: All UI elements seamlessly scale proportionally!
            val fontFamilyOption by viewModel.fontFamilyOption.collectAsState()
            val chosenFontFamily = when(fontFamilyOption) {
                "sans_serif" -> androidx.compose.ui.text.font.FontFamily.SansSerif
                "serif" -> androidx.compose.ui.text.font.FontFamily.Serif
                else -> androidx.compose.ui.text.font.FontFamily.Monospace
            }

            val currentDensity = LocalDensity.current
            val customDensity = remember(currentDensity, fontScale) {
                Density(
                    density = currentDensity.density,
                    fontScale = currentDensity.fontScale * fontScale
                )
            }

            CompositionLocalProvider(LocalDensity provides customDensity) {
                key(activeTheme, fontFamilyOption) {
                    MyApplicationTheme(fontFamily = chosenFontFamily) {
                        val currentTab by viewModel.currentTab.collectAsState()
                        val navBarPosition by viewModel.navBarPosition.collectAsState()
                        val bottomBarStyle by viewModel.bottomBarStyle.collectAsState()
                        val bottomBarLabelVisibility by viewModel.bottomBarLabelVisibility.collectAsState()
                        val bottomBarDensity by viewModel.bottomBarDensity.collectAsState()
                        val isEditProfileOpen by viewModel.isEditProfileOpen.collectAsState()

                        Box(modifier = Modifier.fillMaxSize().background(CyberBlack)) {
                            Scaffold(
                                modifier = Modifier.fillMaxSize().background(CyberBlack),
                                topBar = {
                                    if (navBarPosition == "TOP") {
                                        ScrollableBottomDock(
                                            currentTab = currentTab,
                                            bottomBarStyle = bottomBarStyle,
                                            labelVisibility = bottomBarLabelVisibility,
                                            density = bottomBarDensity,
                                            isTop = true,
                                            onTabSelected = { viewModel.selectTab(it) }
                                        )
                                    }
                                },
                                bottomBar = {
                                    if (navBarPosition == "BOTTOM") {
                                        ScrollableBottomDock(
                                            currentTab = currentTab,
                                            bottomBarStyle = bottomBarStyle,
                                            labelVisibility = bottomBarLabelVisibility,
                                            density = bottomBarDensity,
                                            isTop = false,
                                            onTabSelected = { viewModel.selectTab(it) }
                                        )
                                    }
                                }
                            ) { innerPadding ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                ) {
                                    // Missing Permission Banner (Quick 1-click access without manual settings)
                                    if (!hasPermissions) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 6.dp),
                                            colors = CardDefaults.cardColors(containerColor = Amber.copy(alpha = 0.15f)),
                                            border = BorderStroke(1.dp, Amber),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    modifier = Modifier.weight(1f),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        Icons.Default.Security,
                                                        contentDescription = "Permission Alert",
                                                        tint = Amber,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "Location/Phone permissions needed for Wi-Fi & Tower scans.",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = OffWhite
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Button(
                                                    onClick = { permissionLauncher.launch(permissionsToRequest) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = Amber),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                    shape = RoundedCornerShape(6.dp)
                                                ) {
                                                    Text("GRANT", color = CyberBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                                }
                                            }
                                        }
                                    }

                                    Box(modifier = Modifier.weight(1f)) {
                                        Crossfade(
                                            targetState = currentTab,
                                            animationSpec = androidx.compose.animation.core.tween(150),
                                            label = "TabCrossfade"
                                        ) { tab ->
                                            when (tab) {
                                                NetOpsTab.DASHBOARD -> DashboardScreen(viewModel)
                                                NetOpsTab.TOOLBOX -> ToolboxScreen(viewModel)
                                                NetOpsTab.DEVICES -> DevicesScreen(viewModel)
                                                NetOpsTab.ALERTS -> AlertsScreen(viewModel)
                                                NetOpsTab.TERMINAL -> TerminalScreen(viewModel)
                                                NetOpsTab.SETTINGS -> SettingsScreen(viewModel)
                                            }
                                        }
                                    }
                                }
                            }

                            if (isEditProfileOpen) {
                                com.example.ui.dialogs.OperatorProfileDialog(
                                    viewModel = viewModel,
                                    onDismiss = { viewModel.setEditProfileOpen(false) }
                                )
                            }

                            // 3. Cyberpunk Animated Toast HUD
                            AnimatedVisibility(
                                visible = activeToast != null,
                                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .statusBarsPadding()
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                    .zIndex(100f)
                            ) {
                                activeToast?.let { toast ->
                                    val (tintColor, iconVector) = when (toast.type) {
                                        ToastType.SUCCESS -> ConsoleGreen to Icons.Default.CheckCircle
                                        ToastType.ERROR -> CyberCrimson to Icons.Default.Error
                                        ToastType.WARNING -> Amber to Icons.Default.Warning
                                        ToastType.INFO -> CyberCyan to Icons.Default.Info
                                    }

                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = CyberDark.copy(alpha = 0.96f)),
                                        border = BorderStroke(1.5.dp, tintColor),
                                        shape = RoundedCornerShape(12.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.dismissToast() }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Icon(
                                                imageVector = iconVector,
                                                contentDescription = null,
                                                tint = tintColor,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Text(
                                                text = toast.message,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = OffWhite,
                                                fontWeight = FontWeight.SemiBold,
                                                fontFamily = FontFamily.Monospace,
                                                modifier = Modifier.weight(1f)
                                            )
                                            IconButton(
                                                onClick = { viewModel.dismissToast() },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Close",
                                                    tint = DarkSilver,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 4. Modal Popup Alert Dialog
                            if (activePopup != null) {
                                val popup = activePopup!!
                                val (tintColor, iconVector) = when (popup.type) {
                                    ToastType.SUCCESS -> ConsoleGreen to Icons.Default.CheckCircle
                                    ToastType.ERROR -> CyberCrimson to Icons.Default.Error
                                    ToastType.WARNING -> Amber to Icons.Default.Warning
                                    ToastType.INFO -> CyberCyan to Icons.Default.Info
                                }

                                AlertDialog(
                                    onDismissRequest = { viewModel.dismissPopup() },
                                    icon = {
                                        Icon(imageVector = iconVector, contentDescription = null, tint = tintColor, modifier = Modifier.size(36.dp))
                                    },
                                    title = {
                                        Text(
                                            text = popup.title,
                                            color = tintColor,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    },
                                    text = {
                                        Text(
                                            text = popup.message,
                                            color = OffWhite,
                                            fontFamily = FontFamily.Monospace,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                popup.onConfirm?.invoke()
                                                viewModel.dismissPopup()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = tintColor)
                                        ) {
                                            Text(popup.confirmText, color = CyberBlack, fontWeight = FontWeight.Bold)
                                        }
                                    },
                                    dismissButton = {
                                        OutlinedButton(
                                            onClick = { viewModel.dismissPopup() },
                                            border = BorderStroke(1.dp, CyberGray)
                                        ) {
                                            Text("DISMISS", color = Silver)
                                        }
                                    },
                                    containerColor = CyberDark,
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }

                            // 5. Global Dashboard Customization Dialog
                            if (isCustomizeDashboardOpen) {
                                DashboardCustomizerDialog(
                                    viewModel = viewModel,
                                    onDismiss = { viewModel.setCustomizeDashboardOpen(false) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCustomizerDialog(
    viewModel: NetOpsViewModel,
    onDismiss: () -> Unit
) {
    val showMatrixHeader by viewModel.showMatrixHeader.collectAsState()
    val showDiagnosticStream by viewModel.showDiagnosticStream.collectAsState()
    val showQuickStats by viewModel.showQuickStats.collectAsState()
    val showRecentIncidents by viewModel.showRecentIncidents.collectAsState()
    val showTrafficSnifferWidget by viewModel.showTrafficSnifferWidget.collectAsState()
    val showCellRadarWidget by viewModel.showCellRadarWidget.collectAsState()
    val showDeviceInventory by viewModel.showDeviceInventory.collectAsState()
    val homeLayoutStyle by viewModel.homeLayoutStyle.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = CyberDark),
            border = BorderStroke(1.dp, CyberCyan),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CUSTOMIZE HOME",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ConsoleGreen,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Silver)
                    }
                }

                Text(
                    text = "Toggle active widgets and select preferred grid layout",
                    style = MaterialTheme.typography.labelSmall,
                    color = Silver
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Layout Density", style = MaterialTheme.typography.labelSmall, color = CyberCyan, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("single_column" to "Single List", "two_column" to "2-Col Grid", "compact_focused" to "Compact HUD").forEach { (styleKey, styleLabel) ->
                        val isSel = homeLayoutStyle == styleKey
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) CyberCyan.copy(alpha = 0.2f) else CyberGray)
                                .border(1.dp, if (isSel) CyberCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                .clickable { viewModel.setHomeLayoutStyle(styleKey) }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(styleLabel, style = MaterialTheme.typography.labelSmall, color = if (isSel) CyberCyan else OffWhite, fontSize = 10.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Widgets Visibility", style = MaterialTheme.typography.labelSmall, color = ConsoleGreen, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    WidgetCheckRow("Matrix Rain Header", showMatrixHeader) { viewModel.toggleWidget("matrix_header") }
                    WidgetCheckRow("Interface Diagnostic Stream", showDiagnosticStream) { viewModel.toggleWidget("diagnostic_stream") }
                    WidgetCheckRow("Telemetry Shortcuts", showQuickStats) { viewModel.toggleWidget("quick_stats") }
                    WidgetCheckRow("Network Incidents Log", showRecentIncidents) { viewModel.toggleWidget("incidents") }
                    WidgetCheckRow("Traffic Sniffer Panel", showTrafficSnifferWidget) { viewModel.toggleWidget("sniffer") }
                    WidgetCheckRow("Cellular Antenna Radar", showCellRadarWidget) { viewModel.toggleWidget("cell_radar") }
                    WidgetCheckRow("Discovered Devices List", showDeviceInventory) { viewModel.toggleWidget("device_inventory") }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = ConsoleGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("APPLY & CLOSE", color = CyberBlack, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun WidgetCheckRow(label: String, checked: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (checked) OffWhite else DarkSilver
        )
        Checkbox(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(checkedColor = ConsoleGreen, uncheckedColor = Silver)
        )
    }
}


@Composable
fun ScrollableBottomDock(
    currentTab: NetOpsTab,
    bottomBarStyle: String,
    labelVisibility: String,
    density: String,
    onTabSelected: (NetOpsTab) -> Unit,
    modifier: Modifier = Modifier,
    isTop: Boolean = false
) {
    val bg = when (bottomBarStyle) {
        "glassmorphism" -> CyberDark.copy(alpha = 0.85f)
        else -> CyberDark
    }
    val borderClr = when (bottomBarStyle) {
        "cyber_border" -> CyberCyan
        else -> CyberSlate
    }
    
    val outerPaddingVert = when (density) {
        "compact" -> 4.dp
        "spacious" -> 14.dp
        else -> 8.dp
    }

    val dockShape = if (isTop) {
        RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    }

    val insetsModifier = if (isTop) {
        Modifier.statusBarsPadding()
    } else {
        Modifier.navigationBarsPadding()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(insetsModifier)
            .background(bg, shape = dockShape)
            .border(
                BorderStroke(1.dp, borderClr),
                shape = dockShape
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = outerPaddingVert),
            horizontalArrangement = Arrangement.spacedBy(
                when (density) {
                    "compact" -> 6.dp
                    "spacious" -> 16.dp
                    else -> 10.dp
                }
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScrollableNavItem(
                selected = currentTab == NetOpsTab.DASHBOARD,
                onClick = { onTabSelected(NetOpsTab.DASHBOARD) },
                icon = { tint -> Icon(Icons.Default.Dashboard, contentDescription = "Dashboard", tint = tint) },
                label = "Home",
                labelVisibility = labelVisibility,
                density = density,
                testTag = "tab_dashboard"
            )

            ScrollableNavItem(
                selected = currentTab == NetOpsTab.TOOLBOX,
                onClick = { onTabSelected(NetOpsTab.TOOLBOX) },
                icon = { tint -> Icon(Icons.Default.Build, contentDescription = "Toolbox", tint = tint) },
                label = "Toolbox",
                labelVisibility = labelVisibility,
                density = density,
                testTag = "tab_toolbox"
            )

            ScrollableNavItem(
                selected = currentTab == NetOpsTab.DEVICES,
                onClick = { onTabSelected(NetOpsTab.DEVICES) },
                icon = { tint -> Icon(Icons.Default.Devices, contentDescription = "Inventory", tint = tint) },
                label = "Devices",
                labelVisibility = labelVisibility,
                density = density,
                testTag = "tab_devices"
            )

            ScrollableNavItem(
                selected = currentTab == NetOpsTab.ALERTS,
                onClick = { onTabSelected(NetOpsTab.ALERTS) },
                icon = { tint -> Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = tint) },
                label = "Alerts",
                labelVisibility = labelVisibility,
                density = density,
                testTag = "tab_alerts"
            )

            ScrollableNavItem(
                selected = currentTab == NetOpsTab.TERMINAL,
                onClick = { onTabSelected(NetOpsTab.TERMINAL) },
                icon = { tint -> Icon(Icons.Default.Terminal, contentDescription = "Terminal", tint = tint) },
                label = "Terminal",
                labelVisibility = labelVisibility,
                density = density,
                testTag = "tab_terminal"
            )

            ScrollableNavItem(
                selected = currentTab == NetOpsTab.SETTINGS,
                onClick = { onTabSelected(NetOpsTab.SETTINGS) },
                icon = { tint -> Icon(Icons.Default.Settings, contentDescription = "Settings", tint = tint) },
                label = "Settings",
                labelVisibility = labelVisibility,
                density = density,
                testTag = "tab_settings"
            )
        }
    }
}

@Composable
fun ScrollableNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (Color) -> Unit,
    label: String,
    labelVisibility: String,
    density: String,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val showLabel = when (labelVisibility) {
        "always" -> true
        "selected_only" -> selected
        "hidden" -> false
        else -> true
    }

    val itemPaddingHoriz = when (density) {
        "compact" -> 10.dp
        "spacious" -> 20.dp
        else -> 14.dp
    }
    val itemPaddingVert = when (density) {
        "compact" -> 6.dp
        "spacious" -> 12.dp
        else -> 8.dp
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) CyberCyan.copy(alpha = 0.15f) else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (selected) CyberCyan else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = itemPaddingHoriz, vertical = itemPaddingVert),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val tintColor = if (selected) CyberCyan else DarkSilver
            icon(tintColor)
            if (showLabel) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    color = if (selected) CyberCyan else DarkSilver,
                    fontSize = if (density == "compact") 11.sp else 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
