plugins {
    id("mod.quilt")
    id("mod.lint")
}

dependencies {
    modImplementation(libs.quilt.kotlinLibraries)
    modImplementation(libs.quilted.fabricApi)
}
