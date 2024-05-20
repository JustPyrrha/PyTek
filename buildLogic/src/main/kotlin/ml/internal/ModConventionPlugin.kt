package ml.internal

import org.gradle.api.Plugin
import org.gradle.api.Project

public class ModConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("ml.internal.jvm")
            }

            listOf(
                "apiElements",
                "runtimeElements",
                "mainSourceElements"
            ).forEach {
                configurations.named(it).configure {
                    outgoing {
                        capability("${rootProject.group}:${rootProject.name}:${rootProject.version}")
                    }
                }
            }
        }
    }
}
