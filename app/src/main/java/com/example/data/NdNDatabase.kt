package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [DeliveryEntity::class, HubLocationEntity::class, UserHistoryEntity::class], version = 1, exportSchema = false)
abstract class NdNDatabase : RoomDatabase() {
    abstract fun ndnDao(): NdNDao

    companion object {
        @Volatile
        private var INSTANCE: NdNDatabase? = null

        fun getDatabase(context: Context): NdNDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NdNDatabase::class.java,
                    "ndn_delivery_database"
                )
                    .addCallback(NdNDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class NdNDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateSeedData(database.ndnDao())
                    }
                }
            }

            suspend fun populateSeedData(dao: NdNDao) {
                val currentTime = System.currentTimeMillis()
                val dayMillis = 86400000L

                dao.insertDelivery(
                    DeliveryEntity(
                        trackingId = "IR-2025-4419",
                        sender = "فروشگاه آنلاین",
                        customer = "علی احمدی",
                        hubName = "هاب سعادت‌آباد",
                        status = "در انتظار تحویل",
                        weight = "1.2 kg",
                        timestamp = currentTime - dayMillis
                    )
                )
                dao.insertDelivery(
                    DeliveryEntity(
                        trackingId = "IR-1049-X72",
                        sender = "دیجی‌کالا",
                        customer = "سارا محمدی",
                        hubName = "هاب مرکزی غرب",
                        status = "موجود در هاب",
                        weight = "2.5 kg",
                        timestamp = currentTime - dayMillis * 2
                    )
                )
                dao.insertDelivery(
                    DeliveryEntity(
                        trackingId = "IR-8821-M14",
                        sender = "تکنولایف",
                        customer = "رضا کریمی",
                        hubName = "هاب شمال",
                        status = "آماده ارسال",
                        weight = "0.8 kg",
                        timestamp = currentTime - dayMillis * 3
                    )
                )

                dao.insertHub(HubLocationEntity(name = "هاب سعادت‌آباد", address = "تهران، سعادت‌آباد، خیابان سرو شرقی، پلاک ۲۳", activePackagesCount = 42))
                dao.insertHub(HubLocationEntity(name = "هاب مرکزی غرب", address = "تهران، شهرک غرب، بلوار دادمان", activePackagesCount = 68))
                dao.insertHub(HubLocationEntity(name = "هاب شمال", address = "تهران، تجریش، خیابان فنا خسرو", activePackagesCount = 29))

                dao.insertHistory(UserHistoryEntity(actionType = "LOGIN", description = "کاربر وارد سامانه شد", timestamp = currentTime))
                dao.insertHistory(UserHistoryEntity(actionType = "SYNC", description = "همگام‌سازی آفلاین پایگاه داده Room انجام شد", timestamp = currentTime - 10000))
            }
        }
    }
}
