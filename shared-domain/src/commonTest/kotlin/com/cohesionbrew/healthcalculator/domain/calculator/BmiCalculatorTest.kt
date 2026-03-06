package com.cohesionbrew.healthcalculator.domain.calculator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BmiCalculatorTest {

    @Test
    fun `calculate BMI for standard male returns expected value`() {
        val bmi = BmiCalculator.calculate(heightCm = 180.0, weightKg = 75.0)
        assertTrue(bmi in 22.0..23.0, "BMI should be approximately 22.4, got $bmi")
    }

    @Test
    fun `calculate BMI for standard female returns expected value`() {
        val bmi = BmiCalculator.calculate(heightCm = 165.0, weightKg = 60.0)
        assertTrue(bmi in 22.0..23.0, "BMI should be approximately 22.3, got $bmi")
    }

    @Test
    fun `calculate BMI with zero height returns zero`() {
        assertEquals(0.0, BmiCalculator.calculate(heightCm = 0.0, weightKg = 70.0))
    }

    @Test
    fun `calculate BMI with zero weight returns zero`() {
        assertEquals(0.0, BmiCalculator.calculate(heightCm = 170.0, weightKg = 0.0))
    }

    @Test
    fun `getCategory returns 0 for underweight`() {
        assertEquals(0, BmiCalculator.getCategory(15.0))
        assertEquals(0, BmiCalculator.getCategory(18.4))
    }

    @Test
    fun `getCategory returns 1 for normal weight`() {
        assertEquals(1, BmiCalculator.getCategory(18.5))
        assertEquals(1, BmiCalculator.getCategory(22.0))
        assertEquals(1, BmiCalculator.getCategory(24.9))
    }

    @Test
    fun `getCategory returns 2 for overweight`() {
        assertEquals(2, BmiCalculator.getCategory(25.0))
        assertEquals(2, BmiCalculator.getCategory(29.9))
    }

    @Test
    fun `getCategory returns 3 for obese class I`() {
        assertEquals(3, BmiCalculator.getCategory(30.0))
        assertEquals(3, BmiCalculator.getCategory(34.9))
    }

    @Test
    fun `getCategory returns 4 for obese class II`() {
        assertEquals(4, BmiCalculator.getCategory(35.0))
        assertEquals(4, BmiCalculator.getCategory(39.9))
    }

    @Test
    fun `getCategory returns 5 for obese class III`() {
        assertEquals(5, BmiCalculator.getCategory(40.0))
        assertEquals(5, BmiCalculator.getCategory(50.0))
    }

    @Test
    fun `getHealthyWeightRange returns valid range in metric`() {
        val (min, max) = BmiCalculator.getHealthyWeightRange(heightCm = 180.0, useMetric = true)
        assertTrue(min > 50.0, "Min should be > 50kg")
        assertTrue(max < 90.0, "Max should be < 90kg")
        assertTrue(min < max, "Min should be less than max")
    }

    @Test
    fun `getHealthyWeightRange returns valid range in imperial`() {
        val (min, max) = BmiCalculator.getHealthyWeightRange(heightCm = 180.0, useMetric = false)
        assertTrue(min > 100.0, "Min should be > 100lbs")
        assertTrue(max < 200.0, "Max should be < 200lbs")
        assertTrue(min < max, "Min should be less than max")
    }

    @Test
    fun `getDifferenceFromHealthy returns 0 for healthy weight`() {
        val diff = BmiCalculator.getDifferenceFromHealthy(heightCm = 180.0, weightKg = 70.0, useMetric = true)
        assertEquals(0.0, diff)
    }

    @Test
    fun `getDifferenceFromHealthy returns positive for overweight`() {
        val diff = BmiCalculator.getDifferenceFromHealthy(heightCm = 180.0, weightKg = 100.0, useMetric = true)
        assertTrue(diff > 0, "Should return positive for overweight")
    }
}
