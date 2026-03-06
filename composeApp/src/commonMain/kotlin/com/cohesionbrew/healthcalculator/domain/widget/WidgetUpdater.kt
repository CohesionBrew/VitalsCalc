package com.cohesionbrew.healthcalculator.domain.widget

import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType

interface WidgetUpdater {
    suspend fun updateWidget(type: CalculationType, value: Double, category: String?)
    suspend fun updateAllWidgets()
}

class NoOpWidgetUpdater : WidgetUpdater {
    override suspend fun updateWidget(type: CalculationType, value: Double, category: String?) {}
    override suspend fun updateAllWidgets() {}
}
