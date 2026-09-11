package com.example.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.NetOpsViewModel
import com.example.ui.ToastType
import com.example.ui.theme.*

@Composable
fun BackupRestoreDialog(
    viewModel: NetOpsViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var activeSubTab by remember { mutableStateOf("EXPORT") } // "EXPORT" or "RESTORE"

    var exportedJson by remember { mutableStateOf(viewModel.exportConfigurationJson()) }
    var restoreInputJson by remember { mutableStateOf("") }
    var showResetConfirm by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(1.dp, CyberCyan, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberDark)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Backup,
                        contentDescription = "Backup",
                        tint = CyberCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "BACKUP & RESTORE",
                            style = MaterialTheme.typography.titleMedium,
                            color = CyberCyan,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "JSON Profile Snapshot & Migration",
                            style = MaterialTheme.typography.labelSmall,
                            color = Silver
                        )
                    }
                }

                // Sub-tab toggles: EXPORT vs RESTORE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            activeSubTab = "EXPORT"
                            exportedJson = viewModel.exportConfigurationJson()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeSubTab == "EXPORT") CyberCyan.copy(alpha = 0.25f) else CyberBlack,
                            contentColor = if (activeSubTab == "EXPORT") CyberCyan else Silver
                        ),
                        border = BorderStroke(1.dp, if (activeSubTab == "EXPORT") CyberCyan else CyberSlate),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("EXPORT / BACKUP", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }

                    Button(
                        onClick = { activeSubTab = "RESTORE" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeSubTab == "RESTORE") ConsoleGreen.copy(alpha = 0.25f) else CyberBlack,
                            contentColor = if (activeSubTab == "RESTORE") ConsoleGreen else Silver
                        ),
                        border = BorderStroke(1.dp, if (activeSubTab == "RESTORE") ConsoleGreen else CyberSlate),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("RESTORE SNAPSHOT", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }

                TactileDivider()

                if (activeSubTab == "EXPORT") {
                    Text(
                        text = "Current configuration serialized as portable JSON:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Silver
                    )

                    // Exported JSON preview container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberBlack)
                            .border(1.dp, CyberSlate, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = exportedJson,
                            color = ConsoleGreen,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("NetOps Config JSON", exportedJson)
                            clipboard.setPrimaryClip(clip)
                            viewModel.showToast("Backup JSON copied to clipboard!", ToastType.SUCCESS)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = CyberBlack, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("COPY JSON TO CLIPBOARD", color = CyberBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                } else {
                    Text(
                        text = "Paste your exported JSON configuration below:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Silver
                    )

                    OutlinedTextField(
                        value = restoreInputJson,
                        onValueChange = { restoreInputJson = it },
                        placeholder = { Text("Paste JSON snapshot here...", color = DarkSilver, fontSize = 11.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            color = OffWhite,
                            fontFamily = FontFamily.Monospace
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ConsoleGreen,
                            unfocusedBorderColor = CyberSlate
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = clipboard.primaryClip
                                if (clip != null && clip.itemCount > 0) {
                                    val text = clip.getItemAt(0).text?.toString() ?: ""
                                    restoreInputJson = text
                                    viewModel.showToast("Pasted from clipboard", ToastType.INFO)
                                } else {
                                    viewModel.showToast("Clipboard is empty", ToastType.WARNING)
                                }
                            },
                            border = BorderStroke(1.dp, ConsoleGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("PASTE CLIPBOARD", color = ConsoleGreen, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }

                        Button(
                            onClick = {
                                if (restoreInputJson.isBlank()) {
                                    viewModel.showToast("Please paste valid JSON data first", ToastType.WARNING)
                                } else {
                                    val success = viewModel.restoreConfigurationJson(restoreInputJson)
                                    if (success) {
                                        onDismiss()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ConsoleGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("RESTORE & APPLY", color = CyberBlack, fontWeight = FontWeight.Bold, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                TactileDivider(label = "FACTORY RECOVERY")

                if (!showResetConfirm) {
                    OutlinedButton(
                        onClick = { showResetConfirm = true },
                        border = BorderStroke(1.dp, CyberCrimson),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null, tint = CyberCrimson, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("RESET TO FACTORY DEFAULTS", color = CyberCrimson, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CyberCrimson, RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = CyberCrimsonDim)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "ARE YOU SURE?",
                                color = CyberCrimson,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "This will restore default Skeuomorphic styling, font scale 1.0x, bottom dock, and standard operator profile.",
                                color = OffWhite,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { showResetConfirm = false },
                                    border = BorderStroke(1.dp, Silver),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("CANCEL", color = Silver, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                }

                                Button(
                                    onClick = {
                                        viewModel.restoreFactoryDefaults()
                                        showResetConfirm = false
                                        onDismiss()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberCrimson),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("CONFIRM RESET", color = OffWhite, fontWeight = FontWeight.Bold, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }

                TactileDivider()

                OutlinedButton(
                    onClick = onDismiss,
                    border = BorderStroke(1.dp, CyberSlate),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CLOSE", color = Silver, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
