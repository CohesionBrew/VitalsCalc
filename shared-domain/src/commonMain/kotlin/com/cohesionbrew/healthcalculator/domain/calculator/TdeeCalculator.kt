package com.cohesionbrew.healthcalculator.domain.calculator

/**
 * Calculator for Total Daily Energy Expenditure (TDEE).
 *
 * TDEE = BMR × Activity Level Multiplier
 */
object TdeeCalculator {

    enum class ActivityLevel(val key: String, val multiplier: Double, val description: String) {
        SEDENTARY("sedentary", 1.2, "Little or no exercise"),
        LIGHTLY_ACTIVE("lightly_active", 1.375, "Light exercise 1-3 days/week"),
        MODERATELY_ACTIVE("moderately_active", 1.55, "Moderate exercise 3-5 days/week"),
        VERY_ACTIVE("very_active", 1.725, "Hard exercise 6-7 days/week"),
        EXTRA_ACTIVE("extra_active", 1.9, "Very hard exercise or physical job");

        companion object {
            fun fromKey(key: String): ActivityLevel {
                return entries.find { it.key == key } ?: SEDENTARY
            }
        }
    }

    enum class CalorieGoal(val key: String, val multiplier: Double, val description: String) {
        FASTER_WEIGHT_LOSS("faster_weight_loss", 0.75, "Faster weight loss (-25%)"),
        WEIGHT_LOSS("weight_loss", 0.85, "Weight loss (-15%)"),
        MAINTENANCE("maintenance", 1.0, "Maintenance"),
        WEIGHT_GAIN("weight_gain", 1.10, "Weight gain (+10%)"),
        FASTER_WEIGHT_GAIN("faster_weight_gain", 1.15, "Faster weight gain (+15%)");

        companion object {
            fun fromKey(key: String): CalorieGoal {
                return entries.find { it.key == key } ?: MAINTENANCE
            }
        }
    }

    fun calculate(bmr: Double, activityLevel: ActivityLevel): Double {
        return bmr * activityLevel.multiplier
    }

    fun calculateFromKey(bmr: Double, activityLevelKey: String): Double {
        return calculate(bmr, ActivityLevel.fromKey(activityLevelKey))
    }

    fun calculateGoalCalories(tdee: Double, bmr: Double, goal: CalorieGoal): Int {
        val goalCalories = (tdee * goal.multiplier).toInt()
        return maxOf(goalCalories, bmr.toInt())
    }

    fun calculateGoalCaloriesFromKey(tdee: Double, bmr: Double, goalKey: String): Int {
        return calculateGoalCalories(tdee, bmr, CalorieGoal.fromKey(goalKey))
    }
}
