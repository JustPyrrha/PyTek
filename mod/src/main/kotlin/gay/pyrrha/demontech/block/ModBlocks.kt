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

@file:Suppress("MagicNumber")
package gay.pyrrha.demontech.block

import gay.pyrrha.demontech.DemonTech
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

object ModBlocks {
    val THERMAL_GENERATOR: Block by lazy {
        ThermalGeneratorBlock(QuiltBlockSettings.create().strength(3.5f))
    }

    fun init() {
        register(THERMAL_GENERATOR, "thermal_generator")
    }

    private fun register(block: Block, path: String) {
        Registry.register(Registries.BLOCK, DemonTech.id(path), block)
        Registry.register(Registries.ITEM, DemonTech.id(path), BlockItem(block, QuiltItemSettings()))
    }
}
