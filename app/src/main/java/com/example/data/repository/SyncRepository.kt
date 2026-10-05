package com.example.data.repository

import com.example.data.DeliveryEntity
import com.example.data.NdNDao
import com.example.data.remote.BackendApiService
import com.example.data.remote.BackendDeliveryMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SyncRepository(
    private val ndnDao: NdNDao,
    private val apiService: BackendApiService
) {
    val localDeliveries: Flow<List<DeliveryEntity>> =
        ndnDao.getAllDeliveries()

    /**
     * Synchronize backend deliveries into the local Room cache.
     *
     * Backend API v1 is the business API.
     * Room remains the local/offline cache.
     */
    suspend fun syncRemoteToLocal(authToken: String) =
        withContext(Dispatchers.IO) {
            try {
                val response =
                    apiService.getDeliveries("Bearer $authToken")

                if (response.isSuccessful) {
                    response.body()?.data?.forEach { remoteDelivery ->
                        ndnDao.insertDelivery(
                            BackendDeliveryMapper.fromBackend(remoteDelivery)
                        )
                    }
                }
            } catch (e: Exception) {
                // Offline/unreachable backend:
                // keep using the existing Room cache.
            }
        }

    /**
     * Offline-first delivery creation.
     *
     * The local record is written first.
     * Backend synchronization is attempted afterwards.
     */
    suspend fun addDeliveryOnline(
        delivery: DeliveryEntity,
        authToken: String
    ) = withContext(Dispatchers.IO) {
        ndnDao.insertDelivery(delivery)

        try {
            apiService.insertDelivery(
                authHeader = "Bearer $authToken",
                delivery = BackendDeliveryMapper.toBackend(delivery)
            )
        } catch (e: Exception) {
            // Keep local Room record.
            // A durable sync queue will be introduced
            // in the dedicated offline-sync phase.
        }
    }

    /**
     * Temporary compatibility method.
     *
     * Deletion still requires a proper offline operation queue.
     * Do not silently delete local data when the backend is unavailable.
     */
    suspend fun deleteDeliveryOnline(
        delivery: DeliveryEntity,
        authToken: String
    ) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteDelivery(
                authHeader = "Bearer $authToken",
                trackingId = delivery.trackingId
            )

            if (response.isSuccessful) {
                ndnDao.deleteDelivery(delivery)
            }
        } catch (e: Exception) {
            // Backend unavailable:
            // keep the local record until a durable sync
            // operation mechanism is implemented.
        }
    }
}
