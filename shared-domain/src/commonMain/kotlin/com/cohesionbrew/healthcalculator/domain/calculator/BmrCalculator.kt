package com.cohesionbrew.healthcalculator.domain.calculator

/**
 * Calculator for Basal Metabolic Rate (BMR) using multiple formulas.
 *
 * BMR represents the number of calories your body burns at rest to maintain
 * vital functions like breathing, circulation, and cell production.
 *
 * Supported formulas:
 * - Mifflin-St Jeor (1990) - Most accurate for most people
 * - Harris-Benedict (1919) - Original formula, still widely used
 * - Katch-McArdle (1975) - Uses lean body mass, good for athletic individuals
 * - Oxford/Henry (2005) - Age-bracket based, used by WHO
 */
object BmrCalculator {

    fun calculateMifflinStJeor(
        gender: String,
        weightKg: Double,
        heightCm: Double,
        age: Int
    ): Double {
        val base = (10 * weightKg) + (6.25 * heightCm) - (5 * age)
        return if (gender.lowercase() == "male") base + 5 else base - 161
    }

    fun calculateHarrisBenedict(
        gender: String,
        weightKg: Double,
        heightCm: Double,
        age: Int
    ): Double {
        return if (gender.lowercase() == "male") {
            66.5 + (13.75 * weightKg) + (5.003 * heightCm) - (6.75 * age)
        } else {
            655.1 + (9.563 * weightKg) + (1.850 * heightCm) - (4.676 * age)
        }
    }

    fun calculateKatchMcArdle(
        weightKg: Double,
        bodyFatPercentage: Double
    ): Double {
        val leanBodyMass = weightKg * (1 - bodyFatPercentage / 100)
        return 370 + (21.6 * leanBodyMass)
    }

    fun calculateOxfordHenry(
        gender: String,
        weightKg: Double,
        age: Int
    ): Double {
        val isMale = gender.lowercase() == "male"

        return when {
            age < 31 -> if (isMale) {
                15.1 * weightKg + 692
            } else {
                14.8 * weightKg + 486
            }
            age < 61 -> if (isMale) {
                11.5 * weightKg + 873
            } else {
                8.13 * weightKg + 846
            }
            else -> if (isMale) {
                11.7 * weightKg + 587
            } else {
                9.08 * weightKg + 658
            }
        }
    }

    data class BmrResult(
        val formula: String,
        val bmr: Double
    )

    fun calculateAll(
        gender: String,
        weightKg: Double,
        heightCm: Double,
        age: Int,
        bodyFatPercentage: Double? = null
    ): List<BmrResult> {
        val results = mutableListOf(
            BmrResult("Oxford/Henry", calculateOxfordHenry(gender, weightKg, age)),
            BmrResult("Mifflin-St Jeor", calculateMifflinStJeor(gender, weightKg, heightCm, age)),
            BmrResult("Harris-Benedict", calculateHarrisBenedict(gender, weightKg, heightCm, age))
        )

        if (bodyFatPercentage != null && bodyFatPercentage > 0) {
            results.add(BmrResult("Katch-McArdle", calculateKatchMcArdle(weightKg, bodyFatPercentage)))
        }

        return results
    }

    fun calculateAverage(
        gender: String,
        weightKg: Double,
        heightCm: Double,
        age: Int,
        bodyFatPercentage: Double? = null
    ): Double {
        val results = calculateAll(gender, weightKg, heightCm, age, bodyFatPercentage)
        return results.map { it.bmr }.average()
    }
}
