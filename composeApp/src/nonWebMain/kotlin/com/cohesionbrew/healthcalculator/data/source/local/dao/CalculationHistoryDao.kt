package com.cohesionbrew.healthcalculator.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.cohesionbrew.healthcalculator.data.source.local.entity.CalculationHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationHistoryDao : BaseRoomDao<String, CalculationHistoryEntity> {
    @Query("SELECT * FROM calculation_history WHERE id = :id")
    override suspend fun getById(id: String): CalculationHistoryEntity?

    @Query("SELECT * FROM calculation_history WHERE id = :id")
    override fun getByIdFlow(id: String): Flow<CalculationHistoryEntity?>

    @Query("SELECT * FROM calculation_history ORDER BY created_at DESC")
    override fun getAllFlow(): Flow<List<CalculationHistoryEntity>>

    @Query("SELECT * FROM calculation_history ORDER BY created_at DESC")
    override suspend fun getAll(): List<CalculationHistoryEntity>

    @Query("SELECT * FROM calculation_history WHERE type = :type ORDER BY created_at DESC")
    fun getByTypeFlow(type: String): Flow<List<CalculationHistoryEntity>>

    @Query("SELECT * FROM calculation_history WHERE type = :type ORDER BY created_at DESC LIMIT 1")
    suspend fun getLatestByType(type: String): CalculationHistoryEntity?

    @Query("SELECT COUNT(*) FROM calculation_history")
    fun getCountFlow(): Flow<Int>

    @Query("SELECT * FROM calculation_history WHERE created_at >= :since ORDER BY created_at DESC")
    fun getHistorySinceFlow(since: Long): Flow<List<CalculationHistoryEntity>>

    @Query("DELETE FROM calculation_history WHERE type = :type AND created_at >= :dayStart AND created_at < :dayEnd")
    suspend fun deleteSameDayEntries(type: String, dayStart: Long, dayEnd: Long)

    @Upsert
    override suspend fun upsert(entity: CalculationHistoryEntity)

    @Query("DELETE FROM calculation_history WHERE id = :id")
    override suspend fun deleteById(id: String)

    @Delete
    override suspend fun delete(entity: CalculationHistoryEntity)

    @Query("DELETE FROM calculation_history")
    override suspend fun deleteAll()
}
