package com.measify.kappbuilder.util

import org.koin.core.module.Module

internal expect val platformModule: Module
internal expect fun onApplicationStartPlatformSpecific()
internal expect val isAndroid: Boolean

