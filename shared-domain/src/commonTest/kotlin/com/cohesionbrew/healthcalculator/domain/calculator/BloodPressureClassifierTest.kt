package com.cohesionbrew.healthcalculator.domain.calculator

import com.cohesionbrew.healthcalculator.domain.model.BpCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BloodPressureClassifierTest {

    @Test
    fun `classify normal blood pressure`() {
        assertEquals(BpCategory.NORMAL, BloodPressureClassifier.classify(115, 75))
    }

    @Test
    fun `classify elevated blood pressure`() {
        assertEquals(BpCategory.ELEVATED, BloodPressureClassifier.classify(125, 75))
    }

    @Test
    fun `classify hypertension stage 1 by systolic`() {
        assertEquals(BpCategory.HYPERTENSION_1, BloodPressureClassifier.classify(135, 75))
    }

    @Test
    fun `classify hypertension stage 1 by diastolic`() {
        assertEquals(BpCategory.HYPERTENSION_1, BloodPressureClassifier.classify(115, 85))
    }

    @Test
    fun `classify hypertension stage 2 by systolic`() {
        assertEquals(BpCategory.HYPERTENSION_2, BloodPressureClassifier.classify(145, 75))
    }

    @Test
    fun `classify hypertension stage 2 by diastolic`() {
        assertEquals(BpCategory.HYPERTENSION_2, BloodPressureClassifier.classify(115, 95))
    }

    @Test
    fun `classify crisis by systolic`() {
        assertEquals(BpCategory.CRISIS, BloodPressureClassifier.classify(185, 75))
    }

    @Test
    fun `classify crisis by diastolic`() {
        assertEquals(BpCategory.CRISIS, BloodPressureClassifier.classify(115, 125))
    }

    @Test
    fun `crisis takes priority over stage 2`() {
        assertEquals(BpCategory.CRISIS, BloodPressureClassifier.classify(185, 95))
    }

    // BpValidator tests

    @Test
    fun `validateSystolic accepts valid value`() {
        assertNull(BpValidator.validateSystolic(120))
    }

    @Test
    fun `validateSystolic rejects too low`() {
        assertEquals("Systolic must be at least 60 mmHg", BpValidator.validateSystolic(50))
    }

    @Test
    fun `validateSystolic rejects too high`() {
        assertEquals("Systolic cannot exceed 250 mmHg", BpValidator.validateSystolic(260))
    }

    @Test
    fun `validateDiastolic accepts valid value`() {
        assertNull(BpValidator.validateDiastolic(80))
    }

    @Test
    fun `validatePulse accepts null`() {
        assertNull(BpValidator.validatePulse(null))
    }

    @Test
    fun `validateRelation rejects systolic less than diastolic`() {
        assertEquals("Systolic must be greater than diastolic", BpValidator.validateRelation(70, 80))
    }

    @Test
    fun `validate returns null for valid reading`() {
        assertNull(BpValidator.validate(120, 80, 72))
    }

    @Test
    fun `validate returns first error found`() {
        val error = BpValidator.validate(50, 80)
        assertEquals("Systolic must be at least 60 mmHg", error)
    }
}
