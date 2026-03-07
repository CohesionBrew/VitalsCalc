package com.cohesionbrew.healthcalculator.data.source.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.cohesionbrew.healthcalculator.data.source.local.dao.CalculationHistoryDao
import com.cohesionbrew.healthcalculator.data.source.local.dao.CreditTransactionDao
import com.cohesionbrew.healthcalculator.data.source.local.dao.UserProfileDao
import com.cohesionbrew.healthcalculator.data.source.local.entity.CalculationHistoryEntity
import com.cohesionbrew.healthcalculator.data.source.local.entity.CreditTransactionEntity
import com.cohesionbrew.healthcalculator.data.source.local.entity.UserProfileEntity

@Database(
    entities = [
        CreditTransactionEntity::class,
        CalculationHistoryEntity::class,
        UserProfileEntity::class
    ],
    version = 4
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun creditTransactionDao(): CreditTransactionDao
    abstract fun calculationHistoryDao(): CalculationHistoryDao
    abstract fun userProfileDao(): UserProfileDao
}




interface DatabaseProvider {
    fun provideAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}