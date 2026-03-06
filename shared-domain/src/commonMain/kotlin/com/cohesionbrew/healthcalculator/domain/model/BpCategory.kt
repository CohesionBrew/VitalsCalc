package com.cohesionbrew.healthcalculator.domain.model

/**
 * AHA Blood Pressure Categories.
 *
 * Based on American Heart Association guidelines.
 * Color mapping is handled in the presentation layer.
 */
enum class BpCategory(
    val displayName: String,
    val guidance: String
) {
    NORMAL(
        displayName = "Normal",
        guidance = "Your blood pressure is in a healthy range. Continue maintaining a healthy lifestyle with regular exercise, balanced diet, and stress management."
    ),
    ELEVATED(
        displayName = "Elevated",
        guidance = "Your blood pressure is slightly elevated. Consider lifestyle changes like reducing sodium intake, increasing physical activity, and maintaining a healthy weight."
    ),
    HYPERTENSION_1(
        displayName = "Hypertension Stage 1",
        guidance = "Your blood pressure is high. Consult your healthcare provider about lifestyle changes and possible medication. Monitor regularly and reduce sodium intake."
    ),
    HYPERTENSION_2(
        displayName = "Hypertension Stage 2",
        guidance = "Your blood pressure is significantly elevated. Please consult your healthcare provider as soon as possible. Medication may be necessary along with lifestyle changes."
    ),
    CRISIS(
        displayName = "Hypertensive Crisis",
        guidance = "URGENT: Your blood pressure reading indicates a hypertensive crisis. If you're experiencing symptoms like severe headache, chest pain, shortness of breath, or vision problems, seek emergency medical care immediately."
    )
}
