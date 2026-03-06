package com.cohesionbrew.healthcalculator.domain.model.history

import kotlinx.serialization.Serializable

/**
 * Sealed hierarchy of calculation history entries.
 *
 * Each subtype stores type-specific fields while sharing common properties
 * (id, type, primaryValue, category, createdAt) for display and filtering.
 *
 * Stored in Room via a single table with JSON serialization for type-specific data.
 */
sealed interface CalculationEntry {
    val id: String
    val type: CalculationType
    val primaryValue: Double
    val category: String?
    val createdAt: Long
}

@Serializable
data class BmiHistoryEntry(
    override val id: String,
    override val primaryValue: Double,
    override val category: String? = null,
    override val createdAt: Long,
    val heightCm: Double,
    val weightKg: Double,
    val healthyMinKg: Double? = null,
    val healthyMaxKg: Double? = null
) : CalculationEntry {
    override val type: CalculationType get() = CalculationType.BMI
}

@Serializable
data class BmrHistoryEntry(
    override val id: String,
    override val primaryValue: Double,
    override val category: String? = null,
    override val createdAt: Long,
    val formula: String,
    val tdee: Double? = null,
    val activityLevel: String? = null,
    val gender: String,
    val heightCm: Double,
    val weightKg: Double,
    val age: Int
) : CalculationEntry {
    override val type: CalculationType get() = CalculationType.BMR
}

@Serializable
data class BodyFatHistoryEntry(
    override val id: String,
    override val primaryValue: Double,
    override val category: String? = null,
    override val createdAt: Long,
    val method: String,
    val isMale: Boolean,
    val waistCm: Double? = null,
    val neckCm: Double? = null,
    val hipCm: Double? = null,
    val heightCm: Double,
    val weightKg: Double? = null,
    val age: Int? = null
) : CalculationEntry {
    override val type: CalculationType get() = CalculationType.BODY_FAT
}

@Serializable
data class BloodPressureHistoryEntry(
    override val id: String,
    override val primaryValue: Double,
    override val category: String? = null,
    override val createdAt: Long,
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int? = null,
    val notes: String? = null
) : CalculationEntry {
    override val type: CalculationType get() = CalculationType.BLOOD_PRESSURE
}

@Serializable
data class WeightHistoryEntry(
    override val id: String,
    override val primaryValue: Double,
    override val category: String? = null,
    override val createdAt: Long,
    val weightKg: Double,
    val bmi: Double? = null,
    val bodyFatPercent: Double? = null
) : CalculationEntry {
    override val type: CalculationType get() = CalculationType.WEIGHT
}
