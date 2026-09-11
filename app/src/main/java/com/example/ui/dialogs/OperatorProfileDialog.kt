package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.NetOpsViewModel
import com.example.ui.theme.*

@Composable
fun OperatorProfileDialog(
    viewModel: NetOpsViewModel,
    onDismiss: () -> Unit
) {
    val currentName by viewModel.operatorName.collectAsState()
    val currentCallsign by viewModel.operatorCallsign.collectAsState()
    val currentRole by viewModel.operatorRole.collectAsState()
    val currentClearance by viewModel.operatorClearance.collectAsState()
    val currentUnit by viewModel.operatorUnit.collectAsState()
    val currentAvatar by viewModel.operatorAvatar.collectAsState()

    var name by remember(currentName) { mutableStateOf(currentName) }
    var callsign by remember(currentCallsign) { mutableStateOf(currentCallsign) }
    var role by remember(currentRole) { mutableStateOf(currentRole) }
    var clearance by remember(currentClearance) { mutableStateOf(currentClearance) }
    var unit by remember(currentUnit) { mutableStateOf(currentUnit) }
    var selectedAvatar by remember(currentAvatar) { mutableStateOf(currentAvatar) }

    val clearanceOptions = listOf(
        "Level 5 - Unrestricted Root",
        "Level 4 - Senior Architect",
        "Level 3 - Infrastructure SecOps",
        "Level 2 - Telemetry Operator",
        "Level 1 - Read-Only Auditor"
    )

    val avatarList = listOf(
        "security" to Icons.Default.Security,
        "terminal" to Icons.Default.Terminal,
        "cpu" to Icons.Default.Memory,
        "router" to Icons.Default.Router,
        "satellite" to Icons.Default.SatelliteAlt
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(1.dp, ConsoleGreen, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberDark)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header with tactile badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = "Badge",
                        tint = ConsoleGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "OPERATOR PROFILE",
                            style = MaterialTheme.typography.titleMedium,
                            color = ConsoleGreen,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Identity, Clearance & Terminal Handle",
                            style = MaterialTheme.typography.labelSmall,
                            color = Silver
                        )
                    }
                }

                TactileDivider(label = "AVATAR EMBLEM")

                // Avatar Emblem Selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    avatarList.forEach { (avatarKey, icon) ->
                        val isSelected = selectedAvatar == avatarKey
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) ConsoleGreen.copy(alpha = 0.2f) else CyberBlack)
                                .border(
                                    2.dp,
                                    if (isSelected) ConsoleGreen else CyberGray,
                                    CircleShape
                                )
                                .clickable { selectedAvatar = avatarKey },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = avatarKey,
                                tint = if (isSelected) ConsoleGreen else Silver,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                TactileDivider(label = "CREDENTIALS")

                // Operator Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Operator Name", color = Silver, fontSize = 12.sp) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = OffWhite,
                        fontFamily = FontFamily.Monospace
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = CyberCyan)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ConsoleGreen,
                        unfocusedBorderColor = CyberSlate
                    )
                )

                // Callsign / Handle
                OutlinedTextField(
                    value = callsign,
                    onValueChange = { callsign = it },
                    label = { Text("Callsign / Terminal Handle", color = Silver, fontSize = 12.sp) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = OffWhite,
                        fontFamily = FontFamily.Monospace
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.VpnKey, contentDescription = null, tint = ConsoleGreen)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ConsoleGreen,
                        unfocusedBorderColor = CyberSlate
                    )
                )

                // Tactical Role / Title
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Tactical Role / Title", color = Silver, fontSize = 12.sp) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = OffWhite,
                        fontFamily = FontFamily.Monospace
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Work, contentDescription = null, tint = Amber)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ConsoleGreen,
                        unfocusedBorderColor = CyberSlate
                    )
                )

                // Security Clearance Chips
                Text(
                    text = "CLEARANCE TIER",
                    style = MaterialTheme.typography.labelSmall,
                    color = Silver,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    clearanceOptions.forEach { opt ->
                        val isSelected = clearance == opt
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ConsoleGreen.copy(alpha = 0.15f) else CyberBlack)
                                .border(
                                    1.dp,
                                    if (isSelected) ConsoleGreen else CyberSlate,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { clearance = opt }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = opt,
                                    color = if (isSelected) ConsoleGreen else Silver,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = ConsoleGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Division / Unit
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Operations Lab / Division", color = Silver, fontSize = 12.sp) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = OffWhite,
                        fontFamily = FontFamily.Monospace
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Business, contentDescription = null, tint = CyberCyan)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ConsoleGreen,
                        unfocusedBorderColor = CyberSlate
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        border = BorderStroke(1.dp, CyberSlate),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CANCEL", color = Silver, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }

                    Button(
                        onClick = {
                            viewModel.updateOperatorProfile(
                                name = name,
                                callsign = callsign,
                                role = role,
                                clearance = clearance,
                                unit = unit,
                                avatar = selectedAvatar
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ConsoleGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "APPLY & SAVE",
                            color = CyberBlack,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}
