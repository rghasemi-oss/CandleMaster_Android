package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CustodyAssetEntity
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustodyScreen(
    custodyAssets: List<CustodyAssetEntity>,
    onAddAsset: (String, String, Double, Boolean, String) -> Unit,
    onUpdateAsset: (CustodyAssetEntity) -> Unit,
    onDeleteAsset: (CustodyAssetEntity) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financial Custody & Vaults", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.testTag("add_custody_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Custody Asset")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (custodyAssets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No custody assets registered.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(custodyAssets) { asset ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(asset.assetName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                    Text(currencyFormatter.format(asset.value), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Custodian: ${asset.custodian}", style = MaterialTheme.typography.bodyMedium)
                                Text("Notes: ${asset.notes}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = asset.isFlagged,
                                            onCheckedChange = { flagged ->
                                                onUpdateAsset(asset.copy(isFlagged = flagged))
                                            }
                                        )
                                        Text("Flagged Risk", style = MaterialTheme.typography.bodySmall)
                                    }
                                    TextButton(onClick = { onDeleteAsset(asset) }) {
                                        Text("Delete", color = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddCustodyDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, custodian, valNum, flagged, notes ->
                onAddAsset(name, custodian, valNum, flagged, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddCustodyDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double, Boolean, String) -> Unit
) {
    var assetName by remember { mutableStateOf("") }
    var custodian by remember { mutableStateOf("") }
    var valueStr by remember { mutableStateOf("") }
    var isFlagged by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Custody Asset / Vault") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = assetName,
                    onValueChange = { assetName = it },
                    label = { Text("Asset / Vault Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = custodian,
                    onValueChange = { custodian = it },
                    label = { Text("Custodian Institution") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = valueStr,
                    onValueChange = { valueStr = it },
                    label = { Text("Total Value ($)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Audit Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isFlagged,
                        onCheckedChange = { isFlagged = it }
                    )
                    Text("Flag for Compliance Review")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val value = valueStr.toDoubleOrNull() ?: 0.0
                    if (assetName.isNotBlank()) {
                        onConfirm(assetName, custodian, value, isFlagged, notes)
                    }
                },
                modifier = Modifier.testTag("save_custody_button")
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
