import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.date
import java.lang.System.getenv
import java.util.*

plugins {
    alias(libs.plugins.changelog)
    alias(libs.plugins.detekt)
    alias(libs.plugins.githooks)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.minotaur)
    alias(libs.plugins.quilt.licenser)
    alias(libs.plugins.quilt.loom)
    signing
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
    minecraft(libs.minecraft)
    mappings(variantOf(libs.quilt.mappings) { classifier(libs.versions.quiltMappingsClassifier.get()) })
    modImplementation(libs.quilt.loader)
    modImplementation(libs.quilt.kotlinLibraries)
    modImplementation(libs.quilt.standardLibraries)

    implementation(libs.kotlinx.serializationCbor)

    testImplementation(kotlin("test"))
}

val javaVersion = 17

tasks {
    test {
        useJUnitPlatform()
    }

    processResources {
        filteringCharset = "UTF-8"
        inputs.property("version", project.version)
        filesMatching("quilt.mod.json") {
            expand(mapOf("version" to project.version))
        }

        // https://stackoverflow.com/questions/41028030/gradle-minimize-json-resources-in-processresources#41029113
        doLast {
            // minify json for release builds
            if(!modVersion.contains("-alpha") && !modVersion.contains("-beta")) {
                fileTree(mapOf(
                    "dir" to outputs.files.asPath,
                    "includes" to listOf("**/*.json", "**/*.mcmeta")
                )).forEach {
                    it.writeText(JsonOutput.toJson(JsonSlurper().parse(it)))
                }
            }
        }
    }

    // https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
    withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.isDeprecation = true
        options.release = javaVersion
    }

    java {
        sourceCompatibility = JavaVersion.toVersion(javaVersion)
        targetCompatibility = JavaVersion.toVersion(javaVersion)
        withSourcesJar()
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName.get()}" }
        }
    }
}

kotlin {
    jvmToolchain(javaVersion)
    sourceSets {
        main {
            resources.srcDirs.add(file("src/main/generated"))
        }
    }
}

loom {
    runtimeOnlyLog4j.set(true)

    runs {
        create("datagen") {
            client()
            vmArgs(
                "-Dfabric-api.datagen",
                "-Dfabric-api.datagen.output-dir=${file("src/main/generated")}",
                "-Dfabric-api.datagen.strict-validation",
                "-Dfabric-api.datagen.modid=demontech"
            )
            runDir("build/datagen")
        }
    }
}

license {
    rule(file("codeformat/HEADER"))
    include("**/*.kt")
    include("**/*.java")
}

changelog {
    version.set(modVersion)
    path.set(file("CHANGELOG.md").canonicalPath)
    header.set(provider { "[$modVersion] - ${date()}" })
    // use full semver regex
    headerParserRegex.set("""^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(?:-((?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\.(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\+([0-9a-zA-Z-]+(?:\.[0-9a-zA-Z-]+)*))?${'$'}""")
    introduction.set(
        """
            DemonTech - Technology and a lick of flame.
        """.trimIndent()
    )
    itemPrefix.set("-")
    keepUnreleasedSection.set(true)
    unreleasedTerm.set("[Unreleased]")
    groups.set(listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security"))
    lineSeparator.set("\n")
    combinePreReleases.set(true)
}

signing {
    sign(tasks.remapJar.get())
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, base64Decode(signingKey), base64Decode(signingPassword))

    isRequired = isCI && !isPRBuild
}

// Create a zip file that contains the signature for the primary mod jar
// It needs to look like a mod jar (zip with quilt.mod.json) so modrinth won't reject the upload
val signingZipTask by tasks.creating(type = Zip::class.java) {
    dependsOn(tasks["signRemapJar"])

    from(tasks["signRemapJar"].outputs.files)
    from(files("src/main/resources/quilt.mod.json"))

    archiveFileName.set("DemonTech-${project.version}-signature.zip")
}

// temp workaround since "./gradlew getChangelog --no-header --no-summary --no-empty-sections -q --console=plain --unreleased" doesn't work as intended
// todo: keep an eye on for --no-header, --no-summary and --no-empty-sections being fixed in https://github.com/JetBrains/gradle-changelog-plugin
val getNextChangelog: Task by tasks.creating {
    println(project.changelog.renderItem(
        project.changelog
            .getUnreleased()
            .withHeader(false)
            .withEmptySections(false),
        Changelog.OutputType.MARKDOWN
    ))
}

// https://modrinth.com/mod/demontech
modrinth {
    token.set(getenv("MODRINTH_TOKEN"))
    projectId.set("demontech")
    versionNumber.set(modVersion)
    versionType.set(
        if (modVersion.contains("-alpha")) {
            "alpha"
        } else if (modVersion.contains("-beta")) {
            "beta"
        } else {
            "release"
        }
    )
    uploadFile.set(tasks.remapJar.get())
    additionalFiles.addAll(tasks.remapSourcesJar.get(), signingZipTask)
    dependencies {
        required.project("qsl") // Quilt Standard Libraries
        required.project("qkl") // Quilt Kotlin Libraries
    }
    changelog.set(provider {
        project.changelog.renderItem(
            project.changelog
                .getUnreleased()
                .withHeader(false)
                .withEmptySections(false),
            Changelog.OutputType.MARKDOWN
        )
    })

    syncBodyFrom.set(rootProject.file("README.md").readText())
}

gitHooks {
    setHooks(mapOf("pre-commit" to "checkLicenses detekt"))
}

detekt {
    config.setFrom("codeformat/detekt.yml")
}

fun base64Decode(value: String?): String =
    String(Base64.getDecoder().decode(value ?: ""))
