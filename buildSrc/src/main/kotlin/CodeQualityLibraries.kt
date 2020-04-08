/**
 * This file contains all required gradle dependencies for Static Code Analysis
 */
object CodeQualityLibraries {
    object Versions {
        const val ktlint = "0.36.0"
        const val detekt = "1.7.0"
        const val leakCanary = "2.2"
    }

    const val ktlint = "com.pinterest:ktlint:${Versions.ktlint}"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
}
