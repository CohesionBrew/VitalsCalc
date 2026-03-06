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

class BodyFatWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val dp = WidgetDataProvider(context)
        val bodyFat = dp.getBodyFatValue()
        val category = dp.getBodyFatCategory()

        provideContent {
            GlanceTheme {
                BodyFatWidgetContent(bodyFat = bodyFat, category = category)
            }
        }
    }
}

@Composable
private fun BodyFatWidgetContent(bodyFat: Float?, category: String?) {
    val hasData = bodyFat != null
    val label = WidgetDataProvider.formatCategoryLabel(category)
    val color = Color(WidgetDataProvider.getCategoryColor(category))

    Box(
        modifier = GlanceModifier.fillMaxSize()
            .background(GlanceTheme.colors.widgetBackground)
            .clickable(actionRunCallback<OpenAppAction>())
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Body Fat", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GlanceTheme.colors.onSurface))
            Text(
                text = if (hasData) String.format("%.1f%%", bodyFat) else "--%",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp, color = GlanceTheme.colors.onSurface)
            )
            Text(label, style = TextStyle(fontSize = 12.sp, color = ColorProvider(color)))
            if (!hasData) {
                Text("Open app to calculate", style = TextStyle(fontSize = 10.sp, color = GlanceTheme.colors.secondary), modifier = GlanceModifier.padding(top = 4.dp))
            }
        }
    }
}

class BodyFatWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = BodyFatWidget()
}
