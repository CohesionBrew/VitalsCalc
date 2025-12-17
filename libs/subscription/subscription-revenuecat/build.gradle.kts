plugins {
    id("configure-kmp-library-module")
}

android {
    namespace = "com.measify.kappmaker.subscription.revenuecat"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            api(projects.libs.subscription.subscriptionApi)
        }
        mobileMain.dependencies {
            api(libs.revenuecat.core)
            api(libs.revenuecat.ui)
        }
    }
}
