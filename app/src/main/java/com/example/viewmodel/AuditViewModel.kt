package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiAuditService
import com.example.data.AuditDatabase
import com.example.data.AuditEntity
import com.example.data.AuditRepository
import com.example.data.CustodyAssetEntity
import com.example.worker.scheduleAuditSync
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuditViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AuditRepository
    private val geminiService = GeminiAuditService()

    val allAudits: StateFlow<List<AuditEntity>>
    val allCustodyAssets: StateFlow<List<CustodyAssetEntity>>
    val pendingAuditsCount: StateFlow<Int>
    val flaggedAssetsCount: StateFlow<Int>
    val totalCustodyValue: StateFlow<Double>

    // AI Advisor State
    private val _aiResponse = MutableStateFlow<String>("Ask the AI Compliance Advisor anything regarding custody protocols, regulatory readiness, or risk mitigation.")
    val aiResponse: StateFlow<String> = _aiResponse.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    init {
        val dao = AuditDatabase.getDatabase(application).auditDao()
        repository = AuditRepository(dao)

        allAudits = repository.allAudits
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allCustodyAssets = repository.allCustodyAssets
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        pendingAuditsCount = repository.pendingAuditsCount
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

        flaggedAssetsCount = repository.flaggedAssetsCount
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

        totalCustodyValue = repository.totalCustodyValue
            .map { it ?: 0.0 }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

        // Schedule periodic WorkManager sync on init
        scheduleAuditSync(application)
    }

    fun addAudit(title: String, category: String, status: String, amount: Double, description: String, riskLevel: String) {
        viewModelScope.launch {
            repository.insertAudit(
                AuditEntity(
                    title = title,
                    category = category,
                    status = status,
                    amount = amount,
                    timestamp = System.currentTimeMillis(),
                    description = description,
                    riskLevel = riskLevel
                )
            )
        }
    }

    fun updateAudit(audit: AuditEntity) {
        viewModelScope.launch {
            repository.updateAudit(audit)
        }
    }

    fun deleteAudit(audit: AuditEntity) {
        viewModelScope.launch {
            repository.deleteAudit(audit)
        }
    }

    fun addCustodyAsset(assetName: String, custodian: String, value: Double, isFlagged: Boolean, notes: String) {
        viewModelScope.launch {
            repository.insertCustodyAsset(
                CustodyAssetEntity(
                    assetName = assetName,
                    custodian = custodian,
                    value = value,
                    lastVerified = System.currentTimeMillis(),
                    isFlagged = isFlagged,
                    notes = notes
                )
            )
        }
    }

    fun updateCustodyAsset(asset: CustodyAssetEntity) {
        viewModelScope.launch {
            repository.updateCustodyAsset(asset)
        }
    }

    fun deleteCustodyAsset(asset: CustodyAssetEntity) {
        viewModelScope.launch {
            repository.deleteCustodyAsset(asset)
        }
    }

    fun askAiAdvisor(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            _isAiLoading.value = true
            try {
                val result = geminiService.analyzeAuditCompliance(prompt)
                _aiResponse.value = result
            } catch (e: Exception) {
                _aiResponse.value = "Error getting AI analysis: ${e.localizedMessage}"
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun triggerManualSync() {
        scheduleAuditSync(getApplication())
    }
}
