package com.measify.kappmaker.data.source.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.measify.kappmaker.data.source.local.dao.CreditTransactionDao
import com.measify.kappmaker.data.source.local.dao.ExampleDao
import com.measify.kappmaker.data.source.local.entity.CreditTransactionEntity
import com.measify.kappmaker.data.source.local.entity.ExampleEntity

@Database(
    entities = [ExampleEntity::class, CreditTransactionEntity::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao
    abstract fun creditTransactionDao(): CreditTransactionDao
    //Add other DAOs here
}




interface DatabaseProvider {
    fun provideAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}