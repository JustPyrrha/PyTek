plugins {
    id("mod.quilt.library")
    id("mod.lint")
}

group = "gay.pyrrha"

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.13")
}
