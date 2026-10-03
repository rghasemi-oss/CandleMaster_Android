package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audit_records")
data class AuditEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val category: String, // "Financial", "Custody", "Security", "Compliance"
    val status: String, // "PENDING", "VERIFIED", "FLAGGED"
    val amount: Double,
    val timestamp: Long,
    val description: String,
    val riskLevel: String // "LOW", "MEDIUM", "HIGH"
)

@Entity(tableName = "custody_assets")
data class CustodyAssetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val assetName: String,
    val custodian: String,
    val value: Double,
    val lastVerified: Long,
    val isFlagged: Boolean,
    val notes: String
)
