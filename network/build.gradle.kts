plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(ConfigValues.compile)

    defaultConfig {
        minSdkVersion(ConfigValues.min)
        targetSdkVersion(ConfigValues.target)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        maybeCreate("release").apply {
            consumerProguardFile("proguard-rules.pro")
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
    commonDependencies()

    crossModuleCommonDependencies()

    networkDependencies()

    testDependencies()

    androidTestDependencies()

    persistenceDependencies()

    ktlint(CodeQualityLibraries.ktlint)
}

tasks.register<JavaExec>("ktlint") {
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

tasks.register<JavaExec>("ktlintFormat") {
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
