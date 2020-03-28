import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * This file contains all required gradle dependencies for UI and Unit tests
 */
object TestLibraries {
    private object Versions {
        const val junit4 = "4.12"
        const val testRunner = "1.1.0"
        const val androidJunit = "1.1.1"
        const val espresso = "3.2.0"
    }

    const val junit4 = "junit:junit:${Versions.junit4}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val androidJunit = "androidx.test.ext:junit:${Versions.androidJunit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}

fun DependencyHandlerScope.testDependencies() {
    "testImplementation"(TestLibraries.junit4)
}

fun DependencyHandlerScope.androidTestDependencies() {
    "androidTestImplementation"(TestLibraries.androidJunit)
    "androidTestImplementation"(TestLibraries.espresso)
}
