package com.cohesionbrew.healthcalculator.domain.model

data class IdealWeightResults(
    val robinsonKg: Double,
    val millerKg: Double,
    val devineKg: Double,
    val hamwiKg: Double
) {
    val averageKg: Double
        get() = (robinsonKg + millerKg + devineKg + hamwiKg) / 4

    val minKg: Double
        get() = minOf(robinsonKg, millerKg, devineKg, hamwiKg)

    val maxKg: Double
        get() = maxOf(robinsonKg, millerKg, devineKg, hamwiKg)
}
