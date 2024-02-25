import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.ChangelogPluginConstants.SEM_VER_REGEX
import org.jetbrains.changelog.date
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.blossom)
    alias(libs.plugins.changelog)
    alias(libs.plugins.dokka)
    idea
    alias(libs.plugins.ideaExt)
    alias(libs.plugins.githooks)
    alias(libs.plugins.grgit)
    alias(libs.plugins.licenser)
    alias(libs.plugins.loom)
    alias(libs.plugins.minotaur)
    signing
}

buildscript {
    dependencies {
        classpath(libs.dokka.pluginBase)
        classpath(libs.dokka.pluginVersioning)
    }
}

group = "gay.pyrrha"
version = "0.1.0"
base.archivesName.set("PyTek-Quilt-${libs.versions.minecraft.get()}")

repositories {
    maven("https://maven.wanderia.dev/releases") { name = "Wanderia" }
    mavenCentral()
}

dependencies {
    // game and loader
    minecraft(libs.minecraft)
    mappings(variantOf(libs.quilt.mappings) { classifier("intermediary-v2") })
    modImplementation(libs.quilt.loader)

    // core libraries
    modImplementation(libs.quilt.stdLib.base)
    modImplementation(libs.quilt.kotlin)
    modImplementation(libs.logging)

    // jar-in-jar includes
    include(libs.logging)

    // testing dependencies
    testImplementation(libs.kotlin.test)

    // dokka dependencies
    dokkaHtmlPlugin(libs.dokka.pluginVersioning)
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

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    explicitApiWarning()
    jvmToolchain(17)
}

loom {
    mods {
        val pytek by creating {
            sourceSet(sourceSets.main.get())
        }
    }
}

signing {
    sign(tasks.remapJar.get())
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, signingKey.base64Decode(), signingPassword.base64Decode())

    isRequired = System.getenv("CI") != null && System.getenv("GITHUB_JOB") == "ci_push"
}

tasks {
    test {
        useJUnitPlatform()
    }

    val signatureZipTask by creating(Zip::class.java) {
        dependsOn(getByName("signRemapJar"))

        from(getByName("signRemapJar").outputs.files)
        from(files("src/main/resources/quilt.mod.json"))

        archiveFileName = "${project.base.archivesName}-${project.version}-signature.zip"
    }

    withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    dokkaHtml {
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            customAssets = listOf(file("artSrc/logo-icon.svg"))
            footerMessage = "(c) 2024 Pyrrha Wills"
        }

        val docsVersionsDir = projectDir.resolve("docs/version")
        val docsVersion = project.version.toString()
        val currentDocsDir = docsVersionsDir.resolve(docsVersion)
        outputDirectory = currentDocsDir

        pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
            version = docsVersion
            olderVersionsDir = docsVersionsDir
        }

        doLast {
            currentDocsDir.copyRecursively(file("docs-publishing/"), overwrite = true)
            currentDocsDir.resolve("older").deleteRecursively()
        }
    }

    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs += mutableListOf(
                "-opt-in=kotlin.RequiresOptIn"
            )
        }
    }
}

gitHooks {
    setHooks(mapOf("pre-commit" to "build checkLicenses"))
}

license {
    rule(file("HEADER"))
    include("**/*.kt")
    include("**/*.java")
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = "pytek"
    versionNumber = project.version.toString()
    versionType =
        if (project.version.toString().contains("-alpha")) {
            "alpha"
        } else if (project.version.toString().contains("-beta")) {
            "beta"
        } else {
            "release"
        }
    uploadFile.set(tasks.remapJar)
    additionalFiles.addAll(tasks.remapSourcesJar, tasks.getByName("signatureZipTask"))
    gameVersions = listOf(libs.versions.minecraft.get())
    dependencies {
        required.project("qsl") // Quilt Stdlib
        required.project("qkl") // Quilt Kotlin Stdlib
    }
    changelog = project.changelog.renderItem(
        project.changelog.getUnreleased().withEmptySections(false).withHeader(false),
        Changelog.OutputType.MARKDOWN
    )
    syncBodyFrom = rootProject.file("README.md").absolutePath
}

sourceSets {
    main {
        blossom {
            kotlinSources {
                property("version", project.version.toString())
                property("commit", grgitService.service.orNull?.grgit?.head()?.abbreviatedId ?: "so no head?")
            }
            resources {
                property("version", project.version.toString())
                property("mcVersion", libs.versions.minecraft.get())
                property("qklVersion", libs.versions.quiltKotlin.get())
                property("qslVersion", libs.versions.quiltStdLib.get())
            }
        }
    }
}

fun String?.base64Decode(): String? =
    if (this != null) String(Base64.getDecoder().decode(this)) else null
