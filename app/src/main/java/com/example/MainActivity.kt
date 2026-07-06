package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.NetOpsTab
import com.example.ui.NetOpsViewModel
import com.example.ui.screens.*
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberDark
import com.example.ui.theme.CyberGray
import com.example.ui.theme.CyberSlate
import com.example.ui.theme.ConsoleGreen
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.OffWhite
import com.example.ui.theme.Silver
import com.example.ui.theme.DarkSilver
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: NetOpsViewModel = viewModel()
            val activeTheme by viewModel.activeTheme.collectAsState()

            androidx.compose.runtime.key(activeTheme) {
                MyApplicationTheme {
                    val currentTab by viewModel.currentTab.collectAsState()

                    Scaffold(
                        modifier = Modifier.fillMaxSize().background(CyberBlack),
                        bottomBar = {
                            ScrollableBottomDock(
                                currentTab = currentTab,
                                onTabSelected = { viewModel.selectTab(it) }
                            )
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (currentTab) {
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
        }
    }
}

@Composable
fun ScrollableBottomDock(
    currentTab: NetOpsTab,
    onTabSelected: (NetOpsTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(CyberDark)
            .navigationBarsPadding()
            .border(
                BorderStroke(1.dp, CyberSlate),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScrollableNavItem(
                selected = currentTab == NetOpsTab.DASHBOARD,
                onClick = { onTabSelected(NetOpsTab.DASHBOARD) },
                icon = { tint -> Icon(Icons.Default.Dashboard, contentDescription = "Dashboard", tint = tint) },
                label = "Home",
                testTag = "tab_dashboard"
            )

            ScrollableNavItem(
                selected = currentTab == NetOpsTab.TOOLBOX,
                onClick = { onTabSelected(NetOpsTab.TOOLBOX) },
                icon = { tint -> Icon(Icons.Default.Build, contentDescription = "Toolbox", tint = tint) },
                label = "Toolbox",
                testTag = "tab_toolbox"
            )

            ScrollableNavItem(
                selected = currentTab == NetOpsTab.DEVICES,
                onClick = { onTabSelected(NetOpsTab.DEVICES) },
                icon = { tint -> Icon(Icons.Default.Devices, contentDescription = "Inventory", tint = tint) },
                label = "Devices",
                testTag = "tab_devices"
            )

            ScrollableNavItem(
                selected = currentTab == NetOpsTab.ALERTS,
                onClick = { onTabSelected(NetOpsTab.ALERTS) },
                icon = { tint -> Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = tint) },
                label = "Alerts",
                testTag = "tab_alerts"
            )

            ScrollableNavItem(
                selected = currentTab == NetOpsTab.TERMINAL,
                onClick = { onTabSelected(NetOpsTab.TERMINAL) },
                icon = { tint -> Icon(Icons.Default.Terminal, contentDescription = "Terminal", tint = tint) },
                label = "Terminal",
                testTag = "tab_terminal"
            )

            ScrollableNavItem(
                selected = currentTab == NetOpsTab.SETTINGS,
                onClick = { onTabSelected(NetOpsTab.SETTINGS) },
                icon = { tint -> Icon(Icons.Default.Settings, contentDescription = "Settings", tint = tint) },
                label = "Settings",
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
    testTag: String,
    modifier: Modifier = Modifier
) {
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
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val tintColor = if (selected) CyberCyan else DarkSilver
            icon(tintColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = if (selected) CyberCyan else DarkSilver,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
