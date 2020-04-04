import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${ConfigValues.androidGradleVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${ConfigValues.kotlinGradleVersion}")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${CodeQualityLibraries.Versions.detekt}")

        classpath("org.koin:koin-gradle-plugin:${Libraries.Versions.koin}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version(CodeQualityLibraries.Versions.detekt)
}


allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
    }
}

task<Detekt>("detektAll") {
    description = "Runs detekt on entire "
    buildUponDefaultConfig = true // preconfigure defaults
    source(files(projectDir))
    debug = true
    reports {
        xml {
            destination = file("build/reports/detektAll.xml")
        }
        html.destination = file("build/reports/detektAll.html")
    }
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
}

tasks {
    withType<Detekt> {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        this.jvmTarget = ConfigValues.jvmTarget
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(rootProject.buildDir)
    }
}
