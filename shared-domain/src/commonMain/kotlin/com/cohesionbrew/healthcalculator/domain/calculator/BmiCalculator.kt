package com.cohesionbrew.healthcalculator.domain.calculator

import kotlin.math.pow

/**
 * Calculator for Body Mass Index (BMI) calculations.
 *
 * Uses the "New BMI" formula which adjusts for height to provide more accurate
 * results across different heights. The formula is:
 * BMI = 1.3 × weight(kg) / height(m)^2.5
 *
 * This formula was proposed by Nick Trefethen at Oxford to address the original
 * BMI formula's tendency to overestimate BMI for tall people and underestimate
 * for short people.
 */
object BmiCalculator {

    // BMI category thresholds (WHO standards)
    private const val UNDERWEIGHT_THRESHOLD = 18.5
    private const val NORMAL_THRESHOLD = 25.0
    private const val OVERWEIGHT_THRESHOLD = 30.0
    private const val OBESE_CLASS_I_THRESHOLD = 35.0
    private const val OBESE_CLASS_II_THRESHOLD = 40.0

    // Healthy BMI range for weight calculations
    private const val HEALTHY_BMI_MIN = 18.5
    private const val HEALTHY_BMI_MAX = 24.9

    // Conversion constants
    private const val KG_TO_LB = 2.20462

    /**
     * Calculate BMI using the "New BMI" formula.
     *
     * @param heightCm Height in centimeters
     * @param weightKg Weight in kilograms
     * @return BMI value (kg/m^2.5), or 0.0 if inputs are invalid
     */
    fun calculate(heightCm: Double, weightKg: Double): Double {
        if (heightCm <= 0 || weightKg <= 0) return 0.0
        val heightM = heightCm / 100.0
        return 1.3 * (weightKg / heightM.pow(2.5))
    }

    /**
     * Get the BMI category index.
     *
     * @param bmi BMI value
     * @return Category index:
     *   - 0 = Underweight (BMI < 18.5)
     *   - 1 = Normal (18.5 <= BMI < 25)
     *   - 2 = Overweight (25 <= BMI < 30)
     *   - 3 = Obese Class I (30 <= BMI < 35)
     *   - 4 = Obese Class II (35 <= BMI < 40)
     *   - 5 = Obese Class III (BMI >= 40)
     */
    fun getCategory(bmi: Double): Int {
        return when {
            bmi < UNDERWEIGHT_THRESHOLD -> 0
            bmi < NORMAL_THRESHOLD -> 1
            bmi < OVERWEIGHT_THRESHOLD -> 2
            bmi < OBESE_CLASS_I_THRESHOLD -> 3
            bmi < OBESE_CLASS_II_THRESHOLD -> 4
            else -> 5
        }
    }

    /**
     * Calculate the healthy weight range for a given height.
     *
     * @param heightCm Height in centimeters
     * @param useMetric If true, returns weights in kg; if false, in pounds
     * @return Pair of (minWeight, maxWeight) in the specified unit
     */
    fun getHealthyWeightRange(heightCm: Double, useMetric: Boolean = true): Pair<Double, Double> {
        if (heightCm <= 0) return Pair(0.0, 0.0)

        val heightM = heightCm / 100.0

        // Reverse the New BMI formula to get weight from BMI and height
        // BMI = 1.3 * weight / height^2.5
        // weight = BMI * height^2.5 / 1.3
        val minWeightKg = HEALTHY_BMI_MIN * heightM.pow(2.5) / 1.3
        val maxWeightKg = HEALTHY_BMI_MAX * heightM.pow(2.5) / 1.3

        return if (useMetric) {
            Pair(minWeightKg, maxWeightKg)
        } else {
            Pair(minWeightKg * KG_TO_LB, maxWeightKg * KG_TO_LB)
        }
    }

    /**
     * Calculate how far the current weight is from the healthy range.
     *
     * @param heightCm Height in centimeters
     * @param weightKg Weight in kilograms
     * @param useMetric If true, returns difference in kg; if false, in pounds
     * @return Difference from healthy range:
     *   - Negative = underweight (how much to gain)
     *   - Zero = within healthy range
     *   - Positive = overweight (how much to lose)
     */
    fun getDifferenceFromHealthy(heightCm: Double, weightKg: Double, useMetric: Boolean = true): Double {
        if (heightCm <= 0 || weightKg <= 0) return 0.0

        val (minWeight, maxWeight) = getHealthyWeightRange(heightCm, useMetric = true)

        return when {
            weightKg < minWeight -> {
                val diff = minWeight - weightKg
                if (useMetric) -diff else -diff * KG_TO_LB
            }
            weightKg > maxWeight -> {
                val diff = weightKg - maxWeight
                if (useMetric) diff else diff * KG_TO_LB
            }
            else -> 0.0
        }
    }
}
