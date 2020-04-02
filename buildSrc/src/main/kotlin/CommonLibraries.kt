import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * This file contains all common required gradle dependencies for this project
 */
object CommonLibraries {
    object Versions {
        // gson
        const val gson = "2.8.6"

        // couroutines
        const val couroutines = "1.3.5"
    }

    // gson
    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    // couroutines
    const val couroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.couroutines}"
    const val couroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.couroutines}"
}

fun DependencyHandlerScope.crossModuleCommonDependencies() {
    // gson
    "implementation"(CommonLibraries.gson)

    // coroutines
    "implementation"(CommonLibraries.couroutinesCore)
    "implementation"(CommonLibraries.couroutinesAndroid)
}
