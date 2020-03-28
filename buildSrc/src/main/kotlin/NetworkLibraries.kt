import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * This file contains all required gradle dependencies for Network Layer
 */
object NetworkLibraries {
    private object Versions {
        const val okHttp = "4.4.1"
        const val retrofit = "2.8.1"
    }

    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
}

fun DependencyHandlerScope.networkDependencies() {
    "implementation"(NetworkLibraries.okHttp)
    "implementation"(NetworkLibraries.retrofit)
}
