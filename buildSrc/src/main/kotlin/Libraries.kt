import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * This file contains all default required gradle dependencies for this project
 */
object Libraries {
    private const val kotlinVersion = "1.3.70"

    object Versions {
        const val jetpack = "1.1.0"
        const val ktx = "1.2.0"
        const val constraintLayout = "1.1.3"
        const val materialDesign = "1.1.0"
        const val navigation = "2.2.1"
        const val lifecycle = "2.2.0"

        // Dependency Injection
        const val koin = "2.1.5"
    }

    const val appCompat = "androidx.appcompat:appcompat:${Versions.jetpack}"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val coreKtx = "androidx.core:core-ktx:${Versions.ktx}"

    const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val anNavUi = "androidx.navigation:navigation-ui:${Versions.navigation}"
    const val anNavFragment = "androidx.navigation:navigation-fragment:${Versions.navigation}"

    // FOo ViewModel
    const val anLifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val anLifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"


    const val anNavFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val anNavUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"

    // Dependecy Injection
    const val koinCore = "org.koin:koin-core:${Versions.koin}"

    // Koin AndroidX Scope features
    const val koinScope = "org.koin:koin-androidx-scope:${Versions.koin}"

    // Koin AndroidX ViewModel features
    const val koinViewmodle = "org.koin:koin-androidx-viewmodel:${Versions.koin}"

    // Koin AndroidX Fragment features
    const val koinFragment = "org.koin:koin-androidx-fragment:${Versions.koin}"
}

fun DependencyHandlerScope.commonDependencies() {
    "implementation"(Libraries.kotlinStdLib)
    "implementation"(Libraries.appCompat)
    "implementation"(Libraries.coreKtx)

    "implementation"(Libraries.materialDesign)
    "implementation"(Libraries.constraintLayout)

    "implementation"(Libraries.anNavFragment)
    "implementation"(Libraries.anNavUi)
    "implementation"(Libraries.anNavFragmentKtx)
    "implementation"(Libraries.anNavUiKtx)

    // For ViewModel
    "implementation"(Libraries.anLifecycle)
    "implementation"(Libraries.anLifecycleViewModel)

    // Dependency Injection
    "implementation"(Libraries.koinCore)
    "implementation"(Libraries.koinScope)
    "implementation"(Libraries.koinFragment)
    "implementation"(Libraries.koinViewmodle)
}
