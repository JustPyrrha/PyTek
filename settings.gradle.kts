rootProject.name = "PyTek"

pluginManagement {
    includeBuild("buildLogic")
    repositories {
        maven("https://maven.neoforged.net/releases/") { name = "NeoForge" }
        maven("https://maven.quiltmc.org/repository/release/") { name = "Quilt" }
        maven("https://maven.fabricmc.net/") { name = "Fabric" }

        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        val libs by creating {
            from(files("libs.versions.toml"))
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    "Common",
    "NeoForge"
)
