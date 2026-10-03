package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditDao {
    @Query("SELECT * FROM audit_records ORDER BY timestamp DESC")
    fun getAllAudits(): Flow<List<AuditEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudit(audit: AuditEntity)

    @Update
    suspend fun updateAudit(audit: AuditEntity)

    @Delete
    suspend fun deleteAudit(audit: AuditEntity)

    @Query("SELECT * FROM custody_assets ORDER BY value DESC")
    fun getAllCustodyAssets(): Flow<List<CustodyAssetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustodyAsset(asset: CustodyAssetEntity)

    @Update
    suspend fun updateCustodyAsset(asset: CustodyAssetEntity)

    @Delete
    suspend fun deleteCustodyAsset(asset: CustodyAssetEntity)

    @Query("SELECT COUNT(*) FROM audit_records WHERE status = 'PENDING'")
    fun getPendingAuditsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM custody_assets WHERE isFlagged = 1")
    fun getFlaggedAssetsCount(): Flow<Int>

    @Query("SELECT SUM(value) FROM custody_assets")
    fun getTotalCustodyValue(): Flow<Double?>
}
