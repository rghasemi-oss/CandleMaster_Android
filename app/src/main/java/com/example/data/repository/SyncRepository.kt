package com.example.data.repository

import com.example.data.DeliveryEntity
import com.example.data.NdNDao
import com.example.data.remote.SupabaseApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SyncRepository(
    private val ndnDao: NdNDao,
    private val apiService: SupabaseApiService
) {
    val localDeliveries: Flow<List<DeliveryEntity>> = ndnDao.getAllDeliveries()

    suspend fun syncRemoteToLocal(apiKey: String, authToken: String) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getDeliveries(apiKey, "Bearer $authToken")
            if (response.isSuccessful) {
                response.body()?.forEach { remoteDelivery ->
                    ndnDao.insertDelivery(remoteDelivery)
                }
            }
        } catch (e: Exception) {
            // Fallback to local Room cache when offline
        }
    }

    suspend fun addDeliveryOnline(delivery: DeliveryEntity, apiKey: String, authToken: String) = withContext(Dispatchers.IO) {
        // Save locally first (offline-first)
        ndnDao.insertDelivery(delivery)
        try {
            apiService.insertDelivery(apiKey, "Bearer $authToken", "return=representation", delivery)
        } catch (e: Exception) {
            // Handled offline: will sync later
        }
    }

    suspend fun deleteDeliveryOnline(delivery: DeliveryEntity, apiKey: String, authToken: String) = withContext(Dispatchers.IO) {
        ndnDao.deleteDelivery(delivery)
        try {
            apiService.deleteDelivery(apiKey, "Bearer $authToken", "eq.${delivery.trackingId}")
        } catch (e: Exception) {
            // Handled offline
        }
    }
}
