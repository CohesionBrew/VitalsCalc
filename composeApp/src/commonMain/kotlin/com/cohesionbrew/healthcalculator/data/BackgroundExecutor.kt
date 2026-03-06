package com.cohesionbrew.healthcalculator.data

import com.cohesionbrew.healthcalculator.util.defaultAsyncDispatcher
import com.cohesionbrew.healthcalculator.util.logging.AppLogger
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

class BackgroundExecutor(val scope: CoroutineContext = defaultAsyncDispatcher) {

    companion object {
        val IO by lazy { BackgroundExecutor(defaultAsyncDispatcher) }
    }

    suspend fun <T> execute(
        func: suspend () -> Result<T>
    ): Result<T> = withContext(scope) {
        try {
            func.invoke()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            } else {
                AppLogger.e("Error while executing background task: ${e.message}")
                Result.failure(e)
            }
        }
    }
}