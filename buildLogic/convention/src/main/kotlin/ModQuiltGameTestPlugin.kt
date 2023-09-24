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

import gay.pyrrha.demontech.libs
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class ModQuiltGameTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            val mainSourceSet = extensions.getByType<SourceSetContainer>().named("main").get()

            extensions.configure<SourceSetContainer> {
                create("gametest") {
                    compileClasspath += mainSourceSet.compileClasspath
                    runtimeClasspath += mainSourceSet.runtimeClasspath

                    dependencies {
                        "modImplementation"(libs.findLibrary("quilt-qslCoreTesting").get())
                    }
                }
            }

            extensions.configure<KotlinJvmProjectExtension> {
                sourceSets.getByName("gametest") {
                    kotlin.srcDir(file("src/gametest/kotlin"))
                    dependencies {
                        implementation(mainSourceSet.output)
                    }
                }
            }

            extensions.configure<LoomGradleExtensionAPI> {
                runs {
                    create("gametestClient") {
                        client()
                        source("gametest")
                        configName = "GameTest Client"
                        property("quilt.game_test", "true")
                        runDir("build/gametest")
                    }

                    create("gametestServer") {
                        server()
                        source("gametest")
                        configName = "GameTest Server"
                        property("quilt.game_test", "true")
                        property(
                            "quilt.game_test.report_file",
                            "${project.layout.buildDirectory.asFile.get().path}/test-results/test/TEST-gametest.xml"
                        )
                        runDir("build/gametest")
                    }
                }
            }
        }
    }
}
