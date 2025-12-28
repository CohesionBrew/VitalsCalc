package com.measify.kappmaker.auth.firebase

import com.measify.kappmaker.auth.api.AuthServiceProviderFactory

val AuthServiceProviderFactory.Companion.Firebase: AuthServiceProviderFactory
    get() = authServiceProviderFactory

internal expect val authServiceProviderFactory: AuthServiceProviderFactory