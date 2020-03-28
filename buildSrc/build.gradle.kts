plugins {
    // note the backtick syntax (since `kotlin-dsl` is
    // an extension property on the plugin's scope object)
    `kotlin-dsl`
}

repositories {
    google()
    jcenter() // this is needed to download dependencies for kotlin-dsl
}
