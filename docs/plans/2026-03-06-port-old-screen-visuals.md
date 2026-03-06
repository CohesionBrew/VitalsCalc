# Port Old VitalsCalc Screen Visuals Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Replace the newly-created screen composables with visual components from the old VitalsCalc project so every screen looks/feels identical to the live app, while keeping the new KAppMaker architecture (UiStateHolder/UiState/ScreenRoute).

**Architecture:** Keep all existing UiState, UiStateHolder, and ScreenRoute files. Port old visual components (Canvas charts, custom cards, sparklines) into `presentation/screens/<feature>/components/` or `presentation/components/health/`. Adapt old components to accept new UiState types instead of old State types. Replace screen composables to match old layout.

**Tech Stack:** Compose Multiplatform, KAppMaker UiStateHolder pattern, Material 3, Canvas API

---

## Overview of Changes

**What stays as-is (DO NOT MODIFY):**
- All `*UiState.kt` files
- All `*UiStateHolder.kt` files
- All `*ScreenRoute.kt` files
- All domain calculators and repositories
- DI/Koin setup in `AppInitializer.kt`
- Design system module

**What gets replaced/enhanced:**
- Screen composables (`*Screen.kt`) — rewritten to match old layout
- New component files added under `presentation/screens/<feature>/components/`
- Shared components in `presentation/components/health/`

**Source project:** `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/`
**Target project:** `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalcNew/`

**Old package prefix:** `com.cohesionbrew.healthcalculator.features.<feature>`
**New package prefix:** `com.cohesionbrew.healthcalculator.presentation.screens.<feature>`

## Adaptation Pattern (applies to ALL tasks)

When porting an old component:
1. Copy the old file into the new project's component directory
2. Update the package declaration
3. Replace old state type references: `BmiDetailsState` → individual params or `BmiUiState`
4. Replace old action type references: `BmiDetailsAction` → `BmiUiEvent`
5. Replace old imports (`core.presentation.components.*`, `core.ui.theme.*`) with new design system imports (`designsystem.components.*`)
6. Replace `StandardHealthScaffold` → `ScreenWithToolbar`
7. Replace old resource references (`shared.generated.resources.*`) → new (`generated.resources.*`)
8. Remove old preview functions that reference old types (add new ones if useful)
9. Keep ALL visual logic (Canvas drawing, colors, layouts) unchanged

## Key Import Mappings

| Old Import | New Import |
|---|---|
| `com.cohesionbrew.healthcalculator.core.presentation.components.FormattedDoubleTextField` | `com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField` |
| `com.cohesionbrew.healthcalculator.core.presentation.components.StandardHealthScaffold` | `com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar` |
| `com.cohesionbrew.healthcalculator.core.presentation.components.AutosizeText` | Port or replace with equivalent |
| `com.cohesionbrew.healthcalculator.core.presentation.components.PerfectlyOutlinedText` | Port or replace with equivalent |
| `com.cohesionbrew.healthcalculator.core.ui.theme.HealthCalcProTheme` | `com.cohesionbrew.healthcalculator.designsystem.theme.*` |
| `com.cohesionbrew.healthcalculator.shared.generated.resources.*` | `com.cohesionbrew.healthcalculator.generated.resources.*` |
| `com.cohesionbrew.healthcalculator.features.shared.models.toDisplayableNumber` | Inline or port as utility |
| `com.cohesionbrew.healthcalculator.core.ui.util.smartFormat` | Port or inline |

---

## Task 1: Port Shared Utilities

**Files:**
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/shared/models/ProfileUi.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/shared/data/WeightRange.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/core/presentation/components/AutosizeText.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/core/presentation/components/PerfectlyOutlinedText.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/core/ui/util/smartFormat.kt` (or wherever smartFormat lives)
- Create: Utility files in new project as needed

**Step 1:** Read each old utility file to understand what's needed.

**Step 2:** For each utility that old components depend on, either:
- Port it to `presentation/util/` or `designsystem/` in the new project
- Or inline its logic where it's used (if trivial)

**Step 3:** Verify the new project compiles with `./gradlew assembleDebug`

**Step 4:** Commit: `feat: port shared utilities from old VitalsCalc`

---

## Task 2: Port BMI Screen Visual Components

The BMI screen has the biggest visual gap. The old screen has a rich `BmiDynamicGraphicsChart` with Canvas-drawn arrow segments and person icons. The new screen uses a simpler `BmiIndicatorBar`.

**Files:**
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/bmi/components/BMICategoryDynamicGraphic.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/bmi/BmiDetailsScreen.kt` (for layout reference)
- Read: Helper functions referenced by old components (`getBmiCategoriesForDynamicGraphic`, `getBmiCategory`, `getBmiCategoryLabel`, `getWeightStatusColor`, `calculateHealthyWeightRangeData`, `calculateRawDiffFromHealthy`)
- Create: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/bmi/components/BmiDynamicGraphicsChart.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/bmi/BmiScreen.kt`

**Step 1:** Read old `BMICategoryDynamicGraphic.kt` and all helper functions it depends on.

**Step 2:** Create `BmiDynamicGraphicsChart.kt` in the new project. Adapt:
- Change package to `com.cohesionbrew.healthcalculator.presentation.screens.bmi.components`
- The composable should accept `currentBmi: Double` (from `uiState.bmi`)
- Port `BmiGraphicCategory` data class and `getBmiCategoriesForDynamicGraphic()` — these should use string resources for labels
- Port `AutosizeText` if needed, or replace with standard `Text` with `maxLines` and `overflow`
- Keep ALL Canvas drawing logic (arrow segments, person icons) unchanged

**Step 3:** Update `BmiScreen.kt` to use the old layout:
- Replace `BmiIndicatorBar` with `BmiDynamicGraphicsChart(currentBmi = uiState.bmi)`
- Keep the existing input fields and result card structure
- The result card should still show BMI value, category, weight difference, healthy range

**Step 4:** Verify: `./gradlew assembleDebug`

**Step 5:** Commit: `feat: port BMI dynamic graphics chart from old VitalsCalc`

---

## Task 3: Port BMR Comparison Chart

The old BMR screen had a `BmrComparisonChart` — a visual bar chart comparing different formula results. The new screen has a table which is functionally equivalent but visually different.

**Files:**
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/bmr/components/BmrComparisonChart.kt`
- Read: Old BMR component files for any additional visual elements not in the new screen
- Create: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/bmr/components/BmrComparisonChart.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/bmr/BmrScreen.kt`

**Step 1:** Read old `BmrComparisonChart.kt` and understand its data needs.

**Step 2:** Port the chart component. It should accept `formulaResults: List<BmrCalculator.BmrResult>` (already available in `BmrUiState`).

**Step 3:** Add the chart to `BmrScreen.kt` in the results section, after the existing `BmrDetailsCard`.

**Step 4:** Compare old vs new BMR component files. Port any other visual elements that are in the old but not in the new (e.g., `CalorieGuidanceGrid`, `TdeeCalorieGrid` if they differ significantly from the inline versions).

**Step 5:** Verify: `./gradlew assembleDebug`

**Step 6:** Commit: `feat: port BMR comparison chart from old VitalsCalc`

---

## Task 4: Port Home Screen Components (Sparklines, Trends, Pro Badges)

The old Home screen had rich dashboard cards with `MiniSparkline`, `TrendBadge`, `ProBadge`, and `ProUpgradeBanner`. The new Home screen is simpler.

**Files:**
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/home/HomeScreen.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/home/components/DashboardMetricCard.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/home/components/MiniSparkline.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/home/components/TrendBadge.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/home/components/ProBadge.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/home/components/ProUpgradeBanner.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/home/DashboardMetric.kt`
- Create: Component files in `presentation/components/health/` or `presentation/screens/home/components/`
- Modify: `presentation/components/health/DashboardMetricCard.kt` (enhance existing)
- Modify: `presentation/screens/home/HomeScreen.kt`
- Potentially modify: `presentation/screens/home/HomeUiState.kt` if sparkline/trend data fields are missing

**Step 1:** Read all old Home components and the old HomeState to understand what data fields drive sparklines and trends.

**Step 2:** Port `MiniSparkline.kt` (Canvas-based sparkline) — should accept `List<Double>` data points.

**Step 3:** Port `TrendBadge.kt` — should accept trend direction (up/down/stable).

**Step 4:** Port `ProBadge.kt` and `ProUpgradeBanner.kt` — for premium feature gating UI.

**Step 5:** Enhance `DashboardMetricCard` to match the old one's layout (add sparkline slot, trend badge).

**Step 6:** Update `HomeScreen.kt` to match old layout with enhanced cards.

**Step 7:** Check if `HomeUiState` needs additional fields for sparkline data and trends. If so, update `HomeUiState.kt` and `HomeUiStateHolder.kt` to provide the data. **NOTE:** This is the one screen where UiState changes may be needed to support the richer visuals.

**Step 8:** Verify: `./gradlew assembleDebug`

**Step 9:** Commit: `feat: port home dashboard components (sparklines, trends, pro badges)`

---

## Task 5: Compare and Align Remaining Screens

For each of these screens, read the old and new versions side-by-side, identify visual differences, and port any missing visual elements.

### 5a: Blood Pressure Screen

**Files:**
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/bloodpressure/BloodPressureScreen.kt`
- Compare with: `presentation/screens/bloodpressure/BloodPressureScreen.kt`

The new BP screen already looks quite complete (BpInputCard, BpCategoryDisplay, BpGuidanceCard, MedicalDisclaimerCard, RecentReadingsSection). Compare visual details and align if needed.

### 5b: Heart Rate Screen

**Files:**
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/heartrate/HeartRateZonesScreen.kt`
- Read: Old components: `HeartRateChart.kt`, `HeartRateZoneDetailsCard.kt`, `RestingHeartRateInput.kt`
- Compare with: `presentation/screens/heartrate/HeartRateScreen.kt`

The new HR screen has inline `HeartRateZonesChart` and `HeartRateZoneDetailsCard`. Compare zone chart styling, colors, and layout. The old `HeartRateChart.kt` may have a Canvas-based visual chart (not just a table) — if so, port it.

### 5c: Body Fat Screen

**Files:**
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/bodyfat/BodyFatScreen.kt`
- Read: Old components: `BodyFatResultCard.kt`, `MeasurementInputCard.kt`
- Compare with: `presentation/screens/bodyfat/BodyFatScreen.kt`

The new Body Fat screen already has BodyFatResultCard and BodyFatCategoryReferenceTable inline. Compare styling.

### 5d: Ideal Weight Screen

**Files:**
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/idealweight/components/FormulaComparisonCard.kt`
- Compare with: `presentation/screens/idealweight/IdealWeightScreen.kt`

The new screen already has FormulaComparisonCard inline. Compare styling.

### 5e: Water Intake Screen

**Files:**
- Read: old Water screen
- Compare with: `presentation/screens/waterintake/WaterIntakeScreen.kt`

Likely minimal differences. Compare and align.

**For each sub-task above:**
1. Read old and new side by side
2. If visual differences exist, port the old component
3. If functionally equivalent with only minor styling differences, decide whether to align
4. Verify: `./gradlew assembleDebug`

**Step final:** Commit: `feat: align remaining calculator screens with old VitalsCalc visuals`

---

## Task 6: Port History Screen Components

**Files:**
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/history/components/HistoryEntryCard.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/history/components/HistoryFilterChips.kt`
- Read: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/history/components/EmptyHistoryPlaceholder.kt`
- Compare with existing: `presentation/components/health/HistoryEntryCard.kt`, `HistoryFilterChips.kt`
- Compare with: `presentation/screens/history/HistoryScreen.kt`

**Step 1:** Compare old vs new HistoryEntryCard and HistoryFilterChips — align styling.

**Step 2:** Port `EmptyHistoryPlaceholder.kt` if the old one has a richer empty state (illustration, message) compared to the new simple text.

**Step 3:** Verify: `./gradlew assembleDebug`

**Step 4:** Commit: `feat: align history screen with old VitalsCalc visuals`

---

## Task 7: Port More/Settings/Calculators Screen Components

**Files:**
- Read old: `features/more/MoreScreen.kt`, `features/calculators/CalculatorsScreen.kt`, `features/calculators/CalculatorTile.kt`
- Read old: `features/settings/SettingsCard.kt`, `features/settings/AboutCard.kt`, `features/settings/ReferencesCard.kt`
- Compare with new: `presentation/screens/more/MoreScreen.kt`, `presentation/screens/calculators/CalculatorsScreen.kt`

**Step 1:** Compare layouts and port any richer visual elements.

**Step 2:** Verify: `./gradlew assembleDebug`

**Step 3:** Commit: `feat: align more/settings/calculators screens with old VitalsCalc`

---

## Task 8: Full Build Verification

**Step 1:** Run full Android build: `./gradlew assembleDebug`

**Step 2:** Run iOS framework build: `./gradlew linkDebugFrameworkIosSimulatorArm64 linkDebugFrameworkIosArm64`

**Step 3:** Run tests: `./gradlew :composeApp:jvmTest`

**Step 4:** Fix any compilation issues.

**Step 5:** Commit any fixes: `fix: resolve build issues from visual port`

---

## Priority Order

If time is limited, prioritize in this order:
1. **Task 2 (BMI)** — Biggest visual gap (arrow chart vs gradient bar)
2. **Task 4 (Home)** — Most visible screen, sparklines add polish
3. **Task 3 (BMR)** — Comparison chart adds value
4. **Task 1 (Utilities)** — Dependency for Tasks 2-4
5. **Tasks 5-7** — Smaller gaps, may already be close enough

## Notes

- The old project uses `StandardHealthScaffold` which supports portrait/landscape. The new project uses `ScreenWithToolbar` (portrait only). Landscape support is NOT being ported in this phase — it can be added later if needed.
- The old project has `AnalyticsHelper` integration in screens. This is NOT being ported now — it should be added via a separate analytics task.
- The old project derives BMI data from a `User` profile object (auto-calculates on profile changes). The new project has explicit calculate buttons. This behavioral difference is intentional (KAppMaker pattern) and is NOT being changed.
