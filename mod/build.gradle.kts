import org.jetbrains.changelog.Changelog
import java.lang.System.getenv

plugins {
    id("mod.quilt")
    id("mod.lint")
    id("mod.datagen")
    id("mod.signing")
    id("mod.publishing")

//    alias(libs.plugins.githooks)
    alias(libs.plugins.kotlin.serialization)
}

val isCI = getenv("CI") != null
val runNumberEnvName = "GITHUB_RUN_NUMBER"
val buildNumber: String = if (isCI && getenv(runNumberEnvName) != null) {
    getenv(runNumberEnvName)
} else {
    "local"
}
val jobIdVarName = "GITHUB_JOB"
val isPRBuild: Boolean = if (isCI && getenv(jobIdVarName) != null) {
    getenv(jobIdVarName) == "build_pr"
} else {
    false
}

val modVersion = "0.1.0-alpha+build.$buildNumber-${libs.versions.minecraft.get()}"

group = "gay.pyrrha"
version = modVersion
base.archivesName.set("DemonTech")

dependencies {
    modImplementation(libs.quilt.kotlinLibraries)
    modImplementation(libs.quilt.standardLibraries)
    modImplementation(libs.quilted.fabricApi)

    implementation(libs.kotlinx.serializationCbor)

    testImplementation(kotlin("test"))
}

val javaVersion = 17

tasks {
    test {
        useJUnitPlatform()
    }
}

// workaround for "./gradlew getChangelog --no-header --no-summary --no-empty-sections -q --console=plain --unreleased"
// todo: keep an eye on for --no-header, --no-summary and --no-empty-sections being fixed in https://github.com/JetBrains/gradle-changelog-plugin
tasks.create("getNextChangelog") {
    println(changelog.renderItem(
        changelog
            .getUnreleased()
            .withHeader(false)
            .withEmptySections(false),
        Changelog.OutputType.MARKDOWN
    ))
}
