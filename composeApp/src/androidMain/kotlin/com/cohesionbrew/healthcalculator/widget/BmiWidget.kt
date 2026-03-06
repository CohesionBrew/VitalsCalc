package com.cohesionbrew.healthcalculator.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
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

class BmiWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val dp = WidgetDataProvider(context)
        val bmi = dp.getBmiValue()
        val category = dp.getBmiCategory()

        provideContent {
            GlanceTheme {
                WidgetContent(bmi = bmi, category = category)
            }
        }
    }
}

@Composable
private fun WidgetContent(bmi: Float?, category: String?) {
    val hasData = bmi != null
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
            Text("BMI", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GlanceTheme.colors.onSurface))
            Text(
                text = if (hasData) String.format("%.1f", bmi) else "--",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp, color = GlanceTheme.colors.onSurface)
            )
            Text(label, style = TextStyle(fontSize = 12.sp, color = ColorProvider(color)))
            if (!hasData) {
                Text("Open app to calculate", style = TextStyle(fontSize = 10.sp, color = GlanceTheme.colors.secondary), modifier = GlanceModifier.padding(top = 4.dp))
            }
        }
    }
}

class OpenAppAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(it)
        }
    }
}

class BmiWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = BmiWidget()
}
