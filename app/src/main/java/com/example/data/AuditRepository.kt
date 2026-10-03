package com.example.data

import kotlinx.coroutines.flow.Flow

class AuditRepository(private val auditDao: AuditDao) {
    val allAudits: Flow<List<AuditEntity>> = auditDao.getAllAudits()
    val allCustodyAssets: Flow<List<CustodyAssetEntity>> = auditDao.getAllCustodyAssets()
    val pendingAuditsCount: Flow<Int> = auditDao.getPendingAuditsCount()
    val flaggedAssetsCount: Flow<Int> = auditDao.getFlaggedAssetsCount()
    val totalCustodyValue: Flow<Double?> = auditDao.getTotalCustodyValue()

    suspend fun insertAudit(audit: AuditEntity) {
        auditDao.insertAudit(audit)
    }

    suspend fun updateAudit(audit: AuditEntity) {
        auditDao.updateAudit(audit)
    }

    suspend fun deleteAudit(audit: AuditEntity) {
        auditDao.deleteAudit(audit)
    }

    suspend fun insertCustodyAsset(asset: CustodyAssetEntity) {
        auditDao.insertCustodyAsset(asset)
    }

    suspend fun updateCustodyAsset(asset: CustodyAssetEntity) {
        auditDao.updateCustodyAsset(asset)
    }

    suspend fun deleteCustodyAsset(asset: CustodyAssetEntity) {
        auditDao.deleteCustodyAsset(asset)
    }
}
