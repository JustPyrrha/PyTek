plugins {
    id("mod.quilt.library")
    id("mod.lint")
}

group = "gay.pyrrha"

dependencies {
    modImplementation(libs.quilted.fabricDataGenerationApiV1)
}

kotlin {
    explicitApi()
}
