package com.cohesionbrew.healthcalculator.auth.firebase

import com.cohesionbrew.healthcalculator.auth.api.AuthServiceProviderFactory

internal actual val authServiceProviderFactory: AuthServiceProviderFactory
    get() = AuthServiceProviderFactory { FirebaseAuthServiceProvider() }