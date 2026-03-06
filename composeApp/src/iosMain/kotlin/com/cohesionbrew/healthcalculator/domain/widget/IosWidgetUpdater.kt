package com.cohesionbrew.healthcalculator.domain.widget

import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import platform.Foundation.NSDate
import platform.Foundation.NSUserDefaults
import platform.Foundation.timeIntervalSince1970

class IosWidgetUpdater : WidgetUpdater {

    private val userDefaults: NSUserDefaults?
        get() = NSUserDefaults(suiteName = APP_GROUP_ID)

    override suspend fun updateWidget(type: CalculationType, value: Double, category: String?) {
        val defaults = userDefaults ?: return
        defaults.setDouble(value, forKey = keyValue(type))
        defaults.setObject(category, forKey = keyCategory(type))
        defaults.setDouble(
            NSDate().timeIntervalSince1970 * 1000,
            forKey = keyTimestamp(type)
        )
        defaults.synchronize()
    }

    override suspend fun updateAllWidgets() {
        // Will trigger WidgetKit reloads in Phase 10
    }

    companion object {
        const val APP_GROUP_ID = "group.com.cohesionbrew.healthcalculator"

        fun keyValue(type: CalculationType) = "widget_${type.key}_value"
        fun keyCategory(type: CalculationType) = "widget_${type.key}_category"
        fun keyTimestamp(type: CalculationType) = "widget_${type.key}_timestamp"
    }
}
