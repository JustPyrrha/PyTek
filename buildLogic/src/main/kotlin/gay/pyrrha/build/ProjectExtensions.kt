package gay.pyrrha.build

import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer

internal val Project.libs
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val Project.loom
    get() = extensions.getByType<LoomGradleExtensionAPI>()

internal val Project.sourceSets
    get() = extensions.getByType<KotlinSourceSetContainer>().sourceSets
