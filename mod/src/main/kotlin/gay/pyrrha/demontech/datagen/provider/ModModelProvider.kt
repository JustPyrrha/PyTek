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

package gay.pyrrha.demontech.datagen.provider

import gay.pyrrha.datagenhelper.registerHorizontalRotatable
import gay.pyrrha.demontech.block.ModBlocks
import gay.pyrrha.demontech.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Models

class ModModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        generator.registerHorizontalRotatable(ModBlocks.THERMAL_GENERATOR)
    }

    override fun generateItemModels(generator: ItemModelGenerator) {
        generator.register(ModItems.NETWORK_SCANNER, Models.SINGLE_LAYER_ITEM)
        generator.register(ModItems.ADAPTIVE_THERMAL_PAD, Models.SINGLE_LAYER_ITEM)
        generator.register(ModItems.THERMAL_PAD, Models.SINGLE_LAYER_ITEM)
    }
}
