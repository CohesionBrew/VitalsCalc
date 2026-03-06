package com.cohesionbrew.healthcalculator.widget

import android.content.Context
import com.cohesionbrew.healthcalculator.domain.widget.AndroidWidgetUpdater

class WidgetDataProvider(private val context: Context) {

    private val prefs = context.getSharedPreferences(AndroidWidgetUpdater.PREFS_NAME, Context.MODE_PRIVATE)

    // BMI
    fun getBmiValue(): Float? =
        if (prefs.contains(KEY_BMI_VALUE)) prefs.getFloat(KEY_BMI_VALUE, 0f) else null

    fun getBmiCategory(): String? = prefs.getString(KEY_BMI_CATEGORY, null)

    // Blood Pressure
    fun getBpValue(): Float? =
        if (prefs.contains(KEY_BP_VALUE)) prefs.getFloat(KEY_BP_VALUE, 0f) else null

    fun getBpCategory(): String? = prefs.getString(KEY_BP_CATEGORY, null)

    // Body Fat
    fun getBodyFatValue(): Float? =
        if (prefs.contains(KEY_BF_VALUE)) prefs.getFloat(KEY_BF_VALUE, 0f) else null

    fun getBodyFatCategory(): String? = prefs.getString(KEY_BF_CATEGORY, null)

    // Heart Rate
    fun getHeartRateValue(): Float? =
        if (prefs.contains(KEY_HR_VALUE)) prefs.getFloat(KEY_HR_VALUE, 0f) else null

    fun getHeartRateCategory(): String? = prefs.getString(KEY_HR_CATEGORY, null)

    companion object {
        private const val KEY_BMI_VALUE = "widget_bmi_value"
        private const val KEY_BMI_CATEGORY = "widget_bmi_category"
        private const val KEY_BP_VALUE = "widget_blood_pressure_value"
        private const val KEY_BP_CATEGORY = "widget_blood_pressure_category"
        private const val KEY_BF_VALUE = "widget_body_fat_value"
        private const val KEY_BF_CATEGORY = "widget_body_fat_category"
        private const val KEY_HR_VALUE = "widget_heart_rate_value"
        private const val KEY_HR_CATEGORY = "widget_heart_rate_category"

        fun getCategoryColor(category: String?): Long = when (category?.uppercase()) {
            "UNDERWEIGHT" -> 0xFF1E88E5
            "NORMAL", "NORMAL_WEIGHT" -> 0xFF4CAF50
            "OVERWEIGHT" -> 0xFFFDD835
            "OBESE_I", "OBESE_CLASS_I" -> 0xFFFB8C00
            "OBESE_II", "OBESE_CLASS_II" -> 0xFFD32F2F
            "OBESE_III", "OBESE_CLASS_III" -> 0xFFB71C1C
            "ELEVATED" -> 0xFFFDD835
            "HYPERTENSION_STAGE_1" -> 0xFFFB8C00
            "HYPERTENSION_STAGE_2" -> 0xFFD32F2F
            "HYPERTENSIVE_CRISIS" -> 0xFFB71C1C
            "ESSENTIAL" -> 0xFF1E88E5
            "ATHLETES" -> 0xFF4CAF50
            "FITNESS" -> 0xFF66BB6A
            "AVERAGE" -> 0xFFFDD835
            "OBESE" -> 0xFFD32F2F
            else -> 0xFF9E9E9E
        }

        fun formatCategoryLabel(category: String?): String = when (category?.uppercase()) {
            "UNDERWEIGHT" -> "Underweight"
            "NORMAL", "NORMAL_WEIGHT" -> "Normal"
            "OVERWEIGHT" -> "Overweight"
            "OBESE_I", "OBESE_CLASS_I" -> "Obese I"
            "OBESE_II", "OBESE_CLASS_II" -> "Obese II"
            "OBESE_III", "OBESE_CLASS_III" -> "Obese III"
            "ELEVATED" -> "Elevated"
            "HYPERTENSION_STAGE_1" -> "Stage 1"
            "HYPERTENSION_STAGE_2" -> "Stage 2"
            "HYPERTENSIVE_CRISIS" -> "Crisis"
            "ESSENTIAL" -> "Essential"
            "ATHLETES" -> "Athletes"
            "FITNESS" -> "Fitness"
            "AVERAGE" -> "Average"
            "OBESE" -> "Obese"
            null -> "No data"
            else -> category.replace("_", " ").lowercase()
                .replaceFirstChar { it.uppercase() }
        }
    }
}
