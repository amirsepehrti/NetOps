package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NetOpsViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    viewModel: NetOpsViewModel,
    modifier: Modifier = Modifier
) {
    val logs by viewModel.terminalLogs.collectAsState()
    val snippets by viewModel.sshSnippets.collectAsState()
    val input by viewModel.terminalInput.collectAsState()
    val sshHost by viewModel.sshSessionHost.collectAsState()

    var showAddSnippetDialog by remember { mutableStateOf(false) }
    var snippetTitle by remember { mutableStateOf("") }
    var snippetCmd by remember { mutableStateOf("") }
    var snippetDesc by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Auto scroll terminal to bottom when new logs arrive
    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
    ) {
        // Snippets helper horizontal scroll bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CyberDark)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "SNIPPETS SHELF",
                color = Silver,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall
            )

            IconButton(
                onClick = { showAddSnippetDialog = true },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Default.AddBox, contentDescription = "New Snippet", tint = ConsoleGreen, modifier = Modifier.size(18.dp))
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(CyberDark)
                .border(1.dp, CyberGray)
                .padding(bottom = 8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (snippets.isEmpty()) {
                item {
                    Text(
                        "No saved SSH scripts or templates found.",
                        color = Silver,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } else {
                items(snippets) { sn ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.terminalInput.value = sn.command
                            },
                        colors = CardDefaults.cardColors(containerColor = CyberGray)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(sn.title, fontWeight = FontWeight.Bold, color = ConsoleGreen, style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
                                Text(sn.command, color = OffWhite, style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace)
                            }
                            IconButton(
                                onClick = { viewModel.deleteSshSnippet(sn.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CyberCrimson, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }

        // Terminal Screen (Primary console log)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(CyberBlack)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(logs) { line ->
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = if (line.contains("admin@") || line.contains("netops-client:")) ConsoleGreenLight else if (line.contains("Error") || line.contains("failed")) CyberCrimson else OffWhite
                )
            }
        }

        // Terminal Input Bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyberGray),
            colors = CardDefaults.cardColors(containerColor = CyberDark),
            shape = RoundedCornerShape(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (sshHost != null) "$sshHost:~$ " else "netops-client:~$ ",
                    color = if (sshHost != null) CyberCyan else ConsoleGreen,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                TextField(
                    value = input,
                    onValueChange = { viewModel.terminalInput.value = it },
                    placeholder = { Text("Enter utility command (try 'help')...", color = Silver) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("terminal_input_field"),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = OffWhite,
                        unfocusedTextColor = OffWhite,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            viewModel.executeTerminalCommand()
                            keyboardController?.hide()
                        }
                    ),
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        viewModel.executeTerminalCommand()
                        keyboardController?.hide()
                    },
                    modifier = Modifier.testTag("terminal_send_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = ConsoleGreen)
                }
            }
        }
    }

    // Add SSH Snippet Dialog
    if (showAddSnippetDialog) {
        AlertDialog(
            onDismissRequest = { showAddSnippetDialog = false },
            title = { Text("NEW RUNBOOK SNIPPET", fontFamily = FontFamily.Monospace, color = ConsoleGreen) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = snippetTitle,
                        onValueChange = { snippetTitle = it },
                        label = { Text("Snippet Title") },
                        modifier = Modifier.fillMaxWidth().testTag("add_snippet_title_input")
                    )
                    OutlinedTextField(
                        value = snippetCmd,
                        onValueChange = { snippetCmd = it },
                        label = { Text("SSH Command Script") },
                        modifier = Modifier.fillMaxWidth().testTag("add_snippet_cmd_input")
                    )
                    OutlinedTextField(
                        value = snippetDesc,
                        onValueChange = { snippetDesc = it },
                        label = { Text("Optional Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (snippetTitle.isNotEmpty() && snippetCmd.isNotEmpty()) {
                            viewModel.saveSshSnippet(snippetTitle, snippetCmd, snippetDesc)
                            showAddSnippetDialog = false
                            // reset
                            snippetTitle = ""
                            snippetCmd = ""
                            snippetDesc = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ConsoleGreen, contentColor = CyberBlack),
                    modifier = Modifier.testTag("add_snippet_confirm_button")
                ) {
                    Text("SAVE TO SHELF")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSnippetDialog = false }) {
                    Text("CANCEL")
                }
            },
            containerColor = CyberDark
        )
    }
}
