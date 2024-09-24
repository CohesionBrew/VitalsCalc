package com.measify.kappbuilder.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class ApplicationScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = CoroutineScope(SupervisorJob() + Dispatchers.IO).coroutineContext
}