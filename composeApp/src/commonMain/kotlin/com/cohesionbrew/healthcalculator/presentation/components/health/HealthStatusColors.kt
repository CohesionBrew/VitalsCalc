package com.cohesionbrew.healthcalculator.presentation.components.health

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

data class HealthStatusColors(
    val healthy: Color,
    val warning: Color,
    val alert: Color,
    val critical: Color,
    val info: Color,
    val neutral: Color
)

@Composable
fun rememberHealthStatusColors(): HealthStatusColors {
    val isDark = isSystemInDarkTheme()
    return remember(isDark) {
        if (isDark) {
            HealthStatusColors(
                healthy = Color(0xFF81C784),
                warning = Color(0xFFFFB74D),
                alert = Color(0xFFFF8A65),
                critical = Color(0xFFEF5350),
                info = Color(0xFF64B5F6),
                neutral = Color(0xFF90A4AE)
            )
        } else {
            HealthStatusColors(
                healthy = Color(0xFF2E7D32),
                warning = Color(0xFFE65100),
                alert = Color(0xFFD84315),
                critical = Color(0xFFC62828),
                info = Color(0xFF1565C0),
                neutral = Color(0xFF546E7A)
            )
        }
    }
}

@Composable
fun getBmiCategoryColor(categoryIndex: Int): Color {
    val colors = rememberHealthStatusColors()
    return when (categoryIndex) {
        0 -> colors.info
        1 -> colors.healthy
        2 -> colors.warning
        3 -> colors.alert
        4 -> colors.critical
        5 -> colors.critical
        else -> colors.info
    }
}

@Composable
fun getBpCategoryColor(category: String): Color {
    val colors = rememberHealthStatusColors()
    return when (category.lowercase()) {
        "normal" -> colors.healthy
        "elevated" -> colors.warning
        "hypertension stage 1" -> colors.alert
        "hypertension stage 2" -> colors.critical
        "hypertensive crisis" -> colors.critical
        else -> colors.neutral
    }
}

@Composable
fun getBodyFatCategoryColor(category: String): Color {
    val colors = rememberHealthStatusColors()
    return when (category.lowercase()) {
        "essential" -> colors.info
        "athletes" -> colors.healthy
        "fitness" -> colors.healthy
        "average" -> colors.warning
        "obese" -> colors.critical
        else -> colors.neutral
    }
}

@Composable
fun getCalorieGoalColor(goal: String): Color {
    val colors = rememberHealthStatusColors()
    return when (goal) {
        "faster_weight_loss" -> colors.alert
        "weight_loss" -> colors.warning
        "maintenance" -> colors.healthy
        "weight_gain" -> colors.info
        "faster_weight_gain" -> colors.info
        else -> colors.healthy
    }
}

@Composable
fun getTrainingZoneColor(zone: Int): Color {
    val colors = rememberHealthStatusColors()
    return when (zone) {
        1 -> colors.info
        2 -> colors.healthy
        3 -> colors.healthy
        4 -> colors.warning
        5 -> colors.critical
        else -> colors.neutral
    }
}
