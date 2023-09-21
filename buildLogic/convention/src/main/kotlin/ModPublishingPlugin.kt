/*
 * Copyright 2023 Pyrrha Wills
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.modrinth.minotaur.ModrinthExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.ChangelogPlugin
import org.jetbrains.changelog.ChangelogPluginExtension
import org.jetbrains.changelog.date
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

class ModPublishingPlugin : Plugin<Project> {

    @Suppress("LongMethod")
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.changelog")
                apply("com.modrinth.minotaur")
                apply("mod.signing")
            }

            // Create a zip file that contains the signature for the primary mod jar
            // It needs to look like a mod jar (zip with quilt.mod.json) so modrinth won't reject the upload
            val signingZipTask = tasks.create("signingZip", Zip::class.java) {
                dependsOn(tasks.getByName("signRemapJar"))

                from(tasks.getByName("signRemapJar").outputs.files)
                from(files("src/main/resources/quilt.mod.json"))

                archiveFileName.set("${archivesName.get()}-${project.version}-signature.zip")
            }

            extensions.configure<ChangelogPluginExtension> {
                version.set(project.version.toString())
                path.set(rootProject.file("CHANGELOG.md").canonicalPath)
                header.set(provider { "[${project.version.toString()}] - ${date()}" })
                @Suppress("MaxLineLength")
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

            // https://modrinth.com/mod/demontech
            extensions.configure<ModrinthExtension> {
                token.set(System.getenv("MODRINTH_TOKEN"))
                projectId.set("demontech")
                versionNumber.set(project.version.toString())
                versionType.set(
                    if (project.version.toString().contains("-alpha")) {
                        "alpha"
                    } else if (project.version.toString().contains("-beta")) {
                        "beta"
                    } else {
                        "release"
                    }
                )
                uploadFile.set(tasks.getByName("remapJar"))
                additionalFiles.addAll(tasks.getByName("remapSourcesJar"), signingZipTask)
                dependencies {
                    required.project("qsl") // Quilt Standard Libraries
                    required.project("qkl") // Quilt Kotlin Libraries
                }
                changelog.set(provider {
                    val projectChangelog = extensions.getByType<ChangelogPluginExtension>()

                    projectChangelog.renderItem(
                        projectChangelog
                            .getUnreleased()
                            .withHeader(false)
                            .withEmptySections(false),
                        Changelog.OutputType.MARKDOWN
                    )
                })

                syncBodyFrom.set(rootProject.file("README.md").readText())
            }
        }
    }
}
