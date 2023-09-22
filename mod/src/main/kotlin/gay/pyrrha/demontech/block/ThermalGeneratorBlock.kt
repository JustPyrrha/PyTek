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

package gay.pyrrha.demontech.block

import gay.pyrrha.demontech.block.entity.ModBlockEntities
import gay.pyrrha.demontech.block.entity.ThermalGeneratorBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings

/**
 * Basic, tier 1 generator
 * Generates 1 Unit/tick
 * Stores 600 Units (Output Only)
 */
class ThermalGeneratorBlock : BlockWithEntity(QuiltBlockSettings.create().strength(ModBlocks.METAL_STRENGTH)) {
    init {
        defaultState = stateManager.defaultState.with(FACING, Direction.NORTH)
    }

    companion object {
        val FACING: DirectionProperty = Properties.HORIZONTAL_FACING
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return stateManager.defaultState.with(FACING, ctx.player?.horizontalFacing?.opposite ?: Direction.NORTH)
    }

    @Deprecated("Deprecated in Java")
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        ThermalGeneratorBlockEntity(pos, state)

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        if(!world.isClient) {
            val fluidState = world.getBlockState(pos.down()).fluidState
            val isLavaBelow = fluidState.fluid == Fluids.LAVA && fluidState.isSource
            world.getBlockEntity(pos, ModBlockEntities.THERMAL_GENERATOR).ifPresent { it.active = isLavaBelow }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun neighborUpdate(
        state: BlockState,
        world: World,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        notify: Boolean
    ) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify)
        if(!world.isClient && fromPos == pos.down()) {
            val fluidState = world.getBlockState(fromPos).fluidState
            val isLavaBelow = fluidState.fluid == Fluids.LAVA && fluidState.isSource
            world.getBlockEntity(pos, ModBlockEntities.THERMAL_GENERATOR).ifPresent { it.active = isLavaBelow }
        }
    }

    override fun <T : BlockEntity?> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return checkType(type, ModBlockEntities.THERMAL_GENERATOR) { world, _, _, entity ->
            ThermalGeneratorBlockEntity.tick(world, entity)
        }
    }
}
