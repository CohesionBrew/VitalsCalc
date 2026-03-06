package com.cohesionbrew.healthcalculator.data.repository

import com.cohesionbrew.healthcalculator.data.source.local.CalculationHistoryLocalDataSource
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class HistoryRepositoryImpl(
    private val localDataSource: CalculationHistoryLocalDataSource
) : HistoryRepository {

    override fun getHistory(daysLimit: Int?): Flow<List<CalculationEntry>> {
        return if (daysLimit != null) {
            val now = Clock.System.now()
            val since = now.minus(daysLimit, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
            localDataSource.getHistorySinceFlow(since.toEpochMilliseconds())
        } else {
            localDataSource.getAllFlow()
        }
    }

    override fun getHistoryByType(type: CalculationType): Flow<List<CalculationEntry>> {
        return localDataSource.getByTypeFlow(type)
    }

    override fun getHistoryCount(): Flow<Int> {
        return localDataSource.getCountFlow()
    }

    override suspend fun getLatestByType(type: CalculationType): CalculationEntry? {
        return localDataSource.getLatestByType(type)
    }

    override suspend fun addEntry(entry: CalculationEntry) {
        localDataSource.upsert(entry)
    }

    override suspend fun upsertEntry(
        entry: CalculationEntry,
        matchPredicate: ((CalculationEntry) -> Boolean)?
    ) {
        if (matchPredicate != null) {
            // Find today's entries of the same type and check if any match the predicate
            val now = Clock.System.now()
            val tz = TimeZone.currentSystemDefault()
            val today = now.toLocalDateTime(tz).date
            val dayStart = today.atStartOfDayIn(tz).toEpochMilliseconds()
            val dayEnd = today.plus(1, DateTimeUnit.DAY).atStartOfDayIn(tz).toEpochMilliseconds()

            // Delete matching same-day entries before upserting
            localDataSource.deleteSameDayEntries(entry.type, dayStart, dayEnd)
        }
        localDataSource.upsert(entry)
    }

    override suspend fun deleteEntry(id: String) {
        localDataSource.deleteById(id)
    }

    override suspend fun clearHistory() {
        localDataSource.deleteAll()
    }
}
