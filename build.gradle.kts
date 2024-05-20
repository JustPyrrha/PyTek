import dev.yumi.gradle.licenser.YumiLicenserGradleExtension
import org.jetbrains.changelog.ChangelogPluginConstants.SEM_VER_REGEX
import org.jetbrains.changelog.date
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin

plugins {
    alias(libs.plugins.changelog)
    alias(libs.plugins.dokka)

    alias(libs.plugins.idea.ext) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    alias(libs.plugins.licenser) apply false
    alias(libs.plugins.minotaur) apply false

    alias(libs.plugins.neoforge.userdev) apply false
    alias(libs.plugins.quilt.loom) apply false

    alias(libs.plugins.ml.internal.jvm) apply false
    alias(libs.plugins.ml.internal.loom) apply false
    alias(libs.plugins.ml.internal.mod) apply false
    alias(libs.plugins.ml.common) apply false
    alias(libs.plugins.ml.runtime) apply false
}

buildscript {
    dependencies {
        classpath(libs.dokka.pluginBase)
        classpath(libs.dokka.pluginVersioning)
    }
}

allprojects {
    group = "gay.pyrrha"
    version = "1.0.0-alpha.0+${rootProject.libs.versions.minecraft.get()}"
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = rootProject.libs.plugins.licenser.get().pluginId)
    extensions.configure<YumiLicenserGradleExtension> {
        rule(rootProject.file("HEADER"))
        include("**/*.kt")
        include("**/*.java")
    }
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

tasks {
    dokkaHtmlMultiModule {
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
}
