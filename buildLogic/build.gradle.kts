import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

repositories {
    maven("https://maven.quiltmc.org/repository/release/") { name = "Quilt" }
    maven("https://maven.fabricmc.net/") { name = "Fabric" }
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.quilt.loom.gradlePlugin)
}

kotlin {
    explicitApi()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }

    withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

gradlePlugin {
    plugins {
        val jvmConventionPlugin by creating {
            id = "ml.internal.jvm"
            implementationClass = "ml.internal.JvmConventionPlugin"
        }
        val loomConventionPlugin by creating {
            id = "ml.internal.loom"
            implementationClass = "ml.internal.LoomConventionPlugin"
        }
        val modProjectConventionPlugin by creating {
            id = "ml.internal.mod"
            implementationClass = "ml.internal.ModConventionPlugin"
        }

        val commonProjectConventionPlugin by creating {
            id = "ml.common"
            implementationClass = "ml.CommonProjectConventionPlugin"
        }
        val runtimeProjectConventionPlugin by creating {
            id = "ml.runtime"
            implementationClass = "ml.RuntimeProjectConventionPlugin"
        }
    }
}
