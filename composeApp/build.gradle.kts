import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import java.util.Properties

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

//Custom scripts
apply(from = rootProject.file("gradle/scripts/refactorPackage.gradle.kts"))
apply(from = rootProject.file("gradle/scripts/generateNewScreen.gradle.kts"))


kotlin {
    task("testClasses")
    jvmToolchain(17)
    androidTarget {
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            export(libs.kmpnotifier)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.koin.core)
            api(libs.kmpnotifier)
            implementation(libs.kmpauth.google)
            implementation(libs.kmpauth.firebase)
            implementation(libs.kmpauth.uihelper)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.napier)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.compose)
            implementation(libs.lifecyle.runtime)
            implementation(libs.revenuecat.core)
            implementation(libs.revenuecat.ui)
            implementation(libs.uuid)
            implementation(libs.multiplatformSettings.noargs)
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.messaging)
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.crashlytics)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

    }
}

compose.resources {
    packageOfResClass = "com.measify.kappmaker.generated.resources"
}

android {
    namespace = "com.measify.kappmaker"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

        applicationId = "com.measify.kappmaker"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val keystorePropertiesFile = rootProject.file("distribution/android/keystore/keystore.properties")
    val isSigningKeyExists = keystorePropertiesFile.exists()
    val keystoreProperties = Properties()
    if (isSigningKeyExists) keystorePropertiesFile.inputStream().use { keystoreProperties.load(it) }

    signingConfigs {
        if (isSigningKeyExists)
            create("release") {
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["keystorePassword"] as String?
                keyAlias = keystoreProperties["keyAlias"] as String?
                keyPassword = keystoreProperties["keyPassword"] as String?
            }
    }

    buildTypes {
        val debug by getting {
            isMinifyEnabled = false
            isDebuggable = true
        }

        val release by getting {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName(if (isSigningKeyExists) "release" else "debug")
        }
    }
    buildFeatures {
        buildConfig = true
    }

}

//https://developer.android.com/develop/ui/compose/testing#setup
dependencies {
    androidTestImplementation(libs.androidx.uitest.junit4)
    debugImplementation(libs.androidx.uitest.testManifest)
    //temporary fix: https://youtrack.jetbrains.com/issue/CMP-5864
    androidTestImplementation("androidx.test:monitor") {
        version { strictly("1.6.1") }
    }

    add("kspAndroid",libs.room.compiler)
    add("kspIosX64",libs.room.compiler)
    add("kspIosArm64",libs.room.compiler)
    add("kspIosSimulatorArm64",libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

buildConfig {
    // BuildConfig configuration here.
    // https://github.com/gmazzo/gradle-buildconfig-plugin#usage-in-kts
    packageName("com.measify.kappmaker.common")
    buildConfigField("GOOGLE_WEB_CLIENT_ID", getRequiredProperty("GOOGLE_WEB_CLIENT_ID"))
    buildConfigField("REVENUECAT_ANDROID_API_KEY", getRequiredProperty("REVENUECAT_ANDROID_API_KEY"))
    buildConfigField("REVENUECAT_IOS_API_KEY", getRequiredProperty("REVENUECAT_IOS_API_KEY"))
}

fun getRequiredProperty(
    key: String,
    errorMessage: String = "Make sure you added `$key` in local.properties"
): String {
    val propertyValue: String? = gradleLocalProperties(rootDir, providers).getProperty(key)
    require(!propertyValue.isNullOrEmpty()) { errorMessage }
    return propertyValue
}
