pluginManagement {
    includeBuild("buildLogic")
    repositories {
        maven("https://maven.quiltmc.org/repository/release") { name = "Quilt" }
        maven("https://maven.fabricmc.net") { name = "Fabric" }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "DemonTech"

include(
    ":libraries:datagen",
    ":mod"
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
