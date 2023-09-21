package gay.pyrrha.demontech.block

import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings

class ThermalGeneratorBlock : Block(QuiltBlockSettings.create().strength(ModBlocks.METAL_STRENGTH)) {
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return stateManager.defaultState.with(FACING, ctx.playerLookDirection.opposite)
    }

    init {
        defaultState = stateManager.defaultState.with(FACING, Direction.NORTH)
    }

    companion object {
        val FACING: DirectionProperty = Properties.HORIZONTAL_FACING
    }
}