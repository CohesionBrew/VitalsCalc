package com.cohesionbrew.healthcalculator.auth.api

fun interface AuthServiceProviderFactory {
    companion object {}

    fun create(): AuthServiceProvider
}