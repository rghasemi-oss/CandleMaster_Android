package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [AuditEntity::class, CustodyAssetEntity::class], version = 1, exportSchema = false)
abstract class AuditDatabase : RoomDatabase() {
    abstract fun auditDao(): AuditDao

    companion object {
        @Volatile
        private var INSTANCE: AuditDatabase? = null

        fun getDatabase(context: Context): AuditDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AuditDatabase::class.java,
                    "audit_guard_database"
                )
                    .addCallback(AuditDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AuditDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.auditDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: AuditDao) {
                val currentTime = System.currentTimeMillis()
                val dayMillis = 86400000L

                dao.insertAudit(
                    AuditEntity(
                        title = "Q3 Treasury Reserves Reconciliation",
                        category = "Financial",
                        status = "VERIFIED",
                        amount = 450000.0,
                        timestamp = currentTime - dayMillis * 2,
                        description = "Verified multi-sig wallet holdings against cold storage ledgers.",
                        riskLevel = "LOW"
                    )
                )
                dao.insertAudit(
                    AuditEntity(
                        title = "Smart Contract Access Control Check",
                        category = "Security",
                        status = "PENDING",
                        amount = 1250000.0,
                        timestamp = currentTime - dayMillis * 5,
                        description = "Reviewing administrative privileges and timelock parameters.",
                        riskLevel = "HIGH"
                    )
                )
                dao.insertAudit(
                    AuditEntity(
                        title = "Custodial Vault Insurance Verification",
                        category = "Custody",
                        status = "FLAGGED",
                        amount = 3200000.0,
                        timestamp = currentTime - dayMillis * 10,
                        description = "Insurance policy renewal documentation pending formal underwriter signoff.",
                        riskLevel = "MEDIUM"
                    )
                )

                dao.insertCustodyAsset(
                    CustodyAssetEntity(
                        assetName = "Cold Vault Alpha (BTC)",
                        custodian = "Institutional Custody Corp",
                        value = 2450000.0,
                        lastVerified = currentTime - dayMillis * 1,
                        isFlagged = false,
                        notes = "Hardware Security Module verified."
                    )
                )
                dao.insertCustodyAsset(
                    CustodyAssetEntity(
                        assetName = "Liquidity Pool Reserve (USDC)",
                        custodian = "Prime Trust Custody",
                        value = 1800000.0,
                        lastVerified = currentTime - dayMillis * 4,
                        isFlagged = true,
                        notes = "Audit trail awaiting attestation statement."
                    )
                )
                dao.insertCustodyAsset(
                    CustodyAssetEntity(
                        assetName = "Staked Ethereum Validator Set",
                        custodian = "Self-Custody Node Cluster",
                        value = 920000.0,
                        lastVerified = currentTime - dayMillis * 2,
                        isFlagged = false,
                        notes = "Validator uptime at 99.98%."
                    )
                )
            }
        }
    }
}
