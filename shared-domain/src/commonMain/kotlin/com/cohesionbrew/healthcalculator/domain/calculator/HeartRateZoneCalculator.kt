package com.cohesionbrew.healthcalculator.domain.calculator

/**
 * Calculator for heart rate training zones.
 *
 * Supports two calculation methods:
 * - Max Heart Rate (MHR) method: Zones based on percentage of max heart rate
 * - Heart Rate Reserve (HRR/Karvonen) method: Zones based on reserve heart rate
 */
object HeartRateZoneCalculator {

    data class Zone(
        val number: Int,
        val name: String,
        val minBpm: Int,
        val maxBpm: Int,
        val intensity: String,
        val benefit: String
    )

    fun calculateMaxHeartRate(age: Int): Int = 220 - age

    fun calculateMaxHeartRateTanaka(age: Int): Int = (208 - (0.7 * age)).toInt()

    fun calculateHeartRateReserve(maxHr: Int, restingHr: Int): Int = maxHr - restingHr

    fun calculateZonesMHR(maxHr: Int): List<Zone> {
        return listOf(
            Zone(1, "Zone 1", (maxHr * 0.50).toInt(), (maxHr * 0.60).toInt(), "Very Light", "Recovery, warm-up"),
            Zone(2, "Zone 2", (maxHr * 0.60).toInt(), (maxHr * 0.70).toInt(), "Light", "Fat burning, endurance"),
            Zone(3, "Zone 3", (maxHr * 0.70).toInt(), (maxHr * 0.80).toInt(), "Moderate", "Aerobic fitness"),
            Zone(4, "Zone 4", (maxHr * 0.80).toInt(), (maxHr * 0.90).toInt(), "Hard", "Anaerobic threshold"),
            Zone(5, "Zone 5", (maxHr * 0.90).toInt(), maxHr, "Maximum", "Peak performance")
        )
    }

    fun calculateZonesHRR(maxHr: Int, restingHr: Int): List<Zone> {
        val hrr = calculateHeartRateReserve(maxHr, restingHr)

        return listOf(
            Zone(1, "Zone 1", ((hrr * 0.50) + restingHr).toInt(), ((hrr * 0.60) + restingHr).toInt(), "Very Light", "Recovery, warm-up"),
            Zone(2, "Zone 2", ((hrr * 0.60) + restingHr).toInt(), ((hrr * 0.70) + restingHr).toInt(), "Light", "Fat burning, endurance"),
            Zone(3, "Zone 3", ((hrr * 0.70) + restingHr).toInt(), ((hrr * 0.80) + restingHr).toInt(), "Moderate", "Aerobic fitness"),
            Zone(4, "Zone 4", ((hrr * 0.80) + restingHr).toInt(), ((hrr * 0.90) + restingHr).toInt(), "Hard", "Anaerobic threshold"),
            Zone(5, "Zone 5", ((hrr * 0.90) + restingHr).toInt(), maxHr, "Maximum", "Peak performance")
        )
    }

    fun calculateZones(
        age: Int,
        restingHr: Int? = null,
        useTanakaFormula: Boolean = false
    ): List<Zone> {
        val maxHr = if (useTanakaFormula) {
            calculateMaxHeartRateTanaka(age)
        } else {
            calculateMaxHeartRate(age)
        }

        return if (restingHr != null && restingHr > 0) {
            calculateZonesHRR(maxHr, restingHr)
        } else {
            calculateZonesMHR(maxHr)
        }
    }
}
