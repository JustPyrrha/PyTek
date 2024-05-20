package ml

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.language.jvm.tasks.ProcessResources
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public class RuntimeProjectConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("ml.internal.jvm")
                apply("ml.internal.mod")
            }

            configurations.register("commonSource") {
                isCanBeResolved = true
            }

            configurations.register("commonResources") {
                isCanBeResolved = true
            }

            dependencies {
                "compileOnly"(project(path = ":Common")) {
                    capabilities {
                        requireCapability("${rootProject.group}:${rootProject.name}:${rootProject.version}")
                    }
                }

                "commonSource"(project(path = ":Common", configuration = "commonSource"))
                "commonResources"(project(path = ":Common", configuration = "commonResources"))
            }

            tasks.withType<KotlinCompile>().configureEach {
                dependsOn(configurations.named("commonSource"))
                source(configurations.named("commonSource"))
            }

            tasks.named("processResources", ProcessResources::class.java) {
                dependsOn(configurations.named("commonResources"))
                from(configurations.named("commonResources"))
            }

            tasks.named("kotlinSourcesJar", Jar::class.java) {
                dependsOn(configurations.named("commonSource"))
                dependsOn(configurations.named("commonResources"))
                from(configurations.named("commonSource"))
                from(configurations.named("commonResources"))
            }
        }
    }
}
