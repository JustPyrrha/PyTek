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

package gay.pyrrha.demontech.block.entity

import gay.pyrrha.demontech.DemonTech
import gay.pyrrha.demontech.block.ModBlocks
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.math.BlockPos
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder

object ModBlockEntities {

    lateinit var THERMAL_GENERATOR: BlockEntityType<ThermalGeneratorBlockEntity>

    fun init() {
        THERMAL_GENERATOR = register(ModBlocks.THERMAL_GENERATOR) { p, s -> ThermalGeneratorBlockEntity(p, s) }
    }

    private fun <T : BlockEntity, B : Block> register(
        block: B,
        entityFactory: (pos: BlockPos, state: BlockState) -> T
    ): BlockEntityType<T> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            DemonTech.id("${Registries.BLOCK.getId(block).path}_entity"),
            QuiltBlockEntityTypeBuilder.create(entityFactory, block).build()
        )
}
