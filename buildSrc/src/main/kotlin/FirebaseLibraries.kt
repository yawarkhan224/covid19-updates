import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * This file contains all required gradle Firebase related dependencies for this project
 */
object FirebaseLibraries {
    object Versions {
        const val analytics = "17.2.2"
        const val fcm = "20.1.5"
        const val remoteConfig = "19.1.3"
    }

    const val analytics = "com.google.firebase:firebase-analytics:${Versions.analytics}"
    const val fcm = "com.google.firebase:firebase-messaging:${Versions.fcm}"
    const val remoteConfig = "com.google.firebase:firebase-config:${Versions.remoteConfig}"
}

fun DependencyHandlerScope.firebaseDependencies() {
    "implementation"(FirebaseLibraries.analytics)
    "implementation"(FirebaseLibraries.fcm)
    "implementation"(FirebaseLibraries.remoteConfig)
}
