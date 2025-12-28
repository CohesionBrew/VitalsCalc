package com.measify.kappmaker.auth.firebase

import com.measify.kappmaker.auth.api.AuthServiceProviderFactory

internal actual val authServiceProviderFactory: AuthServiceProviderFactory
    get() = AuthServiceProviderFactory { FirebaseAuthServiceProvider() }