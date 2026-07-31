package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        MemberEntity::class,
        NewsEntity::class,
        EventEntity::class,
        DistrictLeaderEntity::class,
        ComplaintEntity::class,
        WelfareSchemeEntity::class,
        JobTrainingEntity::class,
        ReceiptEntity::class,
        StateExecutiveEntity::class,
        WelfareClaimEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class TnpaDatabase : RoomDatabase() {
    abstract fun tnpaDao(): TnpaDao

    companion object {
        @Volatile
        private var INSTANCE: TnpaDatabase? = null

        fun getInstance(context: Context): TnpaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TnpaDatabase::class.java,
                    "tnpa_union_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
