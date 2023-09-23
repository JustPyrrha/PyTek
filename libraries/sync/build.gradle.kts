plugins {
    id("mod.quilt.library")
    id("mod.lint")
    id("mod.testing")
}

group = "gay.pyrrha"

dependencies {
    implementation(libs.ksp.symbolProcessingApi)

    testImplementation(libs.kotlin.compileTestingKsp)
}
