package com.cohesionbrew.healthcalculator.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

class HeartRateWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val dp = WidgetDataProvider(context)
        val maxHr = dp.getHeartRateValue()
        val zone = dp.getHeartRateCategory()

        provideContent {
            GlanceTheme {
                HrWidgetContent(maxHr = maxHr, zone = zone)
            }
        }
    }
}

@Composable
private fun HrWidgetContent(maxHr: Float?, zone: String?) {
    val hasData = maxHr != null && maxHr > 0
    val heartColor = Color(0xFFE53935)

    Box(
        modifier = GlanceModifier.fillMaxSize()
            .background(GlanceTheme.colors.widgetBackground)
            .clickable(actionRunCallback<OpenAppAction>())
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Heart Rate", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GlanceTheme.colors.onSurface))
            Text(
                text = if (hasData) "${maxHr!!.toInt()}" else "-- bpm",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp, color = if (hasData) ColorProvider(heartColor) else GlanceTheme.colors.onSurface)
            )
            if (hasData) {
                Text("Max bpm", style = TextStyle(fontSize = 10.sp, color = GlanceTheme.colors.secondary))
            }
            if (zone != null && zone.isNotEmpty()) {
                Text(zone, style = TextStyle(fontSize = 11.sp, color = GlanceTheme.colors.secondary), modifier = GlanceModifier.padding(top = 2.dp))
            }
            if (!hasData) {
                Text("Open app to calculate", style = TextStyle(fontSize = 10.sp, color = GlanceTheme.colors.secondary), modifier = GlanceModifier.padding(top = 4.dp))
            }
        }
    }
}

class HeartRateWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = HeartRateWidget()
}
