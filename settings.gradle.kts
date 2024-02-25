rootProject.name = "PyTek"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.quiltmc.org/repository/release/") { name = "Quilt" }
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
