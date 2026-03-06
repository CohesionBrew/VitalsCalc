package com.cohesionbrew.healthcalculator.data.source.local

import androidx.room.Room
import androidx.room.RoomDatabase
import com.cohesionbrew.healthcalculator.util.Constants
import java.io.File

class DatabaseProviderImpl : DatabaseProvider {
    override fun provideAppDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
        val dbFilePath = File(System.getProperty("java.io.tmpdir"), Constants.LOCAL_DB_STORAGE_NAME)
        return Room.databaseBuilder<AppDatabase>(
            name = dbFilePath.absolutePath,
        )
    }
}