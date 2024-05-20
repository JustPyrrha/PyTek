package ml.internal

import gay.pyrrha.build.libs
import gay.pyrrha.build.loom
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

public class LoomConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("ml.internal.jvm")
                apply("org.quiltmc.loom")
            }

            dependencies {
                "minecraft"(libs.findLibrary("minecraft").get())
                "mappings"(loom.officialMojangMappings())
            }
        }
    }
}