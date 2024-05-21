import gay.pyrrha.build.*
import org.jetbrains.changelog.Changelog

plugins {
    alias(libs.plugins.changelog)
    alias(libs.plugins.dokka)
    alias(libs.plugins.ml.runtime)
    alias(libs.plugins.neoforge.userdev)
    alias(libs.plugins.minotaur)

    id("gay.pyrrha.build.kotlin-forge-runtime-library")
}

base.archivesName.set("PyTek-NeoForge")

runs {
    configureEach {
        // SCAN, REGISTRIES, REGISTRYDUMP
        systemProperty("forge.logging.markers", "SCAN")
        systemProperties("forge.logging.console.level", "debug")

        modSource(sourceSets.main.get())
    }
}

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge/") {
        name = "KotlinForForge"
        content { includeGroup("thedarkcolour") }
    }
}

jarJar.enable()
dependencies {
    implementation(libs.neoforge)
    implementation(libs.neoforge.kotlin)

    includeApi(libs.logging)
}

fun DependencyHandlerScope.includeApi(dependency: Provider<*>) {
    api(dependency)
    jarJar(dependency) { isTransitive = false }
    kotlinForgeRuntimeLibrary(dependency)
}

kotlin {
    explicitApi()
}

tasks {
    jarJar.configure {
        archiveClassifier = ""
    }

    jar {
        archiveClassifier = "slim"
    }

    assemble {
        dependsOn(jarJar)
    }
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    debugMode = System.getenv("MODRINTH_DEBUG") == "1"
    projectId = "pytek"
    versionNumber = project.version.toString()
    versionType = with(project.version.toString()) {
        when {
            contains("alpha") -> "alpha"
            contains("beta") -> "beta"
            else -> "release"
        }
    }
    uploadFile.set(tasks.jarJar)
    additionalFiles.set(listOf(tasks.sourcesJar))
    gameVersions = listOf(libs.versions.minecraft.get())
    dependencies {
        required.project("kotlin-for-forge")
    }
    changelog = rootProject.changelog.renderItem(
        rootProject.changelog
            .getUnreleased()
            .withEmptySections(false)
            .withHeader(false),
        Changelog.OutputType.MARKDOWN
    )
    syncBodyFrom = rootProject.file("README.md").readText()
}
