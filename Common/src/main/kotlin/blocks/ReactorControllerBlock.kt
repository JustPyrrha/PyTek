/*
 * Copyright (C) 2024 Pyrrha Wills - All Rights Reserved
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.pyrrha.pytek.blocks

import com.mojang.serialization.MapCodec
import gay.pyrrha.pytek.blocks.entity.ReactorControllerBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

public class ReactorControllerBlock : HorizontalDirectionalBlock(
    Properties.ofFullCopy(Blocks.IRON_BLOCK)
), EntityBlock {
    override fun codec(): MapCodec<out HorizontalDirectionalBlock> =
        MapCodec.unit(::ReactorControllerBlock)

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        ReactorControllerBlockEntity(pos, state)
}
