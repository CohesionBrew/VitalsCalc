# Project Knowledge: VitalsCalcNew

## Project Overview
**Goal:** A comprehensive health metrics calculator app (rebuild of VitalsCalc on the KAppMaker template) providing BMI, BMR/TDEE, Heart Rate Zone, Body Fat, Ideal Weight, Blood Pressure, and Water Intake calculations with a freemium subscription model, authentication, and credit-based AI features. VitalsCalc is currently **live in production** on both the Google Play Store and Apple App Store.

## Technical Configuration
- **Type:** Compose Multiplatform (KAppMaker template)
- **Build System:** Gradle Kotlin DSL (`.kts`) with Version Catalog (`libs.versions.toml`) and `build-logic` included build
- **Language:** Kotlin
- **Min SDK:** 24 (Android 7.0)
- **Target/Compile SDK:** 36 (Android 16)
- **iOS Targets:** `iosArm64` (devices), `iosSimulatorArm64` (Apple Silicon simulators)

## Identifiers
- **Namespace:** `com.cohesionbrew.healthcalculator`
- **Application ID:** `com.cohesionbrew.healthcalculator`
- **iOS Framework:** `ComposeApp` (Static)
- **Root Project Name:** `VitalsCalc`
- **Compose Resources Package:** `com.cohesionbrew.healthcalculator.generated.resources`
- **Design System Resources Package:** `com.cohesionbrew.healthcalculator.designsystem.generated.resources` (aliased as `UiRes`)

## Module Structure
- **`:composeApp`**: Main application logic (Mobile, Desktop, Web).
    - Shared Logic: `composeApp/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/...`
    - Android Specifics: `composeApp/src/androidMain/kotlin/com/cohesionbrew/healthcalculator/...`
    - iOS Specifics: `composeApp/src/iosMain/kotlin/com/cohesionbrew/healthcalculator/...`
    - JVM Stubs: `composeApp/src/jvmMain/kotlin/...` — no-op actuals for running tests on host JVM
    - Non-Web (Room DB): `composeApp/src/nonWebMain/...`
    - Mobile-only: `composeApp/src/mobileMain/...`
- **`:designsystem`**: Reusable UI components and theme.
    - Path: `designsystem/src/commonMain/kotlin/com/cohesionbrew/healthcalculator/designsystem/...`
    - Components: buttons, cards, chips, dialogs, bottom nav, premium UI, onboarding pagers, health-specific UI
    - Theme: colors, typography, spacing
- **`:shared-domain`**: Pure Kotlin domain logic — health calculators and domain models.
    - Calculators: `BmiCalculator`, `BmrCalculator`, `TdeeCalculator`, `BodyFatCalculator`, `BloodPressureClassifier`, `HeartRateZoneCalculator`, `IdealWeightCalculator`, `WaterIntakeCalculator`
    - Models: `Gender`, `UnitSystem`, `BpCategory`, `BodyFatCategory`, `BodyFatMethod`, `IdealWeightResults`
    - Tests: `shared-domain/src/commonTest/kotlin/...` — unit tests for all calculators
- **`:libs:auth:auth-api`**: Auth abstraction layer (AuthServiceProvider interface)
- **`:libs:auth:auth-firebase`**: Firebase Auth implementation (KMPAuth)
- **`:libs:subscription:subscription-api`**: Subscription abstraction layer (SubscriptionProvider interface)
- **`:libs:subscription:subscription-revenuecat`**: RevenueCat implementation (default)
- **`:libs:subscription:subscription-adapty`**: Adapty implementation (alternative)
- **`iosApp/`**: Native iOS entry point (Xcode project with CocoaPods).

## Testing
- **Test command:** `./gradlew :composeApp:jvmTest` — runs all `commonTest` tests on host JVM
- **Domain tests:** `./gradlew :shared-domain:jvmTest` — runs calculator unit tests
- **All tests:** `./gradlew allTests`
- **Test location:** `composeApp/src/commonTest/kotlin/...` and `shared-domain/src/commonTest/kotlin/...`
- **JVM target purpose:** Enables running `commonTest` without iOS simulator (fast, no Xcode dependency)

## Tech Stack & Libraries
- **Architecture:** MVI-style with UiStateHolder (ViewModel) + UiState pattern
- **UI:** Jetpack Compose Multiplatform (Material3)
- **iOS Native UI:** Compose Cupertino (`io.github.robinpcrd` v3.x fork) — Adaptive*/Cupertino* components
- **Navigation:** Jetpack Navigation Compose (type-safe routes via `@Serializable` route classes)
- **DI:** Koin (4.x) with `startKoin` in `AppInitializer`
- **Networking:** Ktor (OkHttp for Android, Darwin for iOS)
- **Serialization:** Kotlinx Serialization (JSON)
- **Image Loading:** Coil 3
- **Database (Local):** Room (SQLite bundled) — `nonWebMain` source set only
- **Logging:** AppLogger (custom) backed by Napier + optional Telegram logger
- **Date/Time:** Kotlinx Datetime
- **Preferences:** Multiplatform Settings (Russhwolf)
- **Build Config:** gmazzo/gradle-buildconfig-plugin
- **UUID:** benasher44/uuid

### Version Policy
> The authoritative versions for all dependencies live in `gradle/libs.versions.toml`.
> Any versions mentioned above are **minimum major/minor versions** the project is designed for.
> Agents must:
> - Treat `libs.versions.toml` as the source of truth for exact versions.
> - Write code compatible with at least these minimum versions.
> - Never downgrade a library.
> - Only change dependency versions when a mission explicitly calls for an upgrade.

## Integrations
- **Auth:** KMPAuth (Google Sign-In + Firebase Auth) via `libs/auth/` modules
- **Notifications:** KMPNotifier (Local & Push via FCM)
- **Firebase:**
    - Analytics (event tracking, toggled via feature flags)
    - Crashlytics (crash reporting)
    - Remote Config (feature flags via `FeatureFlagManager`)
    - Cloud Messaging (FCM push notifications)
- **Ads:** Google AdMob (Android & iOS) — banner, interstitial, rewarded ads
    - Ad IDs configured via `local.properties` / BuildConfig
    - Ads toggleable via feature flags
- **Monetization:** RevenueCat (default) or Adapty (configurable via `SUBSCRIPTION_PROVIDER` in `gradle.properties`)
- **AI APIs:** OpenAI and Replicate API services exist but are not actively used for health features (inherited from KAppMaker template)
- **Settings:** Multiplatform Settings (key-value preferences)

## What This Project Does NOT Have
> Important: AI agents should NOT assume these exist or try to use them.
- **No Cloud Firestore** — No cloud database sync (local Room + API only)
- **No Voyager** — Uses Jetpack Navigation Compose (not Voyager)
- **No Social Features** — No user profiles visible to others, sharing, or community features
- **No DataStore/Protobuf** — Uses Multiplatform Settings for preferences, Room for structured data

## Code Architecture

### Source Set Hierarchy
The project uses a custom Kotlin source set hierarchy:
```
common
├── mobile (Android + iOS)
│   ├── android
│   └── ios
├── nonMobile (JS + WasmJS + JVM)
│   ├── js
│   ├── wasmJs
│   └── jvm
├── nonWeb (Android + iOS + JVM) — has Room DB
│   ├── android
│   ├── ios
│   └── jvm
└── web (JS + WasmJS)
    ├── js
    └── wasmJs
```

### Presentation Structure
Each screen follows UiStateHolder + UiState pattern in `presentation/screens/`:
```
presentation/screens/
├── about/             # Release notes / about info
├── account/           # User account management
├── bloodpressure/     # Blood pressure calculator
├── bmi/               # BMI calculator
├── bmr/               # BMR/TDEE calculator
├── bodyfat/           # Body fat calculator
├── calculators/       # Calculator grid (entry point to all calculators)
├── heartrate/         # Heart rate zone calculator
├── helpandsupport/    # Help & support
├── history/           # Calculation history log
├── home/              # Dashboard with latest metrics
├── idealweight/       # Ideal weight calculator
├── main/              # Main scaffold with bottom nav + nested NavHost
├── more/              # More menu (settings, profile, about, references)
├── onboarding/        # Onboarding flow (2 variations)
├── paywall/           # Paywall (custom + remote via RevenueCat/Adapty)
├── profile/           # User profile editing
├── references/        # Medical references
├── settings/          # App settings
├── signin/            # Authentication
├── subscriptions/     # Subscription management
├── userprofile/       # User profile (age, gender, height, weight)
└── waterintake/       # Water intake calculator

Each screen folder contains:
├── FeatureScreen.kt         # Composable UI
├── FeatureScreenRoute.kt    # @Serializable navigation route + Content()
├── FeatureUiState.kt        # UI state data class + UiEvent sealed interface
└── FeatureUiStateHolder.kt  # ViewModel (extends UiStateHolder/ViewModel)
```

### Health-Specific Components
In addition to the design system, health-specific presentation components live in:
```
presentation/components/health/
├── BmiIndicatorBar.kt         # BMI visual indicator
├── CalculatorTileGrid.kt      # Grid layout for calculator tiles
├── DashboardMetricCard.kt     # Home dashboard metric cards
├── DobInputSection.kt         # Date of birth input
├── HealthStatusColors.kt      # Category color mappings
├── HistoryEntryCard.kt        # History list entry card
├── HistoryFilterChips.kt      # Filter chips for history
└── TrendBadge.kt              # Trend direction badges
```

Design system health components (`designsystem/components/health/`):
```
├── AdaptiveWheelSelector.kt       # Platform-adaptive wheel picker
├── FormattedDoubleTextField.kt    # Numeric input with formatting
├── HealthActionButton.kt         # Calculate/submit button
├── HealthAlertDialog.kt          # Health-specific dialogs
├── HealthDropdownSelector.kt     # Dropdown for activity level, etc.
├── HealthGenderSelectorToggle.kt # Male/Female toggle
├── HealthInfoTooltip.kt          # Info tooltips
├── HealthOutlinedText.kt         # Outlined text for categories
├── HealthScreenTitle.kt          # Screen title component
├── HealthUnitSystemSwitch.kt     # Metric/Imperial toggle
└── OutlinedGroupBox.kt           # Grouped section container
```

### Navigation
- **Two-level NavHost:**
    - `AppNavigation` (root): Onboarding, Main, Paywall
    - `MainNavigation` (nested inside MainScreen): All tab and detail screens
- **Bottom Navigation Tabs:** Home, Calculators, History, More
- **Calculator Detail Screens:** BMI, BMR, Heart Rate, Body Fat, Ideal Weight, Water Intake, Blood Pressure
- **More Sub-screens:** Settings, UserProfile, About, References, Account, HelpAndSupport, Subscriptions, Paywall
- **Route pattern:** `@Serializable` classes implementing `ScreenRoute` with a `Content()` method
- **Local navigator:** `LocalNavigator` CompositionLocal provides the current NavHostController

### Data Layer
```
data/
├── BackgroundExecutor.kt         # Coroutine wrapper for background work
├── repository/
│   ├── CreditRepository.kt      # Credit balance & transactions
│   ├── HistoryRepository.kt     # Calculation history (interface)
│   ├── HistoryRepositoryImpl.kt  # Room-backed history
│   ├── SubscriptionRepository.kt # Subscription state
│   ├── UserProfileRepository.kt  # User health profile (interface)
│   ├── UserProfileRepositoryImpl.kt # Room-backed profile
│   └── UserRepository.kt        # Auth user management
├── source/
│   ├── featureflag/              # Feature flag manager (Firebase Remote Config)
│   ├── local/                    # Room-based local data sources
│   │   ├── CalculationHistoryLocalDataSource.kt
│   │   ├── CreditTransactionLocalDataSource.kt
│   │   ├── LocalDataSource.kt   # Room AppDatabase
│   │   └── UserProfileLocalDataSource.kt
│   ├── preferences/              # Multiplatform Settings wrapper
│   └── remote/
│       ├── apiservices/          # API service classes
│       ├── request/              # Request DTOs
│       └── response/             # Response DTOs
```

### Domain Layer
```
domain/
├── exceptions/           # Domain exceptions
├── model/
│   ├── credit/           # Credit system config DSL
│   ├── history/          # CalculationEntry, CalculationType, typed history entries
│   ├── AuthProvider.kt
│   ├── Subscription.kt
│   ├── User.kt
│   └── UserProfile.kt   # User health profile (age, gender, height, weight, units)
├── premium/
│   ├── FeatureAccessManager.kt  # Pro/free feature gating
│   └── PremiumFeature.kt
└── widget/
    └── WidgetUpdater.kt  # Home screen widget updates
```

### DI Setup
- Koin modules defined in `AppInitializer.kt` (not a separate `di/` folder)
- Three modules: `domainModule`, `dataModule`, `presentationModule`
- Platform module via `platformModule` (expect/actual)
- ViewModels registered via `viewModelOf(::UiStateHolderClass)`
- Credit system configured via DSL in `initializeCreditSystem()` (currently commented out)

## Theme & Adaptive UI

### Adaptive Components (Compose Cupertino)
- **Library:** `io.github.robinpcrd:cupertino` (RobinPcrd v3.x fork)
- **Used in:** Both `:composeApp` and `:designsystem` modules
- **Pattern:** Use `Adaptive*` and `Cupertino*` components for platform-native appearance
- **Behavior:** iOS renders Cupertino styling, Android renders Material3 automatically

## KAppMaker Template Reference

This project is based on KAppMaker, a Compose Multiplatform template. Use these resources when implementing features or checking for upstream updates.

| Resource | URL |
|----------|-----|
| KAppMaker Documentation | https://docs.kappmaker.com/ |
| Architecture Overview | https://docs.kappmaker.com/architecture/overview |
| Design System Components | https://docs.kappmaker.com/design-system |
| Upstream Repository | `git@github.com:KAppMaker/KAppMakerExtended.git` (configured as `upstream` remote) |

### Checking for Upstream Updates
```bash
git fetch upstream
git log HEAD..upstream/main --oneline  # See what's new
```

### Key Patterns from KAppMaker
- **UiStateHolder:** ViewModel pattern for screen state management
- **BackgroundExecutor:** Wrapper for background coroutine execution with Result handling
- **Design System:** Reusable components in `:designsystem` module - check docs before writing custom UI
- **ModifierExt:** Utility extensions like `fillWidthOfParent` for edge-to-edge layouts
- **ScreenWithToolbar:** Standard screen wrapper with toolbar, insets handling, and scroll support

## Design Principles

1. **KAppMaker Template:**
    - Built on the KAppMaker starter template
    - Modular subscription/auth via abstraction layers in `libs/`
    - Design system module for reusable UI components

2. **Local-First with API Layer:**
    - Room for structured local data (nonWeb targets)
    - Multiplatform Settings for preferences
    - Ktor for REST API calls
    - No cloud database sync

3. **Freemium Subscription Model:**
    - Free tier with ads (toggleable via feature flags)
    - Premium subscription via RevenueCat/Adapty
    - Credit system infrastructure exists (currently inactive)

4. **Multiplatform-First:**
    - UI shared via Compose Multiplatform
    - Platform-specific code for: Ads, Auth, Notifications, Billing, Feature Flags

5. **Domain Logic Separation:**
    - All health calculators live in `:shared-domain` module (pure Kotlin, no UI dependencies)
    - Fully unit-tested with `commonTest`

## Migration Context: VitalsCalc -> VitalsCalcNew

This project is a port of the original VitalsCalc app to the KAppMaker architecture. **All core health calculator features have been ported.** The port included migrating from Voyager navigation to Jetpack Navigation Compose.

| Aspect | Old (VitalsCalc) | New (VitalsCalcNew) |
|--------|-------------------|---------------------|
| Navigation | Voyager (`Screen`, `koinScreenModel()`) | Jetpack Navigation Compose (`@Serializable` routes) |
| ViewModels | Voyager ScreenModel | `UiStateHolder` extending `ViewModel` |
| DI Registration | `koinScreenModel()` | `viewModelOf(::UiStateHolder)` |
| Screen Pattern | `ScreenContainer` + `Screen` | `ScreenRoute` (with `Content()`) + `Screen` |
| Design System | Inline `Health*` components in `core/presentation/` | Separate `:designsystem` module |
| Domain Logic | Inline in ViewModels | Separate `:shared-domain` module with unit tests |
| Auth | None (local-only) | KMPAuth (Google + Firebase) |
| Billing | Direct platform billing (one-time purchase) | RevenueCat/Adapty (subscription model) |
| Storage | Multiplatform Settings only | Room + Multiplatform Settings |
| Ads | Lexilabs Basic Ads | Direct AdMob via KAppMaker pattern |
| Monetization | One-time "Remove Ads" purchase | Subscription tiers + credit system |

### Ported Features (Complete)
- BMI Calculator (with category visualization and dynamic chart)
- BMR/TDEE Calculator (calorie guidance with activity levels)
- Blood Pressure Tracker (AHA categories)
- Body Fat Calculator (multiple methods: Navy, BMI-derived, skinfold)
- Heart Rate Zones (5-zone model)
- Ideal Weight Calculator (multi-formula: Devine, Robinson, Miller, Hamwi)
- Water Intake Tracker
- Calculation History with filtering
- Home Dashboard (overview of all latest metrics)
- User Profile (age, gender, height, weight, unit system)
- Settings, About, References screens
- Calculator grid entry screen

### Sister Projects (Same Architecture)
- **ListHarbor** (`/Volumes/Crucial/Dev/cohesionbrew/ListHarbor/`) — Shopping list app, same KAppMaker base
- **CafeConnections** (`/Volumes/Crucial/Dev/cohesionbrew/CafeConnections/`) — Cafe discovery app, same KAppMaker base

These can be referenced for KAppMaker patterns when implementing features.

## Build Commands

| Task | Command |
|------|---------|
| Android Debug | `./gradlew assembleDebug` |
| Android Release | `./gradlew assembleRelease` |
| iOS Framework (Simulator) | `./gradlew linkDebugFrameworkIosSimulatorArm64` |
| iOS Framework (Device) | `./gradlew linkDebugFrameworkIosArm64` |
| iOS Full Build | `xcodebuild -workspace iosApp/iosApp.xcworkspace -scheme iosApp -configuration Debug -destination 'platform=iOS Simulator,name=iPhone 17 Pro' build` |
| JVM Tests | `./gradlew :composeApp:jvmTest` |
| Domain Tests | `./gradlew :shared-domain:jvmTest` |
| All Tests | `./gradlew allTests` |
| Clean | `./gradlew clean` |

For detailed architectural and coding expectations, see `.ai/rules/CODING_STANDARDS.md`.
