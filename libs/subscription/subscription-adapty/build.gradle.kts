plugins {
    id("configure-kmp-library-module")
}

android {
    namespace = "com.measify.kappmaker.subscription.adapty"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.libs.subscription.subscriptionApi)
            implementation(libs.kotlinx.coroutines.core)
        }
        mobileMain.dependencies {
            api(libs.adapty)
            api(libs.adapty.ui)
        }
    }
}
