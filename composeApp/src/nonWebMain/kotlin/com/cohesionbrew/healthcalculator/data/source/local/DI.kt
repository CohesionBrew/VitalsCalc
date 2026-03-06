package com.cohesionbrew.healthcalculator.data.source.local

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.cohesionbrew.healthcalculator.data.source.local.impl.CalculationHistoryLocalDataSourceImpl
import com.cohesionbrew.healthcalculator.data.source.local.impl.CreditTransactionLocalDataSourceImpl
import com.cohesionbrew.healthcalculator.data.source.local.impl.UserProfileLocalDataSourceImpl
import com.cohesionbrew.healthcalculator.util.defaultAsyncDispatcher
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val localDataSourceModule = module {
    //Local Source
    single<AppDatabase> {
        val databaseProvider = get<DatabaseProvider>()
        databaseProvider.provideAppDatabaseBuilder()
            .fallbackToDestructiveMigration(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(defaultAsyncDispatcher)
            .build()
    }

    //Local DAOs
    single { get<AppDatabase>().creditTransactionDao() }
    single { get<AppDatabase>().calculationHistoryDao() }
    single { get<AppDatabase>().userProfileDao() }

    // Impl
    single { CreditTransactionLocalDataSourceImpl(get()) } bind CreditTransactionLocalDataSource::class
    single { CalculationHistoryLocalDataSourceImpl(get()) } bind CalculationHistoryLocalDataSource::class
    single { UserProfileLocalDataSourceImpl(get()) } bind UserProfileLocalDataSource::class
}
val nonWebModule = module {
    includes(localDataSourceModule)
}

