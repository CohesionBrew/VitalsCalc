# VitalsCalc → VitalsCalcNew Migration Plan

## Context

VitalsCalc is a production health calculator app (live on both app stores) built with an older architecture (Voyager, MVVM, multiplatform-settings). It needs to be ported to VitalsCalcNew, which uses the KAppMaker template architecture (Jetpack Navigation Compose, UiStateHolder/MVI, Room DB) — matching sister apps ListHarbor and CafeConnections.

**Goal:** Complete feature-parity port of all 16 screens, 7 calculators, history, premium gating, widgets, and adaptive UI.

**Decisions:**
- Room DB for history (not settings-based JSON)
- One-time purchase via RevenueCat (not subscriptions)
- Widgets included (Android Glance + iOS WidgetKit)
- Health* components in `:designsystem` module
- New `:shared-domain` module for calculators
- Compose Cupertino (RobinPcrd v3.3.0) for adaptive iOS UI

---

## Phase 0: Foundation (Module Setup & Dependencies)

### 0.1 Create `:shared-domain` module
- **Create:** `shared-domain/build.gradle.kts` (KMP library: jvm, iosArm64, iosSimulatorArm64)
  - Dependencies: `kotlinx.datetime`, `kotlinx.coroutines.core`, `kotlin("test")` in commonTest
- **Modify:** `settings.gradle.kts` — add `include(":shared-domain")`
- **Modify:** `composeApp/build.gradle.kts` — add `implementation(projects.sharedDomain)` to commonMain

### 0.2 Add dependencies to version catalog
- **Modify:** `gradle/libs.versions.toml` — add:
  - `cupertino = "3.3.0"` (io.github.robinpcrd: cupertino, cupertino-adaptive, cupertino-native, cupertino-icons-extended)
  - `vico = "2.4.3"` (com.patrykandpatrick.vico:multiplatform)
  - `glance = "1.1.1"` (androidx.glance:glance-appwidget, glance-material3)
- **Modify:** `composeApp/build.gradle.kts` — add cupertino + vico to commonMain, glance to androidMain
- **Modify:** `designsystem/build.gradle.kts` — add cupertino dependencies

### Verify: `./gradlew assembleDebug` + `./gradlew linkDebugFrameworkIosSimulatorArm64`

---

## Phase 1: Domain Layer (shared-domain)

### 1.1 Port existing calculators (copy from old project)
Source: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/shared-domain/src/commonMain/kotlin/.../calculator/`
Target: `shared-domain/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/domain/calculator/`

- `BmiCalculator.kt` — direct copy
- `BmrCalculator.kt` — direct copy (4 formulas)
- `TdeeCalculator.kt` — direct copy (ActivityLevel + CalorieGoal enums)
- `HeartRateZoneCalculator.kt` — direct copy

### 1.2 Extract new calculators from old ViewModels
- `BodyFatCalculator.kt` — extract from old `BodyFatViewModel.kt` (Navy, BMI-based, RFM methods)
- `IdealWeightCalculator.kt` — extract from old `IdealWeightViewModel.kt` (Robinson, Miller, Devine, Hamwi, BMI-based)
- `WaterIntakeCalculator.kt` — extract from old `WaterIntakeViewModel.kt`
- `BloodPressureClassifier.kt` — extract from old `BloodPressure.kt` (AHA classification)

### 1.3 Domain models (in shared-domain)
- `BodyFatCategory.kt`, `BodyFatMethod.kt`
- `BpCategory.kt` (without Color — that stays in presentation)
- `IdealWeightResults.kt`
- `Gender.kt`, `UnitSystem.kt` (shared enums)

### 1.4 Port and create tests
Copy: `BmiCalculatorTest.kt`, `BmrCalculatorTest.kt`, `HeartRateZoneCalculatorTest.kt`
Create: `BodyFatCalculatorTest.kt`, `IdealWeightCalculatorTest.kt`, `WaterIntakeCalculatorTest.kt`, `BloodPressureClassifierTest.kt`

### Verify: `./gradlew :shared-domain:jvmTest`

---

## Phase 2: Data Layer (Room Database)

### 2.1 Domain models for history entries
**Create in commonMain:** `domain/model/history/CalculationEntry.kt`
- Sealed class hierarchy with `@Serializable` subtypes: `BmiHistoryEntry`, `BmrHistoryEntry`, `BodyFatHistoryEntry`, `IdealWeightHistoryEntry`, `WaterIntakeHistoryEntry`, `BloodPressureHistoryEntry`, `WeightHistoryEntry`
- Each has: `id`, `timestamp`, `primaryValue`, type-specific fields

### 2.2 Room entities (nonWebMain)
Following existing pattern: Entity → EntityMapper → DAO → LocalDataSource → Impl

**Create:** `data/source/local/entity/CalculationHistoryEntity.kt`
```
@Entity("calculation_history"): id, type, dataJson, primaryValue, category, createdAt
+ CalculationHistoryEntityMapper (EntityMapper<Entity, CalculationEntry>)
```

**Create:** `data/source/local/entity/UserProfileEntity.kt`
```
@Entity("user_profile"): id, name, dob, gender, heightCm, weightKg, bodyFatPct,
  waistCm, neckCm, hipCm, restingHr, useMetric, activityLevel, calorieGoal, bmrFormula
+ UserProfileEntityMapper
```

### 2.3 DAOs (nonWebMain)
- `data/source/local/dao/CalculationHistoryDao.kt` — extends `BaseRoomDao<String, CalculationHistoryEntity>` + custom queries: `getByTypeFlow(type)`, `getLatestByType(type)`, `getCountFlow()`, `getHistorySince(timestamp)`, `deleteSameDayEntries(type, dayStart, dayEnd)`
- `data/source/local/dao/UserProfileDao.kt` — extends `BaseRoomDao<String, UserProfileEntity>`

### 2.4 LocalDataSource interfaces (commonMain)
- `data/source/local/CalculationHistoryLocalDataSource.kt` — extends `LocalDataSource<String, CalculationEntry>` + `getByType(type)`, `getLatestByType(type)`, `getCountFlow()`
- `data/source/local/UserProfileLocalDataSource.kt` — extends `LocalDataSource<String, UserProfile>`

### 2.5 LocalDataSource implementations (nonWebMain)
- `data/source/local/impl/CalculationHistoryLocalDataSourceImpl.kt` — extends `BaseRoomLocalDataSource`
- `data/source/local/impl/UserProfileLocalDataSourceImpl.kt` — extends `BaseRoomLocalDataSource`

### 2.6 Update AppDatabase
**Modify:** `data/source/local/AppDatabase.kt` — add entities, DAOs, bump version to 2 with migration

### 2.7 Repositories (commonMain)
- `data/repository/HistoryRepository.kt` — interface: `getHistory(daysLimit?)`, `addEntry()`, `upsertEntry()`, `deleteEntry()`, `clearHistory()`, `getHistoryCount()`, `getLatestByType()`
- `data/repository/HistoryRepositoryImpl.kt` — backed by `CalculationHistoryLocalDataSource`
- `data/repository/UserProfileRepository.kt` — interface
- `data/repository/UserProfileRepositoryImpl.kt` — backed by `UserProfileLocalDataSource`

### 2.8 Register in DI
**Modify:** `AppInitializer.kt` dataModule — add DAO singletons, LocalDataSource bindings, Repository bindings

### Verify: `./gradlew assembleDebug` + `./gradlew :composeApp:jvmTest`

---

## Phase 3: Design System Additions

### 3.1 Health components → `designsystem/src/commonMain/.../components/health/`
Port from old `core/presentation/components/`:
- `HealthGenderSelectorToggle.kt` — adaptive gender toggle
- `HealthUnitSystemSwitch.kt` — metric/imperial toggle
- `HealthOutlinedText.kt` — numeric input field (expect/actual for platform styling)
- `HealthActionButton.kt` — calculate button
- `HealthDropdownSelector.kt` — activity levels, formulas
- `HealthAlertDialog.kt` — confirmations
- `BmiIndicatorBar.kt` — color-coded BMI range bar
- `CalculatorTileGrid.kt` — calculator grid for Calculators tab
- `DashboardMetricCard.kt` — home dashboard card
- `HistoryEntryCard.kt` — history list item
- `HistoryFilterChips.kt` — history filter chips
- `DoubleFormattedTextBox.kt` — numeric input with formatting
- `DOBInputPicker.kt` — date of birth picker
- `AdaptiveWheelSelector.kt` — iOS picker / Material dropdown

### 3.2 Update designsystem build.gradle.kts
Add cupertino and vico dependencies to the module.

### Verify: `./gradlew assembleDebug`

---

## Phase 4: Core Infrastructure

### 4.1 Premium/Feature gating
- **Create:** `domain/premium/PremiumFeature.kt` — enum: AD_FREE, UNLIMITED_HISTORY, CHARTS, HEALTH_SYNC, FULL_WIDGETS, DATA_EXPORT
- **Create:** `domain/premium/FeatureAccessManager.kt` — uses `SubscriptionRepository` to check OTP entitlement. `isPro(): Flow<Boolean>`, `hasAccess(feature): Flow<Boolean>`, `getHistoryDaysLimit(isPro): Int?`

### 4.2 Widget updater interface
- **Create:** `domain/widget/WidgetUpdater.kt` (commonMain) — interface + `NoOpWidgetUpdater`
- Platform impls registered in platform DI modules

### 4.3 Register in DI
**Modify:** `AppInitializer.kt` — add `FeatureAccessManager`, repositories to `domainModule`/`dataModule`

### Verify: `./gradlew assembleDebug`

---

## Phase 5: Navigation Restructuring

### 5.1 Create tab screen stubs
- `presentation/screens/calculators/` — CalculatorsScreenRoute, CalculatorsUiState, CalculatorsUiStateHolder, CalculatorsScreen
- `presentation/screens/history/` — HistoryScreenRoute, HistoryUiState, HistoryUiStateHolder, HistoryScreen
- `presentation/screens/more/` — MoreScreenRoute, MoreScreen (stateless)

### 5.2 Reconfigure bottom nav (4 tabs)
**Modify:** `MainScreenRoute.kt` — `getBottomNavItemsWithDestination()` returns:
1. Home (ic_home) → HomeScreenRoute
2. Calculators (ic_calculator) → CalculatorsScreenRoute
3. History (ic_history) → HistoryScreenRoute
4. More (ic_more) → MoreScreenRoute

### 5.3 Add all routes to MainNavigation
**Modify:** `MainNavigation.kt` — add composable routes for all 16 screens

### 5.4 Add icons + string resources
- Add bottom nav icons to designsystem resources (ic_calculator, ic_history, ic_more)
- Add string resources for tab labels, calculator names, categories, units

### 5.5 Register UiStateHolders
**Modify:** `AppInitializer.kt` presentationModule — add all new UiStateHolders

### Verify: `./gradlew assembleDebug` — verify 4-tab nav launches

---

## Phase 6: Screen Implementation (16 Screens)

Each screen follows the pattern:
```
*ScreenRoute.kt  (@Serializable, implements ScreenRoute, gets UiStateHolder, passes nav callbacks)
*UiState.kt      (@Immutable data class + sealed class *UiEvent)
*UiStateHolder.kt (extends UiStateHolder, MutableStateFlow, onUiEvent())
*Screen.kt       (two overloads: stateful with UiStateHolder, stateless with UiState+onUiEvent)
```

### 6A: Home Dashboard
- **Modify:** existing `HomeUiState.kt`, `HomeUiStateHolder.kt`, `HomeScreenRoute.kt`, `HomeScreen.kt`
- Add: `DashboardMetric` model, inject HistoryRepo + FeatureAccessManager, show latest values for all calculators

### 6B: Calculators Grid
- Complete stub from Phase 5 — 7 tiles navigating to calculator screens

### 6C: Calculator Screens (7 screens, ~4 files each = 28 files)

| Screen | Key State Fields | Calculator Used |
|--------|-----------------|-----------------|
| BMI | height, weight, useMetric, bmi, category | BmiCalculator |
| BMR/TDEE | gender, height, weight, age, bodyFat, activityLevel, formula, bmr, tdee | BmrCalculator, TdeeCalculator |
| Heart Rate | age, restingHr, method, zones[], maxHr | HeartRateZoneCalculator |
| Body Fat | method, measurements, result, category | BodyFatCalculator |
| Ideal Weight | height, gender, useMetric, results{} | IdealWeightCalculator |
| Water Intake | weight, activityLevel, hotClimate, result | WaterIntakeCalculator |
| Blood Pressure | systolic, diastolic, pulse, category, recentEntries | BloodPressureClassifier |

Each injects: calculator, UserProfileRepository, HistoryRepository, FeatureAccessManager, WidgetUpdater

### 6D: History Tab
- Complete stub — LazyColumn of HistoryEntryCards, filter chips (All/BMI/BMR/BodyFat/BP/Weight), Pro gating (30-day limit for free), clear/delete

### 6E: More Tab + Settings
- **More:** Stateless menu → Profile, Settings, Premium, About, References
- **Settings:** SettingsScreenRoute + SettingsUiStateHolder — unit system, theme, language, advanced mode
- **User Profile:** UserProfileScreenRoute + UserProfileUiStateHolder — CRUD against UserProfileRepository
- **Premium:** Adapt existing PaywallScreen for OTP (non-consumable via RevenueCat)
- **About:** Simple screen — version, links
- **References:** List of scientific references for formulas

### Verify: `./gradlew assembleDebug` + `./gradlew linkDebugFrameworkIosSimulatorArm64`

---

## Phase 7: Premium/OTP Gating Integration

- Wire `FeatureAccessManager.isPro()` into: HistoryUiStateHolder (days limit), HomeUiStateHolder (charts), AdsManager (ad-free)
- Configure RevenueCat dashboard for non-consumable OTP product
- Existing `SubscriptionRepository.hasPremiumAccess()` works for OTP (RevenueCat treats entitlements uniformly)

### Verify: `./gradlew assembleDebug`

---

## Phase 8: Platform-Specific Code

### 8.1 WidgetUpdater implementations
- `androidMain`: `AndroidWidgetUpdater.kt` — writes to SharedPreferences, triggers Glance updates
- `iosMain`: `IosWidgetUpdater.kt` — writes to UserDefaults (App Groups) for WidgetKit
- `jvmMain`: `NoOpWidgetUpdater`

### 8.2 Platform DI registration
- Each platform module registers its `WidgetUpdater` implementation

### 8.3 Compose Cupertino integration
- Wrap key components with adaptive wrappers (AdaptiveSwitch, AdaptiveNavigationBar)
- Platform-specific theming in iosMain/androidMain

### Verify: `./gradlew assembleDebug` + `./gradlew linkDebugFrameworkIosSimulatorArm64`

---

## Phase 9: Android Widgets (Glance)

- `androidMain/widget/WidgetDataProvider.kt` — reads from SharedPreferences
- 4 widget types (each = GlanceAppWidget + GlanceAppWidgetReceiver):
  - `BmiWidget.kt` + `BmiWidgetReceiver.kt`
  - `BodyFatWidget.kt` + `BodyFatWidgetReceiver.kt`
  - `BloodPressureWidget.kt` + `BloodPressureWidgetReceiver.kt`
  - `HeartRateWidget.kt` + `HeartRateWidgetReceiver.kt`
- Widget info XML files in `res/xml/`
- Register receivers in `AndroidManifest.xml`

### Verify: `./gradlew assembleDebug` — verify widgets in picker

---

## Phase 10: iOS Widgets (WidgetKit)

- Add Widget Extension target in Xcode project
- Configure App Groups for data sharing
- SwiftUI widget views: `BmiWidget.swift`, `BodyFatWidget.swift`, `BloodPressureWidget.swift`, `HeartRateWidget.swift`
- `WidgetDataProvider.swift` — reads from UserDefaults (App Groups)

### Verify: `xcodebuild -workspace iosApp/iosApp.xcworkspace -scheme iosApp -configuration Debug -destination 'platform=iOS Simulator,name=iPhone 17 Pro' build`

---

## Phase 11: Cleanup & Final Integration

- Remove unused KAppMaker screens: `FavoriteScreen`, `CreditBalanceScreen` (or hide from nav)
- Remove example code: `ExampleEntity`, `ExampleDao`, `ExampleLocalDataSource`, `ExampleModel`, `ExampleNativeTextView`
- Complete string resources for all calculator names, categories, units, labels
- Compose Cupertino: AdaptiveNavigationBar, AdaptiveSwitch, AdaptiveDatePicker

### Final Verification
1. `./gradlew clean`
2. `./gradlew assembleDebug` (Android)
3. `./gradlew linkDebugFrameworkIosSimulatorArm64 linkDebugFrameworkIosArm64` (iOS framework)
4. `xcodebuild ... -destination 'platform=iOS Simulator,name=iPhone 17 Pro' build` (Full iOS)
5. `./gradlew :shared-domain:jvmTest` (Calculator tests)
6. `./gradlew :composeApp:jvmTest` (App tests)

---

## Critical Files Summary

| File | Role |
|------|------|
| `settings.gradle.kts` | Add `:shared-domain` module |
| `gradle/libs.versions.toml` | Add cupertino, vico, glance deps |
| `composeApp/build.gradle.kts` | Add new dependencies + shared-domain |
| `designsystem/build.gradle.kts` | Add cupertino deps |
| `root/AppInitializer.kt` | Central DI — all new repos, DAOs, UiStateHolders |
| `data/source/local/AppDatabase.kt` | Room — new entities + DAOs |
| `presentation/screens/main/MainScreenRoute.kt` | Bottom nav → 4 tabs |
| `presentation/screens/main/MainNavigation.kt` | All 16 screen routes |
| `root/AppNavigation.kt` | Top-level nav (onboarding → main → paywall) |

## Estimated New Files: ~140 | Modified Files: ~25

## Source References (Old VitalsCalc)
- Screens: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/.../features/`
- Calculators: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/shared-domain/src/commonMain/.../calculator/`
- Components: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/.../core/presentation/components/`
- Navigation: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/.../core/ui/navigation/Screens.kt`
- Domain: `/Volumes/Crucial/Dev/cohesionbrew/VitalsCalc/composeApp/src/commonMain/.../core/domain/`
