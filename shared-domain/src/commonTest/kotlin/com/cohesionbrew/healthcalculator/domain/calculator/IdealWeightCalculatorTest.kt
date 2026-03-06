package com.cohesionbrew.healthcalculator.domain.calculator

import kotlin.test.Test
import kotlin.test.assertTrue

class IdealWeightCalculatorTest {

    @Test
    fun `calculateInchesOver5Feet for 180cm returns about 10-11 inches`() {
        val inches = IdealWeightCalculator.calculateInchesOver5Feet(180.0)
        // 180cm = ~70.87 inches, 70.87 - 60 = ~10.87
        assertTrue(inches in 10.0..11.5, "Expected ~10.87, got $inches")
    }

    @Test
    fun `calculateInchesOver5Feet for short person returns 0`() {
        val inches = IdealWeightCalculator.calculateInchesOver5Feet(140.0)
        // 140cm = ~55.12 inches, 55.12 - 60 = negative, clamped to 0
        assertTrue(inches == 0.0, "Expected 0 for height under 5 feet")
    }

    @Test
    fun `calculateRobinson for male returns expected value`() {
        val result = IdealWeightCalculator.calculateRobinson(true, 10.0)
        // 52 + 1.9 * 10 = 71
        assertTrue(result in 70.0..72.0, "Expected ~71kg, got $result")
    }

    @Test
    fun `calculateAll returns all 4 formula results`() {
        val results = IdealWeightCalculator.calculateAll(180.0, true)
        assertTrue(results.robinsonKg > 0)
        assertTrue(results.millerKg > 0)
        assertTrue(results.devineKg > 0)
        assertTrue(results.hamwiKg > 0)
    }

    @Test
    fun `calculateAll average is between min and max`() {
        val results = IdealWeightCalculator.calculateAll(175.0, true)
        assertTrue(results.averageKg >= results.minKg)
        assertTrue(results.averageKg <= results.maxKg)
    }

    @Test
    fun `calculateAll male results are generally higher than female`() {
        val male = IdealWeightCalculator.calculateAll(175.0, true)
        val female = IdealWeightCalculator.calculateAll(175.0, false)
        assertTrue(male.averageKg > female.averageKg, "Male avg should be > female avg")
    }

    @Test
    fun `calculateAll returns reasonable weights for average height`() {
        val results = IdealWeightCalculator.calculateAll(175.0, true)
        assertTrue(results.averageKg in 60.0..85.0, "Average should be 60-85kg, got ${results.averageKg}")
    }
}
