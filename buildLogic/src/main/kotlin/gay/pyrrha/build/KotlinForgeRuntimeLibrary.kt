package gay.pyrrha.build

import org.gradle.api.attributes.Attribute
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

public val patchedFMLModType: Attribute<Boolean> = Attribute.of("patchedFMLModType", Boolean::class.javaObjectType)

public fun DependencyHandlerScope.kotlinForgeRuntimeLibrary(dependency: Provider<*>) {
    "localRuntime"(dependency) {
        isTransitive = false
        attributes {
            attribute(patchedFMLModType, true)
        }
    }
}
