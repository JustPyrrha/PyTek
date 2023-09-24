import gay.pyrrha.demontech.buildInfo
import org.jetbrains.changelog.Changelog

plugins {
    id("mod.quilt")
    id("mod.quilt.gametest")
    id("mod.lint")
    id("mod.datagen")
    id("mod.signing")
    id("mod.publishing")

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

val modVersion = "0.1.0-alpha+$buildInfo"

group = "gay.pyrrha"
version = modVersion
base.archivesName.set("DemonTech")

dependencies {
    include(implementation(projects.libraries.datagen) { targetConfiguration = "namedElements" } )
    include(implementation(projects.libraries.sync) { targetConfiguration = "namedElements" })

    modImplementation(libs.quilt.kotlinLibraries)
    modImplementation(libs.quilt.standardLibraries)

    implementation(libs.kotlinx.serializationCbor)

    ksp(projects.libraries.sync) { targetConfiguration = "namedElements" }
}

loom {
    runConfigs.configureEach {
        isIdeConfigGenerated = true
    }
}

// workaround for "./gradlew getChangelog --no-header --no-summary --no-empty-sections -q --console=plain --unreleased"
// todo: keep an eye on for --no-header, --no-summary and --no-empty-sections being fixed in https://github.com/JetBrains/gradle-changelog-plugin
val getNextChangelog: Task by tasks.creating {
    println(changelog.renderItem(
        changelog
            .getUnreleased()
            .withHeader(false)
            .withEmptySections(false),
        Changelog.OutputType.MARKDOWN
    ))
}
