import gay.pyrrha.demontech.libs
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class ModQuiltLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("org.quiltmc.loom")
            }

            extensions.configure<LoomGradleExtensionAPI> {
                runtimeOnlyLog4j.set(true)
            }

            dependencies {
                "minecraft"(libs.findLibrary("minecraft").get())
                "mappings"(variantOf(libs.findLibrary("quilt.mappings").get()) {
                    classifier(libs.findVersion("quiltMappingsClassifier").get().requiredVersion)
                })
            }
        }
    }
}
