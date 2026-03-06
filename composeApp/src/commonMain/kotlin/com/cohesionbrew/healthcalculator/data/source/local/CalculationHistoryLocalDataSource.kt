package com.cohesionbrew.healthcalculator.data.source.local

import com.cohesionbrew.healthcalculator.domain.model.history.CalculationEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import kotlinx.coroutines.flow.Flow

interface CalculationHistoryLocalDataSource : LocalDataSource<String, CalculationEntry> {
    fun getByTypeFlow(type: CalculationType): Flow<List<CalculationEntry>>
    suspend fun getLatestByType(type: CalculationType): CalculationEntry?
    fun getCountFlow(): Flow<Int>
    fun getHistorySinceFlow(since: Long): Flow<List<CalculationEntry>>
    suspend fun deleteSameDayEntries(type: CalculationType, dayStart: Long, dayEnd: Long)
}
