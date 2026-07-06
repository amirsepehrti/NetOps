package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
            MyApplicationTheme {
                val viewModel: NetOpsViewModel = viewModel()
                val currentTab by viewModel.currentTab.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize().background(CyberBlack),
                    bottomBar = {
                        NavigationBar(
                            containerColor = CyberDark,
                            tonalElevation = NavigationBarDefaultsTonalElevation()
                        ) {
                            val navItemColors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CyberDark,
                                selectedTextColor = CyberCyan,
                                unselectedIconColor = DarkSilver,
                                unselectedTextColor = DarkSilver,
                                indicatorColor = CyberCyan
                            )

                            NavigationBarItem(
                                selected = currentTab == NetOpsTab.DASHBOARD,
                                onClick = { viewModel.selectTab(NetOpsTab.DASHBOARD) },
                                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                label = { Text("Home", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                                colors = navItemColors,
                                modifier = Modifier.testTag("tab_dashboard")
                            )

                            NavigationBarItem(
                                selected = currentTab == NetOpsTab.TOOLBOX,
                                onClick = { viewModel.selectTab(NetOpsTab.TOOLBOX) },
                                icon = { Icon(Icons.Default.Build, contentDescription = "Toolbox") },
                                label = { Text("Toolbox", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                                colors = navItemColors,
                                modifier = Modifier.testTag("tab_toolbox")
                            )

                            NavigationBarItem(
                                selected = currentTab == NetOpsTab.DEVICES,
                                onClick = { viewModel.selectTab(NetOpsTab.DEVICES) },
                                icon = { Icon(Icons.Default.Devices, contentDescription = "Inventory") },
                                label = { Text("Devices", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                                colors = navItemColors,
                                modifier = Modifier.testTag("tab_devices")
                            )

                            NavigationBarItem(
                                selected = currentTab == NetOpsTab.ALERTS,
                                onClick = { viewModel.selectTab(NetOpsTab.ALERTS) },
                                icon = { Icon(Icons.Default.Notifications, contentDescription = "Alerts") },
                                label = { Text("Alerts", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                                colors = navItemColors,
                                modifier = Modifier.testTag("tab_alerts")
                            )

                            NavigationBarItem(
                                selected = currentTab == NetOpsTab.TERMINAL,
                                onClick = { viewModel.selectTab(NetOpsTab.TERMINAL) },
                                icon = { Icon(Icons.Default.Terminal, contentDescription = "Terminal") },
                                label = { Text("Terminal", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                                colors = navItemColors,
                                modifier = Modifier.testTag("tab_terminal")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentTab) {
                            NetOpsTab.DASHBOARD -> DashboardScreen(viewModel)
                            NetOpsTab.TOOLBOX -> ToolboxScreen(viewModel)
                            NetOpsTab.DEVICES -> DevicesScreen(viewModel)
                            NetOpsTab.ALERTS -> AlertsScreen(viewModel)
                            NetOpsTab.TERMINAL -> TerminalScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationBarDefaultsTonalElevation() = 8.dp
