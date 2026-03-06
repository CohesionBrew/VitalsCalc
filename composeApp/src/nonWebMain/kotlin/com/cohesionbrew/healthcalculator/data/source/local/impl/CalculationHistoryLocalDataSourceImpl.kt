package com.cohesionbrew.healthcalculator.data.source.local.impl

import com.cohesionbrew.healthcalculator.data.source.local.CalculationHistoryLocalDataSource
import com.cohesionbrew.healthcalculator.data.source.local.dao.CalculationHistoryDao
import com.cohesionbrew.healthcalculator.data.source.local.entity.CalculationHistoryEntity
import com.cohesionbrew.healthcalculator.data.source.local.entity.CalculationHistoryEntityMapper
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationHistoryLocalDataSourceImpl(
    private val historyDao: CalculationHistoryDao,
    private val entityMapper: CalculationHistoryEntityMapper = CalculationHistoryEntityMapper()
) : BaseRoomLocalDataSource<String, CalculationHistoryEntity, CalculationEntry>(
    dao = historyDao,
    mapper = entityMapper
), CalculationHistoryLocalDataSource {

    override fun getByTypeFlow(type: CalculationType): Flow<List<CalculationEntry>> {
        return historyDao.getByTypeFlow(type.key).map { entities ->
            entities.map(entityMapper::toModel)
        }
    }

    override suspend fun getLatestByType(type: CalculationType): CalculationEntry? {
        return historyDao.getLatestByType(type.key)?.let(entityMapper::toModel)
    }

    override fun getCountFlow(): Flow<Int> {
        return historyDao.getCountFlow()
    }

    override fun getHistorySinceFlow(since: Long): Flow<List<CalculationEntry>> {
        return historyDao.getHistorySinceFlow(since).map { entities ->
            entities.map(entityMapper::toModel)
        }
    }

    override suspend fun deleteSameDayEntries(type: CalculationType, dayStart: Long, dayEnd: Long) {
        historyDao.deleteSameDayEntries(type.key, dayStart, dayEnd)
    }
}
