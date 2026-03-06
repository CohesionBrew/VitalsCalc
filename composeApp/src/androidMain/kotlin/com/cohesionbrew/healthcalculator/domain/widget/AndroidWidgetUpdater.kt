package com.cohesionbrew.healthcalculator.domain.widget

import android.content.Context
import android.content.SharedPreferences
import androidx.glance.appwidget.updateAll
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import com.cohesionbrew.healthcalculator.widget.BmiWidget
import com.cohesionbrew.healthcalculator.widget.BloodPressureWidget
import com.cohesionbrew.healthcalculator.widget.BodyFatWidget

class AndroidWidgetUpdater(private val context: Context) : WidgetUpdater {

    private val prefs: SharedPreferences
        get() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun updateWidget(type: CalculationType, value: Double, category: String?) {
        prefs.edit()
            .putFloat(keyValue(type), value.toFloat())
            .putString(keyCategory(type), category)
            .putLong(keyTimestamp(type), System.currentTimeMillis())
            .apply()

        try {
            when (type) {
                CalculationType.BMI -> BmiWidget().updateAll(context)
                CalculationType.BLOOD_PRESSURE -> BloodPressureWidget().updateAll(context)
                CalculationType.BODY_FAT -> BodyFatWidget().updateAll(context)
                else -> {} // No widget for this type
            }
        } catch (_: Exception) {
            // Widget might not be placed yet
        }
    }

    override suspend fun updateAllWidgets() {
        try {
            BmiWidget().updateAll(context)
            BloodPressureWidget().updateAll(context)
            BodyFatWidget().updateAll(context)
        } catch (_: Exception) {}
    }

    companion object {
        const val PREFS_NAME = "vitals_widget_data"

        fun keyValue(type: CalculationType) = "widget_${type.key}_value"
        fun keyCategory(type: CalculationType) = "widget_${type.key}_category"
        fun keyTimestamp(type: CalculationType) = "widget_${type.key}_timestamp"
    }
}
