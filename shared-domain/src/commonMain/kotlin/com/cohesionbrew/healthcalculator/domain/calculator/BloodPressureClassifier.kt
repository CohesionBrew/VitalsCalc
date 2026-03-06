package com.cohesionbrew.healthcalculator.domain.calculator

import com.cohesionbrew.healthcalculator.domain.model.BpCategory

/**
 * Classifies blood pressure readings according to AHA guidelines.
 */
object BloodPressureClassifier {

    /**
     * Classifies a blood pressure reading into an AHA category.
     *
     * @param systolic The systolic (top) number in mmHg
     * @param diastolic The diastolic (bottom) number in mmHg
     * @return The appropriate [BpCategory]
     */
    fun classify(systolic: Int, diastolic: Int): BpCategory {
        return when {
            systolic > 180 || diastolic > 120 -> BpCategory.CRISIS
            systolic >= 140 || diastolic >= 90 -> BpCategory.HYPERTENSION_2
            systolic in 130..139 || diastolic in 80..89 -> BpCategory.HYPERTENSION_1
            systolic in 120..129 && diastolic < 80 -> BpCategory.ELEVATED
            systolic < 120 && diastolic < 80 -> BpCategory.NORMAL
            else -> BpCategory.NORMAL
        }
    }
}

/**
 * Validation utilities for blood pressure inputs.
 */
object BpValidator {
    const val MIN_SYSTOLIC = 60
    const val MAX_SYSTOLIC = 250
    const val MIN_DIASTOLIC = 40
    const val MAX_DIASTOLIC = 150
    const val MIN_PULSE = 40
    const val MAX_PULSE = 200

    fun validateSystolic(value: Int): String? = when {
        value < MIN_SYSTOLIC -> "Systolic must be at least $MIN_SYSTOLIC mmHg"
        value > MAX_SYSTOLIC -> "Systolic cannot exceed $MAX_SYSTOLIC mmHg"
        else -> null
    }

    fun validateDiastolic(value: Int): String? = when {
        value < MIN_DIASTOLIC -> "Diastolic must be at least $MIN_DIASTOLIC mmHg"
        value > MAX_DIASTOLIC -> "Diastolic cannot exceed $MAX_DIASTOLIC mmHg"
        else -> null
    }

    fun validatePulse(value: Int?): String? {
        if (value == null) return null
        return when {
            value < MIN_PULSE -> "Pulse must be at least $MIN_PULSE BPM"
            value > MAX_PULSE -> "Pulse cannot exceed $MAX_PULSE BPM"
            else -> null
        }
    }

    fun validateRelation(systolic: Int, diastolic: Int): String? {
        return if (systolic <= diastolic) "Systolic must be greater than diastolic" else null
    }

    fun validate(systolic: Int, diastolic: Int, pulse: Int? = null): String? {
        return validateSystolic(systolic)
            ?: validateDiastolic(diastolic)
            ?: validatePulse(pulse)
            ?: validateRelation(systolic, diastolic)
    }
}
