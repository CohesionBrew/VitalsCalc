package com.cohesionbrew.healthcalculator.data.source.local.impl

import com.cohesionbrew.healthcalculator.data.source.local.CalculationHistoryLocalDataSource
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InMemoryCalculationHistoryLocalDataSource :
    BaseInMemoryLocalDataSource<String, CalculationEntry>({ it.id }),
    CalculationHistoryLocalDataSource {

    override fun getByTypeFlow(type: CalculationType): Flow<List<CalculationEntry>> =
        getAllFlow().map { list -> list.filter { it.type == type }.sortedByDescending { it.createdAt } }

    override suspend fun getLatestByType(type: CalculationType): CalculationEntry? =
        getAll().filter { it.type == type }.maxByOrNull { it.createdAt }

    override fun getCountFlow(): Flow<Int> =
        getAllFlow().map { it.size }

    override fun getHistorySinceFlow(since: Long): Flow<List<CalculationEntry>> =
        getAllFlow().map { list -> list.filter { it.createdAt >= since }.sortedByDescending { it.createdAt } }

    override suspend fun deleteSameDayEntries(type: CalculationType, dayStart: Long, dayEnd: Long) {
        val toDelete = getAll().filter { it.type == type && it.createdAt in dayStart until dayEnd }
        toDelete.forEach { deleteById(it.id) }
    }
}
