plugins {
    alias(libs.plugins.dokka)
    alias(libs.plugins.ml.common)
}

dependencies {
    implementation(libs.logging)
}

kotlin {
    explicitApi()
}
