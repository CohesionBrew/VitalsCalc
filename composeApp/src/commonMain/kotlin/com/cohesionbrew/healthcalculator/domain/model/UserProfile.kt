package com.cohesionbrew.healthcalculator.domain.model

data class UserProfile(
    val id: String = "default",
    val name: String? = null,
    val dob: String? = null,
    val gender: String = "male",
    val heightCm: Double = 0.0,
    val weightKg: Double = 0.0,
    val bodyFatPct: Double? = null,
    val waistCm: Double = 0.0,
    val neckCm: Double = 0.0,
    val hipCm: Double = 0.0,
    val restingHr: Int = 0,
    val useMetric: Boolean = true,
    val activityLevel: String = "sedentary",
    val calorieGoal: String = "maintenance",
    val bmrFormula: String = "mifflin",
    val advancedMode: Boolean = false,
    val language: String = "en"
)
