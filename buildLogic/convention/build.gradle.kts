import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    alias(libs.plugins.detekt)
    alias(libs.plugins.quilt.licenser)
}

group = "gay.pyrrha.demontech.buildlogic"

val javaVersion = JavaVersion.VERSION_17
java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = javaVersion.toString()
        }
    }
}

dependencies {
    compileOnly(libs.changelog.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.minotaur.gradlePlugin)
    compileOnly(libs.quilt.licenserGradlePlugin)
    compileOnly(libs.quilt.loomGradlePlugin)
}

repositories {
    maven("https://maven.quiltmc.org/repository/release") { name = "Quilt" }
    maven("https://maven.fabricmc.net") { name = "Fabric" }
    mavenCentral()
    gradlePluginPortal()
}

detekt {
    config.setFrom("../../codeformat/detekt.yml")
}

license {
    rule(file("../../codeformat/HEADER"))
    include("**/*.kt")
}

gradlePlugin {
    plugins {
        register("datagen") {
            id = "mod.datagen"
            implementationClass = "ModDataGenPlugin"
        }
        register("lint") {
            id = "mod.lint"
            implementationClass = "ModLintPlugin"
        }
        register("publishing") {
            id = "mod.publishing"
            implementationClass = "ModPublishingPlugin"
        }
        register("quiltGameTest") {
            id = "mod.quilt.gametest"
            implementationClass = "ModQuiltGameTestPlugin"
        }
        register("quiltLibrary") {
            id = "mod.quilt.library"
            implementationClass = "ModQuiltLibraryPlugin"
        }
        register("quiltMod") {
            id = "mod.quilt"
            implementationClass = "ModQuiltModPlugin"
        }
        register("signing") {
            id = "mod.signing"
            implementationClass = "ModSigningPlugin"
        }
        register("testing") {
            id = "mod.testing"
            implementationClass = "ModTestingPlugin"
        }
    }
}
