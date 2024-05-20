package ml.internal

import gay.pyrrha.build.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

public class JvmConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }

            configureKotlin()
        }
    }
}
