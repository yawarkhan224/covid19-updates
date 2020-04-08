import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * This file contains all Persistence required gradle dependencies for this project
 */
object PersistenceLibraries {
    object Versions {
        // room
        const val room = "2.2.5"
    }

    // room
    const val roomRuntime =
        "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler =
        "androidx.room:room-compiler:${Versions.room}"

    const val roomkKtx =
        "androidx.room:room-ktx:${Versions.room}"
}

fun DependencyHandlerScope.persistenceDependencies() {
    // room
    "implementation"(PersistenceLibraries.roomRuntime)
    "kapt"(PersistenceLibraries.roomCompiler)
    "implementation"(PersistenceLibraries.roomkKtx)
}
