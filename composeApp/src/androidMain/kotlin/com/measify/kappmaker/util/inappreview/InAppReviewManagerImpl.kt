package com.measify.kappmaker.util.inappreview

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.review.ReviewManagerFactory
import com.measify.kappmaker.util.logging.AppLogger

class InAppReviewManagerImpl : InAppReviewManager {

    @Composable
    override fun requestReview() {
        val context: Context = LocalContext.current
        val activity = context as? ComponentActivity
        LaunchedEffect(Unit) {
            if (activity == null) return@LaunchedEffect
            val manager = ReviewManagerFactory.create(context.applicationContext)
            manager.requestReviewFlow().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    manager.launchReviewFlow(activity, reviewInfo)
                } else {
                    val reviewError = task.exception
                    AppLogger.e("Error occurred while reviewing the app", reviewError)
                }
            }

        }


    }
}