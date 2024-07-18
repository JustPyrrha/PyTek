import java.util.Base64
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.ChangelogPluginConstants.SEM_VER_REGEX
import org.jetbrains.changelog.date
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.loom)

    alias(libs.plugins.dokka)
    alias(libs.plugins.changelog)
    alias(libs.plugins.minotaur)
    alias(libs.plugins.spotless)

    signing
    `maven-publish`
}

buildscript {
    dependencies {
        classpath(libs.dokka.pluginBase)
        classpath(libs.dokka.pluginVersioning)
    }
}

group = "gay.pyrrha"

version = "1.0.0-alpha.0+${libs.versions.minecraft.get()}"

repositories { mavenCentral() }

dependencies {
    minecraft(libs.minecraft)
    mappings(variantOf(libs.fabric.mappings) { classifier("v2") })
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.kotlin)

    include(libs.logging)
    implementation(libs.logging)

    testImplementation(kotlin("test"))
}

tasks {
    test { useJUnitPlatform() }

    signing {
        sign(remapJar.get())
        isRequired =
            hasProperty("signingKey") &&
                hasProperty("signingKeyId") &&
                hasProperty("signingPassword")

        if (isRequired) {
            useInMemoryPgpKeys(
                (property("signingKey") as String).base64Decode(),
                (property("signingKeyId") as String).base64Decode(),
                (property("signingPassword") as String).base64Decode(),
            )
        }
    }

    dokkaHtml {
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            customAssets = listOf(file("artSrc/logo-icon.svg"))
            footerMessage = "(c) 2024 Pyrrha Wills"
        }

        val docsVersionDir = projectDir.resolve("docs/version")
        val docsVersion = project.version.toString()
        val currentDocsDir = docsVersionDir.resolve(docsVersion)
        outputDirectory = currentDocsDir

        pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
            version = docsVersion
            olderVersionsDir = docsVersionDir
        }

        doLast {
            currentDocsDir.copyRecursively(file("docs-publishing/"), overwrite = true)
            currentDocsDir.resolve("older").deleteRecursively()
        }
    }

    processResources {
        inputs.property("version", project.version.toString())
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version.toString()))
        }
    }

    jar { from("LICENSE") { rename { "${it}_${project.base.archivesName.get()}" } } }
}

changelog {
    version = project.version.toString()
    path = rootProject.file("CHANGELOG.md").canonicalPath
    header = provider { "[${project.version}] - ${date()}" }
    headerParserRegex = SEM_VER_REGEX
    itemPrefix = "-"
    keepUnreleasedSection = true
    unreleasedTerm = "[Unreleased]"
    groups = listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security")
    lineSeparator = "\n"
    combinePreReleases = true
}

val enableEnhancedClassRedefinition: Boolean =
    if (System.getProperty("java.vendor") == "JetBrains s.r.o.") {
        println("JetBrains Runtime found, enabling Enhanced Class Redefinition (DCEVM)")
        true
    } else {
        false
    }

fabricApi { configureDataGeneration() }

loom {
    splitEnvironmentSourceSets()
    runs {
        configureEach {
            if (enableEnhancedClassRedefinition) {
                vmArgs("-XX:+AllowEnhancedClassRedefinition")
            }
            runDir("runs/$name")
        }
    }

    mods {
        val pytek by creating {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }
}

spotless {
    kotlin {
        ktfmt().kotlinlangStyle()
        licenseHeaderFile("$projectDir/HEADER")
    }
    kotlinGradle { ktfmt().kotlinlangStyle() }
    java {
        googleJavaFormat()
        licenseHeaderFile("$projectDir/HEADER")
    }
}

kotlin {
    jvmToolchain(21)
    explicitApi()
    sourceSets {
        val client by getting { explicitApi() }
    }
}

java {
    withSourcesJar()
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    debugMode = System.getenv("MODRINTH_DEBUG") == "1"
    projectId = "pytek"
    versionNumber = project.version.toString()
    versionType =
        with(project.version.toString()) {
            when {
                contains("alpha") -> "alpha"
                contains("beta") -> "beta"
                else -> "release"
            }
        }
    uploadFile.set(tasks.remapJar)
    additionalFiles.set(listOf(tasks.remapSourcesJar))
    gameVersions = listOf(libs.versions.minecraft.get())
    dependencies {
        required.project("fabric-api")
        required.project("fabric-language-kotlin")
    }
    changelog =
        rootProject.changelog.renderItem(
            rootProject.changelog.getUnreleased().withEmptySections(false).withHeader(false),
            Changelog.OutputType.MARKDOWN,
        )
    syncBodyFrom = rootProject.file("README.md").readText()

    if (
        hasProperty("signingKey") && hasProperty("signingKeyId") && hasProperty("signingPassword")
    ) {
        additionalFiles.add(tasks.getByName("signRemapJar"))
    }
}

fun String.base64Decode(): String = Base64.getDecoder().decode(this).decodeToString()
