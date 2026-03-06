package com.cohesionbrew.healthcalculator.domain.calculator

import com.cohesionbrew.healthcalculator.domain.model.IdealWeightResults

/**
 * Calculator for ideal body weight using multiple formulas.
 *
 * All formulas use height in inches over 5 feet as input.
 * Results are in kilograms.
 */
object IdealWeightCalculator {

    fun calculateInchesOver5Feet(heightCm: Double): Double {
        val heightInches = heightCm / 2.54
        return (heightInches - 60).coerceAtLeast(0.0)
    }

    /** Robinson Formula (1983) */
    fun calculateRobinson(isMale: Boolean, inchesOver5Feet: Double): Double {
        return if (isMale) 52.0 + 1.9 * inchesOver5Feet
        else 49.0 + 1.7 * inchesOver5Feet
    }

    /** Miller Formula (1983) */
    fun calculateMiller(isMale: Boolean, inchesOver5Feet: Double): Double {
        return if (isMale) 56.2 + 1.41 * inchesOver5Feet
        else 53.1 + 1.36 * inchesOver5Feet
    }

    /** Devine Formula (1974) */
    fun calculateDevine(isMale: Boolean, inchesOver5Feet: Double): Double {
        return if (isMale) 50.0 + 2.3 * inchesOver5Feet
        else 45.5 + 2.3 * inchesOver5Feet
    }

    /** Hamwi Formula (1964) */
    fun calculateHamwi(isMale: Boolean, inchesOver5Feet: Double): Double {
        return if (isMale) 48.0 + 2.7 * inchesOver5Feet
        else 45.5 + 2.2 * inchesOver5Feet
    }

    /**
     * Calculate ideal weight using all formulas.
     *
     * @param heightCm Height in centimeters
     * @param isMale Whether the subject is male
     * @return Results from all 4 formulas
     */
    fun calculateAll(heightCm: Double, isMale: Boolean): IdealWeightResults {
        val inchesOver5Feet = calculateInchesOver5Feet(heightCm)

        return IdealWeightResults(
            robinsonKg = calculateRobinson(isMale, inchesOver5Feet),
            millerKg = calculateMiller(isMale, inchesOver5Feet),
            devineKg = calculateDevine(isMale, inchesOver5Feet),
            hamwiKg = calculateHamwi(isMale, inchesOver5Feet)
        )
    }
}
