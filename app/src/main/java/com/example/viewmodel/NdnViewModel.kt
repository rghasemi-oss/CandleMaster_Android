package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NdnViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NdNRepository

    val deliveries: StateFlow<List<DeliveryEntity>>
    val hubs: StateFlow<List<HubLocationEntity>>
    val history: StateFlow<List<UserHistoryEntity>>

    init {
        val dao = NdNDatabase.getDatabase(application).ndnDao()
        repository = NdNRepository(dao)

        deliveries = repository.allDeliveries
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        hubs = repository.allHubs
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        history = repository.userHistory
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun addDelivery(trackingId: String, sender: String, customer: String, hubName: String, status: String, weight: String) {
        viewModelScope.launch {
            repository.insertDelivery(
                DeliveryEntity(
                    trackingId = trackingId,
                    sender = sender,
                    customer = customer,
                    hubName = hubName,
                    status = status,
                    weight = weight,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun updateDelivery(delivery: DeliveryEntity) {
        viewModelScope.launch {
            repository.updateDelivery(delivery)
        }
    }

    fun deleteDelivery(delivery: DeliveryEntity) {
        viewModelScope.launch {
            repository.deleteDelivery(delivery)
        }
    }
}
