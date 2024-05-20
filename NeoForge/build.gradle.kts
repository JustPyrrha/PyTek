import gay.pyrrha.build.*
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.ChangelogPluginExtension

plugins {
    alias(libs.plugins.changelog)
    alias(libs.plugins.dokka)
    alias(libs.plugins.ml.runtime)
    alias(libs.plugins.neoforge.userdev)
    alias(libs.plugins.minotaur)

    id("gay.pyrrha.build.kotlin-forge-runtime-library")
}

base.archivesName.set("PyTek-NeoForge")

val includeLibrary: Configuration by configurations.creating {
    exclude(group = "org.jetbrains", module = "annotations")
}

runs {
    configureEach {
        // SCAN, REGISTRIES, REGISTRYDUMP
        systemProperty("forge.logging.markers", "SCAN")
        systemProperties("forge.logging.console.level", "debug")

        modSource(sourceSets.main.get())

        dependencies {
            runtime(includeLibrary)
        }
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

    implementation(libs.logging)
    includeLibrary(libs.logging)
    kotlinForgeRuntimeLibrary(libs.logging)
}

kotlin {
    explicitApi()
}

tasks {
    jarJar.configure {
        archiveClassifier = ""

        from(provider { includeLibrary.map(::zipTree).toTypedArray() })
        manifest {
            attributes(
                "Automatic-Module-Name" to "gay.pyrrha.pytek",
                "FMLModType" to "LIBRARY"
            )
        }
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
    syncBodyFrom = rootProject.file("README.md").absolutePath
}
