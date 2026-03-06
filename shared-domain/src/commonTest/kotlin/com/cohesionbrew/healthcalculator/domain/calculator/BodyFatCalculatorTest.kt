package com.cohesionbrew.healthcalculator.domain.calculator

import com.cohesionbrew.healthcalculator.domain.model.BodyFatCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BodyFatCalculatorTest {

    @Test
    fun `calculateNavy for male returns reasonable value`() {
        val result = BodyFatCalculator.calculateNavy(
            isMale = true,
            waistCm = 85.0,
            neckCm = 38.0,
            heightCm = 180.0
        )
        assertTrue(result in 10.0..25.0, "Male body fat should be 10-25%, got $result")
    }

    @Test
    fun `calculateNavy for female returns reasonable value`() {
        val result = BodyFatCalculator.calculateNavy(
            isMale = false,
            waistCm = 75.0,
            neckCm = 32.0,
            heightCm = 165.0,
            hipCm = 95.0
        )
        assertTrue(result in 15.0..35.0, "Female body fat should be 15-35%, got $result")
    }

    @Test
    fun `calculateNavy returns 0 for invalid circumference`() {
        val result = BodyFatCalculator.calculateNavy(
            isMale = true,
            waistCm = 30.0,
            neckCm = 40.0,
            heightCm = 180.0
        )
        assertEquals(0.0, result, "Should return 0 when waist < neck")
    }

    @Test
    fun `calculateRfm for male returns reasonable value`() {
        val result = BodyFatCalculator.calculateRfm(
            isMale = true,
            heightCm = 180.0,
            waistCm = 85.0
        )
        assertTrue(result in 10.0..30.0, "Male RFM should be 10-30%, got $result")
    }

    @Test
    fun `calculateRfm for female returns reasonable value`() {
        val result = BodyFatCalculator.calculateRfm(
            isMale = false,
            heightCm = 165.0,
            waistCm = 75.0
        )
        assertTrue(result in 15.0..40.0, "Female RFM should be 15-40%, got $result")
    }

    @Test
    fun `calculateRfm returns 0 for zero waist`() {
        assertEquals(0.0, BodyFatCalculator.calculateRfm(true, 180.0, 0.0))
    }

    @Test
    fun `getCategory male essential`() {
        assertEquals(BodyFatCategory.ESSENTIAL, BodyFatCalculator.getCategory(4.0, true))
    }

    @Test
    fun `getCategory male athletes`() {
        assertEquals(BodyFatCategory.ATHLETES, BodyFatCalculator.getCategory(10.0, true))
    }

    @Test
    fun `getCategory male fitness`() {
        assertEquals(BodyFatCategory.FITNESS, BodyFatCalculator.getCategory(16.0, true))
    }

    @Test
    fun `getCategory male average`() {
        assertEquals(BodyFatCategory.AVERAGE, BodyFatCalculator.getCategory(20.0, true))
    }

    @Test
    fun `getCategory male obese`() {
        assertEquals(BodyFatCategory.OBESE, BodyFatCalculator.getCategory(30.0, true))
    }

    @Test
    fun `getCategory female thresholds differ from male`() {
        // 10% is ESSENTIAL for females, ATHLETES for males
        assertEquals(BodyFatCategory.ESSENTIAL, BodyFatCalculator.getCategory(10.0, false))
        assertEquals(BodyFatCategory.ATHLETES, BodyFatCalculator.getCategory(10.0, true))
    }
}
