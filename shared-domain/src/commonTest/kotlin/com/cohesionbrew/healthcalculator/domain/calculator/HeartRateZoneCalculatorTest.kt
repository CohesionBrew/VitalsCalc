package com.cohesionbrew.healthcalculator.domain.calculator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HeartRateZoneCalculatorTest {

    @Test
    fun `calculateMaxHeartRate returns 190 for age 30`() {
        assertEquals(190, HeartRateZoneCalculator.calculateMaxHeartRate(30))
    }

    @Test
    fun `calculateMaxHeartRate returns 180 for age 40`() {
        assertEquals(180, HeartRateZoneCalculator.calculateMaxHeartRate(40))
    }

    @Test
    fun `calculateMaxHeartRateTanaka returns expected value`() {
        assertEquals(187, HeartRateZoneCalculator.calculateMaxHeartRateTanaka(30))
    }

    @Test
    fun `calculateHeartRateReserve returns correct value`() {
        assertEquals(130, HeartRateZoneCalculator.calculateHeartRateReserve(190, 60))
    }

    @Test
    fun `calculateZonesMHR returns 5 zones`() {
        assertEquals(5, HeartRateZoneCalculator.calculateZonesMHR(190).size)
    }

    @Test
    fun `calculateZonesMHR zone 1 is 50-60 percent`() {
        val zones = HeartRateZoneCalculator.calculateZonesMHR(200)
        assertEquals(100, zones.first().minBpm)
        assertEquals(120, zones.first().maxBpm)
    }

    @Test
    fun `calculateZonesMHR zone 5 max equals maxHr`() {
        val zones = HeartRateZoneCalculator.calculateZonesMHR(190)
        assertEquals(190, zones.last().maxBpm)
    }

    @Test
    fun `calculateZonesHRR returns 5 zones`() {
        assertEquals(5, HeartRateZoneCalculator.calculateZonesHRR(190, 60).size)
    }

    @Test
    fun `calculateZonesHRR accounts for resting heart rate`() {
        val zonesMHR = HeartRateZoneCalculator.calculateZonesMHR(190)
        val zonesHRR = HeartRateZoneCalculator.calculateZonesHRR(190, 60)
        assertTrue(zonesHRR[0].minBpm > zonesMHR[0].minBpm)
    }

    @Test
    fun `calculateZones uses MHR method when no resting HR provided`() {
        val zonesDefault = HeartRateZoneCalculator.calculateZones(age = 30)
        val zonesMHR = HeartRateZoneCalculator.calculateZonesMHR(190)
        assertEquals(zonesMHR[0].minBpm, zonesDefault[0].minBpm)
    }

    @Test
    fun `calculateZones uses HRR method when resting HR provided`() {
        val zonesWithResting = HeartRateZoneCalculator.calculateZones(age = 30, restingHr = 60)
        val zonesHRR = HeartRateZoneCalculator.calculateZonesHRR(190, 60)
        assertEquals(zonesHRR[0].minBpm, zonesWithResting[0].minBpm)
    }

    @Test
    fun `zones have correct intensity labels`() {
        val zones = HeartRateZoneCalculator.calculateZonesMHR(190)
        assertEquals("Very Light", zones[0].intensity)
        assertEquals("Light", zones[1].intensity)
        assertEquals("Moderate", zones[2].intensity)
        assertEquals("Hard", zones[3].intensity)
        assertEquals("Maximum", zones[4].intensity)
    }
}
