package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deliveries")
data class DeliveryEntity(
    @PrimaryKey
    val trackingId: String,
    val sender: String,
    val customer: String,
    val hubName: String,
    val status: String, // "PENDING", "IN_HUB", "DELIVERED", "FLAGGED"
    val weight: String,
    val timestamp: Long
)

@Entity(tableName = "hubs")
data class HubLocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val address: String,
    val activePackagesCount: Int
)

@Entity(tableName = "user_history")
data class UserHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val actionType: String,
    val description: String,
    val timestamp: Long
)
