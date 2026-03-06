package com.cohesionbrew.healthcalculator.presentation.screens.references

import androidx.compose.runtime.Immutable

@Immutable
data class ReferencesUiState(
    val references: List<ReferenceItem> = defaultReferences
)

data class ReferenceItem(
    val calculator: String,
    val formula: String,
    val source: String
)

sealed interface ReferencesUiEvent

private val defaultReferences = listOf(
    ReferenceItem(
        calculator = "BMI",
        formula = "Weight (kg) / Height (m)²",
        source = "WHO Global Database on Body Mass Index"
    ),
    ReferenceItem(
        calculator = "BMR (Mifflin-St Jeor)",
        formula = "10 × weight(kg) + 6.25 × height(cm) − 5 × age − 161 (female) or + 5 (male)",
        source = "Mifflin MD et al. Am J Clin Nutr. 1990;51(2):241-247"
    ),
    ReferenceItem(
        calculator = "BMR (Harris-Benedict)",
        formula = "447.593 + 9.247 × weight(kg) + 3.098 × height(cm) − 4.330 × age (female)",
        source = "Harris JA, Benedict FG. Carnegie Institution of Washington. 1919"
    ),
    ReferenceItem(
        calculator = "BMR (Katch-McArdle)",
        formula = "370 + 21.6 × lean body mass(kg)",
        source = "Katch F, McArdle W. Nutrition, Weight Control, and Exercise. 1977"
    ),
    ReferenceItem(
        calculator = "TDEE",
        formula = "BMR × Activity Multiplier (1.2 − 1.9)",
        source = "FAO/WHO/UNU Technical Report Series 724. 1985"
    ),
    ReferenceItem(
        calculator = "Body Fat (Navy Method)",
        formula = "86.010 × log10(waist − neck) − 70.041 × log10(height) + 36.76 (male)",
        source = "Hodgdon JA, Beckett MB. Naval Health Research Center. 1984"
    ),
    ReferenceItem(
        calculator = "Body Fat (RFM)",
        formula = "64 − 20 × (height/waist) + 12 × sex",
        source = "Woolcott OO, Bergman RN. Sci Rep. 2018;8:10980"
    ),
    ReferenceItem(
        calculator = "Heart Rate Zones",
        formula = "220 − age (Fox) or 208 − 0.7 × age (Tanaka)",
        source = "Tanaka H et al. J Am Coll Cardiol. 2001;37(1):153-156"
    ),
    ReferenceItem(
        calculator = "Ideal Weight",
        formula = "Devine, Robinson, Miller, Hamwi formulas",
        source = "Pai MP, Paloucek FP. Ann Pharmacother. 2000;34(9):1066-1069"
    ),
    ReferenceItem(
        calculator = "Blood Pressure",
        formula = "Classification by systolic/diastolic ranges",
        source = "AHA/ACC Hypertension Clinical Practice Guidelines. 2017"
    ),
    ReferenceItem(
        calculator = "Water Intake",
        formula = "~30-35 mL per kg body weight, adjusted for activity",
        source = "EFSA Panel on Dietetic Products. EFSA Journal. 2010;8(3):1459"
    )
)
