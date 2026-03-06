package com.cohesionbrew.healthcalculator.auth.firebase

import com.cohesionbrew.healthcalculator.auth.api.AuthServiceProviderFactory

val AuthServiceProviderFactory.Companion.Firebase: AuthServiceProviderFactory
    get() = authServiceProviderFactory

internal expect val authServiceProviderFactory: AuthServiceProviderFactory