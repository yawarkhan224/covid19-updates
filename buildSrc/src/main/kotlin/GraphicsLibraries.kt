import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * This file contains all common required gradle Graph Creation dependencies for this project
 */
object GraphicsLibraries {
    object Versions {
        // mpAndroidChart
        const val mpAndroidChart = "3.1.0"

        // circleimageview
        const val circleimageview = "3.1.0"
        const val circularimageview = "4.2.0"
    }

    // mpAndroidChart
    const val mpAndroidChart = "com.github.PhilJay:MPAndroidChart:${Versions.mpAndroidChart}"

    // circleimageview
    const val circleimageview = "de.hdodenhof:circleimageview:${Versions.circleimageview}"
    const val circularimageview = "com.mikhaellopez:circularimageview:${Versions.circleimageview}"
}

fun DependencyHandlerScope.graphicsDependencies() {
    // mpAndroidChart
    "implementation"(GraphicsLibraries.mpAndroidChart)

    // circleimageview
    "implementation"(GraphicsLibraries.circleimageview)
    "implementation"(GraphicsLibraries.circularimageview)
}
