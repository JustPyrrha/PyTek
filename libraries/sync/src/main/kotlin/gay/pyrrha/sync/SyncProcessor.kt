/*
 * Copyright 2023 Pyrrha Wills
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gay.pyrrha.sync


import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import gay.pyrrha.sync.converter.BooleanNbtConverter
import gay.pyrrha.sync.converter.ByteArrayNbtConverter
import gay.pyrrha.sync.converter.ByteNbtConverter
import gay.pyrrha.sync.converter.DoubleNbtConverter
import gay.pyrrha.sync.converter.FloatNbtConverter
import gay.pyrrha.sync.converter.IntArrayNbtConverter
import gay.pyrrha.sync.converter.IntNbtConverter
import gay.pyrrha.sync.converter.LongArrayNbtConverter
import gay.pyrrha.sync.converter.LongNbtConverter
import gay.pyrrha.sync.converter.NbtConverter
import gay.pyrrha.sync.converter.ShortNbtConverter
import gay.pyrrha.sync.converter.StringNbtConverter
import java.io.OutputStream

private fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

private val nbtConverters: List<NbtConverter<*>> = listOf(
    BooleanNbtConverter,
    ByteArrayNbtConverter,
    ByteNbtConverter,
    DoubleNbtConverter,
    FloatNbtConverter,
    IntArrayNbtConverter,
    IntNbtConverter,
    LongArrayNbtConverter,
    LongNbtConverter,
    ShortNbtConverter,
    StringNbtConverter,
)

public class SyncProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("gay.pyrrha.sync.Sync")
            .filterIsInstance(KSClassDeclaration::class.java)
        val ret = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it.validate() }
            .forEach { it.accept(SyncVisitor(), Unit) }
        return ret
    }

    public inner class SyncVisitor : KSVisitorVoid() {
        @OptIn(KspExperimental::class)
        @Suppress("LongMethod")
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            logger.info("Visiting ${classDeclaration.qualifiedName!!.asString()}")
            val packageName = classDeclaration.containingFile!!.packageName.asString()
            val className = "${classDeclaration.simpleName.asString()}Sync"
            val syncFields: MutableMap<KSPropertyDeclaration, NbtConverter<*>> = mutableMapOf()
            classDeclaration.getAllProperties()
                .filter { it.hasBackingField && it.isAnnotationPresent(Sync::class) }
                .forEach {
                    val converter = findTypeConverter(it.type.resolve())
                    syncFields[it] = converter
                }
            if (syncFields.isEmpty()) {
                logger.error(
                    "${classDeclaration.qualifiedName!!.asString()} is marked with @Sync but has no @Sync fields.",
                    classDeclaration
                )
                return
            }

            val imports = mutableListOf(
                "gay.pyrrha.sync.SyncData",
                "gay.pyrrha.sync.SyncHelper",
                "net.minecraft.nbt.NbtCompound"
            )

            imports.addAll(syncFields.map { it.value::class.qualifiedName!! }.distinct())
            imports.addAll(
                syncFields
                    .map { it.key.type.resolve().declaration.qualifiedName!!.asString() }
                    .filterNot { it.split(".").size == 2 && it.split(".")[0] == "kotlin" }
                    .distinct()
            )

            val file = codeGenerator.createNewFile(
                Dependencies(false, classDeclaration.containingFile!!),
                packageName,
                className
            )
            file.appendText(
                """
                /*
                 * SYNC GENERATED FILE, DO NOT EDIT
                 */
                
                """.trimIndent()
            )
            file.appendText("package $packageName\n\n")
            imports.sorted().forEach {
                file.appendText("import $it\n")
            }
            file.appendText("\nobject $className : SyncHelper<${className}Data> {\n")
            file.appendText("    // Sync fields: (${syncFields.count()})\n")
            syncFields.forEach {
                @Suppress("MaxLineLength")
                file.appendText("    // ${it.key.simpleName.asString()}: ${it.key.type.resolve().declaration.qualifiedName!!.asString()} (${it.value::class.simpleName!!})\n")
            }

            file.appendText("\n    override fun write(compound: NbtCompound, data: ${className}Data): NbtCompound {\n")
            file.appendText("        data.apply {\n")
            syncFields.forEach {
                @Suppress("MaxLineLength")
                file.appendText("           ${it.value::class.simpleName!!}.write(compound, \"${it.key.simpleName.asString()}\", ${it.key.simpleName.asString()})\n")
            }
            file.appendText("        }\n\n")
            file.appendText("        return compound\n")
            file.appendText("    }\n\n")
            file.appendText("    override fun read(compound: NbtCompound, action: ${className}Data.() -> Unit) {\n")
            file.appendText("        action(\n")
            file.appendText("            ${className}Data(\n")
            syncFields.forEach {
                @Suppress("MaxLineLength")
                file.appendText("                ${it.key.simpleName.asString()} = ${it.value::class.simpleName!!}.read(compound, \"${it.key.simpleName.asString()}\"),\n")
            }
            file.appendText("            )\n")
            file.appendText("        )\n")
            file.appendText("    }\n")
            file.appendText("}\n\n")
            file.appendText("data class ${className}Data(\n")
            syncFields.forEach {
                @Suppress("MaxLineLength")
                file.appendText("    val ${it.key.simpleName.asString()}: ${it.key.type.resolve().declaration.simpleName.asString()},\n")
            }
            file.appendText(") : SyncData\n")
            file.close()
        }
    }

    private fun findTypeConverter(type: KSType): NbtConverter<*> {
        val converter = nbtConverters.find { it.validFor(type.declaration.qualifiedName!!.asString()) }
        if (converter != null) {
            return converter
        }
        throw MissingConverterException("Missing NbtConverter for ${type.declaration.qualifiedName!!.asString()}. ")
    }
}

public class SyncProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        SyncProcessor(environment.codeGenerator, environment.logger)
}
