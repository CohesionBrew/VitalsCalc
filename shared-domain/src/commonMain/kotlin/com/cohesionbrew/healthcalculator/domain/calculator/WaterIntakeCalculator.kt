package com.cohesionbrew.healthcalculator.domain.calculator

import kotlin.math.roundToInt

/**
 * Calculator for recommended daily water intake.
 *
 * Formula:
 * - Base: 33 mL per kg body weight (middle of 30-35 range)
 * - Activity multiplier: 1.0 (sedentary) to 1.5 (very active)
 * - Climate multiplier: 1.0 (normal) or 1.2 (hot/humid)
 */
object WaterIntakeCalculator {

    data class WaterIntakeResult(
        val liters: Double,
        val ounces: Double
    )

    fun calculateBaseWaterIntakeMl(weightKg: Double): Double = weightKg * 33.0

    /**
     * Activity multiplier for water intake.
     * Scale: 1 (sedentary) to 5 (very active)
     * Returns multiplier from 1.0 to 1.5
     */
    fun calculateActivityMultiplier(activityLevel: Int): Double {
        val clampedLevel = activityLevel.coerceIn(1, 5)
        return 1.0 + (clampedLevel - 1) * 0.125
    }

    fun calculateClimateMultiplier(isHotClimate: Boolean): Double {
        return if (isHotClimate) 1.2 else 1.0
    }

    /**
     * Calculate recommended daily water intake.
     *
     * @param weightKg Body weight in kilograms
     * @param activityLevel Activity level from 1 (sedentary) to 5 (very active)
     * @param isHotClimate Whether in hot/humid climate
     * @return Result with liters and ounces (rounded to 1 decimal)
     */
    fun calculate(
        weightKg: Double,
        activityLevel: Int,
        isHotClimate: Boolean
    ): WaterIntakeResult {
        val baseMl = calculateBaseWaterIntakeMl(weightKg)
        val activityMultiplier = calculateActivityMultiplier(activityLevel)
        val climateMultiplier = calculateClimateMultiplier(isHotClimate)

        val totalMl = baseMl * activityMultiplier * climateMultiplier
        val liters = totalMl / 1000.0
        val ounces = liters * 33.814

        return WaterIntakeResult(
            liters = roundToOneDecimal(liters),
            ounces = roundToOneDecimal(ounces)
        )
    }

    private fun roundToOneDecimal(value: Double): Double {
        return (value * 10).roundToInt() / 10.0
    }
}
