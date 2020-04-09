plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    compileSdkVersion(ConfigValues.compile)

    defaultConfig {
        applicationId = "com.aryk.covid"
        minSdkVersion(ConfigValues.min)
        targetSdkVersion(ConfigValues.target)
        versionCode = 1
        versionName = "1.0"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        maybeCreate("release").apply {
            isMinifyEnabled = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }

        maybeCreate("debug").apply {
            applicationIdSuffix = ".debug"
        }
    }

// To inline the bytecode built with JVM target 1.8 into
// bytecode that is being built with JVM target 1.6. (e.g. navArgs)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = ConfigValues.jvmTarget
    }
}

val ktlint by configurations.creating

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    networkDependencies()

    crossModuleCommonDependencies()

    commonDependencies()

    testDependencies()

    androidTestDependencies()

    graphicsDependencies()

    persistenceDependencies()

    firebaseDependencies()

    implementation(project(":network"))

    ktlint(CodeQualityLibraries.ktlint)

    debugImplementation(CodeQualityLibraries.leakCanary)
}

tasks.register < JavaExec > ("ktlint") {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args(
            "--reporter=plain",
            "--reporter=checkstyle,output=$buildDir/ktlint/checkstyle-result.xml",
            "--android"
    )
}

tasks.named("check") {
    dependsOn(ktlint)
}

tasks.register < JavaExec > ("ktlintFormat") {
    group = "formatting"
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args(
            "--reporter=plain",
            "--reporter=checkstyle,output=$buildDir/ktlint/checkstyle-result.xml",
            "--android"
    )
}
