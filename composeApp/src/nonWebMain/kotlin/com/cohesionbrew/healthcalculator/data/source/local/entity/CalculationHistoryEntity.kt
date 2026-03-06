@file:OptIn(ExperimentalUuidApi::class)

package com.cohesionbrew.healthcalculator.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cohesionbrew.healthcalculator.domain.model.history.BmiHistoryEntry
import com.cohesionbrew.healthcalculator.domain.model.history.BmrHistoryEntry
import com.cohesionbrew.healthcalculator.domain.model.history.BloodPressureHistoryEntry
import com.cohesionbrew.healthcalculator.domain.model.history.BodyFatHistoryEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import com.cohesionbrew.healthcalculator.domain.model.history.WeightHistoryEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "calculation_history",
    indices = [Index(value = ["type"]), Index(value = ["created_at"])]
)
data class CalculationHistoryEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = Uuid.random().toString(),
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "data_json") val dataJson: String,
    @ColumnInfo(name = "primary_value") val primaryValue: Double,
    @ColumnInfo(name = "category") val category: String? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)

class CalculationHistoryEntityMapper : EntityMapper<CalculationHistoryEntity, CalculationEntry> {

    private val json = Json { ignoreUnknownKeys = true }

    override fun toEntity(model: CalculationEntry): CalculationHistoryEntity {
        val dataJson = when (model) {
            is BmiHistoryEntry -> json.encodeToString(model)
            is BmrHistoryEntry -> json.encodeToString(model)
            is BodyFatHistoryEntry -> json.encodeToString(model)
            is BloodPressureHistoryEntry -> json.encodeToString(model)
            is WeightHistoryEntry -> json.encodeToString(model)
        }
        return CalculationHistoryEntity(
            id = model.id,
            type = model.type.key,
            dataJson = dataJson,
            primaryValue = model.primaryValue,
            category = model.category,
            createdAt = model.createdAt
        )
    }

    override fun toModel(entity: CalculationHistoryEntity): CalculationEntry {
        return when (CalculationType.fromKey(entity.type)) {
            CalculationType.BMI -> json.decodeFromString<BmiHistoryEntry>(entity.dataJson)
            CalculationType.BMR -> json.decodeFromString<BmrHistoryEntry>(entity.dataJson)
            CalculationType.BODY_FAT -> json.decodeFromString<BodyFatHistoryEntry>(entity.dataJson)
            CalculationType.BLOOD_PRESSURE -> json.decodeFromString<BloodPressureHistoryEntry>(entity.dataJson)
            CalculationType.WEIGHT -> json.decodeFromString<WeightHistoryEntry>(entity.dataJson)
            CalculationType.IDEAL_WEIGHT,
            CalculationType.WATER_INTAKE -> {
                // These types don't save history, but handle gracefully
                json.decodeFromString<BmiHistoryEntry>(entity.dataJson)
            }
        }
    }
}
