package com.example.data

import kotlinx.coroutines.flow.Flow

class NdNRepository(private val ndnDao: NdNDao) {
    val allDeliveries: Flow<List<DeliveryEntity>> = ndnDao.getAllDeliveries()
    val allHubs: Flow<List<HubLocationEntity>> = ndnDao.getAllHubs()
    val userHistory: Flow<List<UserHistoryEntity>> = ndnDao.getUserHistory()

    suspend fun insertDelivery(delivery: DeliveryEntity) {
        ndnDao.insertDelivery(delivery)
        ndnDao.insertHistory(UserHistoryEntity(actionType = "INSERT", description = "بسته ${delivery.trackingId} ثبت شد", timestamp = System.currentTimeMillis()))
    }

    suspend fun updateDelivery(delivery: DeliveryEntity) {
        ndnDao.updateDelivery(delivery)
        ndnDao.insertHistory(UserHistoryEntity(actionType = "UPDATE", description = "وضعیت بسته ${delivery.trackingId} به‌روز شد", timestamp = System.currentTimeMillis()))
    }

    suspend fun deleteDelivery(delivery: DeliveryEntity) {
        ndnDao.deleteDelivery(delivery)
        ndnDao.insertHistory(UserHistoryEntity(actionType = "DELETE", description = "بسته ${delivery.trackingId} حذف شد", timestamp = System.currentTimeMillis()))
    }

    suspend fun insertHub(hub: HubLocationEntity) {
        ndnDao.insertHub(hub)
    }
}
