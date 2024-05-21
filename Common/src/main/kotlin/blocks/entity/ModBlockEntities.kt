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
    public val REACTOR_CONTROLLER: BlockEntityType<ReactorControllerBlockEntity> by lazy {
        create(ModBlocks.REACTOR_CONTROLLER, factory = ::ReactorControllerBlockEntity)
    }

    public val CABLE: BlockEntityType<CableBlockEntity> by lazy {
        create(ModBlocks.CABLE, factory = ::CableBlockEntity)
    }

    private fun <T : BlockEntity> create(
        vararg blocks: Block,
        dfuType: DFUType<*>? = null,
        factory: (BlockPos, BlockState) -> T
    ): BlockEntityType<T> = ModRuntime.instance.createBlockEntityType(*blocks, dfuType = dfuType, factory = factory)

    public fun init(register: (ResourceLocation, BlockEntityType<*>) -> Unit) {
        register(rl("reactor_controller"), REACTOR_CONTROLLER)
        register(rl("cable"), CABLE)
    }
}
