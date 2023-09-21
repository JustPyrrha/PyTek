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

import gay.pyrrha.demontech.configureKotlin
import gay.pyrrha.demontech.libs
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.gradle.language.jvm.tasks.ProcessResources

class ModQuiltModPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("org.quiltmc.loom")
            }

            extensions.configure<LoomGradleExtensionAPI> {
                runtimeOnlyLog4j.set(true)
            }

            configureKotlin()

            dependencies {
                "minecraft"(libs.findLibrary("minecraft").get())
                "mappings"(variantOf(libs.findLibrary("quilt.mappings").get()) {
                    classifier(libs.findVersion("quiltMappingsClassifier").get().requiredVersion)
                })
                "modImplementation"(libs.findLibrary("quilt.loader").get())
            }

            tasks.withType<ProcessResources> {
                inputs.property("version", project.version)

                filesMatching("quilt.mod.json") {
                    expand(mapOf("version" to project.version))
                }

                // https://stackoverflow.com/questions/41028030/gradle-minimize-json-resources-in-processresources#41029113
                doLast {
                    // minify json for release builds
                    if(!project.version.toString().contains("-alpha") &&
                        !project.version.toString().contains("-beta")) {
                        fileTree(mapOf(
                            "dir" to outputs.files.asPath,
                            "includes" to listOf("**/*.json", "**/*.mcmeta")
                        )).forEach {
                            it.writeText(JsonOutput.toJson(JsonSlurper().parse(it)))
                        }
                    }
                }
            }

            tasks.withType<Jar> {
                from(rootProject.file("LICENSE")) {
                    rename { "${it}_${archiveBaseName.get()}" }
                }
            }
        }
    }
}
