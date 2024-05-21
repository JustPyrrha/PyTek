/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.blocks.entity

import gay.pyrrha.pytek.ModRuntime
import gay.pyrrha.pytek.blocks.ModBlocks
import gay.pyrrha.pytek.rl
import net.minecraft.core.BlockPos
import com.mojang.datafixers.types.Type as DFUType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

public object ModBlockEntities {
    private val ALL = hashMapOf<ResourceLocation, BlockEntityType<*>>()

    public val REACTOR_CONTROLLER: BlockEntityType<ReactorControllerBlockEntity> by lazy {
        create(
            "reactor_controller",
            ModBlocks.REACTOR_CONTROLLER,
            factory = ::ReactorControllerBlockEntity
        )
    }

    private fun <T : BlockEntity> create(
        path: String,
        vararg blocks: Block,
        dfuType: DFUType<*>? = null,
        factory: (BlockPos, BlockState) -> T
    ): BlockEntityType<T> {
        val type = ModRuntime.instance.createBlockEntityType(*blocks, dfuType = dfuType, factory = factory)
        val existing = ALL.put(rl(path), type)
        if (existing != null) {
            throw IllegalArgumentException("Duplicate id ${rl(path)}")
        }
        return type
    }

    public fun init(register: (ResourceLocation, BlockEntityType<*>) -> Unit) {
        ALL.forEach { (rl, t) -> register(rl, t) }
    }
}
