# Project Knowledge: VitalsCalcNew

## Project Overview
**Goal:** A comprehensive health metrics calculator app (rebuild of VitalsCalc on the KAppMaker template) providing BMI, BMR/TDEE, Heart Rate Zone, and other health calculations with a freemium subscription model, authentication, and credit-based AI features. VitalsCalc is currently **live in production** on both the Google Play Store and Apple App Store.

## Technical Configuration
- **Type:** Compose Multiplatform (KAppMaker template)
- **Build System:** Gradle Kotlin DSL (`.kts`) with Version Catalog (`libs.versions.toml`) and `build-logic` included build
- **Language:** Kotlin
- **Min SDK:** 24 (Android 7.0)
- **Target/Compile SDK:** 36 (Android 16)
- **iOS Deployment Target:** Configured via Xcode (CocoaPods)
- **iOS Targets:** `iosArm64` (devices), `iosSimulatorArm64` (Apple Silicon simulators)
- **JVM Target:** 17
- **Additional Targets:** JS (browser), WasmJS (experimental), JVM (desktop)

## Identifiers
- **Namespace:** `com.measify.kappmaker`
- **Application ID:** `com.measify.kappmaker`
- **iOS Framework:** `ComposeApp` (Static)
- **Root Project Name:** `KAppMakerAllModules`
- **Compose Resources Package:** `com.measify.kappmaker.generated.resources`

## Module Structure
- **`:composeApp`**: Main application logic (Mobile, Desktop, Web).
    - Shared Logic: `composeApp/src/commonMain/kotlin/com/measify/kappmaker/...`
    - Android Specifics: `composeApp/src/androidMain/kotlin/com/measify/kappmaker/...`
    - iOS Specifics: `composeApp/src/iosMain/kotlin/com/measify/kappmaker/...`
    - Desktop: `composeApp/src/jvmMain/kotlin/...`
    - Non-Web (Room DB): `composeApp/src/nonWebMain/...`
    - Mobile-only: `composeApp/src/mobileMain/...`
- **`:designsystem`**: Reusable UI components and theme.
    - Path: `designsystem/src/commonMain/kotlin/com/measify/kappmaker/designsystem/...`
    - Components: buttons, cards, chips, dialogs, bottom nav, premium UI, onboarding pagers
    - Theme: colors, typography, spacing
- **`:libs:auth:auth-api`**: Auth abstraction layer (AuthServiceProvider interface)
- **`:libs:auth:auth-firebase`**: Firebase Auth implementation (KMPAuth)
- **`:libs:subscription:subscription-api`**: Subscription abstraction layer (SubscriptionProvider interface)
- **`:libs:subscription:subscription-revenuecat`**: RevenueCat implementation (default)
- **`:libs:subscription:subscription-adapty`**: Adapty implementation (alternative)
- **`iosApp/`**: Native iOS entry point (Xcode project with CocoaPods).

## Tech Stack & Libraries
- **Architecture:** MVI-style with UiStateHolder (ViewModel) + UiState pattern
- **UI:** Jetpack Compose Multiplatform (Material3)
- **Navigation:** Jetpack Navigation Compose (type-safe routes via `@Serializable` route classes)
- **DI:** Koin (4.2.x) with `startKoin` in `AppInitializer`
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
- **AI APIs:** OpenAI (chat/image generation), Replicate (predictions) — credit-gated
- **Settings:** Multiplatform Settings (key-value preferences)

## What This Project Does NOT Have
> Important: AI agents should NOT assume these exist or try to use them.
- **No Cloud Firestore** — No cloud database sync (local Room + API only)
- **No Voyager** — Uses Jetpack Navigation Compose (not Voyager)
- **No Social Features** — No user profiles, sharing, or community features
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
└── nonWeb (Android + iOS + JVM) — has Room DB
    ├── android
    ├── ios
    └── jvm
```

### Presentation Structure
Each screen follows UiStateHolder + UiState pattern in `presentation/screens/`:
```
presentation/screens/
├── account/           # User account management
├── creditbalance/     # Credit balance & transactions
├── favorite/          # Favorited items
├── helpandsupport/    # Help & support
├── home/              # Main dashboard
├── main/              # Main scaffold with bottom nav + nested NavHost
├── onboarding/        # Onboarding flow (2 variations)
├── paywall/           # Paywall (custom + remote via RevenueCat/Adapty)
├── profile/           # User profile
├── signin/            # Authentication
└── subscriptions/     # Subscription management

Each screen folder contains:
├── FeatureScreen.kt         # Composable UI
├── FeatureScreenRoute.kt    # @Serializable navigation route + Content()
├── FeatureUiState.kt        # UI state data class
└── FeatureUiStateHolder.kt  # ViewModel (extends ViewModel)
```

### Navigation
- **Two-level NavHost:**
    - `AppNavigation` (root): Onboarding → Main → Paywall
    - `MainNavigation` (nested inside MainScreen): Home, Profile, SignIn, Account, Favorite, Paywall, Help, Subscriptions, CreditBalance
- **Route pattern:** `@Serializable` data classes implementing a `Content()` method
- **Local navigator:** `LocalNavigator` CompositionLocal provides root NavHostController

### Data Layer
```
data/
├── repository/            # Repositories (User, Subscription, Credit)
├── source/
│   ├── featureflag/       # Feature flag manager (Firebase Remote Config)
│   ├── local/             # Room-based local data sources
│   ├── preferences/       # Multiplatform Settings wrapper
│   └── remote/
│       ├── apiservices/   # API service classes (REST, OpenAI, Replicate)
│       ├── request/       # Request DTOs
│       └── response/      # Response DTOs
```

### DI Setup
- Koin modules defined in `AppInitializer.kt` (not a separate `di/` folder)
- Three modules: `domainModule`, `dataModule`, `presentationModule`
- Platform module via `platformModule` (expect/actual)
- ViewModels registered via `viewModelOf(::UiStateHolderClass)`

### Credit System
- Credit-based access to AI features (OpenAI, Replicate)
- `CreditRepository` manages credit balance, transactions, and renewable credits
- Configurable via DSL in `AppInitializer.initializeCreditSystem()`
- Supports one-time bonuses, daily/weekly recurring credits

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

## Testing
- **Test command:** `./gradlew :composeApp:jvmTest` — runs all `commonTest` tests on host JVM
- **Test location:** `composeApp/src/commonTest/kotlin/...`
- **JVM target purpose:** Enables running `commonTest` without iOS simulator (fast, no Xcode dependency)
- **Source set hierarchy:** `commonMain` → `mobileMain` → `androidMain`/`iosMain`; `commonMain` → `jvmMain`

## Design Principles

1. **KAppMaker Template:**
    - Built on the KAppMaker starter template
    - Modular subscription/auth via abstraction layers in `libs/`
    - Design system module for reusable UI components

2. **Local-First with API Layer:**
    - Room for structured local data (nonWeb targets)
    - Multiplatform Settings for preferences
    - Ktor for REST API calls (AI services)
    - No cloud database sync

3. **Freemium Subscription Model:**
    - Free tier with ads (toggleable via feature flags)
    - Premium subscription via RevenueCat/Adapty
    - Credit system for AI-powered features

4. **Multiplatform-First:**
    - UI shared via Compose Multiplatform
    - Platform-specific code for: Ads, Auth, Notifications, Billing, Feature Flags

## Migration Context: VitalsCalc → VitalsCalcNew

This project is a port of the original VitalsCalc app (currently live on Google Play Store and Apple App Store) to the KAppMaker architecture. The port includes migrating from Voyager navigation to Jetpack Navigation Compose. Key differences from the old codebase:

| Aspect | Old (VitalsCalc) | New (VitalsCalcNew) |
|--------|-------------------|---------------------|
| Navigation | Voyager (`Screen`, `koinScreenModel()`) | Jetpack Navigation Compose (`@Serializable` routes) |
| ViewModels | Voyager ScreenModel | `UiStateHolder` extending `ViewModel` |
| DI Registration | `koinScreenModel()` | `viewModelOf(::UiStateHolder)` |
| Screen Pattern | `ScreenContainer` + `Screen` | `ScreenRoute` (with `Content()`) + `Screen` |
| Design System | Inline `Health*` components in `core/presentation/` | Separate `:designsystem` module |
| Auth | None (local-only) | KMPAuth (Google + Firebase) |
| Billing | Direct platform billing (one-time purchase) | RevenueCat/Adapty (subscription model) |
| Storage | Multiplatform Settings only | Room + Multiplatform Settings |
| Ads | Lexilabs Basic Ads | Direct AdMob via KAppMaker pattern |
| Monetization | One-time "Remove Ads" purchase | Subscription tiers + credit system |

### Features to Port
The following health calculator features from VitalsCalc need to be ported:
- BMI Calculator (with category visualization)
- BMR/TDEE Calculator (calorie guidance)
- Blood Pressure Tracker (AHA categories)
- Body Fat Calculator
- Heart Rate Zones
- Ideal Weight Calculator (multi-formula)
- Water Intake Tracker
- History/Trends
- Home Dashboard (overview of all metrics)
- Profile (age, gender, height, weight)
- Settings (preferences, about, references)

### Sister Projects (Same Architecture)
- **ListHarbor** (`/Volumes/Crucial/Dev/cohesionbrew/ListHarbor/`) — Shopping list app, same KAppMaker base
- **CafeConnections** (`/Volumes/Crucial/Dev/cohesionbrew/CafeConnections/`) — Cafe discovery app, same KAppMaker base

These can be referenced for KAppMaker patterns when implementing features.

## Build Commands

| Task | Command |
|------|---------|
| Android Debug | `./gradlew :composeApp:assembleDebug` |
| Android Release | `./gradlew :composeApp:assembleRelease` |
| iOS Framework (Simulator) | `./gradlew linkDebugFrameworkIosSimulatorArm64` |
| iOS Framework (Device) | `./gradlew linkDebugFrameworkIosArm64` |
| JVM Tests | `./gradlew :composeApp:jvmTest` |
| All Tests | `./gradlew allTests` |
| Clean | `./gradlew clean` |

For detailed architectural and coding expectations, see `.ai/rules/CODING_STANDARDS.md`.
