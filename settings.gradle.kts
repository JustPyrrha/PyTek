rootProject.name = "PyTek"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        val libs by creating { from(files("libs.versions.toml")) }
    }
}
