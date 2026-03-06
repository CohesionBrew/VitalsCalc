package com.cohesionbrew.healthcalculator.domain.model.history

enum class CalculationType(val key: String) {
    BMI("bmi"),
    BMR("bmr"),
    BODY_FAT("body_fat"),
    IDEAL_WEIGHT("ideal_weight"),
    WATER_INTAKE("water_intake"),
    BLOOD_PRESSURE("blood_pressure"),
    WEIGHT("weight");

    companion object {
        fun fromKey(key: String): CalculationType {
            return entries.find { it.key == key } ?: BMI
        }
    }
}
