package com.cohesionbrew.healthcalculator.domain.calculator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BmrCalculatorTest {

    @Test
    fun `calculateMifflinStJeor for male returns expected value`() {
        val bmr = BmrCalculator.calculateMifflinStJeor("male", 75.0, 180.0, 30)
        assertEquals(1730.0, bmr, 0.1)
    }

    @Test
    fun `calculateMifflinStJeor for female returns expected value`() {
        val bmr = BmrCalculator.calculateMifflinStJeor("female", 60.0, 165.0, 25)
        assertEquals(1345.25, bmr, 0.1)
    }

    @Test
    fun `calculateMifflinStJeor handles case insensitive gender`() {
        val bmrLower = BmrCalculator.calculateMifflinStJeor("male", 75.0, 180.0, 30)
        val bmrUpper = BmrCalculator.calculateMifflinStJeor("MALE", 75.0, 180.0, 30)
        assertEquals(bmrLower, bmrUpper)
    }

    @Test
    fun `calculateHarrisBenedict for male returns expected value`() {
        val bmr = BmrCalculator.calculateHarrisBenedict("male", 75.0, 180.0, 30)
        assertTrue(bmr in 1790.0..1800.0, "Harris-Benedict BMR should be ~1796, got $bmr")
    }

    @Test
    fun `calculateHarrisBenedict for female returns expected value`() {
        val bmr = BmrCalculator.calculateHarrisBenedict("female", 60.0, 165.0, 25)
        assertTrue(bmr in 1410.0..1420.0, "Harris-Benedict BMR should be ~1417, got $bmr")
    }

    @Test
    fun `calculateKatchMcArdle returns expected value`() {
        val bmr = BmrCalculator.calculateKatchMcArdle(75.0, 15.0)
        assertTrue(bmr in 1740.0..1750.0, "Katch-McArdle BMR should be ~1747, got $bmr")
    }

    @Test
    fun `calculateOxfordHenry for young adult male returns expected value`() {
        val bmr = BmrCalculator.calculateOxfordHenry("male", 75.0, 25)
        assertTrue(bmr in 1820.0..1830.0, "Oxford/Henry BMR should be ~1824.5, got $bmr")
    }

    @Test
    fun `calculateOxfordHenry uses age brackets correctly`() {
        val bmrYoung = BmrCalculator.calculateOxfordHenry("male", 75.0, 25)
        val bmrMiddle = BmrCalculator.calculateOxfordHenry("male", 75.0, 45)
        val bmrOlder = BmrCalculator.calculateOxfordHenry("male", 75.0, 65)
        assertTrue(bmrYoung != bmrMiddle)
        assertTrue(bmrMiddle != bmrOlder)
    }

    @Test
    fun `calculateAll returns 3 formulas without body fat`() {
        val results = BmrCalculator.calculateAll("male", 75.0, 180.0, 30, null)
        assertEquals(3, results.size)
    }

    @Test
    fun `calculateAll returns 4 formulas with body fat`() {
        val results = BmrCalculator.calculateAll("male", 75.0, 180.0, 30, 15.0)
        assertEquals(4, results.size)
        assertTrue(results.any { it.formula == "Katch-McArdle" })
    }

    @Test
    fun `calculateAverage returns reasonable value`() {
        val average = BmrCalculator.calculateAverage("male", 75.0, 180.0, 30)
        assertTrue(average in 1700.0..1800.0, "Average BMR should be 1700-1800, got $average")
    }
}
