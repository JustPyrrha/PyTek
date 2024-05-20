package ml

import gay.pyrrha.build.sourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

public class CommonProjectConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("ml.internal.loom")
                apply("ml.internal.mod")
            }

            val commonSource by configurations.registering {
                isCanBeResolved = false
                isCanBeConsumed = true
            }

            val commonResources by configurations.registering {
                isCanBeResolved = false
                isCanBeConsumed = true
            }

            artifacts {
                val main by sourceSets.getting {
                    kotlin.sourceDirectories.forEach {
                        add("commonSource", it)
                    }
                }
                add("commonResources", main.resources.sourceDirectories.singleFile)
            }
        }
    }
}
