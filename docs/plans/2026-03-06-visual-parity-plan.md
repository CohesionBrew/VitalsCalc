# Visual Parity: Make VitalsCalcNew Look Identical to VitalsCalc

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Make VitalsCalcNew visually indistinguishable from the production VitalsCalc app. Users must not notice any difference.

**Architecture:** Override the KAppMaker design system values (colors, fonts, spacing, component defaults) to match old VitalsCalc, then rewrite each screen's composable to match the old layout structure exactly. The new app keeps its KAppMaker architecture (UiStateHolder, Jetpack Nav, Koin DI) but the UI layer must be a pixel-perfect replica.

**Tech Stack:** Compose Multiplatform, Material 3, Compose Cupertino, KAppMaker design system module

**Reference files:** When implementing any screen, ALWAYS read the old app's version first from `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/` and match it exactly.

---

## Task 1: Fix Design System Colors

**Files:**
- Modify: `designsystem/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/designsystem/theme/Color.kt`
- Modify: `designsystem/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/designsystem/theme/ColorValues.kt`

**What to do:**

Replace KAppMaker colors with old VitalsCalc Material 3 color scheme. The old app uses a full Material 3 `lightColorScheme`/`darkColorScheme`. The new app's `AppColors` is a simplified wrapper.

**Step 1:** Update `Color.kt` - change `PrimaryColor` and `PrimaryAlpha8Color`:
```kotlin
val PrimaryColor = Color(0xFF3F5F90)  // was 0xFF2A4B9F
val PrimaryAlpha8Color = Color(0xFFD6E3FF)  // primaryContainer from old app
```

**Step 2:** Update `lightModeAppColors`:
```kotlin
val lightModeAppColors = AppColors(
    primary = PrimaryColor,                    // #3F5F90
    onPrimary = Color.White,
    alternative = PrimaryAlpha8Color,          // #D6E3FF (primaryContainer)
    onAlternative = Color(0xFF264777),         // onPrimaryContainer
    background = Color(0xFFF9F9FF),            // was #F0EDE5
    surfaceContainer = Color(0xFFEDEDF4),      // was White - old surfaceContainerLight
    text = AppColors.Text(
        primary = Color(0xFF191C20),           // onBackgroundLight
        secondary = Color(0xFF43474E),         // onSurfaceVariantLight
    ),
    textInput = AppColors.TextInput.DefaultLight,
    bottomNav = AppColors.BottomNav(
        background = Color(0xFFF9F9FF),        // match background
        selectedTextIcon = PrimaryColor,
        unselectedTextIcon = Color.GRAY_500
    ),
    outline = Color(0xFF74777F),               // outlineLight from old app
)
```

**Step 3:** Update `darkModeAppColors`:
```kotlin
val darkModeAppColors = AppColors(
    primary = Color(0xFFA8C8FF),               // primaryDark from old app
    onPrimary = Color(0xFF07305F),             // onPrimaryDark
    alternative = Color(0xFF264777),           // primaryContainerDark
    onAlternative = Color(0xFFD6E3FF),         // onPrimaryContainerDark
    background = Color(0xFF111318),            // was #181A20
    surfaceContainer = Color(0xFF1D2024),      // surfaceContainerDark from old
    text = AppColors.Text(
        primary = Color(0xFFE2E2E9),           // onBackgroundDark
        secondary = Color(0xFFC4C6CF),         // onSurfaceVariantDark
    ),
    textInput = AppColors.TextInput.DefaultDark,
    bottomNav = AppColors.BottomNav(
        background = Color(0xFF111318),        // match background
        selectedTextIcon = Color(0xFFA8C8FF),  // primaryDark
        unselectedTextIcon = Color.GRAY_500
    ),
    outline = Color(0xFF8E9099),               // outlineDark from old
)
```

**Step 4:** Add the `asMaterialColorScheme()` to pass through full Material 3 values. Update it to include all the old app's colors (primaryContainer, secondary, tertiary, surface variants, etc.) so components using `MaterialTheme.colorScheme` get correct values.

**Step 5:** Add old VitalsCalc custom colors to `ColorValues.kt`:
```kotlin
val HealthCalcGreen = Color(0xFF00F15E)
val HealthCalcGreen30 = Color(0x4D00F15E)
val HealthCalcGreen10 = Color(0x1A00F15E)
val HealthCalcGreen5 = Color(0x0D00F15E)
val HealthCalcBlack = Color(0xFF080707)
val HealthCalcGray = Color(0xFF87938C)
val HealthCalcGray40 = Color(0x6687938C)
val HealthCalcDarkGray = Color(0xFF232624)
val HealthCalcWhite = Color(0xFFFAFAFA)
val HealthCalcDarkRed = Color(0xFFBB3D3D)
val HealthCalcDarkRed5 = Color(0x0DBB3D3D)
```

**Step 6:** Build: `./gradlew compileDebugKotlin`

**Step 7:** Commit: `git commit -m "fix: Replace KAppMaker colors with VitalsCalc production colors"`

---

## Task 2: Fix Design System Typography

**Files:**
- Modify: `designsystem/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/designsystem/theme/Type.kt`
- Check if Acme font exists: `designsystem/src/commonMain/composeResources/font/`

**What to do:**

The old app uses the **Acme** font (via `displayFontFamily()` which loads `acme_regular`). The new app uses **Poppins**. The old app only overrides 5 Material 3 text styles and uses Material defaults for everything else.

**Step 1:** Copy the `acme_regular.ttf` font file from old app resources to `designsystem/src/commonMain/composeResources/font/acme_regular.ttf`

**Step 2:** Update `Type.kt` - replace Poppins font family with Acme:
```kotlin
private val fontFamily
    @Composable get() = FontFamily(
        Font(
            UiRes.font.acme_regular,
            FontWeight.Normal,
            FontStyle.Normal
        ),
    )
```

Note: Acme only has a Regular weight. The old app uses `displayFontFamily()` which returns only the regular weight for all text. The old app relied on synthetic bold/etc from the system.

**Step 3:** Update `MaterialThemAppTypography` to match old app's `AppTypography`:
```kotlin
val MaterialThemAppTypography
    @Composable
    get() = Typography().copy(
        headlineMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 20.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 24.sp,
        )
    )
```

**Step 4:** Keep the `appTypography` (AppTypography) as-is for KAppMaker components that reference `AppTheme.typography.h4` etc., but ensure the font family is updated to Acme. The existing KAppMaker-only screens (auth, paywall, onboarding) will use these styles.

**Step 5:** Build: `./gradlew compileDebugKotlin`

**Step 6:** Commit: `git commit -m "fix: Replace Poppins with Acme font to match production VitalsCalc"`

---

## Task 3: Fix Design System Spacing

**Files:**
- Modify: `designsystem/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/designsystem/theme/Spacing.kt`

**What to do:**

The old app uses 16.dp for screen padding and 12.dp for card content. The new app uses 24.dp and 16.dp.

**Step 1:** Update `AppSpacing`:
```kotlin
val appSpacing = AppSpacing(
    outerSpacing = 16.dp,           // was 24.dp - matches old app's 16.dp screen padding
    cardContentSpacing = 12.dp,     // was 16.dp - matches old app's 12.dp card padding
    // Keep other values at defaults
)
```

**Step 2:** Build: `./gradlew compileDebugKotlin`

**Step 3:** Commit: `git commit -m "fix: Adjust spacing to match production VitalsCalc (16dp outer, 12dp cards)"`

---

## Task 4: Fix Component Defaults (Card Radius, Button Height)

**Files:**
- Modify: `designsystem/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/designsystem/components/AppCardContainer.kt`
- Modify: `designsystem/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/designsystem/components/Button.kt`
- Modify: `designsystem/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/designsystem/components/AppToolbar.kt`

**What to do:**

**Step 1:** `AppCardContainer.kt` - change default corner radius:
```kotlin
fun AppCardContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),  // was 24.dp
    ...
```

**Step 2:** `Button.kt` - change button sizes to match old app:
```kotlin
enum class ButtonSize(val value: Dp) {
    LARGE(55.dp),       // was 65.dp - matches old HealthActionButton height
    MEDIUM(48.dp),      // was 58.dp
    SMALL(40.dp),       // was 50.dp
    EXTRA_SMALL(34.dp)  // was 43.dp
}
```

Also change button shape default:
```kotlin
fun AppButton(
    ...
    shape: Shape = RoundedCornerShape(100f),  // was CircleShape - matches old pill shape
    ...
```

**Step 3:** `AppToolbar.kt` - the old app doesn't use a toolbar for calculator screens (it uses inline `HealthScreenTitle`). Keep the toolbar for non-health screens but consider reducing height or making it optional.

**Step 4:** Build: `./gradlew compileDebugKotlin`

**Step 5:** Commit: `git commit -m "fix: Update component defaults to match VitalsCalc (12dp radius, 55dp buttons)"`

---

## Task 5: Rewrite Home Screen to Match Old Layout

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/home/HomeScreen.kt`

**Reference:** Read old app's home screen at:
`/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/home/HomeScreen.kt`

**What to do:**

The old home screen is a `PullToRefreshBox` wrapping a `LazyVerticalGrid`. It partitions metrics into "measurements" vs "recommendations" and renders them with `DashboardMetricCard`. The new screen uses `ScreenWithToolbar` and hardcodes each metric individually.

**Step 1:** Read the old HomeScreen.kt completely.

**Step 2:** Rewrite the new HomeScreen to match:
- Remove `ScreenWithToolbar` wrapper
- Add `PullToRefreshBox` with refresh state
- Use the same `LazyVerticalGrid(GridCells.Fixed(2))` with `contentPadding = PaddingValues(12.dp)`, `horizontalArrangement = spacedBy(8.dp)`, `verticalArrangement = spacedBy(8.dp)`
- Section headers "Your Measurements" and "Your Goals & Recommendations" as full-width grid items
- `DashboardMetricCard` for each metric
- `ProUpgradeBanner` at bottom (full-width)
- Keep the UiState/event pattern but match the visual output

**Step 3:** Build: `./gradlew assembleDebug`

**Step 4:** Commit: `git commit -m "fix: Rewrite HomeScreen to match production VitalsCalc layout"`

---

## Task 6: Rewrite BMI Screen to Match Old Layout

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/bmi/BmiScreen.kt`

**Reference:** Read old app at:
`/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/bmi/BmiDetailsScreen.kt`

**What to do:**

Old BMI screen uses `StandardHealthScaffold` with:
- Portrait: `BmiEntryCard` (two inputs side-by-side + calculate button) -> `BmiDataCard` (BMI value left, weight status right) -> `BmiDynamicGraphicsChart`
- Landscape: Left = entry + chart, Right = data card

**Step 1:** Read the old BmiDetailsScreen.kt and all its sub-components (BmiEntryCard, BmiDataCard, BmiDynamicGraphicsChart) completely.

**Step 2:** Replace `ScreenWithToolbar` with health calculator layout pattern. Since the new app doesn't have `StandardHealthScaffold` with ads, create the equivalent scrollable layout:
```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HealthScreenTitle(text = stringResource(Res.string.title_screen_bmi))
        // subtitle with user info if available
        BmiEntryCard(...)  // Two inputs side-by-side
        BmiDataCard(...)   // Results with left/right layout
        BmiDynamicGraphicsChart(...)  // Visual chart
    }
    // Ad banner slot if needed
}
```

**Step 3:** Ensure `BmiEntryCard` has two `FormattedDoubleTextField` side-by-side (120.dp width, 60.dp height each) with the calculate button in the same row or below.

**Step 4:** Ensure `BmiDataCard` matches old layout: left column (Your BMI label + large value), right column (Weight Status + outlined category label + difference text).

**Step 5:** Build: `./gradlew assembleDebug`

**Step 6:** Commit: `git commit -m "fix: Rewrite BMI screen to match production VitalsCalc layout"`

---

## Task 7: Rewrite BMR Screen to Match Old Layout

**Reference:** `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/bmr/BmrDetailsScreen.kt`

Same pattern: read old, match layout exactly. Uses `StandardHealthScaffold`, shows `BmrDetailsCard`, `TdeeDetailsCard`, `CalorieGoalCard`. Profile data in subtitle.

---

## Task 8: Rewrite Heart Rate Screen to Match Old Layout

**Reference:** `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/heartrate/HeartRateZonesScreen.kt`

Same pattern. Uses `StandardHealthScaffold`, `HeartRateZoneDetailsCard`, `HeartRateZonesChart`.

---

## Task 9: Rewrite Body Fat Screen to Match Old Layout

**Reference:** `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/bodyfat/BodyFatScreen.kt`

Same pattern. Uses `StandardHealthScaffold`, `MeasurementInputCard`, `BodyFatResultCard`.

---

## Task 10: Rewrite Ideal Weight Screen to Match Old Layout

**Reference:** `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/idealweight/IdealWeightScreen.kt`

Same pattern. Uses `StandardHealthScaffold`, `FormulaComparisonCard`.

---

## Task 11: Rewrite Water Intake Screen to Match Old Layout

**Reference:** `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/water/WaterIntakeScreen.kt`

Same pattern. Uses `StandardHealthScaffold`, `WaterIntakeInputCard` (activity slider, climate switch in a card), `WaterResultCard` (water drop icon, not info icon).

---

## Task 12: Rewrite Blood Pressure Screen to Match Old Layout

**Reference:** `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/bloodpressure/BloodPressureScreen.kt`

Same pattern. Uses `StandardHealthScaffold`, input card with systolic/diastolic side-by-side, BpCategoryDisplay, guidance card, recent readings.

---

## Task 13: Rewrite More Screen to Match Old Layout

**Reference:** `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/more/MoreScreen.kt`

Old: Grouped section cards with dividers, menu items with icon/title/chevron, 12dp rounded sections. No toolbar.

---

## Task 14: Rewrite Calculators Grid to Match Old Layout

**Reference:** `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/features/calculators/CalculatorsScreen.kt`

Old: `LazyVerticalGrid` with `GridCells.Adaptive(100.dp)`, square tiles with `aspectRatio(1f)`, `ElevatedCard`, icon 32dp + title labelMedium.

---

## Task 15: Verify All Screens Build and Match

**Step 1:** Build Android: `./gradlew assembleDebug`
**Step 2:** Build iOS framework: `./gradlew linkDebugFrameworkIosSimulatorArm64 linkDebugFrameworkIosArm64`
**Step 3:** Full iOS build: `xcodebuild -workspace iosApp/iosApp.xcworkspace -scheme iosApp -configuration Debug -destination 'platform=iOS Simulator,name=iPhone 17 Pro' build`
**Step 4:** Commit: `git commit -m "fix: Complete visual parity with production VitalsCalc"`

---

## Execution Notes

### The Golden Rule for Each Screen

For EVERY screen task (5-14), the implementer MUST:

1. **Read the OLD screen file completely** - every line, every sub-component
2. **Read the OLD sub-components** (BmiEntryCard, BmiDataCard, etc.) completely
3. **Replicate the exact composable tree** - same components, same order, same nesting
4. **Match exact dp/sp values** - padding, spacing, font sizes, icon sizes
5. **Match exact color references** - `MaterialTheme.colorScheme.primary`, custom health colors
6. **Keep the new architecture** - UiStateHolder, UiEvent, Koin injection stays
7. **Only the UI layer changes** - domain logic, navigation, DI untouched

### What Stays From New App
- Navigation (Jetpack Nav Compose with @Serializable routes)
- State management (UiStateHolder + UiState + UiEvent)
- DI (Koin in AppInitializer)
- Design system module structure (but with old values)
- Database (Room)
- Auth, subscriptions, premium gating

### What Gets Replaced
- All screen composable layouts
- Design system color values
- Font family
- Spacing defaults
- Component defaults (card radius, button height)
