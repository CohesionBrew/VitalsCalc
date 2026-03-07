# Visual Parity: BMI, Profile, Settings Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Make BMI Calculator, Profile, and Settings screens in the new app match the old VitalsCalc app 100% visually and functionally.

**Architecture:** Port old composables directly, adapting from Voyager/BmiDetailsViewModel to UiStateHolder pattern + Jetpack Nav + Room DB. Profile data (age, height, gender) is stored once and calculators pull from it.

**Tech Stack:** Compose Multiplatform, Room, Koin DI, UiStateHolder pattern, @Serializable routes

**Reference:** Old app at `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/`

---

## Task 1: Add `advancedMode` and `language` to Data Layer

The `UserProfile` domain model, Room entity, and mapper need two new fields for Settings screen parity. The `dob` field already exists as `String?`.

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/domain/model/UserProfile.kt`
- Modify: `composeApp/src/nonWebMain/kotlin/com/cohesionbrew/healthcalculator/data/source/local/entity/UserProfileEntity.kt`
- Modify: `composeApp/src/nonWebMain/kotlin/com/cohesionbrew/healthcalculator/data/source/local/AppDatabase.kt`

**Step 1: Add fields to UserProfile domain model**

In `UserProfile.kt`, add after `bmrFormula`:
```kotlin
val advancedMode: Boolean = false,
val language: String = "en"
```

**Step 2: Add columns to UserProfileEntity**

In `UserProfileEntity.kt`, add after `bmr_formula` column:
```kotlin
@ColumnInfo(name = "advanced_mode") val advancedMode: Boolean = false,
@ColumnInfo(name = "language") val language: String = "en"
```

**Step 3: Update UserProfileEntityMapper**

Add `advancedMode` and `language` to both `toEntity()` and `toModel()` methods.

**Step 4: Bump database version**

In `AppDatabase.kt`, change `version = 3` to `version = 4`. The existing `fallbackToDestructiveMigration(dropAllTables = true)` in DI.kt handles the schema change automatically.

**Step 5: Build to verify**

Run: `./gradlew :composeApp:compileDebugKotlinAndroid`
Expected: BUILD SUCCESSFUL

**Step 6: Commit**

```
feat: Add advancedMode and language fields to UserProfile data layer
```

---

## Task 2: Rewrite Settings Screen to Match Old App

The old Settings screen has: card container, metric toggle with descriptive subtitle, Advanced Mode toggle, Language dropdown with flag icons, "Save and Close" button. The new screen only has a flat metric toggle and clear history button.

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/settings/SettingsUiState.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/settings/SettingsUiStateHolder.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/settings/SettingsScreen.kt`
- Modify: `composeApp/src/commonMain/composeResources/values/strings.xml` (if new strings needed)

**Step 1: Update SettingsUiState**

Replace content with:
```kotlin
@Immutable
data class SettingsUiState(
    val useMetric: Boolean = true,
    val advancedMode: Boolean = false,
    val language: String = "en",
    val isSaving: Boolean = false,
    val isLoading: Boolean = true
)

sealed interface SettingsUiEvent {
    data class OnUseMetricChanged(val useMetric: Boolean) : SettingsUiEvent
    data class OnAdvancedModeChanged(val enabled: Boolean) : SettingsUiEvent
    data class OnLanguageChanged(val code: String) : SettingsUiEvent
    data object OnSave : SettingsUiEvent
}
```

**Step 2: Update SettingsUiStateHolder**

- Remove `historyRepository` dependency (clear history moves elsewhere or stays as secondary)
- Load `advancedMode` and `language` from profile
- `OnSave` saves all three fields back to profile and navigates back

```kotlin
class SettingsUiStateHolder(
    private val userProfileRepository: UserProfileRepository
) : UiStateHolder() {
    // ...
    private fun loadSettings() = uiStateHolderScope.launch {
        val profile = userProfileRepository.getProfile() ?: UserProfile()
        _uiState.update {
            it.copy(
                useMetric = profile.useMetric,
                advancedMode = profile.advancedMode,
                language = profile.language,
                isLoading = false
            )
        }
    }

    fun onUiEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.OnUseMetricChanged -> _uiState.update { it.copy(useMetric = event.useMetric) }
            is SettingsUiEvent.OnAdvancedModeChanged -> _uiState.update { it.copy(advancedMode = event.enabled) }
            is SettingsUiEvent.OnLanguageChanged -> _uiState.update { it.copy(language = event.code) }
            SettingsUiEvent.OnSave -> saveSettings()
        }
    }

    private fun saveSettings() = uiStateHolderScope.launch {
        _uiState.update { it.copy(isSaving = true) }
        val state = _uiState.value
        val existing = userProfileRepository.getProfile() ?: UserProfile()
        userProfileRepository.saveProfile(
            existing.copy(
                useMetric = state.useMetric,
                advancedMode = state.advancedMode,
                language = state.language
            )
        )
        _uiState.update { it.copy(isSaving = false) }
    }
}
```

**Step 3: Rewrite SettingsScreen composable**

Port the old `SettingsCard.kt` Android layout. Key elements:
- Wrap everything in `Card` with `BorderStroke(6.dp, outline)`, `RoundedCornerShape(16.dp)`
- "Settings" header via `HealthMenuPagesHeaderText` (or Text with titleLarge + centered)
- `AdaptiveSettingsToggleRow` for metric toggle with subtitle "You have chosen: Imperial [Inch & Pound]" / "Metric [cm & kg]"
- `AdaptiveSettingsToggleRow` for Advanced Mode with "Enabled"/"Disabled" subtitle
- Language dropdown with flag icons (port `HealthLanguageDropdown` from old app or create equivalent)
- `HealthActionButton` for "Save and Close"
- HorizontalDividers between sections

Note: The `HealthLanguageDropdown` component and `LanguageMenuItem` model need to be created in the designsystem module or in the settings package. Port from old app's `core.presentation.components.HealthLanguageDropdown`.

**Step 4: Update Koin DI**

If `SettingsUiStateHolder` constructor changed (removed `historyRepository`), update the Koin module factory.

**Step 5: Build and verify**

Run: `./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL

**Step 6: Commit**

```
feat: Rewrite Settings screen to match old VitalsCalc (card, advanced mode, language)
```

---

## Task 3: Rewrite UserProfile Screen to Match Old App

The old Profile screen has: card container, Gender toggle with note, DOB picker with age display, Height as feet/inches dropdowns (imperial) or cm field (metric), "Save and Close" button. No weight or resting HR fields.

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/userprofile/UserProfileUiState.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/userprofile/UserProfileUiStateHolder.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/userprofile/UserProfileScreen.kt`

**Step 1: Update UserProfileUiState**

```kotlin
@Immutable
data class UserProfileUiState(
    val gender: String = "male",
    val dob: String? = null,         // ISO date string "YYYY-MM-DD"
    val age: Int? = null,            // Calculated from DOB
    val heightCm: Double = 0.0,
    val heightFeet: Int = 5,         // For imperial display
    val heightInches: Int = 7,       // For imperial display
    val useMetric: Boolean = true,
    val isSaving: Boolean = false,
    val isLoading: Boolean = true
)

sealed interface UserProfileUiEvent {
    data class OnGenderChanged(val isMale: Boolean) : UserProfileUiEvent
    data class OnDobChanged(val dob: String) : UserProfileUiEvent
    data class OnHeightCmChanged(val value: Double) : UserProfileUiEvent
    data class OnHeightFeetChanged(val feet: Int) : UserProfileUiEvent
    data class OnHeightInchesChanged(val inches: Int) : UserProfileUiEvent
    data object OnSave : UserProfileUiEvent
}
```

**Step 2: Update UserProfileUiStateHolder**

- Load `dob` from profile, calculate age from it
- Load height and convert to feet/inches for imperial display
- On save: convert feet/inches back to cm if imperial, save DOB, gender, height
- Remove weight/restingHr handling

```kotlin
class UserProfileUiStateHolder(
    private val userProfileRepository: UserProfileRepository
) : UiStateHolder() {
    // loadProfile: populate gender, dob, age, heightCm, heightFeet, heightInches, useMetric
    // onSave: convert imperial to cm if needed, save profile, navigate back
}
```

**Step 3: Rewrite UserProfileScreen composable**

Port the old `ProfileCard.kt` layout:
- `Card` with `BorderStroke(6.dp, outline)`, `RoundedCornerShape(16.dp)`
- "Profile" header centered
- Gender toggle with "Select Sex" label and "(used for metabolic calculations)" italic subtitle
- Note text about physiological models (italic)
- `HorizontalDivider`
- `DobInputSection` — date picker field showing DOB, with "Age: X years" helper text
  - Port from old app's `core.presentation.components.DobInputSection` or create equivalent
- `HorizontalDivider`
- Height input: Imperial = `OutlinedGroupBox` with two `AdaptiveWheelSelector` (Feet 3-7, Inches 0-11). Metric = `FormattedDoubleTextField` for cm.
  - Port `AdaptiveWheelSelector` and `OutlinedGroupBox` from old app or create equivalents
- `HorizontalDivider`
- `HealthActionButton` "Save and Close"

**Step 4: Build and verify**

Run: `./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```
feat: Rewrite Profile screen to match old VitalsCalc (card, DOB, height dropdowns)
```

---

## Task 4: Update BMI Screen to Match Old App

The old BMI screen shows "Age X, H = X ft Y in" subtitle from profile, only asks for Weight, and displays rich results. The new screen already has BmiDataCard and BmiDynamicGraphicsChart but is missing the profile subtitle and shows height input unnecessarily.

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/bmi/BmiUiState.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/bmi/BmiUiStateHolder.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/presentation/screens/bmi/BmiScreen.kt`

**Step 1: Add profile info to BmiUiState**

Add fields for the subtitle display:
```kotlin
data class BmiUiState(
    val age: Int? = null,                  // From profile DOB
    val heightDisplayText: String = "",     // e.g., "5 ft 11 in" or "180 cm"
    val heightCm: Double = 0.0,
    val weightKg: Double = 0.0,
    val useMetric: Boolean = true,
    // ... rest unchanged
)
```

**Step 2: Update BmiUiStateHolder.loadDefaults()**

Calculate age from profile DOB and format height display text:
```kotlin
private fun loadDefaults() = uiStateHolderScope.launch {
    val profile = userProfileRepository.getProfile() ?: return@launch
    val age = profile.dob?.let { calculateAge(it) }
    val heightText = if (profile.useMetric) {
        "${profile.heightCm.roundToInt()} cm"
    } else {
        val totalInches = profile.heightCm / 2.54
        val feet = (totalInches / 12).toInt()
        val inches = (totalInches % 12).roundToInt()
        "$feet ft $inches in"
    }
    _uiState.update {
        it.copy(
            age = age,
            heightDisplayText = heightText,
            heightCm = profile.heightCm,
            weightKg = profile.weightKg,
            useMetric = profile.useMetric
        )
    }
}
```

**Step 3: Update BmiScreen composable**

- Add subtitle below title: "Age X, H = X ft Y in" (only shown when profile has data)
- Remove height input from BmiEntryCard (only show weight)
- Remove OnHeightChanged/OnUnitSystemChanged events if no longer user-editable on this screen
- Keep BmiDataCard and BmiDynamicGraphicsChart as-is (they already match old app)

In PortraitLayout, after `HealthScreenTitle`:
```kotlin
HealthScreenTitle(text = title)

// Profile subtitle (matches old app)
if (uiState.age != null && uiState.heightDisplayText.isNotEmpty()) {
    Text(
        text = "Age ${uiState.age}, H = ${uiState.heightDisplayText}",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
```

**Step 4: Build and verify**

Run: `./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```
feat: Update BMI screen with profile subtitle and weight-only input
```

---

## Task 5: Port Shared Components from Old App

Several components referenced by the old screens need to be ported to the new designsystem module or screen packages:

**Components to port (assess which already exist in new app's designsystem):**

1. **`HealthMenuPagesHeaderText`** — Centered title text for card-based screens (Profile, Settings). Check if `HealthScreenTitle` in designsystem can serve this purpose or port the old one.

2. **`AdaptiveSettingsToggleRow`** — Toggle row with title + subtitle for Settings. Port from old `core.presentation.components.AdaptiveSettingsToggleRow`.

3. **`HealthLanguageDropdown` + `LanguageMenuItem`** — Dropdown with flag icons. Port from old app. Flag drawables need to be added to compose resources.

4. **`DobInputSection`** — Date picker with age display. Port from old `core.presentation.components.DobInputSection`.

5. **`AdaptiveWheelSelector`** — Dropdown/wheel for feet and inches selection. Port from old `core.presentation.components.AdaptiveWheelSelector`.

6. **`OutlinedGroupBox`** — Outlined container grouping height inputs. Port from old `core.presentation.components.OutlinedGroupBox`.

7. **Flag drawable resources** — `us.xml`, `es.xml`, `fr.xml`, `flag_germany.xml`, `flag_brazil.xml`, `japan_flag.xml`, `flag_south_korea.xml`, `flag_china.xml`, `flag_taiwan.xml`, `flag_hong_kong.xml`. Copy from old app's drawable resources.

**For each component:**
- Check if an equivalent already exists in new app's `:designsystem` module
- If not, port the old code adapting imports to new package structure
- Place in `designsystem/src/commonMain/kotlin/.../designsystem/components/health/`

**Step 1: Audit existing designsystem components**

Run a glob to see what health components exist in the new designsystem.

**Step 2: Port missing components one by one**

Each component is a separate file. Port with minimal changes — just update package names and imports.

**Step 3: Copy flag drawables**

Copy flag SVG/XML resources from old app's composeResources to new app's designsystem or composeApp resources.

**Step 4: Build and verify**

Run: `./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```
feat: Port shared UI components from old VitalsCalc (toggles, dropdowns, DOB picker)
```

---

## Task 6: Integration Testing and Final Polish

**Step 1: Run full Android build**
Run: `./gradlew assembleDebug`

**Step 2: Run iOS framework build**
Run: `./gradlew linkDebugFrameworkIosSimulatorArm64`

**Step 3: Visual verification**

Launch on Android emulator/device and verify:
- Profile screen matches old screenshots (card, gender, DOB, height dropdowns)
- Settings screen matches old screenshots (card, toggles, language, save button)
- BMI screen matches old screenshots (subtitle, weight-only, results, category bar)

**Step 4: Commit**

```
chore: Visual parity verification for BMI, Profile, Settings screens
```

---

## Execution Order

Tasks 1 → 5 → 2 → 3 → 4 → 6

- **Task 1** (data layer) must go first — other tasks depend on new fields
- **Task 5** (shared components) should go next — screens need the ported components
- **Tasks 2, 3, 4** (screen rewrites) can then proceed in any order
- **Task 6** (integration) is last

## Key Reference Files (Old App)

| Component | Old Path |
|-----------|----------|
| ProfileCard | `features/profile/ProfileCard.kt` |
| SettingsCard | `features/settings/SettingsCard.kt` |
| BmiDetailsScreen | `features/bmi/BmiDetailsScreen.kt` |
| BMI Components | `features/bmi/components/Bmi*.kt` |
| Profile model | `core/domain/model/Profile.kt` |
| User model | `core/domain/model/User.kt` |
| Shared components | `core/presentation/components/*.kt` |
| Flag drawables | `shared/src/commonMain/composeResources/drawable/` |

All paths relative to `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/`
