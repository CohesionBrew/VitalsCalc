package com.cohesionbrew.healthcalculator.domain.calculator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WaterIntakeCalculatorTest {

    @Test
    fun `calculateBaseWaterIntakeMl returns 33ml per kg`() {
        assertEquals(2475.0, WaterIntakeCalculator.calculateBaseWaterIntakeMl(75.0))
    }

    @Test
    fun `calculateActivityMultiplier returns 1_0 for level 1`() {
        assertEquals(1.0, WaterIntakeCalculator.calculateActivityMultiplier(1))
    }

    @Test
    fun `calculateActivityMultiplier returns 1_5 for level 5`() {
        assertEquals(1.5, WaterIntakeCalculator.calculateActivityMultiplier(5))
    }

    @Test
    fun `calculateActivityMultiplier clamps below 1`() {
        assertEquals(1.0, WaterIntakeCalculator.calculateActivityMultiplier(0))
    }

    @Test
    fun `calculateActivityMultiplier clamps above 5`() {
        assertEquals(1.5, WaterIntakeCalculator.calculateActivityMultiplier(10))
    }

    @Test
    fun `calculateClimateMultiplier returns 1_2 for hot climate`() {
        assertEquals(1.2, WaterIntakeCalculator.calculateClimateMultiplier(true))
    }

    @Test
    fun `calculateClimateMultiplier returns 1_0 for normal climate`() {
        assertEquals(1.0, WaterIntakeCalculator.calculateClimateMultiplier(false))
    }

    @Test
    fun `calculate returns reasonable liters for average person`() {
        val result = WaterIntakeCalculator.calculate(
            weightKg = 75.0,
            activityLevel = 3,
            isHotClimate = false
        )
        // 75*33 = 2475ml * 1.25 = 3093ml = ~3.1L
        assertTrue(result.liters in 2.5..4.0, "Expected 2.5-4L, got ${result.liters}")
    }

    @Test
    fun `calculate hot climate increases intake`() {
        val normal = WaterIntakeCalculator.calculate(75.0, 3, false)
        val hot = WaterIntakeCalculator.calculate(75.0, 3, true)
        assertTrue(hot.liters > normal.liters, "Hot climate should increase intake")
    }

    @Test
    fun `calculate higher activity increases intake`() {
        val sedentary = WaterIntakeCalculator.calculate(75.0, 1, false)
        val active = WaterIntakeCalculator.calculate(75.0, 5, false)
        assertTrue(active.liters > sedentary.liters, "Higher activity should increase intake")
    }

    @Test
    fun `calculate ounces converts correctly from liters`() {
        val result = WaterIntakeCalculator.calculate(75.0, 1, false)
        // Both liters and ounces are independently rounded to 1 decimal,
        // so we check they're in the right ballpark (1L ≈ 33.814oz)
        val ratio = result.ounces / result.liters
        assertTrue(
            ratio in 33.0..35.0,
            "Oz/L ratio should be ~33.8, got $ratio"
        )
    }
}
