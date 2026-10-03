package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AuditEntity
import com.example.data.CustodyAssetEntity
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    audits: List<AuditEntity>,
    custodyAssets: List<CustodyAssetEntity>,
    pendingCount: Int,
    flaggedCount: Int,
    totalValue: Double,
    onSyncTriggered: () -> Unit,
    onNavigateToAudits: () -> Unit,
    onNavigateToCustody: () -> Unit,
    onNavigateToAi: () -> Unit
) {
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AuditGuard Pro Dashboard", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                actions = {
                    IconButton(onClick = onSyncTriggered, modifier = Modifier.testTag("sync_button")) {
                        Icon(Icons.Default.Sync, contentDescription = "Trigger WorkManager Sync")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Compliance & Custody Overview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Custody",
                        value = currencyFormatter.format(totalValue),
                        icon = Icons.Default.AccountBalance,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        onClick = onNavigateToCustody
                    )
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Pending Audits",
                        value = pendingCount.toString(),
                        icon = Icons.Default.AssignmentLate,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        onClick = onNavigateToAudits
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Flagged Risks",
                        value = flaggedCount.toString(),
                        icon = Icons.Default.Warning,
                        color = MaterialTheme.colorScheme.errorContainer,
                        onClick = onNavigateToCustody
                    )
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "AI Compliance",
                        value = "Ready",
                        icon = Icons.Default.SmartToy,
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        onClick = onNavigateToAi
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("WorkManager Background Sync", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Periodic audit verification active (Every 15 mins).",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onSyncTriggered,
                            modifier = Modifier.fillMaxWidth().testTag("manual_sync_btn")
                        ) {
                            Icon(Icons.Default.Sync, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Run Audit Sync Now")
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Audits", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onNavigateToAudits) {
                        Text("View All")
                    }
                }
            }

            if (audits.isEmpty()) {
                item {
                    Text("No audit logs found.", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                items(audits.take(3)) { audit ->
                    AuditItemCard(audit = audit)
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = title)
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AuditItemCard(audit: AuditEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(audit.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Badge(
                    containerColor = when (audit.status) {
                        "VERIFIED" -> MaterialTheme.colorScheme.primaryContainer
                        "FLAGGED" -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.secondaryContainer
                    }
                ) {
                    Text(audit.status, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(audit.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Category: ${audit.category}", style = MaterialTheme.typography.bodySmall)
                Text("Risk: ${audit.riskLevel}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}
