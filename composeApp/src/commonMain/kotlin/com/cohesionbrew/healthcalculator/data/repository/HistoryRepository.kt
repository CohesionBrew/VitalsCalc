package com.cohesionbrew.healthcalculator.data.repository

import com.cohesionbrew.healthcalculator.domain.model.history.CalculationEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistory(daysLimit: Int? = null): Flow<List<CalculationEntry>>
    fun getHistoryByType(type: CalculationType): Flow<List<CalculationEntry>>
    fun getHistoryCount(): Flow<Int>
    suspend fun getLatestByType(type: CalculationType): CalculationEntry?
    suspend fun addEntry(entry: CalculationEntry)
    suspend fun upsertEntry(entry: CalculationEntry, matchPredicate: ((CalculationEntry) -> Boolean)? = null)
    suspend fun deleteEntry(id: String)
    suspend fun clearHistory()
}
