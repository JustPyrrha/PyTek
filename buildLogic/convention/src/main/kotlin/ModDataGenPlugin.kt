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
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class ModDataGenPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LoomGradleExtensionAPI> {
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

            extensions.configure<KotlinJvmProjectExtension> {
                sourceSets.getByName("main") {
                    resources.srcDirs(file("src/main/generated/"))
                }
            }

            dependencies{
                "modImplementation"(libs.findLibrary("quilted-fabricDataGenerationApiV1").get())
            }
        }
    }
}
