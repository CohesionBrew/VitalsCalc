package com.cohesionbrew.healthcalculator.domain.calculator

import com.cohesionbrew.healthcalculator.domain.model.BodyFatCategory
import kotlin.math.log10

/**
 * Calculator for body fat percentage using multiple methods.
 *
 * Supports:
 * - US Navy Method: Uses circumference measurements
 * - Relative Fat Mass (RFM): Uses height-to-waist ratio
 */
object BodyFatCalculator {

    /**
     * US Navy Method calculation.
     *
     * Males: 495 / (1.0324 - 0.19077 * log10(waist - neck) + 0.15456 * log10(height)) - 450
     * Females: 495 / (1.29579 - 0.35004 * log10(waist + hip - neck) + 0.22100 * log10(height)) - 450
     *
     * @param isMale Whether the subject is male
     * @param waistCm Waist circumference in cm
     * @param neckCm Neck circumference in cm
     * @param heightCm Height in cm
     * @param hipCm Hip circumference in cm (required for females)
     * @return Body fat percentage, or 0.0 if inputs are invalid
     */
    fun calculateNavy(
        isMale: Boolean,
        waistCm: Double,
        neckCm: Double,
        heightCm: Double,
        hipCm: Double = 0.0
    ): Double {
        return if (isMale) {
            val circumference = waistCm - neckCm
            if (circumference <= 0) return 0.0
            495 / (1.0324 - 0.19077 * log10(circumference) + 0.15456 * log10(heightCm)) - 450
        } else {
            val circumference = waistCm + hipCm - neckCm
            if (circumference <= 0) return 0.0
            495 / (1.29579 - 0.35004 * log10(circumference) + 0.22100 * log10(heightCm)) - 450
        }
    }

    /**
     * Relative Fat Mass (RFM) Index calculation.
     *
     * Males: 64 - (20 * height / waist)
     * Females: 76 - (20 * height / waist)
     *
     * @param isMale Whether the subject is male
     * @param heightCm Height in cm
     * @param waistCm Waist circumference in cm
     * @return Body fat percentage, or 0.0 if waist is zero
     */
    fun calculateRfm(
        isMale: Boolean,
        heightCm: Double,
        waistCm: Double
    ): Double {
        if (waistCm <= 0) return 0.0

        return if (isMale) {
            64 - (20 * heightCm / waistCm)
        } else {
            76 - (20 * heightCm / waistCm)
        }
    }

    /**
     * Determines body fat category based on percentage and gender.
     */
    fun getCategory(percent: Double, isMale: Boolean): BodyFatCategory {
        return if (isMale) {
            when {
                percent < 6 -> BodyFatCategory.ESSENTIAL
                percent < 14 -> BodyFatCategory.ATHLETES
                percent < 18 -> BodyFatCategory.FITNESS
                percent < 25 -> BodyFatCategory.AVERAGE
                else -> BodyFatCategory.OBESE
            }
        } else {
            when {
                percent < 14 -> BodyFatCategory.ESSENTIAL
                percent < 21 -> BodyFatCategory.ATHLETES
                percent < 25 -> BodyFatCategory.FITNESS
                percent < 32 -> BodyFatCategory.AVERAGE
                else -> BodyFatCategory.OBESE
            }
        }
    }
}
