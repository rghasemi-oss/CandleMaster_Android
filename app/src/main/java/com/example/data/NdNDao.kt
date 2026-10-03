package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NdNDao {
    // --- Delivery Status CRUD & Queries ---

    @Query("SELECT * FROM deliveries ORDER BY timestamp DESC")
    fun getAllDeliveries(): Flow<List<DeliveryEntity>>

    @Query("SELECT * FROM deliveries WHERE status = :status ORDER BY timestamp DESC")
    fun getDeliveriesByStatus(status: String): Flow<List<DeliveryEntity>>

    @Query("SELECT * FROM deliveries WHERE trackingId = :trackingId LIMIT 1")
    suspend fun getDeliveryById(trackingId: String): DeliveryEntity?

    @Query("SELECT * FROM deliveries WHERE trackingId LIKE '%' || :query || '%' OR customer LIKE '%' || :query || '%' OR sender LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchDeliveries(query: String): Flow<List<DeliveryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDelivery(delivery: DeliveryEntity)

    @Update
    suspend fun updateDelivery(delivery: DeliveryEntity)

    @Delete
    suspend fun deleteDelivery(delivery: DeliveryEntity)

    @Query("DELETE FROM deliveries")
    suspend fun deleteAllDeliveries()


    // --- Hub Locations CRUD & Queries ---

    @Query("SELECT * FROM hubs ORDER BY name ASC")
    fun getAllHubs(): Flow<List<HubLocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHub(hub: HubLocationEntity)

    @Delete
    suspend fun deleteHub(hub: HubLocationEntity)


    // --- User History Records CRUD & Queries ---

    @Query("SELECT * FROM user_history ORDER BY timestamp DESC")
    fun getUserHistory(): Flow<List<UserHistoryEntity>>

    @Query("SELECT * FROM user_history WHERE actionType = :actionType ORDER BY timestamp DESC")
    fun getUserHistoryByAction(actionType: String): Flow<List<UserHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: UserHistoryEntity)

    @Query("DELETE FROM user_history")
    suspend fun clearUserHistory()
}
