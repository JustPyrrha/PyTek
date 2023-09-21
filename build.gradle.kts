plugins {
    alias(libs.plugins.changelog) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.githooks)
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.minotaur) apply false
    alias(libs.plugins.quilt.licenser) apply false
    alias(libs.plugins.quilt.loom) apply false
}

gitHooks {
    setHooks(mapOf("pre-commit" to listOf(
        "checkLicenses",
        "detekt",
        ":buildLogic:convention:checkLicenses",
        ":buildLogic:convention:detekt"
    ).joinToString(separator = " ")))
}
